package com.javaml;

import com.javaml.converting.converter.ImageConverter;
import com.javaml.converting.converter.SimpleImageConverter;
import com.javaml.image.AsciiImage;
import com.javaml.ml.Tensor;
import com.javaml.ml.classifier.LogisticNaryClassifier;
import com.javaml.ml.classifier.NaryClassifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class ManualClassificationTest {
    public static void main(String[] args) throws Exception {
        List<Tensor<Number>> train = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();
        List<Tensor<Number>> test = new ArrayList<>();
        List<Integer> testLabels = new ArrayList<>();
        int k = 0;
        for (int i = 0; i < 10; i++) {
            File folder = new File("/home/givorenon/Documents/trainingSample/" + i);

            for (final File file : folder.listFiles()) {
                BufferedImage image = ImageIO.read(file);
                ImageConverter converter = new SimpleImageConverter(28, 28);

                AsciiImage asciiImage = converter.Convert(image);
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