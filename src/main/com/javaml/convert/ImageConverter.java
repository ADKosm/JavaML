package com.javaml.convert;

import com.javaml.image.AsciiImage;

import java.awt.image.BufferedImage;

public interface ImageConverter {
    public AsciiImage Convert(BufferedImage img);
}
