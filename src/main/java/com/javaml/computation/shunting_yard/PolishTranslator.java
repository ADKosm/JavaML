package com.javaml.computation.shunting_yard;

import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PolishTranslator {
    Pattern symbolPattern;
    HashMap<String, Integer> priority;
    Stack<String> operators;

    public PolishTranslator() {
        symbolPattern = Pattern.compile("[\\/\\*\\+\\-\\(\\)]|\\d+");
        priority = new HashMap<>();
        operators = new Stack<>();

        priority.put("(", 0);
        priority.put(")", 0);
        priority.put("+", 1);
        priority.put("-", 1);
        priority.put("*", 2);
        priority.put("/", 2);
    }


    /**
     * Convert natural notation into polish notation
     * @param expression
     * @return string with polish notation
     */
    public String translate(String expression) {
        Matcher m = symbolPattern.matcher(expression);
        StringBuilder result = new StringBuilder();
        while(m.find()) {
            String current = m.group(0);

            if(priority.containsKey(current)) { // operator or bracket
                if(current.equals("(")) {
                    operators.push(current);
                    continue;
                }

                if(current.equals(")")) {
                    while (!operators.peek().equals("(")) {
                        result.append(operators.pop()).append(' ');
                    }
                    operators.pop();
                    continue;
                }

                while (!operators.isEmpty() && priority.get(current) <= priority.get(operators.peek())) {
                    result.append(operators.pop()).append(' ');
                }

                operators.push(current);
            } else { // number
                result.append(current).append(' ');
            }
        }

        while (!operators.isEmpty()) { // remain operators
            result.append(operators.pop()).append(' ');
        }

        return result.toString();
    }
}
