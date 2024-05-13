package rip.osu.bancho.packet.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j(topic = "Packet Out Writer")
@Getter
public class ByteDataOutputStream extends OutputStream {
    private final DataOutputStream output;
    private final ByteArrayOutputStream buffer;
    private final ByteArrayOutputStream outputBuffer;

    public ByteDataOutputStream(OutputStream stream) {
        this.output = new DataOutputStream(stream);
        this.buffer = new ByteArrayOutputStream();
        this.outputBuffer = new ByteArrayOutputStream();
    }

    public static void printBytesAsHex(byte[] bytes, int packetId, int bufferSize) {
        StringBuilder output = new StringBuilder();
        output.append(String.format("%s packet struct (size: %s):\n", packetId, bufferSize));
        for (byte b : bytes) {
            output.append(String.format("%02X ", b));
        }

        log.info(String.valueOf(output));
    }

    public int getProtocolVersion() {
        return 19;
    }

    public void dump(int packetId) throws IOException {
        writeShortToOutputBuffer((short) packetId)
                .writeByteToOutputBuffer((byte) 0)
                .writeIntToOutputBuffer(buffer.size())
                .writeBytesToOutputBuffer(buffer.toByteArray());

        output.write(outputBuffer.toByteArray());
        printBytesAsHex(outputBuffer.toByteArray(), packetId, buffer.size());
    }

    @Override
    public void write(int integer) throws IOException {
        writeInt(integer);
    }

    public ByteDataOutputStream writeInt(int number) throws IOException {
        buffer.write(new byte[]{
                (byte) (number & 0xFF),
                (byte) ((number >> 8) & 0xFF),
                (byte) ((number >> 16) & 0xFF),
                (byte) ((number >> 24) & 0xFF),
        });

        return this;
    }

    public ByteDataOutputStream writeIntToOutputBuffer(int number) throws IOException {
        outputBuffer.write(new byte[]{
                (byte) (number & 0xFF),
                (byte) ((number >> 8) & 0xFF),
                (byte) ((number >> 16) & 0xFF),
                (byte) ((number >> 24) & 0xFF),
        });

        return this;
    }

    public ByteDataOutputStream writeShort(short number) throws IOException {
        buffer.write(new byte[]{
                (byte) (number & 0xFF),
                (byte) ((number >> 8) & 0xFF)
        });

        return this;
    }

    public ByteDataOutputStream writeShortToOutputBuffer(short number) throws IOException {
        outputBuffer.write(new byte[]{
                (byte) (number & 0xFF),
                (byte) ((number >> 8) & 0xFF)
        });

        return this;
    }

    public ByteDataOutputStream writeBytes(byte[] array) throws IOException {
        buffer.write(array);
        return this;
    }

    public ByteDataOutputStream writeByte(byte number) throws IOException {
        buffer.write(new byte[]{number});
        return this;
    }

    public ByteDataOutputStream writeBytesToOutputBuffer(byte[] array) throws IOException {
        outputBuffer.write(array);
        return this;
    }

    public ByteDataOutputStream writeByteToOutputBuffer(byte number) throws IOException {
        outputBuffer.write(new byte[]{number});
        return this;
    }

    public ByteDataOutputStream write7BitInteger(int integer) throws IOException {
        long v = integer & 0xFFFFFFFF; // negative numbers. not even gunna lie, idk how this works
        while (v >= 0x80) {
            writeByte((byte) (v | 0x80));
            v >>= 7;
        }
        writeByte((byte) v);
        return this;
    }

    public ByteDataOutputStream writeString(String str) throws IOException {
        if (str.isEmpty()) {
            writeByte((byte) 0);
            return this;
        }

        writeByte((byte) 11); // tell osu! that it's not a null string
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        write7BitInteger(bytes.length); // does this fuckery even work
        buffer.write(bytes);
        return this;
    }

    public ByteDataOutputStream writeFloat(float value) throws IOException {
        byte[] bytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(value).array();
        writeBytes(bytes);
        return this;
    }

    public ByteDataOutputStream writeLong(long number) throws IOException {
        buffer.write(new byte[]{
                (byte) (number & 0xFF),
                (byte) ((number >> 8) & 0xFF),
                (byte) ((number >> 16) & 0xFF),
                (byte) ((number >> 24) & 0xFF),
                (byte) ((number >> 32) & 0xFF),
                (byte) ((number >> 40) & 0xFF),
                (byte) ((number >> 48) & 0xFF),
                (byte) ((number >> 56) & 0xFF),
        });
        return this;
    }

    public ByteDataOutputStream writeIntList(int[] list) throws IOException {
        writeInt(list.length);
        int i;
        for (i = 0; i < list.length; i++) {
            writeInt(list[i]);
        }
        return this;
    }

    public ByteDataOutputStream writeIntList(Integer[] list) throws IOException {
        writeInt(list.length);
        int i;
        for (i = 0; i < list.length; i++) {
            writeInt(list[i]);
        }
        return this;
    }

    public ByteDataOutputStream writeIntList(List<Integer> list) throws IOException {
        writeInt(list.size());
        for (int i : list) {
            writeInt(i);
        }
        return this;
    }
}