package cn.strong.plays.javacv.ch07;

import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;

import static org.bytedeco.javacpp.opencv_core.LINE_AA;
import static org.bytedeco.javacpp.opencv_imgproc.HoughLinesP;
import static org.bytedeco.javacpp.opencv_imgproc.line;

/**
 * 使用概率霍夫转换方法来检测线条分割的帮助类
 *
 * Created by liulongbiao on 16-6-17.
 */
public class LineFinder {
    public double deltaRho = 1;
    public double deltaTheta = Math.PI / 180;
    public int minVotes = 10;
    public double minLength = 0;
    public double minGap = 0d;

    private Mat lines;

    /**
     * 应用概率霍夫变换
     *
     * @param binary
     */
    public void findLines(Mat binary) {
        lines = new Mat();
        HoughLinesP(binary, lines, deltaRho, deltaTheta, minVotes, minLength, minGap);
    }

    /**
     * 在图像上画检测到的图像
     *
     * @param image
     */
    public void drawDetectedLines(Mat image) {
        UByteRawIndexer indexer = image.createIndexer();
        for (int i = 0; i < lines.rows(); i++) {
            Point pt1 = new Point(indexer.get(i, 0, 0), indexer.get(i, 0, 1));
            Point pt2 = new Point(indexer.get(i, 0, 2), indexer.get(i, 0, 3));

            // draw the segment on the image
            line(image, pt1, pt2, new Scalar(0, 0, 255, 128), 1, LINE_AA, 0);
        }
    }
}
