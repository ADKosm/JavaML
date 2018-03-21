package com.javaml.computation;


public interface ExpressionComputator {
    /**
     * Calculate arithmetical expression, written in natural notation
     * By not, this method support brackets `(`, `)` and 4 operations: `/`, `*`, `+`, `-`
     * @param expression
     * @return computed number
     */
    Integer compute(String expression);
}
