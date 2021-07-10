package se.lindhen.qrgame.qr;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;

public class QrCreator {

    public static final Charset CHARSET = StandardCharsets.ISO_8859_1;
    public static final int BYTES_IN_QR_CODE = 2952;

    public static void createQrImage(byte[] data, Path output) throws IOException, WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(byteArrayToString(data), BarcodeFormat.QR_CODE, 1000, 1000);
        MatrixToImageWriter.writeToPath(bitMatrix, "png", output);
    }

    public static byte[] readQrImage(File imageFile) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new QRCodeReader().decode(bitmap, Collections.singletonMap(DecodeHintType.CHARACTER_SET, CHARSET.toString()));
            return stringToByteArray(result.getText());
        } catch (NotFoundException | ChecksumException | FormatException e) {
            throw new IOException("Could not parse QR image: " + e.getMessage(), e);
        }
    }

    public static String byteArrayToString(byte[] data) {
        return new String(data, CHARSET);
    }

    public static byte[] stringToByteArray(String str) {
        return str.getBytes(CHARSET);
    }


}
