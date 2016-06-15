package cn.strong.plays.javacv.ch04;

import cn.strong.plays.javacv.Helper;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.drawOnImage;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;

/**
 * Created by liulongbiao on 16-6-15.
 */
public class Ex6ContentDetectionGrayscale {
    public static void main(String[] args) throws IOException {
        Mat src = Helper.load(new File("data/waves.jpg"), IMREAD_GRAYSCALE);

        Rect rectROI = new Rect(216, 33, 24, 30);

        show(drawOnImage(src, rectROI, new Scalar(1d, 255d, 255d, 0.5)), "Input");

        // Define ROI
        Mat imageROI = src.apply(rectROI);
        show(imageROI, "Reference");

        // Compute histogram within the ROI
        Histogram1D h = new Histogram1D();
        Mat hist = h.getHistogram(imageROI);
        show(h.getHistogramImage(imageROI), "Reference Histogram");

        ContentFinder finder = new ContentFinder();
        finder.setHistogram(hist);

        Mat result1 = finder.find(src);
        Mat tmp = new Mat();
        result1.convertTo(tmp, CV_8U, -1, 255);
        show(tmp, "Back-projection result");

        finder.setThreshold(0.12f);
        Mat result2 = finder.find(src);
        show(result2, "Detection result");
    }
}
