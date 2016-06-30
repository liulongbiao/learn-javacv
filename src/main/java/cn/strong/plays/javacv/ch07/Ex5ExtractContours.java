package cn.strong.plays.javacv.ch07;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_core.CV_8UC3;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgproc.CHAIN_APPROX_NONE;
import static org.bytedeco.javacpp.opencv_imgproc.RETR_EXTERNAL;
import static org.bytedeco.javacpp.opencv_imgproc.drawContours;
import static org.bytedeco.javacpp.opencv_imgproc.findContours;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;

import cn.strong.plays.javacv.utils.CppVectors;
import cn.strong.plays.javacv.utils.MatVectors;

/**
 * 提取轮廓线
 *
 * Created by liulongbiao on 16-6-18.
 */
public class Ex5ExtractContours {
    public static void main(String[] args) throws IOException {
        Mat src = load(new File("data/binaryGroup.bmp"), IMREAD_GRAYSCALE);

        // Extract connected components
        MatVector contours = new MatVector();
        Mat hierarchy = new Mat();
        findContours(src, contours, RETR_EXTERNAL, CHAIN_APPROX_NONE, new Point(0, 0));

        Mat result = new Mat(src.size(), CV_8UC3, new Scalar(0));
        drawContours(result, contours,
                -1, // draw all contours
                new Scalar(0, 0, 255, 0));
        show(result, "Contours");

        // Eliminate too short or too long contours
        long lengthMin = 100;
        long lengthMax = 1000;

		// MatVector filteredContours = filter(contours, lengthMin, lengthMax);
		// MatVector filteredContours = filterIter(contours, lengthMin, lengthMax);

		MatVector filteredContours = filterStream(contours, lengthMin, lengthMax);

        Mat result2 = load(new File("data/group.jpg"), IMREAD_COLOR);
        drawContours(result2, filteredContours,
                -1, // draw all contours
                new Scalar(0, 0, 255, 0));
        show(result2, "Contours Filtered");
    }

	private static MatVector filter(MatVector contours, long lengthMin, long lengthMax) {
		ArrayList<Mat> mats = new ArrayList<>();
		for (long i = 0, sz = contours.size(); i < sz; i++) {
			Mat contour = contours.get(i);
			long total = contour.total();
			if (lengthMin < total && total < lengthMax) {
				mats.add(contour);
			}
		}
		return new MatVector(mats.toArray(new Mat[0]));
	}

	private static MatVector filterIter(MatVector contours, long lengthMin, long lengthMax) {
		ArrayList<Mat> mats = new ArrayList<>();
		for (Mat contour : MatVectors.asCppVector(contours)) {
			long total = contour.total();
			if (lengthMin < total && total < lengthMax) {
				mats.add(contour);
			}
		}
		return new MatVector(mats.toArray(new Mat[0]));
	}

	private static MatVector filterStream(MatVector contours, long lengthMin, long lengthMax) {
		return CppVectors.stream(MatVectors.asCppVector(contours))
				.filter(contour -> {
					long total = contour.total();
					return lengthMin < total && total < lengthMax;
				}).collect(CppVectors.collector(MatVectors::fromList));
	}

}
