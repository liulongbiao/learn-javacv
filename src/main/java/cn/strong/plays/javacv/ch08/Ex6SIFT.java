package cn.strong.plays.javacv.ch08;

import org.bytedeco.javacpp.opencv_core.KeyPointVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_features2d.DrawMatchesFlags;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_features2d.drawKeypoints;
import static org.bytedeco.javacpp.opencv_xfeatures2d.SIFT;

/**
 * Detecting the scale-invariant SIFT features
 *
 * Created by liulongbiao on 16-6-19.
 */
public class Ex6SIFT {
    public static void main(String[] args) throws IOException {
        Mat image = load(new File("data/church01.jpg"));
        // Detect SIFT features.
        KeyPointVector keyPoints = new KeyPointVector();
        int nFeatures = 0;
        int nOctaveLayers = 3;
        double contrastThreshold = 0.03;
        int edgeThreshold = 10;
        double sigma = 1.6;
        SIFT sift = SIFT.create(nFeatures, nOctaveLayers, contrastThreshold, edgeThreshold, sigma);
        sift.detect(image, keyPoints);

        // Draw keyPoints
        Mat featureImage = new Mat();
        drawKeypoints(image, keyPoints, featureImage, new Scalar(255, 255, 255, 0), DrawMatchesFlags.DRAW_RICH_KEYPOINTS);
        show(featureImage, "SIFT Features");
    }
}
