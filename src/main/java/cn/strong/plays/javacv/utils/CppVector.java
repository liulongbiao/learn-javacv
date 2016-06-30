/**
 * 
 */
package cn.strong.plays.javacv.utils;

import java.util.Iterator;
import java.util.Objects;

/**
 * CPP 向量
 * 
 * @author liulongbiao
 *
 */
public interface CppVector<E> extends Iterable<E> {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	default Iterator<E> iterator() {
		return new Iter<>(this);
	}

	static class Iter<T> implements Iterator<T> {
		final CppVector<T> vec;
		long cursor = 0;

		public Iter(CppVector<T> vec) {
			this.vec = Objects.requireNonNull(vec);
		}

		@Override
		public boolean hasNext() {
			return cursor != vec.size();
		}

		@Override
		public T next() {
			return vec.get(cursor++);
		}

	}
}
