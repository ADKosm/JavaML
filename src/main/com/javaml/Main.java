package com.javaml;

import com.javaml.converter.ImageConverter;
import com.javaml.converter.SimpleImageConverter;
import com.javaml.image.AsciiImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

class Main {
    public static void main(String[] args) {
        String inputPath = args[0];
        String outputPath = null;
        if(args.length > 1) {
            outputPath = args[1];
        }

        try {
            BufferedImage image = ImageIO.read(new File(inputPath));
            ImageConverter converter = new SimpleImageConverter(64, 64);

            AsciiImage asciiImage = converter.Convert(image);

            if(outputPath == null) {
                System.out.println(asciiImage);
            } else {
                PrintWriter writer = new PrintWriter(outputPath, "UTF-8");
                writer.println(asciiImage);
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.toString());
            System.exit(1);
        }
    }
}