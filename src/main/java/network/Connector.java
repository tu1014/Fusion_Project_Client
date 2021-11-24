package network;

import java.io.*;
import java.net.Socket;

public class Connector {

    private static Socket socket;
    private static byte[] header;
    private static byte[] body;
    private static InputStream is;
    private static OutputStream os;
    private static ByteArrayInputStream bais;
    private static DataInputStream dis;

    public static boolean connect() {

        try {

            socket = new Socket("127.0.0.1", 3000);
            System.out.println("서버 접속 성공");
            header = new byte[Protocol.LEN_HEADER_SIZE];
            is = socket.getInputStream();
            os = socket.getOutputStream();

            return true;
        }

        catch(IOException ioe) {ioe.printStackTrace();}

        return false;

    }

    public static Socket getSocket() {
        return socket;
    }

    public static void read() {

        System.out.println("대기중...");
        try {

            is.read(header);
            System.out.println("데이터 수신!");

            int bodyLength = byteToInt(header, Protocol.INDEX_BODY_LENGTH);

            body = new byte[bodyLength];
            if (bodyLength != 0) is.read(body);

            bais = new ByteArrayInputStream(body);
            dis = new DataInputStream(bais);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int byteToInt(byte[] data, int pos) {

        int result = ((int) (data[pos] & 0xff) << 8) |
                ((int) data[pos+1] & 0xff);

        return result;

    }

    public static String readUTF() {
        String rs = "";

        try {
            rs =  dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rs;
    }

    public static int readInt() {
        int rs = 0;
        try {
            rs = dis.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rs;
    }




}
