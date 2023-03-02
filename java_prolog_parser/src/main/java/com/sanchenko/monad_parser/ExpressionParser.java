package com.sanchenko.monad_parser;

import com.sanchenko.exp.*;
import org.javafp.parsecj.Parser;
import org.javafp.parsecj.input.Input;

import java.util.List;
import java.util.function.BinaryOperator;

import static com.sanchenko.monad_parser.ParserUtils.*;
import static com.sanchenko.monad_parser.ParserUtils.lexem;
import static org.javafp.parsecj.Combinators.*;
import static org.javafp.parsecj.Text.string;

public class ExpressionParser {

   public static Parser.Ref<Character, Exp> exprP = Parser.ref();


   public static Parser<Character, Exp> numLitExp =
       lexem(numberP).bind((num) ->
           retn(new NumLitExp(num))
       );


   public static Parser<Character, Exp> identifierExp =
       lexem(identifierP).bind((name) ->
           retn(new VarExp(name))
       );


   public static <A> Parser<Character, BinaryOperator<A>> binOpExp(String id, BinaryOperator<A> f) {
      return string(id).bind((__) -> retn(f));
   }

   public static Parser<Character, BinaryOperator<Exp>> addOpExp =
       binOpExp("+", (Exp a, Exp b) -> new BinaryOpExp("+", a, b)).or(
           binOpExp("-", (Exp a, Exp b) -> new BinaryOpExp("-", a, b))
       );

   public static Parser<Character, BinaryOperator<Exp>> mulOpExp =
       binOpExp("*", (a, b) -> new BinaryOpExp("*", a, b));

   public static Parser<Character, List<Exp>> callArgs =
       parens(sepBy(exprP, string(","))).map((args ->
           args.stream().toList()
       ));

   public static Parser<Character, Exp> funcApp =
       lexem(identifierP).bind((name) ->
           callArgs.bind((args) ->
               retn(new FunAppExp(name, args))
           )
       );

   public static Parser<Character, Exp> factorP =
       numLitExp.or(attempt(funcApp)).or(identifierExp).or(parens(exprP));

   public static Parser<Character, Exp> termP =
       chainl1(factorP, lexem(mulOpExp));

   static {
      exprP.set(chainl1(termP, lexem(addOpExp)));
   }

   public static Parser<Character, Exp> fullExprP =
       full(exprP);


   public static Exp parse(String input) throws Exception {
      return fullExprP.parse(Input.of(input)).getResult();
   }
}
