package cn.strong.plays.javacv.ch08;

import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.opencv_core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * Created by liulongbiao on 16-6-19.
 */
public class HarrisDetector {
    /** Neighborhood size for Harris edge detector. */
    int neighborhood = 3;
    /** Aperture size for Harris edge detector. */
    int aperture = 3;
    /** Harris parameter. */
    double k        = 0.01;

    /** Maximum strength for threshold computations. */
    double maxStrength = 0.0;
    /** Size of kernel for non-max suppression. */
    int nonMaxSize = 3;

    /** Image of corner strength, computed by Harris edge detector. It is created by method `detect()`. */
    private Optional<Mat> cornerStrength = Optional.empty();
    /** Image of local corner maxima. It is created by method `detect()`. */
    private Optional<Mat> localMax = Optional.empty();


    /** Compute Harris corners.
     *
     * Results of computation can be retrieved using `getCornerMap` and `getCorners`.
     */
    public void detect(Mat image) {
        // Harris computations
        cornerStrength = Optional.of(new Mat());
        cornerHarris(image, cornerStrength.get(), neighborhood, aperture, k);

        // Internal threshold computation.
        //
        // We will scale corner threshold based on the maximum value in the cornerStrength image.
        // Call to cvMinMaxLoc finds min and max values in the image and assigns them to output parameters.
        // Passing back values through function parameter pointers works in C bout not on JVM.
        // We need to pass them as 1 element array, as a work around for pointers in C API.
        double[] maxStrengthA = {maxStrength};
        minMaxLoc(
                cornerStrength.get(),
                new double[]{0.0} /* not used here, but required by API */ ,
                maxStrengthA, null, null, new Mat());
        // Read back the computed maxStrength
        maxStrength = maxStrengthA[0];

        // Local maxima detection.
        //
        // Dilation will replace values in the image by its largest neighbour value.
        // This process will modify all the pixels but the local maxima (and plateaus)
        Mat dilated = new Mat();
        dilate(cornerStrength.get(), dilated, new Mat());
        localMax = Optional.of(new Mat());
        // Find maxima by detecting which pixels were not modified by dilation
        compare(cornerStrength.get(), dilated, localMax.get(), CMP_EQ);
    }


    /** Get the corner map from the computed Harris values. Require call to `detect`.
     * @throws IllegalStateException if `cornerStrength` and `localMax` are not yet computed.
     */
    public Mat getCornerMap(double qualityLevel) {
        if (!cornerStrength.isPresent() || !localMax.isPresent()) {
            throw new IllegalStateException("Need to call `detect()` before it is possible to compute corner map.");
        }

        // Threshold the corner strength
        double t = qualityLevel * maxStrength;
        Mat cornerTh = new Mat();
        threshold(cornerStrength.get(), cornerTh, t, 255, THRESH_BINARY);

        Mat cornerMap = new Mat();
        cornerTh.convertTo(cornerMap, CV_8U);

        // non-maxima suppression
        bitwise_and(cornerMap, localMax.get(), cornerMap);

        return cornerMap;
    }


    /** Get the feature points from the computed Harris values. Require call to `detect`. */
    public List<Point> getCorners(double qualityLevel) {
        // Get the corner map
        Mat cornerMap = getCornerMap(qualityLevel);
        // Get the corners
        return getCorners(cornerMap);
    }


    /** Get the feature points vector from the computed corner map.  */
    private List<Point> getCorners(Mat cornerMap) {

        UByteIndexer i = cornerMap.createIndexer();

        // Iterate over the pixels to obtain all feature points where matrix has non-zero values
        int width = cornerMap.cols();
        int height = cornerMap.rows();
        List<Point> points = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(i.get(y, x) != 0) {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }


    /**
     * Draw circles at feature point locations on an image
     */
    public void drawOnImage(Mat image, List<Point> points) {
        int radius = 4;
        int thickness = 1;
        Scalar color = new Scalar(255, 255, 255, 0);
        points.forEach(p -> {
            circle(image, new Point(p.x(), p.y()), radius, color, thickness, 8, 0);
        });
    }
}
