package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.BinaryOperator;

public class Equation {

     public static boolean isValidEquation(String equation) {
        return hasBalancedParentheses(equation) && hasValidOperators(equation);
    }

    public static boolean save(Connection connection, String equation) throws SQLException {
        String insertQuery = "INSERT INTO public.equation(expression) VALUES(?)";
        if (isValidEquation(equation)) {
            try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                statement.setString(1, equation);
                statement.executeUpdate();
                return true;
            }
        }
        return false;
    }

    public static boolean hasBalancedParentheses(String equation) {
        Stack<Character> stack = new Stack<>();
        for (char ch : equation.toCharArray()) {
            if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                if (stack.isEmpty()) {
                    return false;
                }
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    private static boolean hasValidOperators(String equation) {
        return !equation.matches(".*(?<!/)[-+*/]{2,}.*") && !equation.matches("^[+*/].*") && !equation.matches(".*[+\\-*/]$");
    }

    public static boolean getRoot(String equation, double x) {
        String[] parts = equation.split("=");
        double leftSide = evaluate(parts[0], x);
        double rightSide = evaluate(parts[1], x);
        return Math.abs(leftSide - rightSide) < 1e-9;
    }

    private static double evaluate(String expression, double x) {
        String[] words = expression.split("(?=[-+*/()])|(?<=[-+*/()])");
        double result = 0;
        double currentNumber = 0;
        BinaryOperator<Double> currentOp = (a, b) -> b;

        for (String word : words) {
            if (word.isEmpty()) continue;

            switch (word) {
                case "+":
                    result = currentOp.apply(result, currentNumber);
                    currentNumber = 0;
                    currentOp = Double::sum;
                    break;
                case "-":
                    result = currentOp.apply(result, currentNumber);
                    currentNumber = 0;
                    currentOp = (a, b) -> a - b;
                    break;
                case "*":
                    currentOp = (a, b) -> a * b;
                    break;
                case "/":
                    currentOp = (a, b) -> a / b;
                    break;
                case "x":
                    currentNumber = x;
                    break;
                default:
                    currentNumber = Double.parseDouble(word);
                    break;
            }
        }

        return currentOp.apply(result, currentNumber);
    }

    public static List<String> findEquationsWithRoots(Connection connection, List<Double> roots) throws SQLException {
        List<String> equations = new ArrayList<>();
        String query = "SELECT expression FROM public.equation";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String equation = resultSet.getString("expression");
                for (double root : roots) {
                    if (getRoot(equation, root)) {
                        equations.add(equation);
                        break;
                    }
                }
            }
        }
        return equations;
    }

    public static List<String> findEquationsWithSingleRoot(Connection connection) throws SQLException {
        List<String> equations = new ArrayList<>();
        String query = "SELECT expression FROM public.equation";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String equation = resultSet.getString("expression");
                List<Double> roots = findAllRoots(equation);
                if (roots.size() == 1) {
                    equations.add(equation);
                }
            }
        }
        return equations;
    }

    private static List<Double> findAllRoots(String equation) {

        return new ArrayList<>();
    }
}
