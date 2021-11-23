package network;

public class Protocol {

    public static final byte LEN_HEADER_SIZE = 9;
    public static final byte LEN_MESSAGE_TYPE = 1;
    public static final byte LEN_ACTION = 1;
    public static final byte LEN_CODE = 1;
    public static final byte LEN_BODY_LENGTH = 2;
    public static final byte LEN_FRAG = 1;
    public static final byte LEN_LAST = 1;
    public static final byte LEN_SEQ_NUMBER = 2;
    public static final int LEN_MAX_LENGTH = 1000;

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

        private Packet() {

            header = new byte[LEN_HEADER_SIZE];
            body = new byte[LEN_MAX_LENGTH];

        }

    }

    private Packet packet;
    private int flag = 0;

    public Protocol() {init();}

    public int getCurrentBodyLength() { return flag; }

    public void init() {

        packet = new Packet();
        flag = 0;

    }

    public void setHeader(
            byte messageType,
            byte action,
            byte code,
            byte frag,
            byte last,
            int seqNumber
    ) {

        packet.header[0] = messageType;
        packet.header[1] = action;
        packet.header[2] = code;

        // set body length >> set Body 끝난 이후 설정하는 것이 좋겠다
        /*packet.header[3] = (byte)(bodyLength >> 8);
        packet.header[4] = (byte)(bodyLength);*/

        packet.header[5] = frag;
        packet.header[6] = last;

        // set SeqNumber
        packet.header[7] = (byte)(seqNumber >> 8);
        packet.header[8] = (byte)(seqNumber);

    }

    public void setBodyLength() {

        // set body length >> set Body 끝난 이후 설정하는 것이 좋겠다
        packet.header[3] = (byte)(flag >> 8);
        packet.header[4] = (byte)(flag);

    }


    // data의 0번째 index부터 count 만큼
    public void addBody(byte[] data) {

        int length = data.length;
        packet.body[flag++] = (byte)(length >> 8);
        packet.body[flag++] = (byte)(length);

        System.arraycopy(data, 0, packet.body, flag, length);
        flag += length;

    }

    public byte[] getPacket() {

        byte[] rs = new byte[LEN_HEADER_SIZE + flag];
        System.arraycopy(packet.header, 0, rs, 0, LEN_HEADER_SIZE);
        System.arraycopy(packet.body, 0, rs, LEN_HEADER_SIZE, flag);

        return rs;

    }

    public byte[] getHeader() {return packet.header;}
    public byte[] getBody() {return packet.body;}

    // 프로토콜 사용법
    // Protocol protocol = new Protocol();
    // protocol.setHeader(1, 0, 0, 0, 0, 0); >> 관리자 로그인 요청
    // String id = "adminID";
    // byte[] tmp = id.getBytes();
    // protocol.addBody(tmp, 0, )





}
