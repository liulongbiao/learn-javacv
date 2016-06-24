/**
 * 
 */
package cn.strong.plays.javacv.utils;

import java.util.ArrayList;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * C++ 向量帮助类
 * 
 * @author liulongbiao
 *
 */
public class CppVectors {

	/**
	 * 返回覆盖指定 CppVector 中所有元素的 {@link Spliterator}
	 * 
	 * <p>The spliterator reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, {@link Spliterator#ORDERED}, and
     * {@link Spliterator#IMMUTABLE}.
	 * 
	 * @param vec
	 * @return
	 */
	public static <T> Spliterator<T> spliterator(CppVector<T> vec) {
		return new CppVectorSpliterator<>(vec, Spliterator.ORDERED | Spliterator.IMMUTABLE);
	}

	/**
	 * 返回覆盖指定 CppVector 中指定范围内元素的 {@link Spliterator}
	 * 
	 * <p>The spliterator reports {@link Spliterator#SIZED},
     * {@link Spliterator#SUBSIZED}, {@link Spliterator#ORDERED}, and
     * {@link Spliterator#IMMUTABLE}.
	 * 
	 * @param vec
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static <T> Spliterator<T> spliterator(CppVector<T> vec, long startInclusive,
			long endExclusive) {
		return new CppVectorSpliterator<>(vec, startInclusive, endExclusive, Spliterator.ORDERED
				| Spliterator.IMMUTABLE);
	}

	/**
	 * 返回指定 CppVector 为源的顺序的 {@link Stream}
	 * 
	 * @param vec
	 * @return
	 */
	public static <T> Stream<T> stream(CppVector<T> vec) {
		return stream(vec, 0, vec.size());
	}

	/**
	 * 返回指定 CppVector 指定范围内元素为源的顺序的 {@link Stream}
	 * 
	 * @param array
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static <T> Stream<T> stream(CppVector<T> array, long startInclusive, long endExclusive) {
		return StreamSupport.stream(spliterator(array, startInclusive, endExclusive), false);
	}

	/**
	 * 向量收集器
	 * 
	 * @param ctor
	 * @return
	 */
	public static <T, R> Collector<T, ArrayList<T>, R> collector(Function<ArrayList<T>, R> ctor) {
		return Collector.of(ArrayList::new, ArrayList::add, (left, right) -> {
			left.addAll(right);
			return left;
		}, (ArrayList<T> list) -> {
			return ctor.apply(list);
		});
	}
}
