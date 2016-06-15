package cn.strong.plays.javacv.ch04;

import cn.strong.plays.javacv.Helper;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;

/**
 * Created by liulongbiao on 16-6-15.
 */
public class Ex2ComputeHistogramGraph {

    public static void main(String[] args) throws IOException {
        Mat src = Helper.load(new File("data/group.jpg"), IMREAD_GRAYSCALE);

        // Calculate histogram
        Histogram1D h = new Histogram1D();
        BufferedImage histogram = h.getHistogramImage(src);

        // Display the graph
        Helper.show(histogram, "Histogram");
    }
}
