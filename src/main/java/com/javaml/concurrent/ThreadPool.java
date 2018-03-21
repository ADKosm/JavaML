package com.javaml.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ThreadPool {

    private List<Thread> pool;

    private Integer threadNum;

    public ThreadPool() {
        this(Runtime.getRuntime().availableProcessors());
    }

    public ThreadPool(Integer threadNum) {
        pool = new ArrayList<>(threadNum);
        this.threadNum = threadNum;
    }

    /**
     * Concurrently apply function to each element of data
     * @param function - function to apply
     * @param data - collection of elements
     * @param <T>
     * @param <R>
     * @return collection with mapped elements
     * @throws InterruptedException
     */
     public <T, R> Collection<R> parallelMap(CheckedFunction<T, R> function, Collection<T> data) throws InterruptedException {
        List<ThreadedMap<T, R>> threadedMaps = new ArrayList<>(threadNum);
        Integer batchSize = data.size() / threadNum;
        // batchSize * threadNum has to be greater or equal then data.size()
        batchSize += data.size() % threadNum != 0 ? 1 : 0;
        for (int i = 0; i < threadNum; i++) {
            Collection<T> batchInput = data.stream().skip(batchSize * i).limit(batchSize).collect(Collectors.toList());
            ThreadedMap<T, R> executable = new ThreadedMap<>(function, batchInput);
            Thread thread = new Thread(executable);
            threadedMaps.add(executable);
            pool.add(thread);
            thread.start();
        }

        Collection<R> result = new ArrayList<>(data.size());
        for (int i = 0; i < threadNum; i++) {
            Thread thread = pool.get(i);
            thread.join();
            if (!threadedMaps.get(i).getExitStatus().equals(0)) {
                throw new InterruptedException();
            }
            result.addAll(threadedMaps.get(i).getResult());
        }

        pool.clear();
        return result;
    }
}
