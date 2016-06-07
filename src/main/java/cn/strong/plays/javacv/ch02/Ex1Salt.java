package cn.strong.plays.javacv.ch02;

import cn.strong.plays.javacv.Helper;
import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;

/**
 * Created by liulongbiao on 16-6-7.
 */
public class Ex1Salt {

    public static void main(String[] args) throws IOException {
        Mat image = Helper.load(new File("data/boldt.jpg"), IMREAD_COLOR);
        Mat dest = salt(image, 2000);
        Helper.display(dest, "Salted");
    }

    /**
     * 加盐噪声
     *
     * @param image
     * @param n
     * @return
     */
    private static Mat salt(Mat image, int n) {
        Random rdm = new Random();
        // 访问图像数据
        UByteIndexer indexer = image.createIndexer();

        int nbChannels = image.channels();
        for(int i = 1; i <= n; i++) {
            // 随机创建一个点
            long row = rdm.nextInt(image.rows());
            long col = rdm.nextInt(image.cols());
            // 通过将每个频道都设置为最大值 (255) 家那个颜色设置为白色
            for(long j = 0; j < nbChannels; j++) {
                indexer.put(row, col, j, (byte) 255);
            }
        }
        return image;
    }
}
