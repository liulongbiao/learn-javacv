package cn.strong.plays.javacv.ch05;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_core.BORDER_CONSTANT;
import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static org.bytedeco.javacpp.opencv_core.add;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * Created by liulongbiao on 16-6-16.
 */
public class Ex4WatershedSegmentation {
    public static void main(String[] args) throws IOException {
        // Read input image
        Mat image  = load(new File("data/group.jpg"), IMREAD_COLOR);
        Mat binary = load(new File("data/binary.bmp"), IMREAD_GRAYSCALE);

        // Eliminate noise and smaller objects, repeat erosion 6 times
        Mat fg = new Mat();
        erode(binary, fg,
                new Mat() /* 3x3 square */ ,
                new opencv_core.Point(-1, -1),
                6 /* iterations */ ,
                BORDER_CONSTANT,
                morphologyDefaultBorderValue());
        show(fg, "Foreground");

        // Identify image pixels pixels objects
        Mat bg = new Mat();
        dilate(binary, bg,
                new Mat() /* 3x3 square */ ,
                new opencv_core.Point(-1, -1),
                6 /* iterations */ ,
                BORDER_CONSTANT,
                morphologyDefaultBorderValue());
        show(bg, "Dilated");

        threshold(bg, bg,
                1 /* threshold */ ,
                128 /* max value */ ,
                THRESH_BINARY_INV);
        show(bg, "Background");

        // Create marker image
        Mat markers = new Mat(binary.size(), CV_8U, new Scalar(0d));
        add(fg, bg, markers);
        show(markers, "Markers");

        WatershedSegmenter segmenter = new WatershedSegmenter();
        segmenter.setMarkers(markers);

        Mat segmentMarkers = segmenter.process(image);
        show(segmentMarkers, "segmentMarkers");

        Mat segmentation = segmenter.segmentation();
        show(segmentation, "Segmentation");

        Mat watershed = segmenter.watersheds();
        show(watershed, "Watersheds");
    }
}
