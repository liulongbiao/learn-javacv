package cn.strong.plays.javacv.ch05;

import org.bytedeco.javacpp.opencv_core;

import java.io.File;
import java.io.IOException;

import static cn.strong.plays.javacv.Helper.load;
import static cn.strong.plays.javacv.Helper.show;
import static cn.strong.plays.javacv.Helper.toMat8U;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 * 使用 GranCut 算法提取前景
 *
 * Created by liulongbiao on 16-6-16.
 */
public class Ex5GrabCut {
    public static void main(String[] args) throws IOException {
        // Open image
        //  val image = loadCvMatAndShowOrExit(new File("data/group.jpg"), IMREAD_COLOR)
        Mat image = load(new File("data/group.jpg"), IMREAD_COLOR);

        // Define bounding rectangle, pixels outside this rectangle will be labeled as background.
        Rect rectangle = new Rect(10, 100, 380, 180);

        Mat result = new Mat();
        int iterCount = 5;
        int mode = GC_INIT_WITH_RECT;

        // Need to allocate arrays for temporary data
        Mat bgdModel = new Mat();
        Mat fgdModel = new Mat();

        // GrabCut segmentation
        grabCut(image, result, rectangle, bgdModel, fgdModel, iterCount, mode);

        // Prepare image for display: extract foreground
        threshold(result, result, GC_PR_FGD - 0.5, GC_PR_FGD + 0.5, THRESH_BINARY);
        show(toMat8U(result), "Result foreground mask");
    }
}
