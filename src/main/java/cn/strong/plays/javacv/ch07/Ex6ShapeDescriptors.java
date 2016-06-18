package cn.strong.plays.javacv.ch07;

import org.bytedeco.javacpp.opencv_core.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * 计算组件的形状描述符
 *
 * Created by liulongbiao on 16-6-18.
 */
public class Ex6ShapeDescriptors {
    private static final Scalar Red     = new Scalar(0, 0, 255, 0);
    private static final Scalar Magenta = new Scalar(255, 0, 255, 0);
    private static final Scalar Yellow  = new Scalar(0, 255, 255, 0);
    private static final Scalar Blue    = new Scalar(255, 0, 0, 0);
    private static final Scalar Cyan    = new Scalar(255, 255, 0, 0);
    private static final Scalar Green   = new Scalar(0, 255, 0, 0);

    public static void main(String[] args) throws IOException {
        //
        // First part is the same as in example `Ex5ExtractContours`; extracts contours.
        //

        // Read input image
        Mat src = load(new File("data/binaryGroup.bmp"), IMREAD_GRAYSCALE);

        // Extract connected components
        MatVector contourVec = new MatVector();
        CvMemStorage storage    = cvCreateMemStorage();
        findContours(src, contourVec, RETR_EXTERNAL, CHAIN_APPROX_NONE);

        // Draw extracted contours
        Mat colorDst = new Mat(src.size(), CV_8UC3, new Scalar(0));
        drawContours(colorDst, contourVec, -1 /* draw all contours */ , Red);
        show(colorDst, "Contours");

        // Eliminate too short or too long contours
        long lengthMin = 100;
        long lengthMax = 1000;

        ArrayList<Mat> mats = new ArrayList<>();
        for(long i = 0, sz = contourVec.size(); i < sz; i++) {
            Mat contour = contourVec.get(i);
            if(lengthMin < contour.total() && contour.total() < lengthMax) {
                mats.add(contour);
            }
        }
        MatVector filteredContours = new MatVector(mats.toArray(new Mat[0]));

        Mat colorDest2 = load(new File("data/group.jpg"), IMREAD_COLOR);
        drawContours(colorDest2, filteredContours, -1, Red, 2, LINE_8, noArray(), Integer.MAX_VALUE, new Point());

        //
        // Second part computes shapes descriptors from the extracted contours.
        //

        // Testing the bounding box
        //  val update = 0
        Rect rectangle0 = boundingRect(filteredContours.get(0));
        // Draw rectangle
        rectangle(colorDest2, rectangle0, Magenta, 2, LINE_AA, 0);

        // Testing the enclosing circle
        Point2f center1 = new Point2f();
        float[] radius1 = { 1f };
        minEnclosingCircle(filteredContours.get(1), center1, radius1);
        // Draw circle
        circle(colorDest2, new Point(cvRound(center1.x()), cvRound(center1.y())), (int) radius1[0], Yellow, 2, LINE_AA, 0);

        // Testing the approximate polygon
        Mat poly2 = new Mat();
        approxPolyDP(filteredContours.get(2), poly2, 5, true);
        // Draw only the first poly.
        polylines(colorDest2, new MatVector(new Mat[]{ poly2}), true, Blue, 2, LINE_AA, 0);

        // Testing the convex hull
        boolean clockwise    = true;
        boolean returnPoints = true;
        Mat hull         = new Mat();
        convexHull(filteredContours.get(3), hull, clockwise, returnPoints);
        polylines(colorDest2, new MatVector(new Mat[]{ hull }), true, Cyan, 2, LINE_AA, 0);

        // Testing the moments for all filtered contours, and marking center of mass on the image
        for(long j = 0, sz = filteredContours.size(); j < sz; j++) {
            Mat c = filteredContours.get(j);
            Moments ms = moments(c);
            int xCenter = (int) Math.round(ms.m10() / ms.m00());
            int yCenter = (int) Math.round(ms.m01() / ms.m00());
            circle(colorDest2, new Point(xCenter, yCenter), 2, Green, 2, LINE_AA, 0);
        }

        // Show the image with marked contours and shape descriptors
        show(colorDest2, "Some Shape Descriptors");
    }
}
