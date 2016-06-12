package cn.strong.plays.javacv.ch03;

import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import static org.bytedeco.javacpp.opencv_core.CV_8U;

/**
 * Created by liulongbiao on 16-6-12.
 */
public class ColorDetector {
    private int colorDistanceThreshold;
    private ColorRGB targetColor;

    public ColorDetector() {
        this(100, new ColorRGB(130, 190, 230));
    }

    public ColorDetector(int minDist, ColorRGB target) {
        this.colorDistanceThreshold = minDist;
        this.targetColor = target;
    }

    public int getColorDistanceThreshold() {
        return colorDistanceThreshold;
    }

    public void setColorDistanceThreshold(int colorDistanceThreshold) {
        this.colorDistanceThreshold = colorDistanceThreshold;
    }

    public ColorRGB getTargetColor() {
        return targetColor;
    }

    public void setTargetColor(ColorRGB targetColor) {
        this.targetColor = targetColor;
    }

    public Mat process(Mat image) {
        UByteIndexer srcIdx = image.createIndexer();

        Mat dest = new Mat(image.rows(), image.cols(), CV_8U);
        UByteIndexer destIdx = dest.createIndexer();

        int[] brg = new int[3];
        for (int y = 0; y < image.rows(); y++) {
            for (int x = 0; x < image.cols(); x++) {
                srcIdx.get(y, x, brg);
                ColorRGB c = ColorRGB.fromBGR(brg);
                byte t = (byte) (distance(c) < colorDistanceThreshold ? (255 & 0xFF) : 0);
                destIdx.put(y, x, t);
            }
        }

        return dest;
    }

    private double distance(ColorRGB color) {
        return Math.abs(targetColor.red - color.red) + Math.abs(targetColor.green - color.green)
                + Math.abs(targetColor.blue - color.blue);
    }
}
