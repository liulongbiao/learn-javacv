package cn.strong.plays.javacv.ch05;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import static cn.strong.plays.javacv.Helper.toBufferedImage;
import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static org.bytedeco.javacpp.opencv_core.absdiff;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * Created by liulongbiao on 16-6-16.
 */
public class MorphoFeatures {
    // 用于产生二进制图像的阈值
    private int thresholdValue = -1;

    public int getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(int thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    // 用于角落检测的结构元素
    private Mat cross = new Mat(5, 5, CV_8U,
            new BytePointer(new byte[] {
                0, 0, 1, 0, 0,
                0, 0, 1, 0, 0,
                1, 1, 1, 1, 1,
                0, 0, 1, 0, 0,
                0, 0, 1, 0, 0
            }));
    private Mat diamond = new Mat(5, 5, CV_8U,
            new BytePointer(new byte[] {
                0, 0, 1, 0, 0,
                0, 1, 1, 1, 0,
                1, 1, 1, 1, 1,
                0, 1, 1, 1, 0,
                0, 0, 1, 0, 0
            }));
    private Mat square = new Mat(5, 5, CV_8U,
            new BytePointer(new byte[] {
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1
            }));
    private Mat x = new Mat(5, 5, CV_8U,
            new BytePointer(new byte[] {
                1, 0, 0, 0, 1,
                0, 1, 0, 1, 0,
                0, 0, 1, 0, 0,
                0, 1, 0, 1, 0,
                1, 0, 0, 0, 1
            }));

    public Mat getEdges(Mat image) {
        // 获取渐变图像
        Mat result = new Mat();
        morphologyEx(image, result, MORPH_GRADIENT, new Mat());

        // 应用阈值来获取二进制图像
        applyThreshold(result);

        return result;
    }

    public Mat getCorners(Mat image) {
        Mat result = new Mat();

        // Dilate with a cross
        dilate(image, result, cross);

        // Erode with a diamond
        erode(result, result, diamond);

        Mat result2 = new Mat();
        // Dilate with X
        dilate(image, result2, x);

        // Erode with a square
        erode(result2, result2, square);

        // Corners are obtained by differentiating the two closed images
        absdiff(result2, result, result);

        // 应用阈值来获取二进制图像
        applyThreshold(result);

        return result;
    }

    private void applyThreshold(Mat image) {
        if (thresholdValue > 0) {
            threshold(image, image, thresholdValue, 255, THRESH_BINARY_INV);
        }
    }

    public Image drawOnImage(Mat binary, Mat image) {
        // OpenCV drawing seems to crash a lot, so use Java2D
        Raster binaryRaster = toBufferedImage(binary).getData();
        int radius = 6;
        int diameter = radius * 2;

        BufferedImage imageBI = toBufferedImage(image);
        int width = imageBI.getWidth();
        int height = imageBI.getHeight();
        Graphics2D g2d = (Graphics2D) imageBI.getGraphics();
        g2d.setColor(Color.WHITE);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int v = binaryRaster.getSample(x, y, 0);
                if (v == 0) {
                    g2d.draw(new Ellipse2D.Double(x - radius, y - radius, diameter, diameter));
                }
            }
        }

        return imageBI;
    }
}
