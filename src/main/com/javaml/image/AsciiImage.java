package com.javaml.image;

import java.util.ArrayList;

public class AsciiImage {
    private ArrayList<ArrayList<Character>> pixels;

    public AsciiImage(Integer height, Integer width) {
        pixels = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            pixels.set(i, new ArrayList<>(width));
        }
    }

    public getPixel(Integer x, Integer y) {
        // TODO: implement
    }

    public setPixel(Character pixel, Integer x, Integer y) {
        // TODO: implement
    }
}
