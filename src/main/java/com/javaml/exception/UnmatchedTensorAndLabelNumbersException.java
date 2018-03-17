package com.javaml.exception;

public class UnmatchedTensorAndLabelNumbersException extends RuntimeException {
    public UnmatchedTensorAndLabelNumbersException() {
        this("Number of tensors and number of labels are not equal.");
    }

    public UnmatchedTensorAndLabelNumbersException(String message)
    {
        super(message);
    }
}
