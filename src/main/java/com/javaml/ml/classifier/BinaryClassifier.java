package com.javaml.ml.classifier;

import com.javaml.ml.Tensor;

import java.util.List;

public interface BinaryClassifier {
    void fit(List<? extends Tensor<Number>> train, List<Boolean> labels);

    Boolean predict(Tensor<Number> test);

    List<Boolean> predict(List<? extends Tensor<Number>> test);

    Double predict_proba(Tensor<Number> test);

    List<Double> predict_proba(List<? extends Tensor<Number>> test);
}
