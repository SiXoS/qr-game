package se.lindhen.qrgame.bytecode;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class BitWriterTest {

    @Test
    public void testWriteInt() {
        BitWriter writer = new BitWriter();
        writer.write(32, Integer.MAX_VALUE);
        int[] buffer = BitReader.byteArrayToIntArray(writer.getBuffer());
        Assert.assertEquals(1, buffer.length);
        Assert.assertEquals(Integer.MAX_VALUE, buffer[0]);
    }

    @Test
    public void testWrite31Bits() {
        BitWriter writer = new BitWriter();
        // max value is:
        // 01111111 11111111 11111111 11111111
        writer.write(31, Integer.MAX_VALUE);
        int[] buffer = BitReader.byteArrayToIntArray(writer.getBuffer());
        // -2 is:
        // 11111111 11111111 11111111 11111110
        Assert.assertEquals(1, buffer.length);
        Assert.assertEquals(-2, buffer[0]);
    }

    @Test
    public void testWrite4BytesAfterInt() {
        BitWriter writer = new BitWriter();
        writer.write(32, Integer.MAX_VALUE);
        writer.write(4, 3);
        int[] buffer = BitReader.byteArrayToIntArray(writer.getBuffer());
        Assert.assertEquals(Integer.MAX_VALUE, buffer[0]);
        Assert.assertEquals(3 << 28, buffer[1]);
    }

    @Test
    public void testNumberCrossesBorder(){
        BitWriter writer = new BitWriter();

        //   111111 11111111 11111111 11111111
        // size is 30 so left shifted number becomes
        // 11111111 11111111 11111111 11111100
        int size30maxNumber = (1 << 30) - 1;
        writer.write(30, size30maxNumber);

        //                                0111
        // size is 4 and previous number was size 30, so the 2 leftmost bits of this new
        // number will be in the previous buffer which will result in:
        // 11111111 11111111 11111111 11111101
        // 11000000 00000000 00000000 00000000
        writer.write(4, 7);
        int[] buffer = BitReader.byteArrayToIntArray(writer.getBuffer());
        Assert.assertEquals((size30maxNumber << 2) + 1, buffer[0]); // +1 is from the second written number
        Assert.assertEquals(3 << 30, buffer[1]); // two bits in the leftmost position
    }

}
