/**
 * 
 */
package cn.strong.plays.javacv;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.Math.round;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.circle;
import static org.bytedeco.javacpp.opencv_imgproc.rectangle;

/**
 * @author liulongbiao
 *
 */
public class Helper {

	/**
	 * 获取图片资源路径地址
	 * 
	 * @param img
	 * @return
	 */
	public static String getResourcePath(String img) {
		return Helper.class.getResource("/" + img).getPath();
	}

	/**
	 * 以彩色空间加载图片
	 *
	 * @param file
	 * @return
	 * @throws IOException
     */
	public static Mat load(File file) throws IOException {
		return load(file, IMREAD_COLOR);
	}

	/**
	 * 加载图片
	 *
	 * @param file
	 * @param flags
	 * @return
	 * @throws IOException
     */
	public static Mat load(File file, int flags) throws IOException {
		if(!file.exists()) {
			throw new FileNotFoundException("Image file does not exist: " + file.getAbsolutePath());
		}
		Mat image = imread(file.getAbsolutePath(), flags);
		if(image == null || image.empty()) {
			throw new IOException("Couldn't load image: " + file.getAbsolutePath());
		}
		return image;
	}


	/**
	 * 显示图片
	 *
	 * @param image
	 * @param caption
	 */
	public static void show(Mat image, String caption) {
		CanvasFrame canvas = new CanvasFrame(caption, 1);
		canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		FrameConverter<Mat> converter = new OpenCVFrameConverter.ToMat();
		canvas.showImage(converter.convert(image));
	}

	/**
	 * 显示图片
	 *
	 * @param image
	 * @param caption
     */
	public static void show(Image image, String caption) {
		CanvasFrame canvas = new CanvasFrame(caption, 1);
		canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		canvas.showImage(image);
	}

	/**
	 * 在图像的指定点画红色的圆
	 */
	public static Mat drawOnImage(Mat image, Point2fVector points) {
		Mat dest = image.clone();
		int radius = 5;
		Scalar red = new Scalar(0, 0, 255, 0);
		for (int i = 0; i < points.size(); i++) {
			Point2f p = points.get(i);
			circle(dest, new Point(round(p.x()), round(p.y())), radius, red);
		}

		return dest;
	}

	/**
	 * 在图像上画一个形状
	 * @param image
	 * @param overlay
	 * @param color
     * @return
     */
	public static Mat drawOnImage(Mat image, Rect overlay, Scalar color) {
		Mat dest = image.clone();
		rectangle(dest, overlay, color);
		return dest;
	}
}
