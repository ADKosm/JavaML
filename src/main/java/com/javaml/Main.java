package com.javaml;

import com.javaml.converting.converter.ImageConverter;
import com.javaml.converting.converter.SimpleImageConverter;
import com.javaml.converting.scaler.NearestNeighborScaler;
import com.javaml.converting.scaler.Scaler;
import com.javaml.image.AsciiImage;
import com.javaml.ml.Tensor;
import com.javaml.ml.classifier.LogisticNaryClassifier;
import com.javaml.ml.classifier.NaryClassifier;
import com.javaml.segmentation.segmenter.ExpressionSegmenter;
import com.javaml.segmentation.segmenter.Segmenter;

import static spark.Spark.*;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

class Main {
    public static void main(String[] args) {
        staticFiles.location("/");

        get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });

        post("/api/compute", (req, res) -> {
            System.out.println(req.queryParams("exp"));
            return "3";
        });

        post("/api/parse", (req, res) -> {
            req.attribute("org.eclipse.jetty.multipartConfig",
                    new MultipartConfigElement("/temp"));
            try (InputStream is = req.raw().getPart("uploaded_file").getInputStream()) {
                Path tempFile = Files.createTempFile("pic", ".tmp");
                System.out.println("Temp file : " + tempFile.toString());
                Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return "1 + 1";
        });
    }

    private static NaryClassifier classifier = train("/PATH/TO/TRAIN");
    private static Map<Integer, String> labelMapping = new HashMap<>();
    static {
        for (int i = 0; i < 10; i++) {
            labelMapping.put(i, String.valueOf(i));
        }
        labelMapping.put(10, "(");
        labelMapping.put(11, ")");
        labelMapping.put(12, "+");
        labelMapping.put(13, "-");
        labelMapping.put(14, "*");
        labelMapping.put(15, "/");
    }

    private static String parseExp(String pathToFile) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(pathToFile));
        } catch (IOException e) {
            throw new RuntimeException("Can't read temp file with image");
        }
        ImageConverter converter = new SimpleImageConverter(image.getHeight(), image.getWidth());

        AsciiImage asciiImage = converter.convert(image);
        List<AsciiImage> segments = segmentImage(asciiImage);

        List<String> labels = predictLabels(classifier, segments);
        return String.join("", labels);
    }

    private static List<AsciiImage> segmentImage(AsciiImage image) {
        Segmenter segmenter = new ExpressionSegmenter();
        return segmenter.segment(image);
    }

    private static List<String> predictLabels(NaryClassifier classifier, List<AsciiImage> images) {
        Scaler scaler = new NearestNeighborScaler();
        return images.stream()
                .map((image) -> scaler.scale(image, 45, 45))
                .map(classifier::predict)
                .map((x) -> labelMapping.get(x))
                .collect(Collectors.toList());
    }

    private static NaryClassifier train(String pathToTrain) {
        List<Tensor<Number>> train = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();

        for (int labelId : labelMapping.keySet()) {
            Path pathToClass = Paths.get(pathToTrain, String.valueOf(labelId));
            File folder = new File(pathToClass.toString());

            for (final File file : folder.listFiles()) {
                BufferedImage image = null;
                try {
                    image = ImageIO.read(file);
                } catch (Exception e) {
                    throw new RuntimeException("Can't read train image");
                }

                ImageConverter converter = new SimpleImageConverter(28, 28);

                AsciiImage asciiImage = converter.convert(image);
                train.add(asciiImage);
                labels.add(labelId);
            }
        }

        long start_time = System.nanoTime();
        System.out.println("Training");
        NaryClassifier c = new LogisticNaryClassifier(100, 0.7, 10);

        c.fit(train, labels);

        long end_time = System.nanoTime();
        double difference = (end_time - start_time) / 1e6;
        System.out.println(String.format("Training took %s units of time", difference));

        return c;
    }
}
