package com.javaml.segmentation.backgroundFetcher;

import com.javaml.image.AsciiImage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FirstBackgroundFetcherTest {
    private FirstBackgroundFetcher fetcher = new FirstBackgroundFetcher();
    AsciiImage image = new AsciiImage(4, 4);
    Character expected = 'M';

    @Before
    public void setUp() {
        image.setPixel(expected, 0, 0);
    }

    @Test
    public void testFetcher() {
        Character actual = fetcher.fetchBackground(image);
        Assert.assertEquals(expected, actual);
    }
}
