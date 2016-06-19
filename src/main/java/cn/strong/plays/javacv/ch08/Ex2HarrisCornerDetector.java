package cn.strong.plays.javacv.ch08;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;

/**
 * Uses Harris Corner strength image to detect well localized corners,
 * replacing several closely located detections (blurred) by a single one.
 *
 * Created by liulongbiao on 16-6-19.
 */
public class Ex2HarrisCornerDetector {
    public static void main(String[] args) throws IOException {
        Mat image = load(new File("data/church01.jpg"), IMREAD_GRAYSCALE);
        // Harris detector instance
        HarrisDetector harris = new HarrisDetector();
        // Compute Harris values
        harris.detect(image);
        // Detect Harris corners
        List<Point> pts = harris.getCorners(0.01);

        // Draw Harris corners
        harris.drawOnImage(image, pts);
        show(image, "Harris Corners");
    }
}
