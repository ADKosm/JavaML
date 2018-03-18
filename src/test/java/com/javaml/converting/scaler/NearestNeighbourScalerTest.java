package com.javaml.converting.scaler;

import com.javaml.image.AsciiImage;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NearestNeighbourScalerTest {
    private NearestNeighbourScaler scaler = new NearestNeighbourScaler();
    private AsciiImage image = new AsciiImage(10, 10);

    @Test
    public void testUpscale() {
        rescaleAndCheck(image, 20, 20   );
    }

    @Test
    public void testDownscale() {
        rescaleAndCheck(image, 5, 5);
    }

    private void rescaleAndCheck(AsciiImage image, Integer width, Integer height) {
        AsciiImage newImage = scaler.scale(image, width, height);
        assertTrue(width.equals(newImage.getWidth()));
        assertTrue(height.equals(newImage.getHeight()));
    }
}
