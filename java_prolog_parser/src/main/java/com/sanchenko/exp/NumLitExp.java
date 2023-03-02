package com.sanchenko.exp;

public class NumLitExp extends Exp {
   public int value;

   public NumLitExp(int value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return "Num(" +
          "" + value +
          ')';
   }
}
