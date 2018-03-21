package com.javaml.segmentation.segmenter;

import com.javaml.image.AsciiImage;

import java.util.List;

public interface Segmenter {
    /**
     * Divide original image to several subimages by some criteria
     * @param image
     * @return
     */
    List<AsciiImage> segment(AsciiImage image);
}
