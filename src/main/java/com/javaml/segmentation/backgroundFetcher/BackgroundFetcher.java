package com.javaml.segmentation.backgroundFetcher;

import com.javaml.image.AsciiImage;

public interface BackgroundFetcher {
    Character fetchBackground(AsciiImage image);
}
