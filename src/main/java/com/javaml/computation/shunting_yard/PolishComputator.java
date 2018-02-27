package com.javaml.computation.shunting_yard;

import com.javaml.computation.ExpressionComputator;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.BinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PolishComputator implements ExpressionComputator {
    Pattern symbolPattern;
    Stack<String> result;
    Map<String, BinaryOperator<Integer>> binaryOperations;

    public PolishComputator() {
        symbolPattern = Pattern.compile("[\\/\\*\\+\\-\\(\\)]|\\d+");
        result = new Stack<>();
        binaryOperations = new HashMap<>();
        binaryOperations.put("-", (x, y) -> y - x);
        binaryOperations.put("+", (x, y) -> y + x);
        binaryOperations.put("*", (x, y) -> y * x);
        binaryOperations.put("/", (x, y) -> y / x);
    }

    @Override
    public Integer compute(String expression) {
        PolishTranslator translator = new PolishTranslator();
        String translatedExpression = translator.translate(expression);
        return polishCompute(translatedExpression);
    }

    private Integer polishCompute(String expression) {
        Matcher m = symbolPattern.matcher(expression);
        while (m.find()) {
            String current = m.group(0);
            if (binaryOperations.containsKey(current)) {
                Integer x = Integer.parseInt(result.pop());
                Integer y = Integer.parseInt(result.pop());
                Integer res = binaryOperations.get(current).apply(x, y);
                result.push(res.toString());
            } else {
                result.push(current);
            }
        }
        return Integer.parseInt(result.pop());
    }
}
