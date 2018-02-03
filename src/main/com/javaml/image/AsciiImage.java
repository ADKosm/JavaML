package com.javaml.image;

import com.javaml.ml.Tensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AsciiImage implements Tensor<Number> {
    public static String C_Palette = new String("   ...',;:clodxkO0KXNWM".toCharArray());

    private ArrayList<ArrayList<Character>> pixels;
    private Integer width;
    private Integer height;

    public AsciiImage(Integer height, Integer width) {
        this.width = width;
        this.height = height;
        pixels = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            pixels.add(new ArrayList<>(Collections.nCopies(width, ' ')));
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

    public List<Integer> getShape() {
        return Arrays.asList(height, width);
    }

    public Number getElement(Integer ordinal) {
        Character pixel = getPixel(ordinal / height, ordinal % height);
        return C_Palette.indexOf(pixel);
    }
}
