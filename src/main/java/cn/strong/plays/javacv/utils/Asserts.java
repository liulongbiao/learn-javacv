package cn.strong.plays.javacv.utils;

import java.util.Collection;

/**
 * Created by liulongbiao on 16-6-16.
 */
public class Asserts {
    /**
     * 验证对象不为 null
     * @param obj
     * @param msg
     */
    public static void notNull(Object obj, String msg) {
        if(obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 验证条件为 true
     * @param bool
     * @param msg
     */
    public static void isTrue(boolean bool, String msg) {
        if(!bool) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 验证条件为 false
     * @param bool
     * @param msg
     */
    public static void isFlase(boolean bool, String msg) {
        if(bool) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 验证集合不为空
     * @param coll
     * @param msg
     */
    public static <E> void notEmpty(Collection<E> coll, String msg) {
        if(coll == null || coll.isEmpty()) {
            throw new IllegalArgumentException(msg);
        }
    }
}
