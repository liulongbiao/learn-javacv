/**
 * 
 */
package cn.strong.plays.javacv.utils;

/**
 * CPP 向量
 * 
 * @author liulongbiao
 *
 */
public interface CppVector<E> {

	/**
	 * 向量大小
	 * 
	 * @return
	 */
	long size();

	/**
	 * 获取指定索引处的元素
	 * 
	 * @param idx
	 * @return
	 */
	E get(long idx);

	/**
	 * 设置指定索引处的元素
	 * 
	 * @param idx
	 * @param item
	 */
	void set(long idx, E item);
}
