package com.sanchenko.exp;

public class BinaryOpExp extends Exp {
   public String operator;
   public Exp left;
   public Exp right;

   public BinaryOpExp(String operator, Exp left, Exp right) {
      this.operator = operator;
      this.left = left;
      this.right = right;
   }

   @Override
   public String toString() {
      return "BinOp(" +
          "'" + operator + '\'' +
          ", " + left +
          ", " + right +
          ')';
   }
}
