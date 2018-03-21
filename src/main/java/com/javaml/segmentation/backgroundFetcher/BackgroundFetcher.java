package com.javaml.segmentation.backgroundFetcher;

import com.javaml.image.AsciiImage;

public interface BackgroundFetcher {
    /**
     * Determine background symbol
     * @param image
     * @return ascii symbol
     */
    Character fetchBackground(AsciiImage image);
}
