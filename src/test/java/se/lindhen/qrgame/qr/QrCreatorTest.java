package se.lindhen.qrgame.qr;

import com.google.zxing.WriterException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class QrCreatorTest {

    @Test
    public void testGenerateAndReadQr() throws IOException, WriterException {
        byte[] ints = new byte[]{(byte) 65, (byte) 124, (byte) -110};
        Path tempFile = Files.createTempFile("qr", ".png");
        QrCreator.createQrImage(ints, tempFile);
        byte[] reconvertedInts = QrCreator.readQrImage(new File(tempFile.toUri()));
        Assert.assertEquals(3, reconvertedInts.length);
        Assert.assertEquals(65, reconvertedInts[0]);
        Assert.assertEquals(124, reconvertedInts[1]);
        Assert.assertEquals(-110, reconvertedInts[2]);
    }

}
