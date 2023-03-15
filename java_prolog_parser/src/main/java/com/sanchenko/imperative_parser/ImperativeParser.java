package com.sanchenko.imperative_parser;

import com.sanchenko.exp.*;

import java.util.List;
import java.util.ArrayList;

public class ImperativeParser {
   private final String input;
   private int pos;

   private ImperativeParser(String input) {
      this.input = input;
      this.pos = 0;
   }

   public static Exp parse(String input) {
      return new ImperativeParser(input).parseExpression();
   }

   private Exp parseExpression() {
      Exp left = parseTerm();

      while (isAddOp(peek())) {
         String operator = nextWithSpaces();
         Exp right = parseTerm();
         left = new BinaryOpExp(operator, left, right);
      }

      return left;
   }

   private Exp parseTerm() {
      Exp left = parseFactor();

      while (isMulOp(peek())) {
         String operator = nextWithSpaces();
         Exp right = parseFactor();

         left = new BinaryOpExp(operator, left, right);
      }

      return left;
   }

   private Exp parseFactor() {
      if (isNumber(peek())) {
         return new NumLitExp(parseNumber());
      }

      if (isLetter(peek())) {
         String identifier = parseIdentifier();

         if (isOpenParen(peek())) {
            nextWithSpaces();
            List<Exp> args = parseArguments();
            expectCloseParen();
            return new FunAppExp(identifier, args);
         }

         return new VarExp(identifier);
      }

      if (isOpenParen(peek())) {
         nextWithSpaces();
         Exp expression = parseExpression();
         expectCloseParen();

         return expression;
      }

      throw new RuntimeException("Unexpected token: " + peek());
   }


   private List<Exp> parseArguments() {
      List<Exp> args = new ArrayList<>();

      if (isCloseParen(peek())) {
         return args;
      }

      Exp arg = parseExpression();
      args.add(arg);
      spaces();

      while (isComma(peek())) {
         nextWithSpaces();
         arg = parseExpression();
         args.add(arg);
         spaces();
      }

      return args;
   }

   private int parseNumber() {
      StringBuilder sb = new StringBuilder();

      while (isDigit(peek())) {
         sb.append(next());
      }

      spaces();

      return Integer.parseInt(sb.toString());
   }

   private String parseIdentifier() {
      StringBuilder sb = new StringBuilder();

      while (isLetterOrDigitOrUnderscore(peek())) {
         sb.append(next());
      }

      spaces();

      return sb.toString();
   }


   private String next() {
      if (pos >= input.length()) {
         throw new RuntimeException("Unexpected end of input");
      }

      return Character.toString(input.charAt(pos++));
   }

   private char peek() {
      if (pos >= input.length()) {
         return '\0';
      }

      return input.charAt(pos);
   }

   private void spaces() {
      while (isWhiteSpace(peek())) {
         next();
      }
   }

   private String nextWithSpaces() {
      String next = next();

      spaces();

      return next;
   }

   private void expectCloseParen() {
      if (!isCloseParen(peek())) {
         throw new RuntimeException("Expected closing parenthesis, but got: " + peek());
      }

      nextWithSpaces();
   }


   private boolean isComma(char peek) {
      return peek == ',';
   }

   private boolean isCloseParen(char peek) {
      return peek == ')';
   }

   private boolean isOpenParen(char peek) {
      return peek == '(';
   }

   private boolean isWhiteSpace(char c) {
      return c == ' ';
   }

   private boolean isAddOp(char c) {
      return c == '+' || c == '-';
   }

   private boolean isMulOp(char c) {
      return c == '*' || c == '/';
   }

   private boolean isNumber(char c) {
      return isDigit(c);
   }

   private boolean isLetter(char c) {
      return Character.isLetter(c);
   }

   private boolean isDigit(char c) {
      return Character.isDigit(c);
   }

   private boolean isLetterOrDigitOrUnderscore(char c) {
      return isLetter(c) || isDigit(c);
   }
}


