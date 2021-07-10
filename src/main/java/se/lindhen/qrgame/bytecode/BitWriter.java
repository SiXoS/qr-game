package se.lindhen.qrgame.bytecode;

import se.lindhen.qrgame.qr.QrCreator;

import java.util.HashMap;
import java.util.Map;

public class BitWriter {


    static final int COMMAND_SIZE = 5;
    static final int COMMON_COMMAND_SIZE = 2;
    // We don't divide by 4 here because we want to avoid out of bounds exceptions
    private final int[] buffer = new int[QrCreator.BYTES_IN_QR_CODE];
    private int index = 0;
    private int offset = 0;
    private static final int MAX_BYTE_SIZE = (1 << 8) - 2; // -2 as our encoding doesn't support zero so we have to add all numbers by 1
    private String context = null;
    private final HashMap<String, Integer> contextualBitsWritten = new HashMap<>();

    public void writeCommand(CommandCode command) {
        writeBool(command.isCommon());
        write(command.isCommon() ? COMMON_COMMAND_SIZE : COMMAND_SIZE, command.getCode());
    }

    public void write(int size, int value) {
        assert size <= 32;
        trackBytesWritten(size, context == null ? "unknown" : context);
        if (size < 32) {
            int maskedValue = value & ((1 << size) - 1);
            if (maskedValue != value) {
                throw new IllegalArgumentException("The value '" + value + "' (" + Integer.toString(value, 2) + ") does not fit in " + size + " bits");
            }
        }
        /*
         * example: xxxxxxxx xxxxxxxx xxxxxxxx xx000000
         * offset: 26
         * to insert: aaaa (size 4)
         * to get how much we need to left shift our number we do 32 - size - offset (32 - 4 - 26 = 2) which becomes:
         * aaaa00. We or this with the current value:
         * xxxxxxxx xxxxxxxx xxxxxxxx xx000000
         *                              aaaa00
         * ----------------------------------- OR
         * xxxxxxxx xxxxxxxx xxxxxxxx xxaaaa00
         */
        if (size + offset <= 32) {
            buffer[index] = buffer[index] | (value << (32 - size - offset) );
            offset += size; // don't need to check if offset == 32. Second if-case will solve that on next write.
        /*
         * Too big to fit in what's left. Need to split the number. Push as much as possible to current index and rest to next.
         * example: xxxxxxxx xxxxxxxx xxxxxxxx xxxx0000
         * offset: 28
         * to insert: aaaaaa (size 6)
         * calculate how much that can't fit: size - (32 - offset) (6 - 32 + 28 = 2)
         * 1. Shift off the overflow and place it in current buffer
         * 2. Move the overflow to the leftmost bits (32 - overflow (32 - 2) = 30)
         */
        } else {
            int overflow = size - (32 - offset);
            buffer[index] = buffer[index] | (value >>> overflow);
            buffer[++index] = buffer[index] | (value << (32 - overflow));
            offset = overflow;
        }
    }

    private void trackBytesWritten(int size, String target) {
        contextualBitsWritten.compute(target, (k,v) -> (v == null ? 0 : v) + size);
    }

    public void setContext(String context) {
        if (this.context != null) {
            throw new IllegalStateException("Already in context " + this.context);
        }
        this.context = context;
    }

    public void exitContext() {
        this.context = null;
    }

    public Map<String, Integer> getContextualBitsWritten() {
        return contextualBitsWritten;
    }

    public byte[] getBuffer() {
        return intArrayToByteArray(buffer, index + 1);
    }

    public static byte[] intArrayToByteArray(int[] data, int size) {
        byte[] bytes = new byte[size*4];
        int bytesIndex = 0;
        for (int i = 0; i < size; i++) {
            bytes[bytesIndex++] = (byte) (data[i] >>> 24);
            bytes[bytesIndex++] = (byte) (data[i] >>> 16);
            bytes[bytesIndex++] = (byte) (data[i] >>> 8);
            bytes[bytesIndex++] = (byte) data[i];
        }
        return bytes;
    }

    /*
     * example: 135
     * log2(135) = 7.07 (00111b0)
     * rest = 135 - (1 << 7 = 1000000b0 = 64) = 71 (1000111b0) (notice that the required bits for the rest is the same as floor(log2(135)))
     *
     */
    public void writeInt(int number) {
        if(number == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Can only write integers down to one more than minimum value");
        }
        long positiveNumber = number < 0 ? -((long) number) + Integer.MAX_VALUE : number; // using long as unsigned int
        if(positiveNumber == 0) {
            writeBool(true);
            return;
        } else {
            writeBool(false);
        }
        int exponent = (int) Math.floor(MathUtil.log2(positiveNumber));
        long rest = positiveNumber - (1L << exponent);
        write(5, exponent);
        write(exponent, (int) rest);
    }

    public void writePositiveByte(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Bytes have to be zero or greater, was " + value);
        }
        if (value > MAX_BYTE_SIZE) {
            throw new IllegalArgumentException("Bytes has to be less than " + MAX_BYTE_SIZE + ", was " + value);
        }
        int actualValue = value + 1;
        int exponent = (int) Math.floor(MathUtil.log2(actualValue));
        int rest = actualValue - (1 << exponent);
        write(3, exponent);
        write(exponent, rest);
    }

    public void writeBool(boolean bool) {
        write(1, bool ? 1 : 0);
    }

    public int getBytesWritten() {
        return index*4 + (int) Math.ceil(offset/8.0);
    }
}
