package cn.strong.plays.javacv.ch10;

import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.CanvasFrame;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cn.strong.plays.javacv.Helper.toBufferedImage;
import static cn.strong.plays.javacv.Helper.toMatPoint3f;
import static org.bytedeco.javacpp.opencv_calib3d.*;
import static org.bytedeco.javacpp.opencv_core.CV_32FC1;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.INTER_LINEAR;
import static org.bytedeco.javacpp.opencv_imgproc.cornerSubPix;

/**
 * Camera calibration helper, used in example `Ex2CalibrateCamera`.
 *
 * Created by liulongbiao on 16-6-23.
 */
public class CameraCalibrator {

    // Input points
    private ArrayList<ArrayList<Point3f>> objectPoints = new ArrayList<>();
    private ArrayList<Mat> imagePoints = new ArrayList<>();
    // Output matrices
    private Mat _cameraMatrix = new Mat();
    private Mat _distortionCoeffs = new Mat();
    // flag to specify how calibration is done
    private int flag = 0;
    private boolean mustInitUndistort = true;

    /** Return a copy of the camera matrix. */
    public Mat cameraMatrix() {
        return _cameraMatrix.clone();
    }

    /** Return a copy of the distortion coefficients. */
    public Mat distortionCoeffs() {
        return _distortionCoeffs.clone();
    }

    /**
     * Set the calibration options.
     *
     * @param radial8CoeffEnabled should be true if 8 radial coefficients are required (5 is default).
     * @param tangentialParamEnabled should be true if tangential distortion is present.
     */
    public void setCalibrationFlag(boolean radial8CoeffEnabled, boolean tangentialParamEnabled) {
        // Set the flag used in cv::calibrateCamera()
        flag = 0;
        if (!tangentialParamEnabled) flag += CALIB_ZERO_TANGENT_DIST;
        if (radial8CoeffEnabled) flag += CALIB_RATIONAL_MODEL;
    }

    public int addChessboardPoints(List<File> fileList, Size boardSize) throws IOException {
        objectPoints.clear();
        imagePoints.clear();

        // 3D Scene Points:
        // Initialize the chessboard corners
        // in the chessboard reference frame
        // The corners are at 3D location (X,Y,Z)= (i,j,0)
        ArrayList<Point3f> objectCorners = new ArrayList<>(boardSize.height() * boardSize.width());
        for (int i = 0; i < boardSize.height(); i++) {
            for (int j = 0; j < boardSize.width(); j++) {
                objectCorners.add(new Point3f(i, j, 0));
            }
        }

        // 2D Image points:
        int successes = 0;
        // for all viewpoints
        for (File file : fileList) {

            // Open the image
            Mat image = imread(file.getAbsolutePath(), IMREAD_GRAYSCALE);
            if (image == null) {
                throw new IOException("Couldn't load image: " + file.getAbsolutePath());
            }

            // Get the chessboard corners
            // Allocate array to pass back corner coordinates: (x0, y0, x1, y1, ...)
            Mat imageCorners = new Mat();
            //      val cornerCount = new Size()
            boolean found = findChessboardCorners(image, boardSize, imageCorners);

            // Get subpixel accuracy on the corners
            cornerSubPix(
                    image, imageCorners, new Size(5, 5), new Size(-1, -1),
                    new TermCriteria(
                            TermCriteria.MAX_ITER + TermCriteria.EPS,
                            30, // max number of iterations
                            0.1) // min accuracy
            );

            // If we have a good board, add it to our data
            if (imageCorners.size().area() == boardSize.area()) {
                // Add image and scene points from one view
                imagePoints.add(imageCorners);
                objectPoints.add(objectCorners);
                successes += 1;
            }

            // Draw the corners
            drawChessboardCorners(image, boardSize, imageCorners, found);
            CanvasFrame canvas = new CanvasFrame("Corners on Chessboard: " + file.getName(), 1);
            canvas.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            canvas.showImage(toBufferedImage(image));
        }

        return successes;
    }

    /**
     * Calibrate the camera.
     *
     * @return final re-projection error.
     */
    public double calibrate(Size imageSize) {
        // undistorter must be reinitialized
        mustInitUndistort = true;

        // Prepare object and image points in format suitable for `cvCalibrateCamera2`
        MatVector[] matVevts = convertPoints();
        MatVector objectPointsMatVect = matVevts[0];
        MatVector imagePointsMatVect = matVevts[1];

        //Output rotations and translations
        MatVector rotationVectors = new MatVector();
        MatVector translationVectors = new MatVector();
        return calibrateCamera(
                objectPointsMatVect, // the 3D points
                imagePointsMatVect, // the image points
                imageSize, // image size
                _cameraMatrix, // output camera matrix
                _distortionCoeffs, // output distortion matrix
                rotationVectors, translationVectors, // Rs, Ts
                flag, // set options
                new TermCriteria(TermCriteria.MAX_ITER + TermCriteria.EPS, 30, Double.MIN_VALUE)
        );
    }

    /** Remove distortion in an image (after calibration). */
    public Mat remap(Mat image) {
        Mat undistorted = new Mat();
        Mat map1 = new Mat();
        Mat map2 = new Mat();
        if (mustInitUndistort) {
            // Called once per calibration
            opencv_imgproc.initUndistortRectifyMap(
                    _cameraMatrix, // computed camera matrix
                    _distortionCoeffs, // computed distortion matrix
                    new Mat(), // optional rectification (none)
                    new Mat(), // camera matrix to generate undistorted
                    image.size(), // size of undistorted
                    CV_32FC1, // type of output map
                    map1, map2); // the x and y mapping functions
            mustInitUndistort = false;
        }

        // Apply mapping functions
        opencv_imgproc.remap(image, undistorted, map1, map2, INTER_LINEAR);

        return undistorted;
    }

    /** Prepare object points, image points, and point counts in format required by `cvCalibrateCamera2`. */
    private MatVector[] convertPoints() {
        assert objectPoints.size() == imagePoints.size(); // Number of object and image points must match.

        int sz = objectPoints.size();
        MatVector objectPointsMatVect = new MatVector(sz);
        MatVector imagePointsMatVect = new MatVector(sz);
        for (int i = 0; i < sz; i++) {
            ArrayList<Point3f> objectP = objectPoints.get(i);
            Mat imageP = imagePoints.get(i);
            objectPointsMatVect.put(i, toMatPoint3f(objectP));
            imagePointsMatVect.put(i, imageP);
        }

        return new MatVector[]{objectPointsMatVect, imagePointsMatVect};
    }
}
