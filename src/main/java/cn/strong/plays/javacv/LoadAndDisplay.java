/**
 * 
 */
package cn.strong.plays.javacv;

import static org.bytedeco.javacpp.opencv_highgui.WINDOW_AUTOSIZE;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.namedWindow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

import org.bytedeco.javacpp.opencv_core.Mat;

/**
 * @author liulongbiao
 *
 */
public class LoadAndDisplay {

	static final String WIN_NAME = "Display window";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String imgName = "a15.jpg";
		Mat image = imread(Helper.getResourcePath(imgName));
		if (image.empty()) {
			throw new RuntimeException("cannot find img " + imgName + " in classpath");
		}
		namedWindow(WIN_NAME, WINDOW_AUTOSIZE); // Create a window for display.
		imshow(WIN_NAME, image); // Show our image inside it.
		waitKey(0); // Wait for a keystroke in the window
	}

}
