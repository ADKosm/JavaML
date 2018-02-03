package com.javaml.segmentation.segmenter;

import com.javaml.image.AsciiImage;
import com.javaml.segmentation.backgroundFetcher.BackgroundFetcher;
import com.javaml.segmentation.backgroundFetcher.FirstBackgroundFetcher;
import com.javaml.segmentation.backgroundFetcher.ModeBackgroundFetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LinearSegmenter implements Segmenter {
    public enum BackgroundFetchStrategy {FIRST, MODE};
    private enum Axis {X, Y};

    private BackgroundFetcher fetcher;

    public LinearSegmenter() {
        fetcher = new ModeBackgroundFetcher();
    }

    public LinearSegmenter(BackgroundFetchStrategy strategy) {
        switch (strategy) {
            case MODE:
                fetcher = new ModeBackgroundFetcher();
                break;
            case FIRST:
                fetcher = new FirstBackgroundFetcher();
                break;
        }
    }

    @Override
    public List<AsciiImage> segment(AsciiImage image) {
        List<AsciiImage> result = new ArrayList<>();

        Character background = fetcher.fetchBackground(image);

        List<Integer> horizontalLines = fetchLines(image, background, Axis.X);
        List<Integer> verticalLines = fetchLines(image, background, Axis.Y);

        for(int x = 0; x < verticalLines.size(); x++) {
            for(int y = 0; y < horizontalLines.size(); y++) {
                int lx = x - 1 < 0 ? 0 : verticalLines.get(x - 1);
                int ly = y - 1 < 0 ? 0 : horizontalLines.get(y - 1);
                int rx = verticalLines.get(x);
                int ry = horizontalLines.get(y);
                result.add(image.getCrop(lx, ly, rx, ry));
            }
        }

        return result;
    }

    private List<Integer> fetchLines(AsciiImage image, Character background, Axis axis) {
        List<Integer> result = new ArrayList<>();

        Integer cursor = 0;
        Integer end = (axis == Axis.X) ? image.getWidth() : image.getHeight();

        Integer beginGap;

        while (cursor < end) {
            if(checkLine(image, cursor, axis, background)) {
                beginGap = cursor;
                while (checkLine(image, cursor, axis, background) && cursor < end) cursor++;
                result.add( (cursor + beginGap) / 2);
            }
            cursor++;
        }
        return result;
    }

    private Boolean checkLine(AsciiImage image, Integer index, Axis axis, Character background) {
        Function<Integer, Character> getPixel = (Integer pos) ->
                axis == Axis.X ? image.getPixel(index, pos) : image.getPixel(pos, index);

        Integer end = (axis == Axis.X) ? image.getHeight() : image.getWidth();

        for(int position = 0; position < end; position++) {
            if(getPixel.apply(position) != background) {
                return false;
            }
        }
        return true;
    }
}