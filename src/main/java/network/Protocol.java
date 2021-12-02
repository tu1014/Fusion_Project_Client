package network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Protocol {

    public static final byte LEN_HEADER_SIZE = 9;
    public static final byte LEN_INT_DATA = 2;
    public static final int LEN_MAX_LENGTH = 1000;

    public static final int UNUSED = 0;
    public static final int USED = 1;

    public static final byte INDEX_MESSAGE_TYPE = 0;
    public static final byte INDEX_ACTION = 1;
    public static final byte INDEX_CODE = 2;
    public static final byte INDEX_BODY_LENGTH = 3;
    public static final byte INDEX_FRAG = 5;
    public static final byte INDEX_LAST = 6;
    public static final byte INDEX_SEQ_NUMBER = 7;

    // message type
    public static final byte REQUEST = 0;
    public static final byte RESPONSE = 1;

    // action
    public static final byte LOGIN = 0;
    public static final byte LOGOUT = 1;
    public static final byte CREATE = 2;
    public static final byte READ = 3;
    public static final byte UPDATE = 4;
    public static final byte DELETE = 5;

    // code
    public static final byte ADMIN = 0;
    public static final byte STUDENT = 1;
    public static final byte PROFESSOR = 2;
    public static final byte SUBJECT = 3;
    public static final byte LECTURE_TIME_TABLE = 4;
    public static final byte OPENING_SUBJECT = 5;
    public static final byte REGISTRATION = 6;
    public static final byte STUDENT_TIME_TABLE = 7;
    public static final byte SYLLABUS = 8;
    public static final byte FAIL = 9;
    public static final byte SUCCESS = 10;
    public static final byte DEPARTMENT = 11;
    public static final byte PROFESSOR_TIME_TABLE = 12;
    public static final byte LECTURE_ROOM = 13;

    // 헤더의 길이는 고정이지만 바디의 길이는 달라질 수 있기 때문에 두 바이트 배열을 사용하는
    // 내부 클래스를 정의
    private class Packet {

        private byte[] header;
        private byte[] body;

        byte[] getHeader() {return header;}
        byte[] getBody() {return body;}
        void setHeader() {}
        void setBody() {}

        private Packet() { header = new byte[LEN_HEADER_SIZE]; }
    }

    private Packet packet;

    ByteArrayOutputStream baos;
    // 데이터를 담을 때 처음부터 큰 배열을 생성하고 담는 방법과 짧은 배열을 점차 늘려가며 담는 방법 중
    // 후자를 선택. ByteArrayOutputStream에서 내부적으로 resize 로직을 수행한다.
    // toByteArray 메서드를 호출하면 담은 데이터 만큼의 바이트 배열을 리턴

    ArrayList<byte[]> packetList;
    // 한 번에 전송 가능한 최대 바디의 수를 1000 바이트로 정의.
    // 이를 초과하면 여기에 저장하였다가 전송을 위해 getAllPacket을 호출하면
    // 이를 리턴한다

    boolean isFragmented;
    int packetSeqNum;

    public Protocol() {init();}

    // 여러 컨트롤러에서 이 프로토콜을 공유하기 때문에 전송 이후 쓰레기 데이터가 남아있음
    // 원활한 통신을 위해 프로토콜의 패킷에 데이터를 담기 전에 초기화를 필요로 한다
    public void init() {
        packet = new Packet();
        baos = new ByteArrayOutputStream();
        packetList = new ArrayList<>();
        isFragmented = false;
        packetSeqNum = 0;
    }

    // 패킷의 헤더에 세팅
    // 메세지 타입 / 동작(CRUD와 유사) / CODE(target 또는 요청에 대한 성공/실패 여부) / 바디길이를 설정한다.
    // 분할여부 / 마지막 분할 / 시퀀스 넘버는 내부적으로 처리하여 사용자는 신경쓰지 않도록 한다
    public void setHeader(
            int messageType,
            int action,
            int code
    ) {

        packet.header[INDEX_MESSAGE_TYPE] = (byte)messageType;
        packet.header[INDEX_ACTION] = (byte)action;
        packet.header[INDEX_CODE] = (byte)code;

    }

    // 데이터를 모두 담고 getPacket을 호출하면 이 메서드가 자동으로 호출되어 바디 길이를
    // 헤더에 기록 한 후 리턴한다
    public void setBodyLength() {

        packet.body = baos.toByteArray();
        int bodyLength = packet.body.length;

        packet.header[INDEX_BODY_LENGTH] = (byte)(bodyLength >> 8);
        packet.header[INDEX_BODY_LENGTH+1] = (byte)(bodyLength);

    }

    // 패킷 바디에 문자열 데이터를 담는다 사용자 측은 String.getBytes()의 결과를 인자로 넘겨준다
    public void addBodyStringData(byte[] data) {

        try {

            // 만약 데이터를 담았을 때 바디의 길이가 1000바이트를 초과하게 된다면 리스트에 패킷을 저장하고
            // 새 패킷에 데이터를 담는다
            if(baos.size() + LEN_INT_DATA + data.length >= LEN_MAX_LENGTH) {

                isFragmented = true;
                packet.header[INDEX_FRAG] = USED;
                packet.header[INDEX_SEQ_NUMBER] = (byte)(packetSeqNum >> 8);
                packet.header[INDEX_SEQ_NUMBER+1] = (byte)(packetSeqNum);
                packetSeqNum++;
                packetList.add(getPacket());
                baos.reset();

            }
            addBodyIntData(data.length);
            baos.write(data);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addBodyIntData(int data) {

        // 만약 데이터를 담았을 때 바디의 길이가 1000바이트를 초과하게 된다면 리스트에 패킷을 저장하고
        // 새 패킷에 데이터를 담는다
        if(baos.size() + LEN_INT_DATA >= LEN_MAX_LENGTH) {

            isFragmented = true;
            packet.header[INDEX_FRAG] = USED;
            packet.header[INDEX_SEQ_NUMBER] = (byte)(packetSeqNum >> 8);
            packet.header[INDEX_SEQ_NUMBER+1] = (byte)(packetSeqNum);
            packetSeqNum++;
            packetList.add(getPacket());
            baos.reset();

        }

        baos.write((byte)(data >> 8));
        baos.write((byte)(data));

    }

    public byte[] getPacket() {

        // 담은 데이터 만큼의 패킷을 리턴
        setBodyLength();
        byte[] rs = new byte[LEN_HEADER_SIZE + packet.body.length];
        System.arraycopy(packet.header, 0, rs, 0, LEN_HEADER_SIZE);
        System.arraycopy(packet.body, 0, rs, LEN_HEADER_SIZE, packet.body.length);
        return rs;

    }

    public ArrayList<byte[]> getAllPacket() {

        setBodyLength();
        if (isFragmented) packet.header[INDEX_LAST] = USED;
        packetList.add(getPacket());

        return packetList;
    }

    public byte[] getHeader() {return packet.header;}
    public byte[] getBody() {return packet.body;}


}