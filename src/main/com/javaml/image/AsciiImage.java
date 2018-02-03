package com.javaml.image;

import java.util.ArrayList;
import java.util.Collections;

public class AsciiImage {
    public static char[] C_Palette = "   ...',;:clodxkO0KXNWM".toCharArray();

    private ArrayList<ArrayList<Character>> pixels;

    public AsciiImage(Integer height, Integer width) {
        this.pixels = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            this.pixels.add(new ArrayList<>(Collections.nCopies(width, ' ')));
        }
    }

    public Character getPixel(Integer x, Integer y) {
        return pixels.get(x).get(y);
    }

    public void setPixel(Character pixel, Integer x, Integer y) {
        pixels.get(y).set(x, pixel);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(ArrayList<Character> line : pixels) {
            for(Character pixel : line) {
                builder.append(pixel);
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
