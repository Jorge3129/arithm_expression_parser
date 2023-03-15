% optionally many spaces
spaces --> [].
spaces --> [' '], spaces.

% integer parsing util
parseInt(Number) --> sign(S), digits(D), { D \= [] }, { number_codes(Number, [S|D]) }.
parseInt(Number) -->  digits(D), { D \= [] }, { number_codes(Number, D) }.

sign('-') --> ['-'].
sign  --> [].

digits([D|T]) --> [D], { code_type(D, digit) }, digits(T).
digits([]) --> [].

% identifier parsing util
ident([C|Cs]) --> upper(C), alpha_numeric(Cs).

upper(C) --> [C], { code_type(C, upper) }.
upper(C) --> [C], { code_type(C, alpha) }.

alpha_numeric([C|Cs]) --> [C], { code_type(C, alnum) ; C = 95 }, alpha_numeric(Cs).
alpha_numeric([]) --> [].

%  number literal node
number(num(N)) --> parseInt(N), spaces.

%  variable node
variable(var(Name)) --> ident(V), { string_chars(Name, V)}, spaces.

%  function application node
function(funApp(Name, Args)) --> ident(F), { string_chars(Name, F)}, ['('], arguments(Args), [')'], spaces.

arguments([]) --> [].
arguments([A|T]) --> expr(A), [','], arguments(T).
arguments([A]) --> expr(A).

% factor non-terminal
factor(N) --> number(N).
factor(T) --> function(T).
factor(V) --> variable(V).
factor(T) --> ['('], spaces, expr(T), [')'], spaces.

% multiplication chain
mulop(LeftExp, Tree) --> 
    [Op], { member(Op, ['*', '/']) }, spaces, 
    factor(RightExp), { NewExp = binOp(Op, LeftExp, RightExp) }, 
    mulop(NewExp, Tree).
mulop(Tree, Tree) --> [].

% term non-terminal
term(T) --> factor(T1), mulop(T1, T).

% addition chain
addop(LeftExp, Tree) --> 
    [Op], { member(Op, ['+', '-']) }, spaces, 
    term(RightExp), { NewExp = binOp(Op, LeftExp, RightExp) }, 
    addop(NewExp, Tree).
addop(Tree, Tree) --> [].

% expression non-terminal
expr(T) --> term(T1), addop(T1, T).

% full parse function
parse(Input, Tree) :- string_chars(Input,Codes), expr(Tree,Codes,[]).