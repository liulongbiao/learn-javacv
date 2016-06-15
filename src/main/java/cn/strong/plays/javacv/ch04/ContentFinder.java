package cn.strong.plays.javacv.ch04;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import static org.bytedeco.javacpp.opencv_core.normalize;
import static org.bytedeco.javacpp.opencv_imgproc.THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.calcBackProject;
import static org.bytedeco.javacpp.opencv_imgproc.threshold;

/**
 * Created by liulongbiao on 16-6-15.
 */
public class ContentFinder {
    private Mat _histogram = new Mat();
    private float _threshold = -1f;
    private boolean _isSparse = false;

    /**
     * Find content back projecting a histogram.
     * @param image input used for back projection.
     * @return Result of the back-projection of the histogram. Image is binary (0,255) if threshold is larger than 0.
     */
    Mat find(Mat image) {
        int[] channels = new int[]{0, 1, 2};
        return find(image, 0.0f, 255.0f, channels);
    }

    Mat find(Mat image, float minValue, float maxValue, int[] channels) {
        Mat result = new Mat();

        // Create parameters that can be used for both 1D and 3D/color histograms.
        // Since C++ calcBackProject is using arrays of arrays we need to do some wrapping `PointerPointer` objects.
        float[] histRange = new float[]{minValue, maxValue};
        IntPointer intPtrChannels = new IntPointer(channels);
        PointerPointer<FloatPointer> ptrPtrHistRange = new PointerPointer<>(histRange, histRange, histRange);

        calcBackProject(image, 1, intPtrChannels, _histogram, result, ptrPtrHistRange, 255, true);

        if (_threshold > 0) {
            threshold(result, result, 255 * _threshold, 255, THRESH_BINARY);
        }

        return result;
    }

    public float getThreshold() {
        return _threshold;
    }

    /**
     * Set threshold for converting the back-projected image to a binary.
     * If value is negative no thresholding will be done.
     */
    public void setThreshold(float t) {
        _threshold = t;
    }

    public Mat getHistogram() {
        return _histogram;
    }

    /**
     * Set reference histogram, it will be normalized.
     */
    public void setHistogram(Mat h) {
        _isSparse = false;
        normalize(h, _histogram);
    }

}
