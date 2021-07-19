grammar QrGame;

program: constDef* init run input definition*;

constDef: CONSTANT_KEYWORD CONSTANT ASSIGN expression;

init: 'init' statement;

run: 'run' statement;

input: 'input' LPAREN NAME ',' NAME RPAREN statement;

statement
    : conditional #conditionalStatement
    | conditionalElse #conditionalElseStatement
    | whileLoop #whileLoopStatement
    | forEachLoop #forEachLoopStatement
    | forLoop #forLoopStatement
    | when #whenStatement
    | returnStatement #aReturnStatement
    | breakRule #breakStatement
    | continueRule # continueStatement
    | expression #expressionStatement
    | block #blockStatement
    ;

block: LBIRD statement+ RBIRD;

conditional: 'if' LPAREN expression RPAREN statement;
conditionalElse: conditional 'else' statement;

whileLoop: 'while' LPAREN expression RPAREN label? statement;

forEachLoop: 'for' LPAREN forEachCondition RPAREN label? statement;
forEachCondition: NAME 'in' expression;

forLoop: 'for' LPAREN forLoopPartInit? SEMICOLON expression SEMICOLON forLoopPartEnd? RPAREN label? statement;
forLoopPartInit: forLoopPart;
forLoopPartEnd: forLoopPart;
forLoopPart: expression (',' forLoopPart)?;

when: 'when' LPAREN whenParameter RPAREN LBIRD whenClause+ RBIRD;
whenParameter: expression;
whenClause: whenCase ARROW statement;
whenCase: atom (',' whenCase)?;

label: LABEL NAME;

returnStatement: 'return' expression?;
breakRule: 'break' (LABEL NAME)?;
continueRule: 'continue' (LABEL NAME)?;

expression
   :  expression DOT function # methodCall
   |  expression DOT NAME #structFetch
   |  MINUS expression #negateExpression
   |  NOT expression #notExpression
   |  expression (TIMES | DIV | MOD) expression # multiplicativeOperation
   |  expression (PLUS | MINUS) expression # additiveOperation
   |  expression GREATER expression # greaterComparison
   |  expression LESS expression # lessComparison
   |  expression EQUALS expression # equalsComparison
   |  expression NEQUALS expression # notEqualsComparison
   |  expression GEQUALS expression # greaterEqualsComparison
   |  expression LEQUALS expression # lessEqualsComparison
   |  expression AND expression # andOperation
   |  expression OR expression # orOperation
   |  expression QUESTION expression COLON expression # conditionalExpression
   |  structInstantiation # structInstantiationCall
   |  function # functionCall
   |  LPAREN expression RPAREN # parenthesis
   |  TYPEFETCH expression # typeFetch
   |  expression DOT NAME ASSIGN expression # structAssign
   |  NAME ASSIGN expression # assignExpression
   |  NAME MODIFY_ADD expression # modifyAddExpression
   |  NAME MODIFY_SUBTRACT expression # modifySubtractException
   |  NAME INCREMENT # getAndIncrement
   |  INCREMENT NAME # incrementAndGet
   |  NAME DECREMENT # getAndDecrement
   |  DECREMENT NAME # decrementAndGet
   |  NULL COLON type # nullExpression
   |  atom # atomicOperation
   ;

function: NAME LPAREN argument? RPAREN;
argument: expression (',' argument)?;

structInstantiation: NEW NAME LPAREN argument? RPAREN;

atom: BFALSE | BTRUE | NAME | NUMBER | CONSTANT;

definition: struct | functionDefinition;

struct: STRUCT NAME LBIRD structField+ RBIRD;
structField: NAME COLON type;

functionDefinition: 'fun' NAME LPAREN parameters? RPAREN (COLON type)? statement;
parameters: parameter (',' parameters)?;
parameter: NAME COLON type;

type: NAME genericType?;
genericType: LESS genericTypeArg GREATER;
genericTypeArg: type (',' genericTypeArg)?;

NUMBER : [0-9]+('.'[0-9]+)?;
BTRUE: 'true';
BFALSE: 'false';
STRUCT: 'struct';
NEW: 'new';
NULL: 'null';
ELSE: 'else';
CONSTANT_KEYWORD: 'const';
CONSTANT : [A-Z0-9_]+;
NAME : [a-zA-Z0-9_]+;
TIMES : '*';
DIV : '/';
MOD : '%';
LPAREN : '(';
RPAREN : ')';
LSQUARE: '[';
RSQUARE: ']';
PLUS : '+';
MINUS : '-';
LBIRD: '{';
RBIRD: '}';
EQUALS: '==';
ASSIGN: '=';
MODIFY_ADD: '+=';
MODIFY_SUBTRACT: '-=';
INCREMENT: '++';
DECREMENT: '--';
NEQUALS: '!=';
GEQUALS: '>=';
LEQUALS: '<=';
GREATER: '>';
LESS: '<';
NOT: '!';
AND: '&&';
OR: '||';
QUESTION: '?';
COLON: ':';
SEMICOLON: ';';
TYPEFETCH: '~';
DOT: '.';
ARROW: '->';
LABEL: '@';
WS: [ \t\r\n\u000C]+ -> skip;
COMMENT: '/*' .*? '*/' -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;
