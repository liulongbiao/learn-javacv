package cn.strong.plays.javacv.ch09;

import org.bytedeco.javacpp.opencv_core.*;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * Finds best match between a small patch from first image (template) and a second image.
 *
 * Created by liulongbiao on 16-6-19.
 */
public class Ex2TemplateMatching {
    public static void main(String[] args) throws IOException {
        Mat image1 = load(new File("data/church01.jpg"), IMREAD_GRAYSCALE);
        Mat image2 = load(new File("data/church02.jpg"), IMREAD_GRAYSCALE);

        // define a template
        Mat target = new Mat(image1, new Rect(120, 40, 30, 30));
        show(target, "Template");


        // define search region
        Mat roi = new Mat(image2,
                // here top half of the image
                new Rect(0, 0, image2.cols(), image2.rows() / 2));

        // perform template matching
        Mat result = new Mat();
        matchTemplate(
                roi, // search region
                target, // template
                result, // result
                CV_TM_SQDIFF);
        // similarity measure

        // find most similar location
        double[] minVal = new double[1];
        double[] maxVal = new double[1];
        Point minPt  = new Point();
        Point maxPt  = new Point();
        minMaxLoc(result, minVal, maxVal, minPt, maxPt, null);

        System.out.println("minPt = (" + minPt.x() + ", " + maxPt.y() + ")");


        // draw rectangle at most similar location
        // at minPt in this case
        rectangle(roi, new Rect(minPt.x(), minPt.y(), target.cols(), target.rows()), new Scalar(255, 255, 255, 0));

        show(roi, "Best match");
    }
}
