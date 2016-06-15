package cn.strong.plays.javacv.utils;

/**
 * Created by liulongbiao on 16-6-15.
 */
public class Floats {

    /**
     * 计算最大值
     * @param array
     * @return
     */
    public static float max(float[] array) {
        float max = Float.MIN_VALUE;
        for(float f : array) {
            if(f > max) {
                max = f;
            }
        }
        return max;
    }

    /**
     * 求和
     * @param array
     * @return
     */
    public static float sum(float[] array) {
        float v = 0;
        for(float f : array) {
            v += f;
        }
        return v;
    }
}
