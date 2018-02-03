package com.javaml.ml.classifier;

import com.javaml.exception.TensorSizeException;
import com.javaml.exception.UnmatchedTensorAndLabelNumbersException;
import com.javaml.exception.UnmatchedTensorSizesException;
import com.javaml.ml.Tensor;

import java.util.ArrayList;
import java.util.List;

public class LogisticBinaryClassifier implements BinaryClassifier {

    private ArrayList<Double> weights;
    private Integer iterationNumber;
    private Double learningRate;

    private static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    private double predict_(Tensor<Number> x) {
        double logit = .0;
        for (int i=0; i<weights.size();i++)  {
            logit += weights.get(i) * x.getElement(i).doubleValue();
        }
        return sigmoid(logit);
    }

    private void checkInput(List<Tensor<Number>> train, List<Boolean> labels)
            throws TensorSizeException, UnmatchedTensorAndLabelNumbersException, UnmatchedTensorSizesException {
        if (train.size() != labels.size()) {
            throw new UnmatchedTensorAndLabelNumbersException();
        }
        Integer tensorSize = getTensorSize(train.get(0));
        for (Tensor<Number> tensor : train) {
            if (!tensorSize.equals(getTensorSize(tensor))) {
                throw new UnmatchedTensorSizesException();
            }
        }
    }

    private Integer getTensorSize(Tensor<Number> tensor) throws TensorSizeException {
        return tensor.getShape().stream().reduce(Math::multiplyExact).orElseThrow(TensorSizeException::new);
    }

    public LogisticBinaryClassifier(Integer iterationNumber, Double learningRate) {
        this.iterationNumber = iterationNumber;
        this.learningRate = learningRate;
    }

    @Override
    public void fit(List<Tensor<Number>> train, List<Boolean> labels)
            throws TensorSizeException, UnmatchedTensorAndLabelNumbersException, UnmatchedTensorSizesException {
        checkInput(train, labels);
        Integer tensorSize = getTensorSize(train.get(0));
        this.weights = new ArrayList<>(tensorSize);
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
    public List<Boolean> predict(List<Tensor<Number>> test) {
        List<Boolean> res = new ArrayList<>(test.size());
        for (Tensor<Number> tensor : test) {
            res.add(predict_(tensor) > 0.5);
        }
        return res;
    }
}
