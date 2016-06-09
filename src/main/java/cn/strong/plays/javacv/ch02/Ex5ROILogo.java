package cn.strong.plays.javacv.ch02;

import cn.strong.plays.javacv.Helper;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;

import java.io.File;
import java.io.IOException;

import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;

/**
 * Created by liulongbiao on 16-6-9.
 */
public class Ex5ROILogo {

    public static void main(String[] args) throws IOException {
        Mat logo = Helper.load(new File("data/logo.bmp"), IMREAD_COLOR);
        Mat mask = Helper.load(new File("data/logo.bmp"), IMREAD_GRAYSCALE);
        Mat image = Helper.load(new File("data/boldt.jpg"), IMREAD_COLOR);

        // 定义匹配 logo 尺寸的感兴趣的区域
        Mat imageROI = image.apply(new Rect(image.cols() - logo.cols(), image.rows() - logo.rows(), logo.cols(), logo.rows()));

        // 组合输入图像和 logo. Mask 用于控制 blending.
        logo.copyTo(imageROI, mask);

        Helper.display(imageROI, "With Logo");
    }
}
