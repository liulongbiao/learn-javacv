package cn.strong.plays.javacv.ch04;

import cn.strong.plays.javacv.utils.Floats;
import org.bytedeco.javacpp.indexer.FloatRawIndexer;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;

import static cn.strong.plays.javacv.utils.Floats.max;
import static org.bytedeco.javacpp.opencv_core.LUT;
import static org.bytedeco.javacpp.opencv_imgproc.calcHist;
import static org.bytedeco.javacpp.opencv_imgproc.equalizeHist;

/**
 * Helper class that simplifies usage of OpenCV `calcHist` function for single channel images.
 *
 * Created by liulongbiao on 16-6-15.
 */
public class Histogram1D {

    /**
     * 给 image 应用查找表操作，它是对 OpenCV LUT 函数的一个封装
     *
     * @param image
     * @param lookup
     * @return
     */
    public static Mat applyLookUp(Mat image, Mat lookup) {
        Mat dest = new Mat();
        LUT(image, lookup, dest);
        return dest;
    }

    /**
     * Equalize histogram of an image. The algorithm normalizes the brightness and increases the contrast of the image.
     * It is a wrapper for OpenCV function `equalizeHist`.
     *
     * @param src
     * @return
     */
    public static Mat equalize(Mat src) {
        Mat dest = new Mat();
        equalizeHist(src, dest);
        return dest;
    }

    private int numberOfBins = 256;
    private int[] channels = new int[]{0};
    private float _minRange = 0.0f;
    private float _maxRange = 255.0f;

    public void setRanges(float minRange, float maxRange) {
        _minRange = minRange;
        _maxRange = maxRange;
    }

    public BufferedImage getHistogramImage(Mat image) {

        // Output image size
        int width = numberOfBins;
        int height = numberOfBins;

        float[] hist = getHistogramAsArray(image);
        // Set highest point to 90% of the number of bins
        double scale = 0.9 / max(hist) * height;

        // Create a color image to draw on
        BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = canvas.createGraphics();

        // Paint background
        g.setPaint(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // Draw a vertical line for each bin
        g.setPaint(Color.BLUE);
        for (int bin = 0 ; bin < numberOfBins; bin++) {
            int h = (int) Math.round(hist[bin] * scale);
            g.drawLine(bin, height - 1, bin, height - h - 1);
        }

        // Cleanup
        g.dispose();

        return canvas;
    }

    /**
     * Computes histogram of an image.
     *
     * @param image input image
     * @return histogram represented as an array
     */
    float[] getHistogramAsArray(Mat image) {
        // Create and calculate histogram object
        Mat hist = getHistogram(image);

        // Extract values to an array
        float[] dest = new float[numberOfBins];
        FloatRawIndexer histI = hist.createIndexer();
        for (int bin = 0 ; bin < numberOfBins; bin++) {
            dest[bin] = histI.get(bin);
        }

        return dest;
    }

    Mat getHistogram(Mat image) {
        return getHistogram(image, new Mat());
    }


    /**
     * Computes histogram of an image.
     *
     * @param image input image
     * @param mask optional mask
     * @return OpenCV histogram object
     */
    Mat getHistogram(Mat image, Mat mask) {
        int[] histSize = new int[]{numberOfBins};
        float[] ranges = new float[]{_minRange, _maxRange};
        // Compute histogram
        Mat hist = new Mat();
        calcHist(image, 1, channels, mask, hist, 1, histSize, ranges);
        return hist;
    }
}
