package com.javaml.ml.classifier;

import com.javaml.ml.Tensor;

import java.io.FileNotFoundException;
import java.util.List;

public interface NaryClassifier {
    void fit(List<? extends Tensor<Number>> train, List<Integer> labels);

    Integer predict(Tensor<Number> test);

    List<Integer> predict(List<? extends Tensor<Number>> test);

    void dump(String path);

    void load(String path) throws FileNotFoundException;
}
