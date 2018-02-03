package com.javaml.segmentation.garbageFilter;

import com.javaml.image.AsciiImage;

public class SizeGarbageFilter extends GarbageFilter {
    private Integer threshold;

    public SizeGarbageFilter() {
        threshold = 7;
    }

    public SizeGarbageFilter(Integer threshold) {
        this.threshold = threshold;
    }

    @Override
    public Boolean checkImage(AsciiImage image) {
        return (image.getWidth() > threshold) && (image.getHeight() > threshold);
    }
}
