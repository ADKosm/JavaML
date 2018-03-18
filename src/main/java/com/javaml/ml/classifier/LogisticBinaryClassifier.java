package com.javaml.ml.classifier;

import com.google.gson.Gson;
import com.javaml.exception.TensorSizeException;
import com.javaml.exception.UnmatchedTensorAndLabelNumbersException;
import com.javaml.exception.UnmatchedTensorSizesException;
import com.javaml.ml.Tensor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LogisticBinaryClassifier implements BinaryClassifier {

    private ArrayList<Double> weights;
    private Integer iterationNumber;
    private Double learningRate;
    private Double classificationThreshold;
    private Integer tensorSize;

    private static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    private double predict_(Tensor<Number> tensor) {
        double logit = .0;
        for (int i = 0; i < weights.size(); i++)  {
            logit += weights.get(i) * tensor.getElement(i).doubleValue();
        }
        return sigmoid(logit);
    }

    private void checkInput(List<? extends Tensor<Number>> tensors, List<Boolean> labels) {
        if (tensors.size() != labels.size()) {
            throw new UnmatchedTensorAndLabelNumbersException();
        }
        checkInput(tensors);
    }

    private void checkInput(List<? extends Tensor<Number>> tensors) {
        for (Tensor<Number> tensor : tensors) {
            checkInput(tensor);
        }
    }

    private void checkInput(Tensor<Number> tensor) {
        if (!this.tensorSize.equals(getTensorSize(tensor))) {
            throw new UnmatchedTensorSizesException();
        }
    }

    private Integer getTensorSize(Tensor<Number> tensor) {
        return tensor.getShape().stream().reduce(Math::multiplyExact).orElseThrow(TensorSizeException::new);
    }

    public LogisticBinaryClassifier(Integer iterationNumber) {
        this(iterationNumber, 0.7, 0.5);
    }

    public LogisticBinaryClassifier(Integer iterationNumber, Double learningRate) {
        this(iterationNumber, learningRate, 0.5);
    }

    public LogisticBinaryClassifier(Integer iterationNumber, Double learningRate, Double classificationThreshold) {
        this.iterationNumber = iterationNumber;
        this.learningRate = learningRate;
        this.classificationThreshold = classificationThreshold;
    }

    public LogisticBinaryClassifier(String path) {
        this.load(path);
    }

    @Override
    public void fit(List<? extends Tensor<Number>> train, List<Boolean> labels) {
        this.tensorSize = getTensorSize(train.get(0));
        checkInput(train, labels);

        this.weights = new ArrayList<>(Collections.nCopies(this.tensorSize, 0.0));
        // TODO: implement break criterion
        for (int n = 0; n < this.iterationNumber; n++) {

            for (int i = 0; i < train.size(); i++) {
                Tensor<Number> x = train.get(i);
                double label = labels.get(i) ? 1.0 : 0.0;
                double predicted = predict_(x);
                for (int j = 0; j < this.weights.size(); j++) {
                    Double updatedValue = this.weights.get(j)
                            + this.learningRate * (label - predicted) * x.getElement(j).doubleValue();
                    this.weights.set(j, updatedValue);
                }
            }
        }
    }

    @Override
    public Boolean predict(Tensor<Number> test) {
            checkInput(test);
            return predict_(test) > this.classificationThreshold;
    }

    @Override
    public List<Boolean> predict(List<? extends Tensor<Number>> test) {
        checkInput(test);
        List<Boolean> res = new ArrayList<>(test.size());
        for (Tensor<Number> tensor : test) {
            res.add(predict(tensor));
        }
        return res;
    }

    @Override
    public Double predict_proba(Tensor<Number> test) {
        checkInput(test);
        return predict_(test);
    }

    @Override
    public List<Double> predict_proba(List<? extends Tensor<Number>> test) {
        checkInput(test);
        List<Double> res = new ArrayList<>(test.size());
        for (Tensor<Number> tensor : test) {
            res.add(predict_proba(tensor));
        }
        return res;
    }

    @Override
    public void dump(String path) {
        try {
            Gson gson = new Gson();
            String serialized = gson.toJson(this.weights.toArray());

            PrintWriter writer = new PrintWriter(path);
            writer.print(serialized);
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Can't load to file");
        }
    }

    @Override
    public void load(String path) {
        try {
            Gson gson = new Gson();
            String serialised = new String(Files.readAllBytes(Paths.get(path)));

            Double[] weights = gson.fromJson(serialised, Double[].class);
            this.weights = new ArrayList<>(Arrays.asList(weights));
            this.tensorSize = this.weights.size();
        } catch (IOException e) {
            throw new RuntimeException("Can't load from file");
        }
    }
}
