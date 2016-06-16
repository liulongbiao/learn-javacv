package cn.strong.plays.javacv.ch07;

import org.bytedeco.javacpp.indexer.FloatRawIndexer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.*;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static org.bytedeco.javacpp.opencv_core.LINE_8;
import static org.bytedeco.javacpp.opencv_core.cvCreateMemStorage;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * 使用标准霍夫转换方法来检测线条
 *
 * Created by liulongbiao on 16-6-17.
 */
public class Ex2HoughLines {
    public static void main(String[] args) throws IOException {
        Mat src = load(new File("data/road.jpg"), IMREAD_GRAYSCALE);

        // Canny canny
        Mat canny = new Mat();
        int threshold1 = 125;
        int threshold2 = 350;
        int apertureSize = 3;
        Canny(src, canny, threshold1, threshold2, apertureSize, false /*L2 gradient*/);

        show(canny, "Canny Contours");

        // Hough transform for line detection
        Mat lines                      = new Mat();
        CvMemStorage storage           = cvCreateMemStorage(0);
        int method                     = HOUGH_STANDARD;
        int distanceResolutionInPixels = 1;
        double angleResolutionInRadians= Math.PI / 180;
        int minimumVotes               = 80;
        HoughLines(
                canny,
                lines,
                distanceResolutionInPixels,
                angleResolutionInRadians,
                minimumVotes);

        // Draw lines on the canny contour image
        FloatRawIndexer indexer = lines.createIndexer();
        Mat result  = new Mat();
        src.copyTo(result);
        cvtColor(src, result, COLOR_GRAY2BGR);
        for (int i = 0; i < lines.rows(); i++) {
            float rho = indexer.get(i, 0, 0);
            float theta = indexer.get(i, 0, 1);

            Point pt1;
            Point pt2;

            if (theta < Math.PI / 4.0 || theta > 3.0 * Math.PI / 4.0) {
                // ~vertical line
                // point of intersection of the line with first row
                pt1 = new Point((int) round(rho / cos(theta)), 0);
                // point of intersection of the line with last row
                pt2 = new Point((int) round((rho - result.rows() * sin(theta)) / cos(theta)), result.rows());
            } else {
                // ~horizontal line
                // point of intersection of the line with first column
                pt1 = new Point(0, (int) round(rho / sin(theta)));
                // point of intersection of the line with last column
                pt2 = new Point(result.cols(), (int) round((rho - result.cols() * cos(theta)) / sin(theta)));
            }

            // draw a white line
            line(result, pt1, pt2, new Scalar(0, 0, 255, 0), 1, LINE_8, 0);
        }

        save(new File("target/result.tif"), result);
        show(toMat8U(result), "Hough Lines");
    }
}
