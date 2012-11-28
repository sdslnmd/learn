package com.engineer.sun.algorithm;

import java.util.Stack;

/**
 * User: sunluning
 * Date: 12-11-17 下午5:18
 */
public class Evaluate {
    public static void main(String[] args) {

        Stack<String> ops=new Stack<String>();
        Stack<Double> vals=new Stack<Double>();

        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            //读取字符，如果是运算符则入栈
            if (s.equals("(")) {
            }else if (s.equals("+")) {
                ops.push(s);
            }else if (s.equals("-")) {
                ops.push(s);
            }else if (s.equals("*")) {
                ops.push(s);
            }else if (s.equals("/")) {
                ops.push("/");
            }else if (s.equals(")")) {
                //如果字符为"}",弹出运算符和操作数,计算结果并压入栈.
                String op = ops.pop();
                Double v = vals.pop();
                if (op.equals("+")) {
                    v = vals.pop() + v;
                } else if (op.equals("-")) {
                    v = vals.pop() - v;
                } else if (op.equals("*")) {
                    v = vals.pop() * v;
                } else if (op.equals("/")) {
                    v = vals.pop() / v;
                }
                vals.push(v);

            } else {
                vals.push(Double.parseDouble(s));
            }


            StdOut.print(vals.pop());


        }

    }


}
