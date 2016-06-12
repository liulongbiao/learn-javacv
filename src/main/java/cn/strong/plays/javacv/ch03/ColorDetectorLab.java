package cn.strong.plays.javacv.ch03;

import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.javacpp.opencv_core.Mat;

import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_RGB2Lab;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;

/**
 * Created by liulongbiao on 16-6-12.
 */
public class ColorDetectorLab {
    private int colorDistanceThreshold;
    private ColorLab targetColor;

    public ColorDetectorLab() {
        this(30, new ColorLab(74, -9, -26));
    }

    public ColorDetectorLab(int minDist, ColorLab target) {
        this.colorDistanceThreshold = minDist;
        this.targetColor = target;
    }

    public int getColorDistanceThreshold() {
        return colorDistanceThreshold;
    }

    public void setColorDistanceThreshold(int colorDistanceThreshold) {
        this.colorDistanceThreshold = Math.max(colorDistanceThreshold, 0);
    }

    public ColorLab getTargetColor() {
        return targetColor;
    }

    public void setTargetColor(ColorLab targetColor) {
        this.targetColor = targetColor;
    }

    public Mat process(Mat rgbImg) {
        Mat labImg = new Mat();
        cvtColor(rgbImg, labImg, COLOR_RGB2Lab);

        UByteRawIndexer indexer = labImg.createIndexer();

        Mat dest = new Mat(labImg.rows(), labImg.cols(), CV_8U);
        UByteIndexer destIdx = dest.createIndexer();
        for (int r = 0; r < labImg.rows(); r++) {
            for (int c = 0; c < labImg.cols(); c++) {
                byte t = (byte) (distance(colorAt(indexer, r, c)) < colorDistanceThreshold ? (255 & 0xFF) : 0);
                destIdx.put(r, c, t);
            }
        }
        return dest;
    }

    private static class Triple {
        public final int l;
        public final int a;
        public final int b;

        public Triple(int l, int a, int b) {
            this.l = l;
            this.a = a;
            this.b = b;
        }
    }

    Triple colorAt(UByteRawIndexer indexer, int row, int col) {
        return new  Triple(indexer.get(row, col, 0), indexer.get(row, col, 1), indexer.get(row, col, 2));
    }

    double distance(Triple color) {
        return Math.abs(targetColor.lAsUInt8() - color.l) / 255d * 100d +
                Math.abs(targetColor.aAsUInt8() - color.a) +
                Math.abs(targetColor.bAsUInt8() - color.b);
    }
}
