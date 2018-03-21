package com.javaml.ml.classifier;

import com.javaml.ml.Tensor;

import java.io.FileNotFoundException;
import java.util.List;

public interface NaryClassifier {
    /**
     * Train model on real data
     * @param train
     * @param labels
     */
    void fit(List<? extends Tensor<Number>> train, List<Integer> labels);

    /**
     * Determine class of object
     * @param test
     * @return number of class
     */
    Integer predict(Tensor<Number> test);

    /**
     * Determine class of each object in the list
     * @param test
     * @return list of class numbers
     */
    List<Integer> predict(List<? extends Tensor<Number>> test);

    /**
     * Serialize model
     * @param path - where store the model
     */
    void dump(String path);

    /**
     * Deserialize model
     * @param path - where to load model
     */
    void load(String path) throws FileNotFoundException;
}
