package com.javaml.image;

import com.javaml.converting.scaler.NearestNeighbourScaler;
import com.javaml.converting.scaler.Scaler;
import com.javaml.ml.Tensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Representation of ascii image
 */
public class AsciiImage implements Tensor<Number> {
    public static String defaultPalette = "      ',;:clodxkO0KXNWM";
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


    /**
     * Get resized image. Do not change original image
     * @param newWidth
     * @param newHeight
     * @return
     */
    public AsciiImage getScaled(Integer newWidth, Integer newHeight) {
        Scaler scaler = new NearestNeighbourScaler();  // TODO: make scale strategy parametrized
        return scaler.scale(this, newWidth, newHeight);
    }

    /**
     * Get subimage of original image
     * @param lx - left x coordinate
     * @param ly - left y coordinate
     * @param rx - right x coordinate
     * @param ry - right y coordinate
     * @return
     */
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

    /**
     * Get string appropriate to the original
     * @return
     */
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

    /**
     * Get pair of width and length
     * @return
     */
    public List<Integer> getShape() {
        return Arrays.asList(height, width);
    }

    /**
     * Get ascii symbol according to linearized coordinate
     * @param ordinal
     * @return
     */
    public Number getElement(Integer ordinal) {
        Character pixel = getPixel(ordinal / height, ordinal % height);
        return defaultPalette.indexOf(pixel);
    }
}
