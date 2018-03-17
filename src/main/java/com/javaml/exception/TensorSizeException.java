package com.javaml.exception;

public class TensorSizeException extends RuntimeException {
    public TensorSizeException() {
        this("Failed to calculate number of elements in tensor. It may be caused by overflow while multiplying sizes of tensor.");
    }

    public TensorSizeException(String message)
    {
        super(message);
    }
}
