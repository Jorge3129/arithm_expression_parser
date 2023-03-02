package com.sanchenko.exp;

import java.util.List;

public class FunAppExp extends Exp {
   public String functionName;
   public List<Exp> args;


   public FunAppExp(String functionName, List<Exp> args) {
      this.functionName = functionName;
      this.args = args;
   }

   @Override
   public String toString() {
      return "FunApp(" +
          "'" + functionName + '\'' +
          ", " + args +
          ")";
   }
}
