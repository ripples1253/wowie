package rip.osu.bancho.packet.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;


public class ByteDataInputStream extends InputStream {

    private final DataInputStream input;

    public ByteDataInputStream(InputStream stream) {
        input = new DataInputStream(stream);
    }

    public int getProtocolVersion() {
        return 19;
    }

    @Override
    public int read() throws IOException {
        return input.readUnsignedByte();
    }

    public int readInt() throws IOException {
        return readBytes(4);
    }

    public short readShort() throws IOException {
        return (short) readBytes(2);
    }

    public int readUShort() throws IOException {
        return readBytes(2);
    }

    public boolean readBoolean() throws IOException {
        return readByte() == 1;
    }

    public short readUByte() throws IOException {
        return (short) readBytes(1);
    }

    public int readBytes(int num) throws IOException {
        int sum = 0;
        for (int i = 0; i < num; i++)
            sum += (input.readUnsignedByte() << i * 8);
        return sum;
    }

    public void skipBytes(int n) throws IOException {
        input.skipBytes(n);
    }

    public byte readByte() throws IOException {
        return input.readByte();
    }

    public int read7BitInteger() throws IOException {
        int count = 0, shift = 0;
        byte b;
        do {
            if (shift == 5 * 7)
                throw new IllegalArgumentException("Bad 7-bit integer format");
            b = readByte();
            count |= (b & 0x7F) << shift;
            shift += 7;
        } while ((b & 0x80) != 0);
        return count;
    }

    public String readString() throws IOException {
        byte typeCheck = readByte();
        if (typeCheck == 0)
            return null;
        if (typeCheck != 11)
            throw new IOException("Input is not a string");
        int len = read7BitInteger();
        if (len == 0) return "";
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++)
            bytes[i] = readByte();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public byte[] readByteArray(int length) throws IOException {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++)
            bytes[i] = readByte();
        return bytes;
    }

    public float readFloat() throws IOException {
        byte[] bytes = readByteArray(4);
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    public double readDouble() throws IOException {
        byte[] bytes = readByteArray(8);
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getDouble();
    }

    public long readLong() throws IOException {
        long int1 = readInt();
        long int2 = readInt();
        return int1 + (int2 << 32);
    }

    public int[] readIntList() throws IOException {
        int length = readInt();
        int[] list = new int[length];
        for (int i = 0; i < length; i++) {
            list[i] = readInt();

        }
        return list;
    }
}