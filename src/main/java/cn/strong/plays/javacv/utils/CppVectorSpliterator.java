/**
 * 
 */
package cn.strong.plays.javacv.utils;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author liulongbiao
 *
 */
public class CppVectorSpliterator<T> implements Spliterator<T> {
	
	private final CppVector<T> vec;
	private long index; // current index, modified on advance/split
	private final long fence; // one past last index
	private final int characteristics;

	public CppVectorSpliterator(CppVector<T> vec, int additionalCharacteristics) {
		this(vec, 0, vec.size(), additionalCharacteristics);
	}

	public CppVectorSpliterator(CppVector<T> vec, long origin, long fence,
			int additionalCharacteristics) {
		this.vec = vec;
		this.index = origin;
		this.fence = fence;
		this.characteristics = additionalCharacteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
	}

	@Override
	public boolean tryAdvance(Consumer<? super T> action) {
		if (action == null)
			throw new NullPointerException();
		if (index >= 0 && index < fence) {
			T e = vec.get(index++);
			action.accept(e);
			return true;
		}
		return false;
	}

	@Override
	public Spliterator<T> trySplit() {
		long lo = index, mid = (lo + fence) >>> 1;
		return (lo >= mid) ? null : 
			new CppVectorSpliterator<T>(vec, lo, index = mid, characteristics);
	}

	@Override
	public void forEachRemaining(Consumer<? super T> action) {
		CppVector<T> a; long i, hi; // hoist accesses and checks from loop
		if (action == null)
			throw new NullPointerException();
		if ((a = vec).size() >= (hi = fence) && (i = index) >= 0 && i < (index = hi)) {
			do {
				action.accept(a.get(i));
			} while (++i < hi);
		}
	}

	@Override
	public long estimateSize() {
		return fence - index;
	}

	@Override
	public int characteristics() {
		return characteristics;
	}

	@Override
	public Comparator<? super T> getComparator() {
		if (hasCharacteristics(Spliterator.SORTED))
			return null;
		throw new IllegalStateException();
	}
}
