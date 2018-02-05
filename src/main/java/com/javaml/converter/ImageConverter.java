package com.javaml.converter;

import com.javaml.image.AsciiImage;

import java.awt.image.BufferedImage;

public interface ImageConverter {
    public AsciiImage Convert(BufferedImage img);
}
