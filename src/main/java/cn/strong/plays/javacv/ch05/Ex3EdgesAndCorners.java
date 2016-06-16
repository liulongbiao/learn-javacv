package cn.strong.plays.javacv.ch05;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * Created by liulongbiao on 16-6-16.
 */
public class Ex3EdgesAndCorners {
    public static void main(String[] args) throws IOException {
        Mat image = load(new File("data/building2.jpg"), IMREAD_GRAYSCALE);

        //  resize(image, image, new Size(), 0.7, 0.7, INTER_LINEAR)

        MorphoFeatures morpho = new MorphoFeatures();
        morpho.setThresholdValue(40);

        Mat edges = morpho.getEdges(image);
        show(edges, "Edges");

        morpho.setThresholdValue(-1);
        Mat corners = morpho.getCorners(image);
        morphologyEx(corners, corners, MORPH_TOPHAT, new Mat());
        threshold(corners, corners, 35, 255, THRESH_BINARY_INV);
        Image cornersOnImage = morpho.drawOnImage(corners, image);
        show(cornersOnImage, "Corners on image");
    }
}
