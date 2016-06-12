package cn.strong.plays.javacv.ch03;

import java.awt.*;

/**
 * Created by liulongbiao on 16-6-12.
 */
public class ColorRGB {

    public static ColorRGB fromBGR(byte[] b) {
        assert b.length == 3;
        return new ColorRGB(b[2] & 0xFF, b[1] & 0xFF, b[0] & 0xFF);
    }

    public static ColorRGB fromBGR(int[] b) {
        assert b.length == 3;
        return new ColorRGB(b[2], b[1], b[0]);
    }

    public final int red;
    public final int green;
    public final int blue;

    public ColorRGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public ColorRGB(Color color) {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
    }

    public Color toColor() {
        return new Color(red, green, blue);
    }
}
