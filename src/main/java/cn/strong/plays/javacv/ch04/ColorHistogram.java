package cn.strong.plays.javacv.ch04;

import cn.strong.plays.javacv.utils.Asserts;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;

import static org.bytedeco.javacpp.opencv_core.split;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * Helper class that simplifies usage of OpenCV `calcHist` function for color images.
 *
 * Created by liulongbiao on 16-6-16.
 */
public class ColorHistogram {

    private int numberOfBins = 256;
    private float _minRange = 0.0f;
    private float _maxRange = 255.0f;

    public int getNumberOfBins() {
        return numberOfBins;
    }

    public void setNumberOfBins(int numberOfBins) {
        this.numberOfBins = numberOfBins;
    }

    /**
     * 计算图像的直方图
     *
     * @param image
     * @return
     */
    public Mat getHistogram(Mat image) {
        Asserts.notNull(image);
        Asserts.isTrue(image.channels() == 3, "Expecting 3 channel (color) image");

        // Compute histogram
        Mat hist = new Mat();
        // Since C++ `calcHist` is using arrays of arrays we need wrap to do some wrapping
        // in `IntPointer` and `PointerPointer` objects.
        IntPointer intPtrChannels = new IntPointer(0, 1, 2);
        IntPointer intPtrHistSize = new IntPointer(numberOfBins, numberOfBins, numberOfBins);
        float[] histRange = {_minRange, _maxRange};
        PointerPointer<FloatPointer> ptrPtrHistRange = new PointerPointer<>(histRange, histRange, histRange);
        calcHist(image,
                1, // histogram of 1 image only
                intPtrChannels, // the channel used
                new Mat(), // no mask is used
                hist, // the resulting histogram
                3, // it is a 3D histogram
                intPtrHistSize, // number of bins
                ptrPtrHistRange, // pixel value range
                true, // uniform
                false); // no accumulation
        return hist;
    }

    /**
     * 将图像从 RGB 空间转换为 HSV 颜色空间，并计算 hue 频道的直方图
     *
     * @param image RGB 图像
     * @param minSaturation 最小饱和度
     * @return
     */
    public Mat getHueHistogram(Mat image, int minSaturation) {
        Asserts.notNull(image);
        Asserts.isTrue(image.channels() == 3, "Expecting 3 channel (color) image");

        // Convert RGB to HSV color space
        Mat hsvImage = new Mat();
        cvtColor(image, hsvImage, COLOR_BGR2HSV);

        Mat saturationMask = new Mat();
        if (minSaturation > 0) {
            // Split the 3 channels into 3 images
            MatVector hsvChannels = new MatVector();
            split(hsvImage, hsvChannels);

            threshold(hsvChannels.get(1), saturationMask, minSaturation, 255, THRESH_BINARY);
        }

        // Prepare arguments for a 1D hue histogram
        Mat hist = new Mat();
        // range is from 0 to 180
        float[] histRanges = {0f, 180f};
        // the hue channel
        int[] channels = {0};

        // Compute histogram
        calcHist(hsvImage,
                1, // histogram of 1 image only
                channels, // the channel used
                saturationMask, // binary mask
                hist, // the resulting histogram
                1, // it is a 1D histogram
                new int[]{numberOfBins}, // number of bins
                histRanges // pixel value range
        );

        return hist;
    }

    /**
     * 计算 2D ab 直方图。BGR 原图像会被转换为 Lab
     *
     * @param image
     * @return
     */
    public Mat getabHistogram(Mat image) {
        Mat hist = new Mat();
        // Convert to Lab color space
        Mat lab = new Mat();
        cvtColor(image, lab, COLOR_BGR2Lab);

        // Prepare arguments for a 2D color histogram
        float[] histRange = {0f, 255f};
        PointerPointer<FloatPointer> ptrPtrHistRange = new PointerPointer<>(histRange, histRange, histRange);
        // the two channels used are ab
        IntPointer intPtrChannels = new IntPointer(1, 2);
        IntPointer intPtrHistSize = new IntPointer(numberOfBins, numberOfBins);


        // Compute histogram
        calcHist(lab,
                1, // histogram of 1 image only
                intPtrChannels, // the channel used
                new Mat(), // no mask is used
                hist, // the resulting histogram
                2, // it is a 2D histogram
                intPtrHistSize, // number of bins
                ptrPtrHistRange, // pixel value range
                true, // Uniform
                false // do not accumulate
        );

        return hist;
    }
}
