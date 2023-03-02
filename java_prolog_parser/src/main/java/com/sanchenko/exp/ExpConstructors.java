package com.sanchenko.exp;

import java.util.Arrays;

public class ExpConstructors {
   public static VarExp Var(String name) {
      return new VarExp(name);
   }

   public static NumLitExp Num(int num) {
      return new NumLitExp(num);
   }

   public static BinaryOpExp BinOp(String op, Exp left, Exp right) {
      return new BinaryOpExp(op, left, right);
   }

   public static FunAppExp FunApp(String name, Exp... args) {
      return new FunAppExp(name, Arrays.asList(args));
   }
}
