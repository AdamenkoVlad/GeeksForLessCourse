package org.example;

import junit.framework.TestCase;

import static org.example.Equation.isValidEquation;



public class GreeksForLessAppTest extends TestCase {


    public void testValidEquationInput() {
        String equation = "2*(x+5+х)+5=10";
        assertTrue("Equation should be valid", Equation.isValidEquation(equation));
    }



    public void testCorrectParenthesesPlacement() {
        String equation = "(())";
        assertTrue("Parentheses should be correctly placed", Equation.hasBalancedParentheses(equation));
    }



    public void testConsecutiveOperations() {
        String invalidEquation = "17=2**x+5";
        assertFalse("Equation with consecutive operations should be invalid", Equation.isValidEquation(invalidEquation));

        String validEquation = "17=2/ -x+5";
        assertTrue("Equation with valid operations should be valid", Equation.isValidEquation(validEquation));
    }


}
