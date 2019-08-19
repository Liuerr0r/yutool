package com.yupaits.yutool.file.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 水印工具类
 * 参考：https://www.cnblogs.com/wzluo09/p/9669989.html
 * @author yupaits
 * @date 2019/8/9
 */
public class WatermarkUtils {

    public static BufferedImage textToBufferedImage(String text) {
        return textToBufferedImage(text, 96, 32);
    }

    public static BufferedImage textToBufferedImage(String text, int width, int height) {
        return textToBufferedImage(text, width, height, Color.LIGHT_GRAY, 0.8f);
    }

    public static BufferedImage textToBufferedImage(String text, int width, int height, Color color, float alpha) {
        //JDK默认字体
        Font font = new Font(Font.DIALOG, Font.ROMAN_BASELINE, 18);
        return textToBufferedImage(text, width, height, color, font, 0d, alpha);
    }

    /**
     * 文字转水印图片
     * @param text 文字内容
     * @param width 文字区域宽度
     * @param height 文字区域高度
     * @param color 文字颜色
     * @param font 字体
     * @param degree 水印旋转度数
     * @param alpha 水印图片透明度
     * @return 水印BufferedImage图片
     */
    public static BufferedImage textToBufferedImage(String text, int width, int height, Color color, Font font, Double degree, float alpha) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        //背景透明
        image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = image.createGraphics();
        //设置对线段的锯齿边缘处理
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        //设置水印旋转
        if (degree != null) {
            g2d.rotate(Math.toRadians(degree), (double) image.getWidth() / 2, (double) image.getHeight() / 2);
        }
        //设置颜色
        g2d.setColor(color);
        //设置字体
        g2d.setFont(font);
        //设置透明度
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        //文字居中
        float realWidth = getRealFontWidth(text);
        float fontSize = g2d.getFont().getSize();
        float x = width * 0.5f - fontSize * realWidth * 0.5f;
        float y = height * 0.5f + fontSize * 0.5f;
        //画出文字
        g2d.drawString(text, x, y);
        g2d.dispose();
        return image;
    }

    /**
     * 获取真实字符串宽度，ascii字符占用0.5，中文字符占用1.0
     */
    public static float getRealFontWidth(String text) {
        int len = text.length();
        float width = 0f;
        for (int i = 0; i < len; i++) {
            if (text.charAt(i) < 256) {
                width += 0.5f;
            } else {
                width += 1.0f;
            }
        }
        return width;
    }
}
