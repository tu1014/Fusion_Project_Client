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

    private class Packet {

        private byte[] header;
        private byte[] body;

        byte[] getHeader() {return header;}
        byte[] getBody() {return body;}
        void setHeader() {}
        void setBody() {}

        private Packet() { header = new byte[LEN_HEADER_SIZE]; }

        public void printPacket() {
            System.out.println("<Packet Header>");
            if (header[INDEX_MESSAGE_TYPE] == REQUEST) System.out.print("REQUEST ");
            if (header[INDEX_MESSAGE_TYPE] == RESPONSE) System.out.print("RESPONSE ");
            switch (header[INDEX_ACTION]) {
                case Protocol.LOGIN: System.out.print("LOGIN "); break;
                case Protocol.LOGOUT: System.out.print("LOGOUT "); break;
                case Protocol.CREATE: System.out.print("CRATE "); break;
                case Protocol.READ: System.out.print("READ "); break;
                case Protocol.UPDATE: System.out.print("UPDATE "); break;
                case Protocol.DELETE: System.out.print("DELETE "); break;
            }
            switch (header[INDEX_CODE]) {
                case Protocol.ADMIN: System.out.print("ADMIN "); break;
                case Protocol.STUDENT: System.out.print("STUDENT "); break;
                case Protocol.PROFESSOR: System.out.print("PROFESSOR "); break;
                case Protocol.SUBJECT: System.out.print("SUBJECT "); break;
                case Protocol.LECTURE_TIME_TABLE: System.out.print("LECTURE_TIME_TABLE "); break;
                case Protocol.OPENING_SUBJECT: System.out.print("OPENING_SUBJECT "); break;
                case Protocol.REGISTRATION: System.out.print("REGISTRATION "); break;
                case Protocol.STUDENT_TIME_TABLE: System.out.print("STUDENT_TIME_TABLE "); break;
                case Protocol.SYLLABUS: System.out.print("SYLLABUS "); break;
            }
            System.out.println();
            int bodyLength = ((int) (header[INDEX_BODY_LENGTH] & 0xff) << 8) |
                    ((int) header[INDEX_BODY_LENGTH+1] & 0xff);

            System.out.print(bodyLength + " ");
            System.out.print(header[INDEX_FRAG] + " ");
            System.out.print(header[INDEX_LAST] + " ");
            int seqNum = ((int) (header[INDEX_SEQ_NUMBER] & 0xff) << 8) |
                    ((int) header[INDEX_SEQ_NUMBER+1] & 0xff);
            System.out.print(seqNum + " ");
            System.out.println();

        } // printPacket
    }

    private Packet packet;
    ByteArrayOutputStream baos;
    ArrayList<byte[]> packetList;
    boolean isFragmented;
    int packetSeqNum;

    public Protocol() {init();}

    public void init() {
        packet = new Packet();
        baos = new ByteArrayOutputStream();
        packetList = new ArrayList<>();
        isFragmented = false;
        packetSeqNum = 0;
    }

    public void setHeader(
            int messageType,
            int action,
            int code
    ) {

        packet.header[INDEX_MESSAGE_TYPE] = (byte)messageType;
        packet.header[INDEX_ACTION] = (byte)action;
        packet.header[INDEX_CODE] = (byte)code;

    }

    public void setBodyLength() {

        baos.size();

        packet.body = baos.toByteArray();
        int bodyLength = packet.body.length;

        packet.header[INDEX_BODY_LENGTH] = (byte)(bodyLength >> 8);
        packet.header[INDEX_BODY_LENGTH+1] = (byte)(bodyLength);

    }


    // data의 0번째 index부터 count 만큼
    public void addBodyStringData(byte[] data) {

        try {

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

        baos.write((byte)(data >> 8));
        baos.write((byte)(data));

    }

    public byte[] getPacket() {

        setBodyLength();
        byte[] rs = new byte[LEN_HEADER_SIZE + packet.body.length];
        System.arraycopy(packet.header, 0, rs, 0, LEN_HEADER_SIZE);
        System.arraycopy(packet.body, 0, rs, LEN_HEADER_SIZE, packet.body.length);
        packet.printPacket();
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