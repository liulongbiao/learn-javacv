package cn.strong.plays.javacv.ch03;

import cn.strong.plays.javacv.Helper;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.io.File;
import java.io.IOException;

import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;

/**
 * Created by liulongbiao on 16-6-12.
 */
public class Ex4ConvertingColorSpaces {
    public static void main(String[] args) throws IOException {
        ColorDetectorLab detector = new ColorDetectorLab();
        Mat image = Helper.load(new File("data/boldt.jpg"), IMREAD_COLOR);
        detector.setColorDistanceThreshold(30);
        detector.setTargetColor(new ColorLab(74.3705, -9.0003, -25.9781));
        Mat dist = detector.process(image);
        Helper.show(dist, "result");
    }
}
