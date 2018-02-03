package com.javaml.converter;

import com.javaml.image.AsciiImage;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SimpleImageConverter implements ImageConverter {
    private Integer height;
    private Integer width;

    public SimpleImageConverter(Integer height, Integer width) {
        this.height = height;
        this.width = width;
    }

    public AsciiImage Convert(BufferedImage bufferedImage) {
        BufferedImage scaledImage = scale(bufferedImage);
        char[] imagePalette = AsciiImage.C_Palette;

        AsciiImage asciiImage = new AsciiImage(height, width);
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Character asciiPixel = convertPixel(scaledImage.getRGB(x, y), imagePalette);
                asciiImage.setPixel(asciiPixel, x, y);
            }
        }
        return asciiImage;
    }

    private BufferedImage scale(BufferedImage bufferedImage) {
        Image scaledImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics graphics = resultImage.getGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        graphics.dispose();

        return resultImage;
    }

    private Character convertPixel(Integer pixel, char[] palette) {
        Integer red = (pixel >>> 16) & 0xFF;
        Integer green = (pixel >>>  8) & 0xFF;
        Integer blue  = (pixel >>>  0) & 0xFF;

        Float luminance = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;
        Float paletteRange = (float) (palette.length - 1);

        Integer position = (int) (paletteRange * luminance + 0.5f);

        return palette[position];
    }
}
