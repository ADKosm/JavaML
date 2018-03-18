package com.javaml.segmentation.backgroundFetcher;

import com.javaml.image.AsciiImage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ModeBackgroundFetcherTest {
    private ModeBackgroundFetcher fetcher = new ModeBackgroundFetcher();
    AsciiImage image = new AsciiImage(4, 4);
    Character expected = 'M';

    @Before
    public void setUp() {
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                image.setPixel(expected, i, j);
            }
        }
    }

    @Test
    public void testFetcher() {
        Character actual = fetcher.fetchBackground(image);
        Assert.assertEquals(expected, actual);
    }
}
