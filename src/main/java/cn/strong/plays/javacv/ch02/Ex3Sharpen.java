package cn.strong.plays.javacv.ch02;

import cn.strong.plays.javacv.Helper;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;

import java.io.File;
import java.io.IOException;

import static org.bytedeco.javacpp.opencv_core.CV_32F;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.javacpp.opencv_imgproc.filter2D;

/**
 * 锐化
 *
 * Created by liulongbiao on 16-6-9.
 */
public class Ex3Sharpen {

    public static void main(String[] args) throws IOException {
        // 加载原图
        Mat image = Helper.load(new File("data/boldt.jpg"), IMREAD_COLOR);
        // 定义输出图像
        Mat dest = new Mat();

        // 构造锐化核，初始化所有值为 0
        Mat kernel = new Mat(3, 3, CV_32F, new Scalar(0));
        // Indexer 被用于访问矩阵中的值
        FloatIndexer ki = kernel.createIndexer();
        ki.put(1, 1, 5);
        ki.put(0, 1, -1);
        ki.put(2, 1, -1);
        ki.put(1, 0, -1);
        ki.put(1, 2, -1);

        // g过滤图像
        filter2D(image, dest, image.depth(), kernel);

        Helper.show(dest, "Sharpened");
    }
}
