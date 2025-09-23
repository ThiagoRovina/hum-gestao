package humcare.utilitarios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * ZXing ("zebra crossing") is an open-source, multi-format 1D/2D barcode image
 * processing library implemented in Java.
 *
 * Supported Formats: UPC-A, UPC-E, EAN-8, EAN-13, UPC/EAN Extension 2/5, Code
 * 39, Code 93, Code 128, Codabar, ITF, QR Code, Data Matrix, Aztec, PDF 417,
 * MaxiCode, RSS-14, RSS-Expanded. More info: https://github.com/zxing/zxing
 *
 * @author alison
 */
public class Barcode {

    public byte[] createByteArrayImage(String data, BarcodeFormat type, int height, int width) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes("UTF-8"), "UTF-8"), type, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig con = new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF);
        MatrixToImageWriter.writeToStream(matrix, "PNG", pngOutputStream, con);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }

    public BufferedImage createBufferedImage(String data, BarcodeFormat type , int height, int width) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes("UTF-8"), "UTF-8"), type, width, height);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    public void createDiskImage(String data, BarcodeFormat type, int height, int width, String path) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes("UTF-8"), "UTF-8"), type, width, height);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
    }

    public String readFromDisk(String path) throws FileNotFoundException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(path)))));
        Result result = new MultiFormatReader().decode(binaryBitmap);
        return result.getText();
    }

}
