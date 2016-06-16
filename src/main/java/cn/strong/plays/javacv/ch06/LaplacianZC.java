package cn.strong.plays.javacv.ch06;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * Created by liulongbiao on 16-6-16.
 */
public class LaplacianZC {
    /**
     * Laplacian 核的大小
     */
    public int aperture = 5;

    /**
     * Compute floating point Laplacian.
     */
    public Mat computeLaplacian(Mat src) {
        Mat laplace = new Mat();
        Laplacian(src, laplace, CV_32F, aperture, 1 /*scale*/ , 0 /*delta*/ , BORDER_DEFAULT);
        return laplace;
    }

    /**
     * Get binary image of the zero-crossings
     * if the product of the two adjustment pixels is
     * less than threshold then this is a zero crossing
     * will be ignored.
     */
    public Mat getZeroCrossings(Mat laplace) {
        // Threshold at 0
        Mat signImage = new Mat();
        threshold(laplace, signImage, 0, 255, THRESH_BINARY);

        // Convert the +/- image into CV_8U
        Mat binary = new Mat();
        signImage.convertTo(binary, CV_8U);

        // Dilate the binary image +/- regions
        Mat dilated = new Mat();
        dilate(binary, dilated, new Mat());

        // Return the zero-crossing contours
        Mat dest = new Mat();
        subtract(dilated, binary, dest);
        return dest;
    }
}
