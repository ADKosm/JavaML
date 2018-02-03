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
        String input = args[0];
        String output = null;
        if(args.length > 1) {
            output = args[1];
        }

        try {
            BufferedImage image = ImageIO.read(new File(input));
            ImageConverter converter = new SimpleImageConverter(64, 64);

            AsciiImage asciiImage = converter.Convert(image);

            PrintWriter writer = (output == null) ? new PrintWriter(System.out) : new PrintWriter(output, "UTF-8");
            writer.println(asciiImage);
            writer.close();
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.toString());
            System.exit(1);
        }
    }
}
