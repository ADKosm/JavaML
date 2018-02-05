package com.javaml.segmentation.segmenter;

import com.javaml.image.AsciiImage;

import java.util.List;

public interface Segmenter {
    List<AsciiImage> segment(AsciiImage image);
}
