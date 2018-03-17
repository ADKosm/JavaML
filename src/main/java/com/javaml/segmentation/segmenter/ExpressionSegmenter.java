package com.javaml.segmentation.segmenter;

import com.javaml.image.AsciiImage;
import com.javaml.segmentation.backgroundFetcher.BackgroundFetcher;
import com.javaml.segmentation.backgroundFetcher.ModeBackgroundFetcher;
import com.javaml.segmentation.garbageFilter.CompositeGarbageFilter;
import com.javaml.segmentation.garbageFilter.GarbageFilter;
import com.javaml.segmentation.garbageFilter.SizeGarbageFilter;
import com.javaml.segmentation.garbageFilter.WeightGarbageFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExpressionSegmenter implements Segmenter {
    private BackgroundFetcher fetcher;
    private GarbageFilter filter;

    public ExpressionSegmenter() {
        fetcher = new ModeBackgroundFetcher();
        filter = new CompositeGarbageFilter(Arrays.asList(
                new SizeGarbageFilter(7),
                new WeightGarbageFilter(0.5f)
        ));
    }

    @Override
    public List<AsciiImage> segment(AsciiImage image) {
        List<AsciiImage> result = new ArrayList<>();

        Character background = fetcher.fetchBackground(image);

        List<Integer> pivots = fetchPivots(image, background);

        for(int i = 1; i < pivots.size(); i++) {
            Integer lx = pivots.get(i - 1);
            Integer ly = 0;
            Integer rx = pivots.get(i);
            Integer ry = image.getHeight();
            result.add(image.getCrop(lx, ly, rx, ry));
        }

        return filter.filter(result);
    }

    private List<Integer> fetchPivots(AsciiImage image, Character background) {
        List<Integer> result = new ArrayList<>();

        result.add(0);
        for(int cursor = 0; cursor < image.getWidth(); cursor++) {
            if(checkLine(image, cursor, background)) result.add(cursor);
        }
        result.add(image.getWidth());

        return result;
    }

    private Boolean checkLine(AsciiImage image, Integer index, Character background) {
        for(int position = 0; position < image.getHeight(); position++) {
            if(image.getPixel(index, position) != background) return false;
        }
        return true;
    }
}
