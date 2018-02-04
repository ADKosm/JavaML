package com.javaml.converting.scaler;

import com.javaml.image.AsciiImage;

public interface Scaler {
    AsciiImage scale(AsciiImage image, Integer newWidth, Integer newHeight);
}
