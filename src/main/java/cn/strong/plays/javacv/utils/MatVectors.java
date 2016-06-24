/**
 * 
 */
package cn.strong.plays.javacv.utils;

import java.util.ArrayList;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;

/**
 * MatVector 帮助类
 * 
 * @author liulongbiao
 *
 */
public class MatVectors {

	/**
	 * 转换为CppVector<Mat>
	 * 
	 * @param vec
	 * @return
	 */
	public static CppVector<Mat> asCppVector(MatVector vec) {
		return new CppVector<Mat>() {

			@Override
			public long size() {
				return vec.size();
			}

			@Override
			public void set(long idx, Mat item) {
				vec.put(idx, item);
			}

			@Override
			public Mat get(long idx) {
				return vec.get(idx);
			}
		};
	}

	/**
	 * 从列表构造 MatVector
	 * 
	 * @param list
	 * @return
	 */
	public static MatVector fromList(ArrayList<Mat> list) {
		Mat[] array = list.toArray(new Mat[list.size()]);
		return new MatVector(array);
	}
}
