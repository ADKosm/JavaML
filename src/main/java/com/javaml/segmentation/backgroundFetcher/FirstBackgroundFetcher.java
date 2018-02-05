package com.javaml.segmentation.backgroundFetcher;

import com.javaml.image.AsciiImage;

public class FirstBackgroundFetcher implements BackgroundFetcher {
    @Override
    public Character fetchBackground(AsciiImage image) {
        return image.getPixel(0, 0);
    }
}
