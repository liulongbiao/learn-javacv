package cn.strong.plays.javacv.ch08;

import org.bytedeco.javacpp.opencv_core.KeyPointVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.DrawMatchesFlags;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_core.Scalar;
import static org.bytedeco.javacpp.opencv_features2d.drawKeypoints;
import static org.bytedeco.javacpp.opencv_xfeatures2d.SURF;

/**
 * Detecting the scale-invariant SURF features
 *
 * Created by liulongbiao on 16-6-19.
 */
public class Ex5SURF {
    public static void main(String[] args) throws IOException {
        Mat image = load(new File("data/church01.jpg"));
        // Detect SURF features.
        KeyPointVector keyPoints = new KeyPointVector();
        double hessianThreshold = 2500d;
        int nOctaves = 4;
        int nOctaveLayers = 2;
        boolean extended = true;
        boolean upright = false;
        SURF surf = SURF.create(hessianThreshold, nOctaves, nOctaveLayers, extended, upright);
        surf.detect(image, keyPoints);

        // Draw keyPoints
        //    val featureImage = cvCreateImage(cvGetSize(image), image.depth(), 3)
        Mat featureImage = new Mat();
        drawKeypoints(image, keyPoints, featureImage, new Scalar(255, 255, 255, 0), DrawMatchesFlags.DRAW_RICH_KEYPOINTS);
        show(featureImage, "SURF Features");
    }
}
