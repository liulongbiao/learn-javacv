package cn.strong.plays.javacv.ch10;

import org.bytedeco.javacpp.indexer.DoubleRawIndexer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Size;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;

/**
 * Created by liulongbiao on 16-6-23.
 */
public class Ex2CalibrateCamera {
    public static void main(String[] args) throws IOException {
        ArrayList<File> fileList = new ArrayList<>(20);
        for(int i = 1; i <= 20; i++) {
            fileList.add(new File(String.format("data/chessboards/chessboard%02d.jpg", i)));
        }

        CameraCalibrator cameraCalibrator = new CameraCalibrator();

        // Add the corners from the chessboard
        System.out.println("Adding chessboard points from images...");
        Size boardSize = new Size(6, 4);
        cameraCalibrator.addChessboardPoints(fileList, boardSize);

        // Load image for that will be undistorted
        Mat image = load(fileList.get(6));

        // Calibrate camera
        System.out.println("Calibrating...");
        cameraCalibrator.calibrate(image.size());

        // Undistort
        System.out.println("Undistorting...");
        Mat undistorted = cameraCalibrator.remap(image);

        // Display camera matrix
        Mat m     = cameraCalibrator.cameraMatrix();
        DoubleRawIndexer mIndx = m.createIndexer();
        System.out.println("Camera intrinsic: " + m.rows() + "x" + m.cols());
        for (long i = 0; i < 3; i++) {
            for (long j = 0; j < 3; i++) {
                System.out.print(String.format("%7.2f  ", mIndx.get(i, j)));
            }
            System.out.println("");
        }

        show(undistorted, "Undistorted image.");
    }
}
