/**
 * 
 */
package cn.strong.plays.javacv;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

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
	public static void display(Mat image, String caption) {
		CanvasFrame canvas = new CanvasFrame(caption, 1);
		canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		FrameConverter<Mat> converter = new OpenCVFrameConverter.ToMat();
		canvas.showImage(converter.convert(image));
	}
}
