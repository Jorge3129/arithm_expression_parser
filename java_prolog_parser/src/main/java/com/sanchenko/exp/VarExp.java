package com.sanchenko.exp;

public class VarExp extends Exp {
   public String name;

   public VarExp(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return "Var(" +
          "'" + name + '\'' +
          ')';
   }
}
