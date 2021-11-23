package network;

import java.io.IOException;
import java.net.Socket;

public class Connect {

    private static Socket socket;

    public static boolean connect() {

        try {

            socket = new Socket("127.0.0.1", 3000);
            System.out.println("서버 접속 성공");
            return true;
        }

        catch(IOException ioe) {ioe.printStackTrace();}

        return false;

    }

    public static Socket getSocket() {
        return socket;
    }
}
