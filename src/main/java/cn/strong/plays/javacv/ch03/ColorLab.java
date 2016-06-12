package cn.strong.plays.javacv.ch03;

/**
 * 表示 L*a*b* 颜色空间的颜色
 *
 * Created by liulongbiao on 16-6-12.
 */
public class ColorLab {

    public final double l;
    public final double a;
    public final double b;

    public ColorLab(double l, double a, double b) {
        this.l = l;
        this.a = a;
        this.b = b;
    }

    public int lAsUInt8() {
        return (int) (l * 255 / 100);
    }

    public int aAsUInt8() {
        return (int) (a + 128);
    }

    public int bAsUInt8() {
        return (int) (b + 128);
    }

}
