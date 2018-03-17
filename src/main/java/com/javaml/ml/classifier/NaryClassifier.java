package com.javaml.ml.classifier;

import com.javaml.exception.TensorSizeException;
import com.javaml.exception.UnmatchedTensorAndLabelNumbersException;
import com.javaml.exception.UnmatchedTensorSizesException;
import com.javaml.ml.Tensor;

import java.util.List;

public interface NaryClassifier {
    void fit(List<? extends Tensor<Number>> train, List<Integer> labels) throws TensorSizeException,
            UnmatchedTensorAndLabelNumbersException, UnmatchedTensorSizesException, InterruptedException;

    Integer predict(Tensor<Number> test) throws TensorSizeException, UnmatchedTensorSizesException;

    List<Integer> predict(List<? extends Tensor<Number>> test) throws TensorSizeException, UnmatchedTensorSizesException;
}
