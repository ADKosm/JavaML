package com.javaml.converting.scaler;

import com.javaml.image.AsciiImage;

public interface Scaler {
    /**
     * Resize AsciiImage
     * @param image - original ascii image
     * @param newWidth - width of new image
     * @param newHeight - height of new image
     * @return
     */
    AsciiImage scale(AsciiImage image, Integer newWidth, Integer newHeight);
}
