package cn.strong.plays.javacv.ch02;

import cn.strong.plays.javacv.Helper;
import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.io.File;
import java.io.IOException;

import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;

/**
 * Created by liulongbiao on 16-6-9.
 */
public class Ex2ColorReduce {

    public static void main(String[] args) throws IOException {
        Mat image = Helper.load(new File("data/boldt.jpg"), IMREAD_COLOR);
        Mat dest = colorReduce(image);
        Helper.show(dest, "Color Reduced");
    }

    public static Mat colorReduce(Mat image) {
        return colorReduce(image, 64);
    }


    public static Mat colorReduce(Mat image, int div) {
        UByteIndexer indexer = image.createIndexer();
        // 元素总量，联合了每个通道的组件
        int nbElements = image.rows() * image.cols() * image.channels();
        for (int i = 0; i < nbElements; i++) {
            // 转换为整型， byte 被处理为一个无符号值
            int v = indexer.get(i) & 0xFF;
            // 使用整数除数来约减值的数量
            int newV = v / div * div + div / 2;
            // 回设给图像
            indexer.put(i, (byte) (newV & 0xFF));
        }

        return image;
    }

}
