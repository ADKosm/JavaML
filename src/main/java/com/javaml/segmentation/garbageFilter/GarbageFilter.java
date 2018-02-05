package com.javaml.segmentation.garbageFilter;

import com.javaml.image.AsciiImage;

import java.util.ArrayList;
import java.util.List;

public abstract class GarbageFilter {
    public List<AsciiImage> filter(List<AsciiImage> images) {
        List<AsciiImage> result = new ArrayList<>();
        for(AsciiImage image : images) {
            if(checkImage(image)) result.add(image);
        }
        return result;
    }

    public abstract Boolean checkImage(AsciiImage image);
}
