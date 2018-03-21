package com.javaml.concurrent;

/**
 * Wrapper for run lambdas with exceptions
 * @param <T> - type of passed parameter
 * @param <R> - type of returned parameter
 */
@FunctionalInterface
public interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;
}