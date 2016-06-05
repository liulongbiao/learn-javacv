/**
 * 
 */
package cn.strong.plays.javacv;

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
}
