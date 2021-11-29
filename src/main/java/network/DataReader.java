package network;

public class DataReader {

    byte[] buffer;
    int index;

    public DataReader(byte[] body) {
        buffer = body;
        index = 0;
    }

    public int readInt() throws ArrayIndexOutOfBoundsException {

        int result = ((int) (buffer[index++] & 0xff) << 8) |
                    ((int) buffer[index++] & 0xff);

        return result;
    }

    public String readString() throws ArrayIndexOutOfBoundsException {

        int length = readInt();
        String result = new String(buffer, index, length);
        index += length;

        return result;
    }

}
