package cn.strong.plays.javacv.ch04;

import cn.strong.plays.javacv.Helper;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.drawOnImage;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;

/**
 * Created by liulongbiao on 16-6-16.
 */
public class Ex7ContentDetectionColor {
    public static void main(String[] args) throws IOException {
        Mat colorImage = Helper.load(new File("data/waves.jpg"), IMREAD_COLOR);

        // Blue sky area
        Rect rectROI = new Rect(0, 0, 100, 45);

        // Display image with marked ROI
        show(drawOnImage(colorImage, rectROI, new opencv_core.Scalar(0d, 255d, 255d, 0.5)), "Input");

        // Define ROI
        Mat imageROI = colorImage.apply(rectROI);
        show(imageROI, "Reference");

        // Compute histogram within the ROI
        ColorHistogram hc = new ColorHistogram();
        hc.setNumberOfBins(8);
        Mat hist = hc.getHistogram(imageROI);

        ContentFinder finder = new ContentFinder();
        finder.setHistogram(hist);
        finder.setThreshold(0.05f);

        Mat result1 = finder.find(colorImage);
        show(result1, "Color Detection Result");

        // Second color image
        Mat colorImage2 = Helper.load(new File("data/dog.jpg"), IMREAD_COLOR);

        Mat result2 = finder.find(colorImage2);
        show(result2, "Color Detection Result 2");
    }
}
