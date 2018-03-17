package com.javaml.exception;

public class UnmatchedTensorSizesException extends RuntimeException {
    public UnmatchedTensorSizesException() {
        this("Tensor sizes in list do not match.");
    }

    public UnmatchedTensorSizesException(String message)
    {
        super(message);
    }
}
