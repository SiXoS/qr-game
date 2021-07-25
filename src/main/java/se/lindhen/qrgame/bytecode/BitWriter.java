package se.lindhen.qrgame.bytecode;

import se.lindhen.qrgame.qr.QrCreator;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BitWriter {

    static final int COMMAND_SIZE = 5;
    static final int COMMON_COMMAND_SIZE = 2;
    // Multiply by 4 to avoid out of bounds exceptions
    private final byte[] buffer = new byte[QrCreator.BYTES_IN_QR_CODE*4];
    private int index = 0;
    private byte offset = 0;
    private static final int MAX_BYTE_SIZE = (1 << 8) - 2; // -2 as our encoding doesn't support zero so we have to add all numbers by 1
    private static final int MAX_WORD_SIZE = (1 << 16) - 2;
    static final long DOUBLE_EXPONENT_MASK = 0x7ff0000000000000L;
    static final int DOUBLE_EXPONENT_OFFSET = 52;
    static final long DOUBLE_MANTISSA_MASK = 0x000fffffffffffffL;
    static final long DOUBLE_SIGN_MASK = 0x8000000000000000L;
    private String context = null;
    private final HashMap<String, Integer> contextualBitsWritten = new HashMap<>();

    public void writeCommand(CommandCode command) {
        writeBool(command.isCommon());
        write(command.isCommon() ? COMMON_COMMAND_SIZE : COMMAND_SIZE, command.getCode());
    }

    public void write(int size, long value) {
        assert size <= 64;
        trackBytesWritten(size, context == null ? "unknown" : context);
        if (size < 64) {
            long maskedValue = value & ((1L << size) - 1L);
            if (maskedValue != value) {
                throw new IllegalArgumentException("The value '" + value + "' (" + Long.toString(value, 2) + ") does not fit in " + size + " bits");
            }
        }
        /*
         * Too big to fit in what's left. Need to split the number. Push as much as possible to current index and rest to next.
         * example: [xxxxx000]
         * offset: 5
         * to insert: aaaaaaaaaa (size 10)
         * calculate how much to store: 8 - offset = 8 - 5 = 3
         * calculate overflow: size - toStore = 8
         * Shift off the overflow and place it in current buffer [xxxxxaaa]
         * create mask for what's left: (1 << overflow) - 1 = 100000000 - 1 = 11111111
         * mask the value: aaa aaaaaaa & 11111111 = aaaaaaaa
         * reset offset
         * set size to overflow
         */
        while (size + offset > 8) {
            int toStore = 8 - offset;
            int overflow = size - toStore;
            buffer[index] = (byte) (buffer[index] | (value >>> overflow));
            value = value & ((1L << overflow) - 1L);
            index++;
            offset = 0;
            size = overflow;
        }
        /*
         * example: xx000000
         * offset: 2
         * to insert: aaaa (size 4)
         * to get how much we need to left shift our number we do 8 - size - offset (8 - 4 - 2 = 2) which becomes:
         * When left-shifted we get aaaa00
         * aaaa00. We or this with the current value:
         * xx000000
         *   aaaa00
         * -------- OR
         * xxaaaa00
         * and add the size to the offset (2 + 4 = 6 which is the new ofset)
         */
        if (size > 0) {
            buffer[index] = (byte) (buffer[index] | (value << (8 - size - offset)));
            offset += size;
            if (offset == 8) {
                index++;
                offset = 0;
            }
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
        return Arrays.copyOf(buffer, index + 1);
    }

    public void writeInt(int number) {
        writeSignedNumber(number, Integer.MIN_VALUE, Integer.MAX_VALUE, 5);
    }

    public void writeLong(long number) {
        writeSignedNumber(number, Long.MIN_VALUE, Long.MAX_VALUE, 6);
    }

    /*
     * example: 135
     * exponent = numberOfBits(135) - 1 = numberOfBits(1000 0111) - 1 = 7
     * rest = 135 - (1 << 7 = 100 0000 = 64) = 71 (100 0111) (notice that the required bits for the rest is the same as the value of exponent)
     *
     */
    private void writeSignedNumber(long number, long minValue, long maxValue, int exponentSize) {
        if(number <= minValue) {
            throw new IllegalArgumentException("Value too small, must be greater than " + minValue);
        }
        if(number == 0) {
            writeBool(true);
            return;
        } else {
            writeBool(false);
        }
        BigInteger bigNumber = number < 0 ? BigInteger.valueOf(-number).add(BigInteger.valueOf(maxValue)) : BigInteger.valueOf(number);
        int exponent = bigNumber.bitLength() - 1;
        long rest = bigNumber.subtract(BigInteger.ONE.shiftLeft(exponent)).longValue();
        write(exponentSize, exponent);
        write(exponent, rest);
    }

    public void writeDouble(double number) {
        if (Double.isInfinite(number)) {
            writeBool(true);
            writeBool(number > 0);
            return;
        }
        writeBool(false);
        if (Double.isNaN(number)) {
            writeBool(true);
            return;
        }
        writeBool(false);
        long bits = Double.doubleToLongBits(number);
        long mantissa = bits & DOUBLE_MANTISSA_MASK;
        int exponent = (int) ((bits & DOUBLE_EXPONENT_MASK) >>> DOUBLE_EXPONENT_OFFSET);
        writeBool((bits & DOUBLE_SIGN_MASK) != 0);
        writeLong(mantissa);
        writePositiveWord(exponent);
    }

    public void writePositiveByte(int value) {
        writeUnsignedValue(value, MAX_BYTE_SIZE, 3);
    }

    private void writePositiveWord(int value) {
        writeUnsignedValue(value, MAX_WORD_SIZE, 4);
    }

    private void writeUnsignedValue(int value, int maxSize, int exponentSize) {
        if (value < 0) {
            throw new IllegalArgumentException("Unsigned values have to be zero or greater, was " + value);
        }
        if (value > maxSize) {
            throw new IllegalArgumentException("Unsigned value has to be less than " + maxSize + ", was " + value);
        }
        int actualValue = value + 1;
        int exponent = (int) Math.floor(MathUtil.log2(actualValue));
        int rest = actualValue - (1 << exponent);
        write(exponentSize, exponent);
        write(exponent, rest);
    }

    public void writeBool(boolean bool) {
        write(1, bool ? 1 : 0);
    }

    public int getBytesWritten() {
        return index + (int) Math.ceil(offset/8.0);
    }
}
