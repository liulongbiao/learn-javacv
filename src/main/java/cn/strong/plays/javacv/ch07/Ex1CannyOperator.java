package cn.strong.plays.javacv.ch07;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgproc.Canny;

/**
 * 使用 Canny 操作符检测轮廓线
 *
 * Created by liulongbiao on 16-6-17.
 */
public class Ex1CannyOperator {
    public static void main(String[] args) throws IOException {
        Mat src = load(new File("data/road.jpg"), IMREAD_GRAYSCALE);

        // Canny contours
        Mat contours = new Mat();
        int threshold1 = 125;
        int threshold2 = 350;
        int apertureSize = 3;
        Canny(src, contours, threshold1, threshold2, apertureSize, true /*L2 gradient*/);

        show(contours, "Canny Contours");
    }
}
