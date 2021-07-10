package se.lindhen.qrgame.bytecode;

import static se.lindhen.qrgame.bytecode.BitWriter.COMMAND_SIZE;
import static se.lindhen.qrgame.bytecode.BitWriter.COMMON_COMMAND_SIZE;

public class BitReader {

    private final int[] buffer;
    private int index;
    private int offset;

    public BitReader(byte[] buffer) {
        this.buffer = byteArrayToIntArray(buffer);
    }

    public static int[] byteArrayToIntArray(byte[] bytes) {
        int index = 0;
        int[] data = new int[bytes.length/4];
        for (int i = 0; i < bytes.length; i += 4) {
            data[index++] = (((int) bytes[i] & 255) << 24) | (((int)bytes[i+1] & 255) << 16) | (((int) bytes[i+2] & 255) << 8) | (((int) bytes[i+3] & 255));
        }
        return data;
    }

    public int read(int size) {
        assert size <= 32;
        /*
         * Let's say offset is 20 and we want to read 4 bytes:
         * xxxxxxxx xxxxxxxx xxxxaaaa xxxxxxxx
         * We first move our number to the right (32 - (offset + size) = 32 - (20 + 4) = 8):
         * 00000000 xxxxxxxx xxxxxxxx xxxxaaaa
         *
         * We then take a 1 and move it to the left of our number:
         * 00000000 00000000 00000000 00010000
         * Taking the above number and subtracting 1 we get:
         * 00000000 00000000 00000000 00001111
         * Which we can bitwise and with the number above to only get the relevant bits
         */
        if (size + offset <= 32) {
            int rightShifted = buffer[index] >>> (32 - (offset + size));
            offset += size;
            return rightShifted & ((1 << size) - 1);
        /*
         * Let's say offset is 30 and we want to read 5 bytes:
         * the overflow is (size + offset - 32 = 5 + 30 - 32 = 3)
         * the bits in the first part is (size - overflow = 5 - 3 = 2)
         *
         * We use the move 1 to the left and subtract 1 trick described above to get:
         * xxxxxxxx xxxxxxxx xxxxxxxx xxxxxxaa
         * 00000000 00000000 00000000 00000011
         * ----------------------------------- bitwise and
         * 00000000 00000000 00000000 000000aa = first part
         *
         * Second part will be at the left-most position so we just move it (32 - overflow = 32 - 3 = 29) steps
         * bbbxxxxx xxxxxxxx xxxxxxxx xxxxxxxx
         * ----------------------------------- >>> 29
         * 00000000 00000000 00000000 00000bbb
         *
         * Move the first part to the left and merge them with bitwise or:
         * 00000000 00000000 00000000 000000aa
         * ----------------------------------- << overflow (3)
         * 00000000 00000000 00000000 000aa000
         *
         * 00000000 00000000 00000000 000aa000
         * 00000000 00000000 00000000 00000bbb
         * ----------------------------------- bitwise or
         * 00000000 00000000 00000000 000aabbb
         */
        } else {
            int overflow = size + offset - 32;
            int bitsInFirstPart = size - overflow;
            int firstPart = buffer[index++] & ((1 << bitsInFirstPart) - 1);
            int secondPart = buffer[index] >>> 32 - overflow;
            offset = overflow;
            return (firstPart << overflow) | secondPart;
        }
    }

    public int readInt() {
        if(readBool()) {
            return 0;
        }
        int exponent = read(5);
        int rest = read(exponent);
        long unsigned = (1L << exponent) + (long) rest;
        return unsigned > Integer.MAX_VALUE ? (int) -(unsigned - Integer.MAX_VALUE) : (int) unsigned;
    }

    public int readPositiveByte() {
        int exponent = read(3);
        int rest = read(exponent);
        return (1 << exponent) + rest - 1;
    }

    public boolean readBool() {
        return read(1) == 1;
    }

    public CommandCode readCommand() {
        boolean isCommon = readBool();
        int code = read(isCommon ? COMMON_COMMAND_SIZE : COMMAND_SIZE);
        return CommandCode.fromParams(isCommon, code);
    }
}
