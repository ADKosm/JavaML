package com.javaml.concurrent;

import java.util.ArrayList;
import java.util.Collection;

public class ThreadedMap<T, R> implements Runnable {

    private CheckedFunction<T, R> function;
    private Collection<T> data;

    public Integer getExitStatus() {
        return exitStatus;
    }

    private Integer exitStatus;

    public Collection<R> getResult() {
        return result;
    }

    private Collection<R> result;

    public ThreadedMap(CheckedFunction<T, R> function, Collection<T> input) {
        this.function = function;
        this.data = input;
        this.exitStatus = 0;
    }

    @Override
    public void run() {
        result = new ArrayList<>(data.size());
        try {
            for (T entity : data) {
                result.add(function.apply(entity));
            }
        } catch (Exception e) {
            this.exitStatus = 1;
        }
    }
}
