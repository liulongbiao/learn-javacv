package cn.strong.plays.javacv.ch05;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static org.bytedeco.javacpp.opencv_imgproc.MORPH_CLOSE;
import static org.bytedeco.javacpp.opencv_imgproc.MORPH_OPEN;
import static org.bytedeco.javacpp.opencv_imgproc.morphologyEx;

/**
 * 使用形态学开、闭操作示例
 *
 * Created by liulongbiao on 16-6-16.
 */
public class Ex2OpeningAndClosing {
    public static void main(String[] args) throws IOException {
        Mat image = load(new File("data/binary.bmp"));
        // 创建 5x5 结构元素
        Mat element5 = new Mat(5, 5, CV_8U, new Scalar(1d));
        // 闭操作
        Mat closed = new Mat();
        morphologyEx(image, closed, MORPH_CLOSE, element5);
        show(closed, "Closed");

        // 开操作
        Mat opened = new Mat();
        morphologyEx(image, opened, MORPH_OPEN, element5);
        show(opened, "Opened");
    }
}
