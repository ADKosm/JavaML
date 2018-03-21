package com.javaml.converting.converter;

import com.javaml.image.AsciiImage;

import java.awt.image.BufferedImage;

public interface ImageConverter {
    /**
     * Transform real image into AsciiImage
     * @param img - original image
     * @return converted image of type AsciiImage
     */
    public AsciiImage convert(BufferedImage img);
}
