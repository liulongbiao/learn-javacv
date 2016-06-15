package cn.strong.plays.javacv.ch04;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;

/**
 * Created by liulongbiao on 16-6-16.
 */
public class Ex9ImageComparator {

    public static void main(String[] args) throws IOException {
        File referenceImageFile = new File("data/waves.jpg");

        File[] testImageFiles = {
                new File("data/waves.jpg"),
                new File("data/beach.jpg"),
                new File("data/dog.jpg"),
                new File("data/polar.jpg"),
                new File("data/bear.jpg"),
                new File("data/lake.jpg"),
                new File("data/moose.jpg")
        };

        // Load reference image
        Mat reference = load(referenceImageFile, IMREAD_COLOR);

        // Setup comparator
        ImageComparator comparator = new ImageComparator(reference);

        // Show reference image after color reduction done by `ImageComparator`
        show(comparator.getReferenceImage(), "Reference");

        // Compute similarity for test images
        for (File file : testImageFiles) {
            Mat image = load(file, IMREAD_COLOR);
            int imageSize = image.cols() * image.rows();
            // Compute histogram match and normalize by image size.
            // 1 means perfect match.
            double score = comparator.compare(image) / imageSize;
            String desc = file.getName() + String.format(", score: %6.4f", score);
            System.out.println(desc);
            show(image, desc);
        }
    }
}
