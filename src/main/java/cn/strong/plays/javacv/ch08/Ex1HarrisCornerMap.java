package cn.strong.plays.javacv.ch08;

import org.bytedeco.javacpp.opencv_core.Mat;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static cn.strong.plays.javacv.Helper.toMat8U;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgproc.THRESH_BINARY_INV;
import static org.bytedeco.javacpp.opencv_imgproc.cornerHarris;
import static org.bytedeco.javacpp.opencv_imgproc.threshold;

/**
 * Computes Harris corners strength image and displays after applying a corner strength threshold.
 *
 * Created by liulongbiao on 16-6-19.
 */
public class Ex1HarrisCornerMap {

    public static void main(String[] args) throws IOException {
        Mat image = load(new File("data/church01.jpg"), IMREAD_GRAYSCALE);
        // Image to store the Harris detector responses.
        Mat cornerStrength = new Mat();
        // Detect Harris Corners
        cornerHarris(image, cornerStrength,
                3 /* neighborhood size */ ,
                3 /* aperture size */ ,
                0.01 /* Harris parameter */);

        // Threshold to retain only locations of strongest corners
        Mat harrisCorners = new Mat();
        double t             = 0.0001;
        threshold(cornerStrength, harrisCorners, t, 255, THRESH_BINARY_INV);
        // FIXME: `show` should work without converting to 8U
        show(toMat8U(harrisCorners), "Harris Corner Map");
    }
}
