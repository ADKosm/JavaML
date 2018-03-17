package com.javaml.ml.classifier;

import com.javaml.concurrent.CheckedFunction;
import com.javaml.concurrent.ThreadPool;
import com.javaml.ml.Tensor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LogisticNaryClassifier implements NaryClassifier {

    private Collection<BinaryClassifier> binaryClassifiers;
    private Integer iterationNumber;
    private Double learningRate;
    private Integer numberOfLabels;

    public LogisticNaryClassifier(Integer iterationNumber, Double learningRate, Integer numberOfLabels) {
        this.iterationNumber = iterationNumber;
        this.learningRate = learningRate;
        this.numberOfLabels = numberOfLabels;
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
            binaryClassifiers = threadPool.parallelMap(learnClassicier,
                    IntStream.range(0, numberOfLabels).boxed().collect(Collectors.toList()));
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
}
