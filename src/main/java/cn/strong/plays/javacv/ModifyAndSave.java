/**
 * 
 */
package cn.strong.plays.javacv;

import static org.bytedeco.javacpp.opencv_highgui.WINDOW_AUTOSIZE;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.namedWindow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;

import java.io.File;
import java.io.IOException;

import org.bytedeco.javacpp.opencv_core.Mat;

/**
 * @author liulongbiao
 *
 */
public class ModifyAndSave {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String imgName = "lena.png";
		Mat image = imread(Helper.getResourcePath(imgName), IMREAD_COLOR);
		Mat grayImage = new Mat();
		cvtColor(image, grayImage, COLOR_BGR2GRAY);

		File distFile = File.createTempFile("lena_gray", ".png");
		String distName = distFile.getName();
		imwrite(distFile.getAbsolutePath(), grayImage);
		namedWindow(imgName, WINDOW_AUTOSIZE);
		namedWindow(distName, WINDOW_AUTOSIZE);
		imshow(imgName, image);
		imshow(distName, grayImage);
		waitKey(0);
	}

}
