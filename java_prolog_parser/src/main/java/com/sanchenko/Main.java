package com.sanchenko;


import com.sanchenko.exp.Exp;
import com.sanchenko.imperative_parser.ImperativeParser;

import java.util.Scanner;

public class Main {
   public static void main(String[] args) throws Exception {
      Scanner input = new Scanner(System.in);
      while (true) {
         System.out.print("expr>");
         String line = input.nextLine();
         if (":q".equals(line)) {
            break;
         }
         try {
            Exp tree = ImperativeParser.parse(line);
            System.out.println(tree);
         } catch (Exception e) {
            System.out.println(e);
         }
      }
   }
}