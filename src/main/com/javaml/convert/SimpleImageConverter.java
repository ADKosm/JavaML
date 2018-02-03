package com.javaml.convert;

import com.javaml.image.AsciiImage;

import java.awt.image.BufferedImage;

public class SimpleImageConverter implements ImageConverter {
    private Integer height;
    private Integer width;

    public SimpleImageConverter(Integer height, Integer width) {
        this.height = height;
        this.width = width;
    }

    public AsciiImage Convert(BufferedImage bufferedImage) {
        return new AsciiImage(height, width);
    }
}
