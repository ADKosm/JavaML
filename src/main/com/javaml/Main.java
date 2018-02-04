package com.javaml;

import com.javaml.converting.converter.ImageConverter;
import com.javaml.converting.converter.SimpleImageConverter;
import com.javaml.image.AsciiImage;
import com.javaml.segmentation.backgroundFetcher.BackgroundFetcher;
import com.javaml.segmentation.backgroundFetcher.FirstBackgroundFetcher;
import com.javaml.segmentation.segmenter.LinearSegmenter;
import com.javaml.segmentation.segmenter.Segmenter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

class Main {
    public static void main(String[] args) {
        String input = args[0];

        try {
            BufferedImage image = ImageIO.read(new File(input));
            ImageConverter converter = new SimpleImageConverter(28, 28);

            AsciiImage asciiImage = converter.Convert(image);

            System.out.println(asciiImage);

            AsciiImage scaledImage = asciiImage.getScaled(128, 128);
            System.out.println("New size: " + scaledImage.getWidth() + " x " + scaledImage.getHeight());
            System.out.println(scaledImage);

            /*
            BackgroundFetcher fetcher = new FirstBackgroundFetcher();
            System.out.println("Back first: `" + fetcher.fetchBackground(asciiImage) + "`");
            System.out.println("Back mode: `" + fetcher.fetchBackground(asciiImage) + "`");

            Segmenter segmenter = new LinearSegmenter(LinearSegmenter.BackgroundFetchStrategy.FIRST);
            List<AsciiImage> images = segmenter.segment(asciiImage);

            System.out.println(asciiImage);

            System.out.println("Number of pictures: " + images.size());
            for(AsciiImage croppedImage : images) {
                System.out.println("Size: " + croppedImage.getHeight() + " - " + croppedImage.getWidth());
                System.out.println(String.valueOf(Collections.nCopies(30, '-')));
                System.out.println(croppedImage);
            }
            */
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.toString());
            System.exit(1);
        }
    }
}
