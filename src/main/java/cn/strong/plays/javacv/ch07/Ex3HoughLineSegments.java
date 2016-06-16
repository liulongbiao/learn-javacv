package cn.strong.plays.javacv.ch07;

import org.bytedeco.javacpp.opencv_core.Mat;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * 使用概率霍夫转换方法来检测线条分割
 *
 * Created by liulongbiao on 16-6-17.
 */
public class Ex3HoughLineSegments {

    public Ex3HoughLineSegments() {
        super();
    }

    public static void main(String[] args) throws IOException {
        Mat src = load(new File("data/road.jpg"), IMREAD_GRAYSCALE);

        // Canny contours
        Mat contours = new Mat();
        int threshold1 = 125;
        int threshold2 = 350;
        int apertureSize = 3;
        Canny(src, contours, threshold1, threshold2, apertureSize, false /*L2 gradient*/);
        show(contours, "Canny Contours");

        // Set probabilistic Hough transform
        LineFinder finder = new LineFinder();
        finder.minLength = 100;
        finder.minGap = 20;
        finder.minVotes = 80;

        finder.findLines(contours);

        // Draw lines on the canny contour image
        Mat colorDst = new Mat();
        cvtColor(contours, colorDst, COLOR_GRAY2BGR);
        finder.drawDetectedLines(colorDst);
        show(colorDst, "Hough Line Segments");
    }
}
