package com.javaml.ml.classifier;

import com.javaml.exception.TensorSizeException;
import com.javaml.exception.UnmatchedTensorAndLabelNumbersException;
import com.javaml.exception.UnmatchedTensorSizesException;
import com.javaml.ml.Tensor;

import java.util.List;

public interface NaryClassifier {
    void fit(List<Tensor<Number>> train, List<Integer> labels) throws TensorSizeException,
            UnmatchedTensorAndLabelNumbersException, UnmatchedTensorSizesException;

    Integer predict(Tensor<Number> test);

    List<Integer> predict(List<Tensor<Number>> test) throws TensorSizeException, UnmatchedTensorSizesException;
}
