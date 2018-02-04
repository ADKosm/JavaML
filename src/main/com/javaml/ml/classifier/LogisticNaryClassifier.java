package com.javaml.ml.classifier;

import com.javaml.concurrent.CheckedFunction;
import com.javaml.concurrent.ThreadPool;
import com.javaml.exception.TensorSizeException;
import com.javaml.exception.UnmatchedTensorAndLabelNumbersException;
import com.javaml.exception.UnmatchedTensorSizesException;
import com.javaml.ml.Tensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogisticNaryClassifier implements NaryClassifier {

    private List<BinaryClassifier> binaryClassifiers;

    public LogisticNaryClassifier(Integer iterationNumber, Double learningRate, Integer numberOfLabels) {
        binaryClassifiers = new ArrayList<>(numberOfLabels);
        for (int i = 0; i < numberOfLabels; i++) {
            binaryClassifiers.add(new LogisticBinaryClassifier(iterationNumber, learningRate));
        }
    }

    @Override
    public void fit(List<Tensor<Number>> train, List<Integer> labels) throws TensorSizeException,
            UnmatchedTensorAndLabelNumbersException, UnmatchedTensorSizesException, InterruptedException {
        CheckedFunction<Integer, BinaryClassifier> learnClassicier = (Integer i) -> {
            List<Boolean> booleanLabels = new ArrayList<>(labels.size());
            for (Integer label : labels) {
                booleanLabels.add(label.equals(i));
            }
            BinaryClassifier binaryClassifier = binaryClassifiers.get(i);
            binaryClassifier.fit(train, booleanLabels);
            return binaryClassifier;
        };
        ThreadPool threadPool = new ThreadPool(4);
        threadPool.parallelMap(learnClassicier, Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        for (int i = 0; i < this.binaryClassifiers.size(); i++) {
            List<Boolean> booleanLabels = new ArrayList<>(labels.size());
            for (int j = 0; j < labels.size(); j++) {
                booleanLabels.add(labels.get(j) == i);
            }
            BinaryClassifier binaryClassifier = binaryClassifiers.get(i);
            binaryClassifier.fit(train, booleanLabels);
        }

    }

    @Override
    public Integer predict(Tensor<Number> test) throws TensorSizeException, UnmatchedTensorSizesException {
        Double maxProba = 0.0;
        Integer predictedLabel = 0;
        for (int i = 0; i < this.binaryClassifiers.size(); i++) {
            BinaryClassifier binaryClassifier = binaryClassifiers.get(i);
            Double proba = binaryClassifier.predict_proba(test);
            if (proba > maxProba) {
                maxProba = proba;
                predictedLabel = i;
            }
        }
        return predictedLabel;
    }

    @Override
    public List<Integer> predict(List<Tensor<Number>> test) throws TensorSizeException, UnmatchedTensorSizesException {
        List<Integer> res = new ArrayList<>(test.size());
        for (Tensor<Number> tensor : test) {
            res.add(predict(tensor));
        }
        return res;
    }
}
