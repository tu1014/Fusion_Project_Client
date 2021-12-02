package network;

public class DataReader {

    byte[] buffer;
    int index;

    // 바디의 데이터를 순차적으로 읽으며 필요한 데이터로 변환하여 리턴한다. DataInputStream과 비슷한 역할
    public DataReader(byte[] body) {
        buffer = body;
        index = 0;
    }

    public int readInt() throws ArrayIndexOutOfBoundsException {

        // 양의 정수로 변환하여 리턴
        // 서버는 양의 정수를 고정으로 2바이트로 변환하여 전송한다 (고정길이)
        // 버퍼의 범위를 초과하면 예외를 던짐
        int result = ((int) (buffer[index++] & 0xff) << 8) |
                    ((int) buffer[index++] & 0xff);

        return result;
    }

    public String readString() throws ArrayIndexOutOfBoundsException {

        // 서버 측에서는 String의 길이를 먼저 바디에 담고 String을 담는다 (가변길이)
        // 때문에 수신 측에서도 뒤의 데이터 길이를 먼저 읽고 String을 읽는다
        int length = readInt();
        String result = new String(buffer, index, length);
        index += length;

        return result;
    }

}
