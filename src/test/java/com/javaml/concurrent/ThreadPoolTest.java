package com.javaml.concurrent;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class ThreadPoolTest {
    private ThreadPool threadPool = new ThreadPool(4);

    @Test
    public void testParallelMap() {
        List<Integer> data = new ArrayList<>();
        data.add(5);
        data.add(2);
        data.add(6);
        data.add(1);
        List<Integer> expected = new ArrayList<>();
        expected.add(25);
        expected.add(4);
        expected.add(36);
        expected.add(1);
        List<Integer> result = null;
        try {
            result = new ArrayList<>(threadPool.parallelMap((Integer x) -> x * x, data));
        } catch (InterruptedException e) {
            // We can't do anything in case somebody interrupted child process
            // But we hope that this will not happen
        }
        assertArrayEquals(expected.toArray(), result.toArray());
    }
}
