package com.javaml.ml.classifier;

import com.javaml.concurrent.CheckedFunction;
import com.javaml.concurrent.ThreadPool;
import com.javaml.ml.Tensor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LogisticNaryClassifier implements NaryClassifier {

    private List<BinaryClassifier> binaryClassifiers;
    private Integer iterationNumber;
    private Double learningRate;
    private Integer numberOfLabels;

    public LogisticNaryClassifier(Integer iterationNumber, Double learningRate, Integer numberOfLabels) {
        this.iterationNumber = iterationNumber;
        this.learningRate = learningRate;
        this.numberOfLabels = numberOfLabels;
    }

    public LogisticNaryClassifier(String path) throws FileNotFoundException {
        this.load(path);
    }

    @Override
    public void fit(List<? extends Tensor<Number>> train, List<Integer> labels) {
        CheckedFunction<Integer, BinaryClassifier> learnClassicier = (Integer i) -> {
            List<Boolean> booleanLabels = new ArrayList<>(labels.size());
            for (Integer label : labels) {
                booleanLabels.add(label.equals(i));
            }
            BinaryClassifier binaryClassifier = new LogisticBinaryClassifier(iterationNumber, learningRate);
            binaryClassifier.fit(train, booleanLabels);
            return binaryClassifier;
        };
        ThreadPool threadPool = new ThreadPool();
        try {
            binaryClassifiers = new ArrayList<>(threadPool.parallelMap(learnClassicier,
                    IntStream.range(0, numberOfLabels).boxed().collect(Collectors.toList())));
        } catch (InterruptedException e) {
            throw new RuntimeException("Training in separate thread failed");
        }
    }

    @Override
    public Integer predict(Tensor<Number> test) {
        Double maxProba = 0.0;
        Integer predictedLabel = 0;
        int index = 0;
        for (BinaryClassifier binaryClassifier : binaryClassifiers) {
            Double proba = binaryClassifier.predict_proba(test);
            if (proba > maxProba) {
                maxProba = proba;
                predictedLabel = index;
            }
            index++;
        }
        return predictedLabel;
    }

    @Override
    public List<Integer> predict(List<? extends Tensor<Number>> test) {
        List<Integer> res = new ArrayList<>(test.size());
        for (Tensor<Number> tensor : test) {
            res.add(predict(tensor));
        }
        return res;
    }

    @Override
    public void dump(String path) {
        try {
            Files.createDirectories(Paths.get(path));
            for (Integer index = 0; index < this.binaryClassifiers.size(); index++) {
                String fullPath = Paths.get(path, index.toString()).toString();
                this.binaryClassifiers.get(index).dump(fullPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Boba");
        }
    }

    @Override
    public void load(String path) throws FileNotFoundException {
        this.binaryClassifiers = new ArrayList<>();
        File folder = new File(path);
        File[] files = folder.listFiles();
        if(files == null) throw new FileNotFoundException(path);

        Integer size = files.length;
        for(Integer index = 0; index < size; index++) {
            String fullPath = Paths.get(path, index.toString()).toString();
            this.binaryClassifiers.add(new LogisticBinaryClassifier(fullPath));
        }
    }
}
