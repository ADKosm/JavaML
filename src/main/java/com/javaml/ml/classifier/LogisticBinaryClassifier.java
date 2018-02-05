package com.javaml.ml.classifier;

import com.javaml.exception.TensorSizeException;
import com.javaml.exception.UnmatchedTensorAndLabelNumbersException;
import com.javaml.exception.UnmatchedTensorSizesException;
import com.javaml.ml.Tensor;

import java.util.ArrayList;
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

    private void checkInput(List<Tensor<Number>> tensors, List<Boolean> labels)
            throws TensorSizeException, UnmatchedTensorAndLabelNumbersException, UnmatchedTensorSizesException {
        if (tensors.size() != labels.size()) {
            throw new UnmatchedTensorAndLabelNumbersException();
        }
        checkInput(tensors);
    }

    private void checkInput(List<Tensor<Number>> tensors)
            throws TensorSizeException, UnmatchedTensorSizesException {
        for (Tensor<Number> tensor : tensors) {
            checkInput(tensor);
        }
    }

    private void checkInput(Tensor<Number> tensor)
            throws TensorSizeException, UnmatchedTensorSizesException {
        if (!this.tensorSize.equals(getTensorSize(tensor))) {
            throw new UnmatchedTensorSizesException();
        }
    }

    private Integer getTensorSize(Tensor<Number> tensor) throws TensorSizeException {
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

    @Override
    public void fit(List<Tensor<Number>> train, List<Boolean> labels)
            throws TensorSizeException, UnmatchedTensorAndLabelNumbersException, UnmatchedTensorSizesException {
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
    public Boolean predict(Tensor<Number> test) throws TensorSizeException, UnmatchedTensorSizesException {
            checkInput(test);
            return predict_(test) > this.classificationThreshold;
    }

    @Override
    public List<Boolean> predict(List<Tensor<Number>> test) throws TensorSizeException, UnmatchedTensorSizesException {
        checkInput(test);
        List<Boolean> res = new ArrayList<>(test.size());
        for (Tensor<Number> tensor : test) {
            res.add(predict(tensor));
        }
        return res;
    }

    @Override
    public Double predict_proba(Tensor<Number> test) throws TensorSizeException, UnmatchedTensorSizesException  {
        checkInput(test);
        return predict_(test);
    }

    @Override
    public List<Double> predict_proba(List<Tensor<Number>> test)
            throws TensorSizeException, UnmatchedTensorSizesException {
        checkInput(test);
        List<Double> res = new ArrayList<>(test.size());
        for (Tensor<Number> tensor : test) {
            res.add(predict_proba(tensor));
        }
        return res;
    }
}
