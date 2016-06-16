package cn.strong.plays.javacv.ch06;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static cn.strong.plays.javacv.Helper.toMat8U;
import static org.bytedeco.javacpp.opencv_core.CV_32F;
import static org.bytedeco.javacpp.opencv_core.magnitude;
import static org.bytedeco.javacpp.opencv_core.minMaxLoc;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgproc.Sobel;
import static org.bytedeco.javacpp.opencv_imgproc.THRESH_BINARY_INV;
import static org.bytedeco.javacpp.opencv_imgproc.threshold;

/**
 * 使用方向滤波器来侦测边缘
 *
 * Created by liulongbiao on 16-6-16.
 */
public class Ex3DirectionalFilters {
    public static void main(String[] args) throws IOException {
        Mat src = load(new File("data/boldt.jpg"), IMREAD_GRAYSCALE);

        // Sobel edges in X
        Mat sobelX = new Mat();
        Sobel(src, sobelX, CV_32F, 1, 0);
        show(toMat8U(sobelX), "Sobel X");

        // Sobel edges in Y
        Mat sobelY = new Mat();
        Sobel(src, sobelY, CV_32F, 0, 1);
        show(toMat8U(sobelY), "Sobel Y");

        // Compute norm of directional images to create Sobel edge image
        Mat sobel = sobelX.clone();
        magnitude(sobelX, sobelY, sobel);
        show(toMat8U(sobel), "Sobel1");

        DoublePointer min = new DoublePointer(1);
        DoublePointer max = new DoublePointer(1);
        minMaxLoc(sobel, min, max, null, null, new Mat());
        System.out.println("Sobel min: " + min.get(0) + ", max: " + max.get(0) + ".");

        // Threshold edges
        // Prepare image for display: extract foreground
        Mat thresholded = new Mat();
        threshold(sobel, thresholded, 100, 255, THRESH_BINARY_INV);

        // FIXME: There us a crash if trying to display directly
        //   Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 16711426
        //	   at java.awt.image.ComponentColorModel.getRGBComponent(ComponentColorModel.java:903)
        //  show(thresholded, "Thresholded")
        //  save(new File("Ex3DirectionalFilters-thresholded.tif"), thresholded)
        show(toMat8U(thresholded), "Thresholded");
    }
}
