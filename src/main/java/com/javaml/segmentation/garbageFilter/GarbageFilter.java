package com.javaml.segmentation.garbageFilter;

import com.javaml.image.AsciiImage;

import java.util.ArrayList;
import java.util.List;

public abstract class GarbageFilter {
    /**
     * Build list with matched images
     * @param images - original list
     * @return
     */
    public List<AsciiImage> filter(List<AsciiImage> images) {
        List<AsciiImage> result = new ArrayList<>();
        for(AsciiImage image : images) {
            if(checkImage(image)) result.add(image);
        }
        return result;
    }

    /**
     * Check if image is match to the filter criteria
     * @param image
     * @return
     */
    public abstract Boolean checkImage(AsciiImage image);
}
