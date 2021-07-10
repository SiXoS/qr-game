grammar QrGame;

program: init run input definition*;

init: 'init' statement;

run: 'run' statement;

input: 'input' LPAREN WORD ',' WORD RPAREN statement;

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
forEachCondition: WORD 'in' expression;

forLoop: 'for' LPAREN forLoopPartInit? SEMICOLON expression SEMICOLON forLoopPartEnd? RPAREN label? statement;
forLoopPartInit: forLoopPart;
forLoopPartEnd: forLoopPart;
forLoopPart: expression (',' forLoopPart)?;

when: 'when' LPAREN whenParameter RPAREN LBIRD whenClause+ RBIRD;
whenParameter: expression;
whenClause: whenCase ARROW statement;
whenCase: atom (',' whenCase)?;

label: LABEL WORD;

returnStatement: 'return' expression?;
breakRule: 'break' (LABEL WORD)?;
continueRule: 'continue' (LABEL WORD)?;

expression
   :  expression DOT function # methodCall
   |  expression DOT WORD #structFetch
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
   |  expression DOT WORD ASSIGN expression # structAssign
   |  WORD ASSIGN expression # assignExpression
   |  WORD MODIFY_ADD expression # modifyAddExpression
   |  WORD MODIFY_SUBTRACT expression # modifySubtractException
   |  WORD INCREMENT # getAndIncrement
   |  INCREMENT WORD # incrementAndGet
   |  WORD DECREMENT # getAndDecrement
   |  DECREMENT WORD # decrementAndGet
   |  NULL COLON type # nullExpression
   |  atom # atomicOperation
   ;

function: WORD LPAREN argument? RPAREN;
argument: expression (',' argument)?;

structInstantiation: NEW WORD LPAREN argument? RPAREN;

atom: BFALSE | BTRUE | WORD | NUMBER;

definition: struct | functionDefinition;

struct: STRUCT WORD LBIRD structField+ RBIRD;
structField: WORD COLON type;

functionDefinition: 'fun' WORD LPAREN parameters? RPAREN (COLON type)? statement;
parameters: parameter (',' parameters)?;
parameter: WORD COLON type;

type: WORD genericType?;
genericType: LESS genericTypeArg GREATER;
genericTypeArg: type (',' genericTypeArg)?;

NUMBER : [0-9]+;
BTRUE: 'true';
BFALSE: 'false';
STRUCT: 'struct';
NEW: 'new';
NULL: 'null';
ELSE: 'else';
WORD : [a-zA-Z0-9_]+;
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
