package cn.strong.plays.javacv.ch04;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.TermCriteria;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.drawOnImage;
import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGR2HSV;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_video.meanShift;

/**
 * 使用移动平均算法来找到'模板'在另一个图像中的最佳匹配位置。
 * Matching is done using the hue channel of the input image converted to HSV color space.
 * Histogram of a region in the hue channel is used to create a 'template'.
 *
 * The target image, where we want to find a matching region, is also converted to HSV.
 * Histogram of the template is back projected in the hue channel.
 * The mean shift algorithm searches in the back projected image to find best match to the template.
 *
 * Example for section "Using the mean shift algorithm to find an object" in Chapter 4.
 *
 * Created by liulongbiao on 16-6-16.
 */
public class Ex8MeanShiftDetector {
    public static void main(String[] args) throws IOException {
        Scalar Red = new Scalar(0, 0, 255, 128);

        //
        // Prepare 'template'
        //

        // Load image as a color
        Mat templateImage = load(new File("data/baboon1.jpg"), IMREAD_COLOR);

        // Display image with marked ROI
        Rect rect = new Rect(110, 260, 35, 40);
        show(drawOnImage(templateImage, rect, Red), "Input template");

        // Define ROI for sample histogram
        Mat imageROI = templateImage.apply(rect);

        // Compute histogram within the ROI
        int minSaturation = 65;
        Mat templateHueHist = new ColorHistogram().getHueHistogram(imageROI, minSaturation);

        ContentFinder finder = new ContentFinder();
        finder.setHistogram(templateHueHist);

        //
        //  Search a target image for best match to the 'template'
        //

        // Load the second image where we want to locate a new baboon face
        Mat targetImage = load(new File("data/baboon3.jpg"), IMREAD_COLOR);

        // Convert to HSV color space
        Mat hsvTargetImage = new Mat();
        cvtColor(targetImage, hsvTargetImage, COLOR_BGR2HSV);

        // Get back-projection of hue histogram
        finder.setThreshold(-1f);
        Mat hueBackProjectionImage = finder.find(hsvTargetImage, 0f, 180f, new int[]{0});
        show(hueBackProjectionImage, "Backprojection of second image");

        // Search for object with mean-shift
        TermCriteria criteria = new TermCriteria(TermCriteria.MAX_ITER, 10, 0.01);
        int r = meanShift(hueBackProjectionImage, rect, criteria);
        System.out.println("meanshift = " + r);

        show(drawOnImage(targetImage, rect, Red), "Image 2 result in " + r + " iterations");
    }
}
