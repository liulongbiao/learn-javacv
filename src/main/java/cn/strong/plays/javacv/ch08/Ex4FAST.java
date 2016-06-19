package cn.strong.plays.javacv.ch08;

import org.bytedeco.javacpp.opencv_core.KeyPointVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_features2d.DrawMatchesFlags;
import org.bytedeco.javacpp.opencv_features2d.FastFeatureDetector;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_features2d.drawKeypoints;

/**
 * Detecting FAST features
 *
 * Created by liulongbiao on 16-6-19.
 */
public class Ex4FAST {
    public static void main(String[] args) throws IOException {
        Mat image = load(new File("data/church01.jpg"));
        // Detect FAST features
        FastFeatureDetector ffd = FastFeatureDetector.create(
                40 /* threshold for detection */ ,
                true /* non-max suppression */ ,
                FastFeatureDetector.TYPE_9_16);
        KeyPointVector keyPoints = new KeyPointVector();
        ffd.detect(image, keyPoints);

        // Draw keyPoints
        Mat canvas = new Mat();
        drawKeypoints(image, keyPoints, canvas, new Scalar(255, 255, 255, 0), DrawMatchesFlags.DEFAULT);
        show(canvas, "FAST Features");
    }
}
