package cn.strong.plays.javacv.ch05;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_core.BORDER_CONSTANT;
import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static org.bytedeco.javacpp.opencv_imgproc.dilate;
import static org.bytedeco.javacpp.opencv_imgproc.erode;
import static org.bytedeco.javacpp.opencv_imgproc.morphologyDefaultBorderValue;

/**
 * Created by liulongbiao on 16-6-16.
 */
public class Ex1ErodingAndDilating {
    public static void main(String[] args) throws IOException {
        Mat image = load(new File("data/binary.bmp"));

        // 腐蚀图像，默认使用 3x3 的元素
        Mat eroded = new Mat();
        erode(image, eroded, new Mat());
        show(eroded, "Eroded");

        // 膨胀图像，默认使用 3x3 的元素
        Mat dilated = new Mat();
        dilate(image, dilated, new Mat());
        show(dilated, "Dilated");

        // 以 7x7 结构元素腐蚀
        // 首先定义大小为 7x7 的矩形核
        Mat eroded7x7 = new Mat();
        // 注意这里 scalar 参数为 double 意味着这是一个初始值，值为 int 将意味着大小
        Mat element   = new Mat(7, 7, CV_8U, new Scalar(1d));
        erode(image, eroded7x7, element);
        show(eroded7x7, "Eroded 7x7");

        // 以 7x7 结构元素腐蚀
        // 你可以使用 3x3 内核并迭代 3 次
        // 注：迭代 2 次等价于 5x5 内核，迭代 4 次等价于 9x9, ...
        Mat eroded3x3i3 = new Mat();
        erode(image, eroded3x3i3, new Mat(), new Point(-1, -1), 3, BORDER_CONSTANT, morphologyDefaultBorderValue());
        show(eroded3x3i3, "Eroded 3x3, 3 times (effectively 7x7)");
    }
}
