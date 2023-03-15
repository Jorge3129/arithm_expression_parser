module Parser where

import Text.ParserCombinators.Parsec

-- Data types
data Op = Add | Minus | Mul | Div deriving (Eq, Show)

data Exp = Num Int
    | Var String 
    | BinOp Op Exp Exp 
    | FunApp String [Exp] 
    deriving (Eq, Show)

-- Utils
binOp :: String -> (a -> a -> a) -> Parser (a -> a -> a)
binOp x f = string x >> return f

lexem :: Parser a -> Parser a
lexem p = do { a <- p; spaces; return a}

reserved :: String -> Parser ()
reserved s = do { _ <- string s; spaces }

parens :: Parser a -> Parser a
parens p = do {reserved "("; n <- lexem p; reserved ")"; return n} 

-- Number utils
number :: Parser Int
number = do 
  sign <- option "" (string "-")
  digits <- many1 digit
  return $ read (sign ++ digits)

-- Identifier utils
latinAlf :: String
latinAlf = ['A'..'Z'] ++ ['a'..'z']

subseqLetter :: String
subseqLetter = latinAlf ++ ['0'..'9'] ++ "_"

ident :: Parser String
ident = do 
  st <- oneOf latinAlf
  nx <- many (oneOf subseqLetter)
  return (st:nx)

-- Number node
numberParser :: Parser Exp
numberParser = do {n <- lexem number; return (Num n)}

-- Variable node
identParser :: Parser Exp
identParser = do
  st <- lexem ident
  return (Var st)

-- Operations
addop, mulop :: Parser (Exp -> Exp -> Exp)
addop = binOp "+" (BinOp Add) <|> (binOp "-"(BinOp Minus))
mulop = binOp "*"(BinOp Mul) <|> (binOp "/"(BinOp Minus))

funcApp :: Parser Exp
funcApp = do
  nm <- lexem ident
  args <- callArgs
  return (FunApp nm args)

callArgs :: Parser [Exp]
callArgs = parens ((lexem expr) `sepBy` (reserved ","))

-- Expressions
expr, term, factor :: Parser Exp
factor = numberParser <|> try funcApp <|> identParser <|> parens expr
term = chainl1 factor (lexem mulop)
expr = chainl1 term (lexem addop)

full :: Parser a -> Parser a
full p = do { _ <- spaces; v <- p; eof; return v}

parseOrThrow :: Parser a -> String -> a
parseOrThrow p str = 
  case parse ((full) p) "" str of
    (Left x) -> error (show x)
    (Right x) -> x

parseExpr :: String -> Exp
parseExpr input = parseOrThrow expr input