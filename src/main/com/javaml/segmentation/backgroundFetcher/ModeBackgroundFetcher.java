package com.javaml.segmentation.backgroundFetcher;

import com.javaml.image.AsciiImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModeBackgroundFetcher implements BackgroundFetcher {
    @Override
    public Character fetchBackground(AsciiImage image) {
        List<Integer> counts = new ArrayList<>(Collections.nCopies(AsciiImage.C_Palette.length, 0));
        String palette = String.valueOf(AsciiImage.C_Palette);

        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                Integer index = palette.indexOf(image.getPixel(x, y));
                counts.set(index, counts.get(index) + 1);
            }
        }

        Integer maxIndex = 0;
        Integer maxValue = counts.get(0);
        for(int i = 0; i < counts.size(); i++) {
            if(counts.get(i) > maxValue) {
                maxIndex = i;
                maxValue = counts.get(i);
            }
        }
        return palette.charAt(maxIndex);
    }
}
