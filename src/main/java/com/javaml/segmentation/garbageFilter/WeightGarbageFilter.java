package com.javaml.segmentation.garbageFilter;

import com.javaml.image.AsciiImage;

public class WeightGarbageFilter extends GarbageFilter{
    private Float threshold;

    public WeightGarbageFilter() {
        threshold = 0.5f;
    }

    public WeightGarbageFilter(Float threshold) {
        this.threshold = threshold;
    }

    @Override
    public Boolean checkImage(AsciiImage image) {
        Integer sum = 0;
        String palette = String.valueOf(AsciiImage.C_Palette);
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                Character pixel = image.getPixel(x, y);
                sum += Math.max(0, palette.indexOf(pixel) - 7);
            }
        }
        return sum >= (threshold * image.getHeight()*image.getWidth());
    }
}
