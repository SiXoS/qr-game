package se.lindhen.qrgame.bytecode;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class BitWriterTest {

    @Test
    public void testWriteInt() {
        BitWriter writer = new BitWriter();
        writer.write(32, Integer.MAX_VALUE);
        byte[] buffer = writer.getBuffer();
        Assert.assertEquals(5, buffer.length);
        Assert.assertEquals(0x7F, buffer[0]);
        Assert.assertEquals(-1, buffer[1]);
        Assert.assertEquals(-1, buffer[2]);
        Assert.assertEquals(-1, buffer[3]);
        Assert.assertEquals(0, buffer[4]);
    }

    @Test
    public void testWritePosByte() {
        BitWriter writer = new BitWriter();
        // actual stored value is + 1 as we don't have zero
        // exponent = 7 = 111
        // rest = 255 - 2^7 = 127 = 111 1111
        // first buffer:
        // 111
        //    1 1111
        // second buffer:
        // 1100 0000
        writer.writePositiveByte(254);
        byte[] buffer = writer.getBuffer();
        Assert.assertEquals(2, buffer.length);
        Assert.assertEquals(-1, buffer[0]);
        Assert.assertEquals(-64, buffer[1]);
    }

    @Test
    public void testWrite31Bits() {
        BitWriter writer = new BitWriter();
        // max value is:
        // 01111111 11111111 11111111 11111111
        writer.write(31, Integer.MAX_VALUE);
        byte[] buffer = writer.getBuffer();
        // -2 is:
        // 11111111 11111111 11111111 11111110
        Assert.assertEquals(4, buffer.length);
        Assert.assertEquals(-1, buffer[0]);
        Assert.assertEquals(-1, buffer[1]);
        Assert.assertEquals(-1, buffer[2]);
        Assert.assertEquals(-2, buffer[3]);
    }

    @Test
    public void testWriteMaxLong() {
        BitWriter bitWriter = new BitWriter();
        // 1 bit set to zero for non-zero value
        // should write 6 bit exponent 32:
        // 11 1110
        // and the rest: MAX_VALUE - 2^32 = 9 223 372 036 854 775 807 - 4 611 686 018 427 387 904 = 4 611 686 018 427 387 903
        // 11 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111
        bitWriter.writeLong(Long.MAX_VALUE);
        // result should be:
        // 0111 1101 = 125
        // 1111 1111 = -1
        // 1111 1111
        // 1111 1111
        // 1111 1111
        // 1111 1111
        // 1111 1111
        // 1111 1111
        // 1111 1     = -8
        byte[] buffer = bitWriter.getBuffer();
        Assert.assertEquals(9, buffer.length);
        Assert.assertEquals(125, buffer[0]);
        for (int i = 1; i < 8; i++) {
            Assert.assertEquals("index " + i, -1, buffer[i]);
        }
        Assert.assertEquals(-8, buffer[8]);
    }

    @Test
    public void testWrite4BytesAfterInt() {
        BitWriter writer = new BitWriter();
        writer.write(32, Integer.MAX_VALUE);
        writer.write(4, 3);
        byte[] buffer = writer.getBuffer();
        Assert.assertEquals(0x7F, buffer[0]);
        Assert.assertEquals(-1, buffer[1]);
        Assert.assertEquals(-1, buffer[2]);
        Assert.assertEquals(-1, buffer[3]);
        Assert.assertEquals(3 << 4, buffer[4]);
    }

    @Test
    public void testNumberCrossesBorder(){
        BitWriter writer = new BitWriter();

        // 00111111
        // size is 6 so left shifted number becomes
        // 11111100
        int size6maxNumber = (1 << 6) - 1;
        writer.write(6, size6maxNumber);

        // 0111
        // size is 4 and previous number was size 6, so the 2 leftmost bits of this new
        // number will be in the previous buffer which will result in:
        // 11111101 = -3
        // 11000000
        writer.write(4, 7);
        byte[] buffer = writer.getBuffer();
        Assert.assertEquals(-3, buffer[0]); // +1 is from the second written number
        Assert.assertEquals(-64, buffer[1]); // two bits in the leftmost position
    }

}
