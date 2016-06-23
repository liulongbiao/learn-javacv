package cn.strong.plays.javacv.ch10;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Size;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static org.bytedeco.javacpp.opencv_calib3d.drawChessboardCorners;
import static org.bytedeco.javacpp.opencv_calib3d.findChessboardCorners;

/**
 * Illustrates one of calibration steps, detection of a chessboard pattern in a calibration board.
 *
 * Created by liulongbiao on 16-6-23.
 */
public class Ex1FindChessboardCorners {
    public static void main(String[] args) throws IOException {
        Mat image = load(new File("data/chessboards/chessboard07.jpg"));

        // Find chessboard corners
        Size boardSize    = new Size(6, 4);
        // Allocate array to pass back corner coordinates: (x0, y0, x1, y1, ...)
        Mat imageCorners = new Mat();
        boolean patternFound = findChessboardCorners(image, boardSize, imageCorners);

        // Draw the corners
        drawChessboardCorners(image, boardSize, imageCorners, patternFound);
        show(image, "Corners on Chessboard");
    }
}
