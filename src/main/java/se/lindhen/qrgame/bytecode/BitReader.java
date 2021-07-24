package se.lindhen.qrgame.bytecode;

import java.math.BigInteger;
import java.util.Arrays;

import static se.lindhen.qrgame.bytecode.BitWriter.*;

public class BitReader {

    private final byte[] buffer;
    private int index;
    private int offset;

    public BitReader(byte[] buffer) {
        this.buffer = Arrays.copyOf(buffer, buffer.length);
    }

    public static int[] byteArrayToIntArray(byte[] bytes) {
        int index = 0;
        int[] data = new int[bytes.length/4];
        for (int i = 0; i < bytes.length; i += 4) {
            data[index++] = (((int) bytes[i] & 255) << 24) | (((int)bytes[i+1] & 255) << 16) | (((int) bytes[i+2] & 255) << 8) | (((int) bytes[i+3] & 255));
        }
        return data;
    }

    public long read(int size) {
        long result = 0;
        /*
         * Let's say offset is 6 and we want to read 5 bytes:
         * the overflow is (size + offset - 8 = 5 + 6 - 8 = 3)
         * the bits in the first part is (size - overflow = 5 - 3 = 2)
         *
         * We use the move 1 to the left bitsInPart steps and subtract one, then we'll get:
         * 1 << 2 => 0000 0100, subtract by one: 0000 0100 - 1 = 0000 0011.
         * Use this mask to get the last 2 bits
         * xxxx xxaa
         * 0000 0011
         * -------- bitwise and
         * 0000 00aa = first part
         *
         * Shift that number by the overflow and store in result:
         * 0000 00aa << 3 = 000a a000
         * adjust index, reset offset and subtract bitsInPart from size
         */
        while (size + offset > 8) {
            int overflow = size + offset - 8;
            int bitsInPart = size - overflow;
            long part = buffer[index++] & ((1L << bitsInPart) - 1L);
            result = result | (part << overflow);
            offset = 0;
            size -= bitsInPart;
        }
        /*
         * Read what's left:
         *
         * continuing from the previous example offset would be 0 but lets just say its 2 to get a more complicated example.
         * xxaa axxx
         *
         * From the example above we now have size bits left to read:
         * size is now 5 - 2 = 3
         * calculate how much we should right shift the value:
         * 8 - (offset + size) = 8 - (2 + 3) = 3
         *
         * right shift the value: xxaa axxx => 000x xaaa
         *
         * Calculate the mask for the bits we care about:
         * (1 << size) - 1 = (0000 0001 << 3) - 1 = 0000 1000 - 1 = 0000 0111
         *
         * mask our read value and store it in result
         *
         */
        if (size > 0) {
            int rightShifted = buffer[index] >>> (8 - (offset + size));
            offset += size;
            result = result | (rightShifted & ((1L << size) - 1L));
        }
        return result;
    }

    public int readInt() {
        return (int) readSignedValue(5, Integer.MAX_VALUE);
    }

    public long readLong() {
        return readSignedValue(6, Long.MAX_VALUE);
    }

    private long readSignedValue(int exponentSize, long maxValue) {
        if(readBool()) {
            return 0;
        }
        int exponent = (int) read(exponentSize);
        long rest = read(exponent);
        BigInteger unsigned = BigInteger.ONE.shiftLeft(exponent).add(BigInteger.valueOf(rest));
        return unsigned.compareTo(BigInteger.valueOf(maxValue)) > 0 ? -unsigned.subtract(BigInteger.valueOf(maxValue)).longValueExact() : unsigned.longValueExact();
    }

    public int readPositiveByte() {
        return readUnsignedValue(3);
    }

    public int readPositiveWord() {
        return readUnsignedValue(4);
    }

    private int readUnsignedValue(int exponentSize) {
        int exponent = (int) read(exponentSize);
        int rest = (int) read(exponent);
        return (1 << exponent) + rest - 1;
    }

    public double readDouble() {
        boolean isInfinite = readBool();
        if (isInfinite) {
            return readBool() ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        boolean isNan = readBool();
        if (isNan) {
            return Double.NaN;
        }
        long negativeBit = readBool() ? DOUBLE_SIGN_MASK : 0;
        long mantissa = readLong();
        long exponent = readPositiveWord();
        return Double.longBitsToDouble(negativeBit | (exponent << DOUBLE_EXPONENT_OFFSET) | mantissa);
    }

    public boolean readBool() {
        return read(1) == 1;
    }

    public CommandCode readCommand() {
        boolean isCommon = readBool();
        int code = (int) read(isCommon ? COMMON_COMMAND_SIZE : COMMAND_SIZE);
        return CommandCode.fromParams(isCommon, code);
    }
}
