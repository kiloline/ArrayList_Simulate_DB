/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package count;

/**
 *
 * @author rkppo
 */
public class Calculate {
    public static boolean isOperator(String operator) {    
        if (operator.equals("+") || operator.equals("-")    
                || operator.equals("*") || operator.equals("/")    
                || operator.equals("(") || operator.equals(")"))    
            return true;    
        else   
            return false;    
    }    
   
    // 设置操作符号的优先级别    
    public static int priority(String operator) {    
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":    
                return 2;
            case "(":
            case ")":
                return 3;
            case "[":
            case "]":
                return 4;
            default:
                return 0;
        }
    }    
   
    // 做2值之间的计算    
    public static String twoResult(String operator, String a, String b) {    
        try {    
            String op = operator;    
            String rs = new String();    
            double x = Double.parseDouble(b);    
            double y = Double.parseDouble(a);    
            double z = 0;    
            if (op.equals("+"))    
                z = x + y;    
            else if (op.equals("-"))    
                z = x - y;    
            else if (op.equals("*"))    
                z = x * y;    
            else if (op.equals("/"))    
                z = x / y;    
            else   
                z = 0;    
            return rs + z;    
        } catch (NumberFormatException e) {    
            System.out.println("input has something wrong!");    
            return "Error";    
        }    
    }    
}
