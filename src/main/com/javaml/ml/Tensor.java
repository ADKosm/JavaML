package com.javaml.ml;

import java.util.List;

public interface Tensor<T> {
    List<Integer> getShape();

    T getElement(Integer ordinal);
}
