package com.javaml.segmentation.garbageFilter;

import com.javaml.image.AsciiImage;

import java.util.List;

public class CompositeGarbageFilter extends GarbageFilter {
    private List<GarbageFilter> filters;

    public CompositeGarbageFilter(List<GarbageFilter> filters) {
        this.filters = filters;
    }

    @Override
    public Boolean checkImage(AsciiImage image) {
        Boolean result = true;
        for(GarbageFilter filter : filters) {
            result = result && filter.checkImage(image);
        }
        return result;
    }
}
