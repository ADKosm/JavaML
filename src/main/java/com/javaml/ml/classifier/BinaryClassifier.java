package com.javaml.ml.classifier;

import com.javaml.exception.TensorSizeException;
import com.javaml.exception.UnmatchedTensorAndLabelNumbersException;
import com.javaml.exception.UnmatchedTensorSizesException;
import com.javaml.ml.Tensor;

import java.util.List;

public interface BinaryClassifier {
    void fit(List<Tensor<Number>> train, List<Boolean> labels) throws TensorSizeException,
            UnmatchedTensorAndLabelNumbersException, UnmatchedTensorSizesException;

    Boolean predict(Tensor<Number> test) throws TensorSizeException, UnmatchedTensorSizesException;

    List<Boolean> predict(List<Tensor<Number>> test) throws TensorSizeException, UnmatchedTensorSizesException;

    Double predict_proba(Tensor<Number> test) throws TensorSizeException, UnmatchedTensorSizesException;

    List<Double> predict_proba(List<Tensor<Number>> test) throws TensorSizeException, UnmatchedTensorSizesException;
}