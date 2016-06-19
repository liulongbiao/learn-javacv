package cn.strong.plays.javacv.ch09;

import org.bytedeco.javacpp.opencv_features2d;
import org.bytedeco.javacpp.opencv_features2d.BFMatcher;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static cn.strong.plays.javacv.Helper.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_features2d.drawMatches;
import static org.bytedeco.javacpp.opencv_xfeatures2d.SURF;

/**
 * Computes SURF features,  extracts their descriptors, and finds best matching descriptors between two images of the same object.
 * There are a couple of tricky steps, in particular sorting the descriptors.
 *
 * Created by liulongbiao on 16-6-19.
 */
public class Ex7DescribingSURF {
    public static void main(String[] args) throws IOException {
        // Read input image
        Mat[] images = new Mat[]{
                load(new File("data/church01.jpg")),
                load(new File("data/church02.jpg"))
        };

        // Setup SURF feature detector and descriptor.
        double hessianThreshold = 2500d;
        int nOctaves = 4;
        int nOctaveLayers = 2;
        boolean extended = true;
        boolean upright = false;
        SURF surf = SURF.create(hessianThreshold, nOctaves, nOctaveLayers, extended, upright);
        //    val surfDesc = DescriptorExtractor.create("SURF")
        //  val surfDesc = SurfDescriptorExtractor.create("SURF")
        KeyPointVector[] keyPoints = {new KeyPointVector(), new KeyPointVector()};
        Mat[] descriptors = new Mat[2];

        // Detect SURF features and compute descriptors for both images
        for (int i = 0; i <= 1; i++) {
            surf.detect(images[i], keyPoints[i]);
            // Create CvMat initialized with empty pointer, using simply `new CvMat()` leads to an exception.
            descriptors[i] = new Mat();
            surf.compute(images[i], keyPoints[i], descriptors[i]);
        }

        // Create feature matcher
        BFMatcher matcher = new BFMatcher(NORM_L2, false);

        DMatchVector matches = new DMatchVector();
        // "match" is a keyword in Scala, to avoid conflict between a keyword and a method match of the BFMatcher,
        // we need to enclose method name in ticks: `match`.
        matcher.match(descriptors[0], descriptors[1], matches);
        System.out.println("Matched: " + matches.capacity());

        // Select only 25 best matches
        DMatchVector bestMatches = selectBest(matches, 25);

        // Draw best matches
        //  val imageMatches = cvCreateImage(new CvSize(images(0).width + images(1).width, images(0).height), images(0).depth, 3)
        Mat imageMatches = new Mat();
        byte[] mask = null;
        drawMatches(
                images[0], keyPoints[0], images[1], keyPoints[1],
                bestMatches, imageMatches, new Scalar(0, 0, 255, 0), new Scalar(255, 0, 0, 0), mask, opencv_features2d.DrawMatchesFlags.DEFAULT);
        show(imageMatches, "Best SURF Feature Matches");
    }

    /** Select only the best matches from the list. Return new list. */
    private static DMatchVector selectBest(DMatchVector matches, int numberToSelect) {
        // Convert to Scala collection, and sort
        DMatch[] sorted = toArray(matches);
        Arrays.sort(sorted, (a, b) -> {
            return a.lessThan(b) ? -1 : 1;
        });
        DMatch[] best = Arrays.copyOf(sorted, numberToSelect);
        // Select the best, and return in native vector
        return new DMatchVector(best);
    }
}
