package com.javaml.ml.classifier;

import com.javaml.ml.Tensor;

import java.util.List;

public interface BinaryClassifier {
    /**
     * Train model on real data
     * @param train
     * @param labels
     */
    void fit(List<? extends Tensor<Number>> train, List<Boolean> labels);

    /**
     * Determine class of the object
     * @param test
     * @return true or false depending on the predicted class
     */
    Boolean predict(Tensor<Number> test);

    /**
     * Determine classes of each object in the list
     * @param test
     * @return list of booleans depending of the predictes classes
     */
    List<Boolean> predict(List<? extends Tensor<Number>> test);

    /**
     * Determine probability of first class
     * @param test
     * @return probability - number between 0 and 1
     */
    Double predict_proba(Tensor<Number> test);

    /**
     * Determine probabilities of first class of each object in the list
     * @param test
     * @return list of probabilities
     */
    List<Double> predict_proba(List<? extends Tensor<Number>> test);

    /**
     * Serialize model
     * @param path - where store the model
     */
    void dump(String path);

    /**
     * Deserialize model
     * @param path - where to load model
     */
    void load(String path);
}
