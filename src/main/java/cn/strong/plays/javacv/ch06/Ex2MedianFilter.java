package cn.strong.plays.javacv.ch06;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_core.min;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgproc.medianBlur;

/**
 * 中通滤波器
 *
 * Created by liulongbiao on 16-6-16.
 */
public class Ex2MedianFilter {
    public static void main(String[] args) throws IOException {
        Mat src = load(new File("data/boldt_salt.jpg"), IMREAD_GRAYSCALE);
        // Remove noise with a median filter
        Mat dest       = new Mat();
        int kernelSize = 3;
        medianBlur(src, dest, kernelSize);
        show(dest, "Median filtered");

        // Since median filter really cleans up outlier with values above (salt) and below (pepper),
        // in this case, we can reconstruct dark pixels that are most likely not effected by the noise.
        Mat dest2 = new Mat();
        min(src, dest, dest2);
        show(dest2, "Median filtered + dark pixel recovery");
    }
}
