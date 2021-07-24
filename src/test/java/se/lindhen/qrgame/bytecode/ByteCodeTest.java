package se.lindhen.qrgame.bytecode;

import org.junit.Assert;
import org.junit.Test;

public class ByteCodeTest {

    @Test
    public void testWriteAndReadMany() {
        int[][] toWrite = new int[][]{
                new int[]{4,0x7},
                new int[]{6, 0x3F},
                new int[]{1, 0},
                new int[]{1, 1},
                new int[]{13, 0xA45},
                new int[]{20, 0x8E3B},
                new int[]{7, 0x7},
                new int[]{30, 0x29D8B24f},
                new int[]{8, 0xFF}};
        BitWriter writer = new BitWriter();
        for (int[] pair : toWrite) {
            writer.write(pair[0], pair[1]);
        }
        byte[] buffer = writer.getBuffer();
        BitReader reader = new BitReader(buffer);
        for (int[] pair : toWrite) {
            int actual = (int) reader.read(pair[0]);
            Assert.assertEquals("Expected " + Integer.toString(pair[1], 16) + " with size " + pair[0] + " but got " + Integer.toString(actual, 16), pair[1], actual);
        }
    }

    private void printBits(int size, int value) {
        System.out.print(String.format("%" + size + "s", Integer.toString(value, 2)).replace(' ', '0'));
    }

    @Test
    public void testWriteAndReadArbitraryInt() {
        writeAndReadInt(135);
    }

    @Test
    public void testWriteAndReadArbitraryNegativeInt() {
        writeAndReadInt(-135);
    }

    @Test
    public void testWriteAndReadZero() {
        writeAndReadInt(0);
    }

    @Test
    public void testWriteAndReadOne() {
        writeAndReadInt(1);
    }

    @Test
    public void testWriteAndReadMinusOne() {
        writeAndReadInt(-1);
    }

    @Test
    public void testWriteAndReadMaxInt() {
        writeAndReadInt(Integer.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteAndReadMinInt() {
        int number = Integer.MIN_VALUE;
        BitWriter writer = new BitWriter();
        writer.writeInt(number);
    }

    @Test
    public void testWriteAndReadOneMoreThanMinInt() {
        writeAndReadInt(Integer.MIN_VALUE + 1);
    }

    @Test
    public void testWriteAndReadArbitraryLong() {
        writeAndReadLong(135);
    }

    @Test
    public void testWriteAndReadArbitraryNegativeLong() {
        writeAndReadLong(-135);
    }

    @Test
    public void testWriteAndReadZeroLong() {
        writeAndReadLong(0);
    }

    @Test
    public void testWriteAndReadOneLong() {
        writeAndReadLong(1);
    }

    @Test
    public void testWriteAndReadMinusOneLong() {
        writeAndReadLong(-1);
    }

    @Test
    public void testWriteAndReadMaxLong() {
        writeAndReadLong(Long.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteAndReadMinLong() {
        Long number = Long.MIN_VALUE;
        BitWriter writer = new BitWriter();
        writer.writeLong(number);
    }

    @Test
    public void testWriteAndReadOneMoreThanMinLong() {
        writeAndReadLong(Long.MIN_VALUE + 1);
    }

    @Test
    public void testWriteAndReadMaxPosByte() {
        writeAndReadPositiveByte(254);
    }

    @Test
    public void testWriteAndReadZeroPosByte() {
        writeAndReadPositiveByte(0);
    }

    @Test
    public void testWriteAndReadArbitraryPosByte() {
        writeAndReadPositiveByte(42);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteAndReadTooBigPosByte() {
        writeAndReadPositiveByte(255);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteAndReadNegativePosByte() {
        writeAndReadPositiveByte(255);
    }

    @Test
    public void testWriteAndReadArbitraryDouble() {
        writeAndReadDouble(135.643);
    }

    @Test
    public void testWriteAndReadArbitraryNegativeDouble() {
        writeAndReadDouble(-135.6345);
    }

    @Test
    public void testWriteAndReadBigDouble() {
        writeAndReadDouble(400000000D);
    }

    @Test
    public void testWriteAndReadVeryNegativeDouble() {
        writeAndReadDouble(-400000000D);
    }

    @Test
    public void testWriteAndReadBigDecimalDouble() {
        writeAndReadDouble(4000.23364554353545353453534535345345645344);
    }

    @Test
    public void testWriteAndReadVeryNegativeDecimalDouble() {
        writeAndReadDouble(-400.4522523);
    }

    @Test
    public void testWriteAndReadZeroDouble() {
        writeAndReadDouble(0D);
    }

    @Test
    public void testWriteAndReadOneDouble() {
        writeAndReadDouble(1D);
    }

    @Test
    public void testWriteAndReadMinusOneDouble() {
        writeAndReadDouble(-1D);
    }

    @Test
    public void testWriteAndReadMaxDouble() {
        writeAndReadDouble(Double.MAX_VALUE);
    }

    @Test
    public void testWriteAndReadMinDouble() {
        writeAndReadDouble(Double.MIN_VALUE);
    }

    private void writeAndReadInt(int number) {
        BitWriter writer = new BitWriter();
        writer.writeInt(number);
        BitReader reader = new BitReader(writer.getBuffer());
        int result = reader.readInt();
        Assert.assertEquals(number, result);
    }
    private void writeAndReadLong(long number) {
        BitWriter writer = new BitWriter();
        writer.writeLong(number);
        BitReader reader = new BitReader(writer.getBuffer());
        long result = reader.readLong();
        Assert.assertEquals(number, result);
    }

    private void writeAndReadPositiveByte(int number) {
        BitWriter writer = new BitWriter();
        writer.writePositiveByte(number);
        BitReader reader = new BitReader(writer.getBuffer());
        int result = reader.readPositiveByte();
        Assert.assertEquals(number, result);
    }

    private void writeAndReadDouble(Double number) {
        BitWriter writer = new BitWriter();
        writer.writeDouble(number);
        BitReader reader = new BitReader(writer.getBuffer());
        double result = reader.readDouble();
        Assert.assertEquals(number, result, 0.000001);
    }

}
