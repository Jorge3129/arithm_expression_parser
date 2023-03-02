package com.sanchenko;

import com.sanchenko.exp.Exp;
import org.javafp.parsecj.input.Input;


import static com.sanchenko.monad_parser.ExpressionParser.*;


public class Main {
   public static void main(String[] args) throws Exception {
      Exp exp = fullExprP.parse(Input.of("(1+b)*3 + a * 3")).getResult();
      System.out.println(exp);

      Exp exp2 = fullExprP.parse(Input.of("sin(a) * (foo + max(1,f))")).getResult();
      System.out.println(exp2);

      Exp exp3 = fullExprP.parse(Input.of("2 + sin(2,3)")).getResult();
      System.out.println(exp3);
   }
}