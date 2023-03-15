package com.sanchenko.monadic_parser;

import org.javafp.data.IList;
import org.javafp.parsecj.*;

import java.util.stream.Collectors;

import static org.javafp.parsecj.Combinators.*;
import static org.javafp.parsecj.Text.*;


public class ParserUtils {

   public static <A> Parser<Character, A> full(Parser<Character, A> p) {
      return many(wspace).bind((sp) ->
          p.bind((result ->
              Combinators.<Character>eof().bind((__) ->
                  retn(result)
              )
          ))
      );
   }

   public static Parser<Character, Integer> numberP =
       option(string("-"), "").bind((sign) ->
           many1(digit).bind((digits) ->
               retn(Integer.parseInt(concat(sign, digits)))
           )
       );

   public static Parser<Character, String> identifierP =
       satisfy(ParserUtils::isFirstLetter).bind((Character firstChar) ->
           many(satisfy(ParserUtils::isNextLetter)).bind((nextChars) ->
               retn(concat(firstChar, nextChars))
           )
       );

   private static boolean isNum(Character c) {
      return c >= '0' && c <= '9';
   }

   private static boolean isAlph(Character c) {
      return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
   }

   private static boolean isFirstLetter(Character c) {
      return isAlph(c);
   }

   private static boolean isNextLetter(Character c) {
      return isAlph(c) || isNum(c);
   }

   public static <A> Parser<Character, A> lexem(Parser<Character, A> p) {
      return p.bind((result) ->
          many(wspace).bind((sp) ->
              retn(result)
          )
      );
   }

   public static Parser<Character, String> reserved(String s) {
      return string(s).bind((result) ->
          many(wspace).bind((__) ->
              retn(result)
          )
      );
   }

   public static <A> Parser<Character, A> parens(Parser<Character, A> p) {
      return reserved("(").bind((__) ->
          lexem(p).bind((result) ->
              reserved(")").bind((___) ->
                  retn(result)
              )
          )
      );
   }

   public static String charsToString(IList<Character> chars) {
      return chars.stream()
          .map(String::valueOf)
          .collect(Collectors.joining());
   }

   public static String concat(String str1, IList<Character> str2) {
      return str1 + charsToString(str2);
   }

   public static String concat(Character c, IList<Character> str2) {
      return c + charsToString(str2);
   }
}
