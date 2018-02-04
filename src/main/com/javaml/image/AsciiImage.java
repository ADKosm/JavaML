package com.javaml.image;

import com.javaml.converting.scaler.NearestNeighborScaler;
import com.javaml.converting.scaler.Scaler;
import com.javaml.ml.Tensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AsciiImage implements Tensor<Number> {
    public static String C_Palette = "      ',;:clodxkO0KXNWM";
    public enum Axis {X, Y};

    private ArrayList<ArrayList<Character>> pixels;
    private Integer height;
    private Integer width;

    public AsciiImage(Integer height, Integer width) {
        this.height = height;
        this.width = width;

        pixels = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            pixels.add(new ArrayList<>(Collections.nCopies(width, ' ')));
        }
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public Character getPixel(Integer x, Integer y) {
        return pixels.get(y).get(x);
    }

    public void setPixel(Character pixel, Integer x, Integer y) {
        pixels.get(y).set(x, pixel);
    }

    public AsciiImage getScaled(Integer newWidth, Integer newHeight) {
        Scaler scaler = new NearestNeighborScaler();  // TODO: make scale strategy parametrized
        return scaler.scale(this, newWidth, newHeight);
    }

    public AsciiImage getCrop(Integer lx, Integer ly, Integer rx, Integer ry) {
        AsciiImage result = new AsciiImage(ry - ly, rx - lx);
        for(int x = 0; x < result.getWidth(); x++) {
            for(int y = 0; y < result.getHeight(); y++) {
                Character pixel = getPixel(lx + x, ly + y);
                result.setPixel(pixel, x, y);
            }
        }
        return result;
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
