package com.javaml.converting.scaler;

import com.javaml.image.AsciiImage;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class NearestNeighbourScaler implements Scaler {
    @Override
    public AsciiImage scale(AsciiImage image, Integer newWidth, Integer newHeight) {
        AsciiImage result = image;

        result = (result.getWidth() < newWidth) ?
                upscale(result, newWidth, AsciiImage.Axis.X) : downscale(result, newWidth, AsciiImage.Axis.X);

        result = (result.getHeight() < newHeight) ?
                upscale(result, newHeight, AsciiImage.Axis.Y) : downscale(result, newHeight, AsciiImage.Axis.Y);

        return result;
    }

    private AsciiImage upscale(AsciiImage image, Integer newSize, AsciiImage.Axis axis) {
        Function<Double, Double> log1_5 = (Double x) -> Math.log(x) / Math.log(1.5);

        Double originalSize = ((axis == AsciiImage.Axis.X) ? image.getWidth() : image.getHeight()).doubleValue();
        Double finalSize = newSize.doubleValue();

        Integer doubling = log1_5.apply(finalSize / originalSize).intValue();

        AsciiImage result = image;
        for(int i = 0; i < doubling; i++) {
            Integer currentSize = (axis == AsciiImage.Axis.X) ? result.getWidth() : result.getHeight();
            result = upscale_(result, currentSize + currentSize/2, axis);
        }
        result = upscale_(result, newSize, axis);

        return result;
    }

    private AsciiImage downscale(AsciiImage image, Integer newSize, AsciiImage.Axis axis) {
        Function<Double, Double> log2 = (Double x) -> Math.log(x) / Math.log(2.0);

        Double originalSize = ((axis == AsciiImage.Axis.X) ? image.getWidth() : image.getHeight()).doubleValue();
        Double finalSize = newSize.doubleValue();

        Integer halfing = log2.apply(originalSize / finalSize).intValue();

        AsciiImage result = image;
        for(int i = 0; i < halfing; i++) {
            Integer currentSize = (axis == AsciiImage.Axis.X) ? result.getWidth() : result.getHeight();
            result = downscale_(result, (currentSize+1) / 2, axis);
        }
        result = downscale_(result, newSize, axis);

        return result;
    }

    private AsciiImage upscale_(AsciiImage image, Integer newSize, AsciiImage.Axis axis) {
        Integer originalSize = (axis == AsciiImage.Axis.X) ? image.getWidth() : image.getHeight();

        if(originalSize.equals(newSize)) return image;

        AsciiImage result = (axis == AsciiImage.Axis.X) ?
                new AsciiImage(image.getHeight(), newSize) : new AsciiImage(newSize, image.getWidth());

        Integer resultCursor = 0;
        Integer originalCursor = 0;
        Integer end = (axis == AsciiImage.Axis.X) ? image.getWidth() : image.getHeight();

        Integer delta = newSize - originalSize;
        Integer step = originalSize / delta;

        for(; originalCursor < end; originalCursor++) {
            copyLine(image, result, originalCursor, resultCursor, axis);
            if(originalCursor % step == 0) {
                resultCursor++;
                if(resultCursor.equals(newSize)) break;
                copyLine(image, result, originalCursor, resultCursor, axis);
            }
            resultCursor++;
            if(resultCursor.equals(newSize)) break;
        }
        return result;
    }

    private AsciiImage downscale_(AsciiImage image, Integer newSize, AsciiImage.Axis axis) {
        Integer originalSize = (axis == AsciiImage.Axis.X) ? image.getWidth() : image.getHeight();

        if(originalSize.equals(newSize)) return image;

        AsciiImage result = (axis == AsciiImage.Axis.X) ?
                new AsciiImage(image.getHeight(), newSize) : new AsciiImage(newSize, image.getWidth());

        Integer resultCursor = 0;
        Integer originalCursor = 0;
        Integer end = (axis == AsciiImage.Axis.X) ? image.getWidth() : image.getHeight();

        Integer delta = originalSize - newSize;
        Integer step = originalSize / delta;

        for(; originalCursor < end; originalCursor++) {
            if(originalCursor % step == 0) {
                continue;
            }
            copyLine(image, result, originalCursor, resultCursor, axis);
            resultCursor++;
            if(resultCursor.equals(newSize)) break;
        }
        return result;
    }

    private void copyLine(AsciiImage from, AsciiImage to, Integer fromIndex, Integer toIndex, AsciiImage.Axis axis) {
        Function<Integer, Character> getPixel = (Integer pos) ->
                axis == AsciiImage.Axis.X ? from.getPixel(fromIndex, pos) : from.getPixel(pos, fromIndex);
        BiConsumer<Integer, Character> setPixel = (Integer pos, Character pixel) -> {
            if (axis == AsciiImage.Axis.X) to.setPixel(pixel, toIndex, pos);
            else to.setPixel(pixel, pos, toIndex);
        };

        Integer end = (axis == AsciiImage.Axis.X) ? to.getHeight() : to.getWidth();

        for(int position = 0; position < end; position++) {
            Character pixel = getPixel.apply(position);
            setPixel.accept(position, pixel);
        }
    }
}
