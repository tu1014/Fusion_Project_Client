package network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Protocol {

    public static final byte LEN_HEADER_SIZE = 9;
    public static final int LEN_MAX_LENGTH = 1000;

    public static final int UNUSED = 0;

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
    DataOutputStream dos;

    public Protocol() {init();}

    public void init() {
        packet = new Packet();
        baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
    }

    public void setHeader(
            int messageType,
            int action,
            int code,
            int frag,
            int last,
            int seqNumber
    ) {

        packet.header[INDEX_MESSAGE_TYPE] = (byte)messageType;
        packet.header[INDEX_ACTION] = (byte)action;
        packet.header[INDEX_CODE] = (byte)code;

        packet.header[INDEX_FRAG] = (byte)frag;
        packet.header[INDEX_LAST] = (byte)last;

        // set SeqNumber
        packet.header[INDEX_SEQ_NUMBER] = (byte)(seqNumber >> 8);
        packet.header[INDEX_SEQ_NUMBER+1] = (byte)(seqNumber);

    }

    public void setBodyLength() {

        packet.body = baos.toByteArray();
        int bodyLength = packet.body.length;
        packet.header[INDEX_BODY_LENGTH] = (byte)(bodyLength >> 8);
        packet.header[INDEX_BODY_LENGTH+1] = (byte)(bodyLength);

    }


    // data의 0번째 index부터 count 만큼
    public void addBodyStringData(String data) {

        try {
            dos.writeUTF(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addBodyIntData(int data) {

        try {
            dos.writeInt(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public byte[] getPacket() {

        setBodyLength();
        byte[] rs = new byte[LEN_HEADER_SIZE + packet.body.length];
        System.arraycopy(packet.header, 0, rs, 0, LEN_HEADER_SIZE);
        System.arraycopy(packet.body, 0, rs, LEN_HEADER_SIZE, packet.body.length);

        return rs;

    }

    public byte[] getHeader() {return packet.header;}
    public byte[] getBody() {return packet.body;}


}
