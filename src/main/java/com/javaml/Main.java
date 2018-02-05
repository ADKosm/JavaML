package com.javaml;

import com.javaml.concurrent.CheckedFunction;
import com.javaml.concurrent.ThreadPool;
import com.javaml.converting.converter.ImageConverter;
import com.javaml.converting.converter.SimpleImageConverter;
import com.javaml.image.AsciiImage;
import com.javaml.ml.Tensor;
import com.javaml.ml.classifier.LogisticNaryClassifier;
import com.javaml.ml.classifier.NaryClassifier;
import com.javaml.segmentation.segmenter.LinearSegmenter;
import com.javaml.segmentation.segmenter.Segmenter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class Main {
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                printHelp();
            } else {
                switch (args[0]) {
                    case "help":
                        printHelp();
                        break;
                    case "convert":
                        String convertPath = args[1];
                        convertImage(convertPath);
                        break;
                    case "scale":
                        String scalePath = args[1];
                        Integer width = Integer.valueOf(args[2]);
                        Integer height = Integer.valueOf(args[3]);
                        scaleImage(scalePath, width, height);
                        break;
                    case "segment":
                        String segmentPath = args[1];
                        segmentImage(segmentPath);
                        break;
                    case "predict":
                        String pathToTrain = args[1];
                        String pathToImage = args[2];
                        predictNumber(pathToTrain, pathToImage);
                        break;
                    case "validate":
                        String validatePath = args[1];
                        validate(validatePath);
                        break;
                    default:
                        printHelp();
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.toString());
            System.exit(1);
        }
    }

    private static void printHelp() {
        StringBuilder builder = new StringBuilder();
        builder.append("Usage of this program:\n");
        builder.append("    javaml help | Print this message\n");
        builder.append("    javaml convert path/to/image | convert image to ascii\n");
        builder.append("    javaml scale path/to/image width height | convert and scale ascii image\n");
        builder.append("    javaml segment path/to/image | Segmentate numbers on image\n");
        builder.append("    javaml predict path/to/train path/to/image | Predict numbers on image\n");
        builder.append("    javaml validate /path/to/train | Validate model\n");
        builder.append("\n");
        builder.append("NB: path/to/train consists of subdirectories 0, 1, ..., N with corresponded images");
        System.out.println(builder);
    }

    private static void convertImage(String path) throws Exception {
        BufferedImage image = ImageIO.read(new File(path));
        ImageConverter converter = new SimpleImageConverter(image.getHeight(), image.getWidth());

        AsciiImage asciiImage = converter.convert(image);

        System.out.println(asciiImage);
    }

    private static void scaleImage(String path, Integer width, Integer height) throws Exception {
        BufferedImage image = ImageIO.read(new File(path));
        ImageConverter converter = new SimpleImageConverter(image.getHeight(), image.getWidth());

        AsciiImage asciiImage = converter.convert(image);
        AsciiImage scaledImage = asciiImage.getScaled(width, height);

        System.out.println(scaledImage);
    }

    private static void segmentImage(String path) throws Exception {
        BufferedImage image = ImageIO.read(new File(path));
        ImageConverter converter = new SimpleImageConverter(image.getHeight(), image.getWidth());

        AsciiImage asciiImage = converter.convert(image);
        Segmenter segmenter = new LinearSegmenter(LinearSegmenter.BackgroundFetchStrategy.FIRST);
        List<AsciiImage> images = segmenter.segment(asciiImage);

        for(int i = 0; i < images.size(); i++) {
            System.out.println("Image №" + (i+1) + ": ");
            System.out.println(images.get(i));
        }
    }

    private static void predictNumber(String pathToTrain, String pathToImage) throws Exception{
        List<Tensor<Number>> train = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            Path fullPathToTrain = Paths.get(pathToTrain, String.valueOf(i));
            File folder = new File(fullPathToTrain.toString());

            for(final File file : folder.listFiles()) {
                BufferedImage image = ImageIO.read(file);
                ImageConverter converter = new SimpleImageConverter(28, 28);

                AsciiImage asciiImage = converter.convert(image);
                train.add(asciiImage);
                labels.add(i);
            }
        }

        NaryClassifier c = new LogisticNaryClassifier(100, 0.7, 10);
        System.out.println(String.format("Training"));

        c.fit(train, labels);

        System.out.println("Complete!");

        BufferedImage image = ImageIO.read(new File(pathToImage));
        ImageConverter converter = new SimpleImageConverter(image.getHeight(), image.getWidth());
        AsciiImage asciiImage = converter.convert(image);

        Segmenter segmenter = new LinearSegmenter(LinearSegmenter.BackgroundFetchStrategy.FIRST);
        List<AsciiImage> images = segmenter.segment(asciiImage);

        CheckedFunction<AsciiImage, Integer> predictOneImage = (AsciiImage img) ->
                c.predict(img.getScaled(28, 28));

        ThreadPool threadPool = new ThreadPool();
        Collection<Integer> answers = threadPool.parallelMap(predictOneImage, images);

        System.out.println("Numbers: " + answers);
    }

    private static void validate(String pathToTrain) throws Exception {
        List<Tensor<Number>> train = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();
        List<Tensor<Number>> test = new ArrayList<>();
        List<Integer> testLabels = new ArrayList<>();
        int k = 0;
        for (int i = 0; i < 10; i++) {

            Path fullPathToTrain = Paths.get(pathToTrain, String.valueOf(i));
            File folder = new File(fullPathToTrain.toString());

            for (final File file : folder.listFiles()) {
                BufferedImage image = ImageIO.read(file);
                ImageConverter converter = new SimpleImageConverter(28, 28);

                AsciiImage asciiImage = converter.convert(image);
                k++;
                if (k % 10 != 0) {
                    train.add(asciiImage);
                    labels.add(i);
                } else {
                    test.add(asciiImage);
                    testLabels.add(i);
                }
            }
        }
        long start_time = System.nanoTime();

        System.out.println(String.format("Training"));
        NaryClassifier c = new LogisticNaryClassifier(100, 0.7, 10);

        c.fit(train, labels);

        long end_time = System.nanoTime();
        double difference = (end_time - start_time) / 1e6;
        System.out.println(String.format("Training took %s units of time", difference));

        int correct = 0;
        for (int i = 0; i < test.size(); i++) {
            Integer res = c.predict(test.get(i));
            System.out.println(String.format("predicted category: %s, correct category: %s", res, testLabels.get(i)));
            if (res.equals(testLabels.get(i))) {
                correct++;
            }
        }
        System.out.println(String.format("correct: %s, total: %s", correct, testLabels.size()));
    }
}
