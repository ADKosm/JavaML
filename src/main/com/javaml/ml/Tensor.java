package com.javaml.ml;

import java.util.ArrayList;

public interface Tensor<T> {
    ArrayList<Integer> getShape();

    T getElement(Integer ordinal);
}
