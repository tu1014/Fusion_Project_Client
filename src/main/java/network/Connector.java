package network;

import java.io.*;
import java.net.Socket;

/*
클라이언트 측에서는 소켓을 통해 서버와 통신하고 있고,
각 화면의 컨트롤러에서 이를 공유하여야 했습니다.
때문에 Connector와 DataReader 클래스를 만들어 통신을 수행하였습니다
*/
public class Connector {

    private static Socket socket;
    private static byte[] header;
    private static byte[] body;
    private static InputStream is;
    private static OutputStream os;
    private static DataReader reader;
    private static Protocol protocol;

    // 접속
    public static boolean connect() {

        try {

            socket = new Socket("127.0.0.1", 3000);
            System.out.println("서버 접속 성공");
            // 헤더의 길이는 고정
            header = new byte[Protocol.LEN_HEADER_SIZE];
            is = socket.getInputStream();
            os = socket.getOutputStream();
            protocol = new Protocol();

            return true;
        }

        catch(IOException ioe) {ioe.printStackTrace();}

        return false;

    }

    // 각 화면의 컨트롤러에게 프로토콜 전달
    public static Protocol getProtocol() { return protocol; }

    // 각 화면의 컨트롤러에게 소켓 전달
    public static Socket getSocket() {
        return socket;
    }

    public static void read() {

        System.out.println("대기중...");
        try {

            is.read(header);
            System.out.println("데이터 수신!");
            int bodyLength = byteToInt(header, Protocol.INDEX_BODY_LENGTH);

            /*
            헤더의 길이는 고정길이. 헤더를 먼저 읽고 바디의 길이를 알아낸 다음
            그 길이만큼 동적으로 배열을 생성해  바디를 읽는다
            */

            body = new byte[bodyLength];
            if (bodyLength != 0) is.read(body);

            // DataReader는 DataInputStream과 비슷한 역할
            reader = new DataReader(body);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int byteToInt(byte[] data, int pos) {

        // 바이트 배열의 2바이트를 읽어들여 int로 변환 (양의 정수)
        int result = ((int) (data[pos] & 0xff) << 8) |
                ((int) data[pos+1] & 0xff);

        return result;

    }

    // DataReader를 통해 전송받은 바디에서 String을 리턴
    public static String readString() {

        String rs = "";

        try { rs = reader.readString(); }

        // 예외가 발생하면 분할 전송으로 간주하여 다시 데이터를 읽고 String을 리턴한다
        catch (ArrayIndexOutOfBoundsException e) {

            read();
            return readString();
        }

        return rs;
    }

    // 위 String과 같은 로직
    public static int readInt() {

        int rs = 0;

        try { rs = reader.readInt(); }

        catch (ArrayIndexOutOfBoundsException e) {
            read();
            return readInt();
        }

        return rs;
    }

    // 각 컨트롤러에서 header의 값을 조회하기 위해 사용
    public static byte[] getHeader() { return header; }




}
