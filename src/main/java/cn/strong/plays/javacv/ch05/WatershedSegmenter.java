package cn.strong.plays.javacv.ch05;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static org.bytedeco.javacpp.opencv_imgproc.watershed;

/**
 * Created by liulongbiao on 16-6-16.
 */
public class WatershedSegmenter {
    private Mat _markers;

    public void setMarkers(Mat markerImage) {
        this._markers = new Mat();
        markerImage.convertTo(_markers, CV_32SC1);
    }

    public Mat process(Mat image) {
        watershed(image, _markers);
        return _markers;
    }

    public Mat segmentation() {
        // all segment with label higher than 255
        // will be assigned value 255
        Mat result = new Mat();
        _markers.convertTo(result, CV_8U, 1 /* scale */ , 0 /* shift */);
        return result;
    }

    public Mat watersheds() {
        Mat result = new Mat();
        _markers.convertTo(result, CV_8U, 255 /* scale */ , 255 /* shift */);
        return result;
    }
}
