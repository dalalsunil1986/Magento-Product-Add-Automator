package mariell.image;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * http://stackoverflow.com/questions/1625137/image-resize-quality-java#answer-16859459
 */
public class ImageUtil {

    public static boolean resizeUsingJavaAlgo(String source, File dest, int width, int height, float jpegQuality) throws IOException {
        BufferedImage sourceImage = ImageIO.read(new FileInputStream(source));
        double ratio = (double) sourceImage.getWidth() / sourceImage.getHeight();

        if (width < 1) {
            width = (int) (height * ratio + 0.4);
        } else if (height < 1) {
            height = (int) (width / ratio + 0.4);
        }

        Image scaled = sourceImage.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
        BufferedImage bufferedScaled = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedScaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(scaled, 0, 0, width, height, null);
        dest.createNewFile();
        writeJpeg(bufferedScaled, dest.getCanonicalPath(), jpegQuality);
        return true;
    }


    /**
     * Write a JPEG file setting the compression quality.
     *
     * @param image    a BufferedImage to be saved
     * @param destFile destination file (absolute or relative path)
     * @param quality  a float between 0 and 1, where 1 means uncompressed.
     * @throws IOException in case of problems writing the file
     */
    public static void writeJpeg(BufferedImage image, String destFile, float quality)
            throws IOException {
        ImageWriter writer = null;
        FileImageOutputStream output = null;
        try {
            writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
            output = new FileImageOutputStream(new File(destFile));
            writer.setOutput(output);
            IIOImage iioImage = new IIOImage(image, null, null);
            writer.write(null, iioImage, param);
        } finally {
            if (writer != null) {
                writer.dispose();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    public static void processImage(String filename) throws IOException {
        BufferedImage bimg = ImageIO.read(new File(filename));
        int width = bimg.getWidth();
        int height = bimg.getHeight();

        writeJpeg(bimg, filename, 0.8f);

//        if (width <= 300 && height <= 300) {
//            System.out.println("Set as thumbnail");
//            return;
//        }

        if (width <= 620 && height <= 800) {
            System.out.println("Don't resize: " + filename);
            return;
        }

        Thumbnails.of(filename).size(620, 800).outputFormat("jpg").toFiles(Rename.NO_CHANGE);
    }

    public static boolean isThumbnailSize(String filename) throws IOException {
        BufferedImage bimg = ImageIO.read(new File(filename));
        int width = bimg.getWidth();
        int height = bimg.getHeight();

        return width <= 300 && height <= 300;
    }

    public static void main(String[] args) throws IOException {
        String filename = "/home/sharafat/Desktop/test4.jpg";

        BufferedImage bimg = ImageIO.read(new File(filename));
        int width = bimg.getWidth();
        int height = bimg.getHeight();

        String newFile = filename.replace(".jpg", "-80.jpg");
        writeJpeg(bimg, newFile, 0.8f);

        if (width <= 300 && height <= 300) {
            System.out.println("Set as thumbnail");
            return;
        }

        if (width <= 620 && height <= 800) {
            System.out.println("Don't resize");
            return;
        }

        Thumbnails.of(newFile).size(620, 800).outputFormat("jpg").toFiles(Rename.SUFFIX_HYPHEN_THUMBNAIL);
    }
}
