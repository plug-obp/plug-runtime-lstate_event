grammar SEDiag;
//clock relation definition language

LINE_COMMENT : '//' .*? '\n' -> skip ;
COMMENT : '/*' .*? '*/' -> skip ;
WS : [ \r\t\n]+ -> skip ;

expression :
          NUMBER                                                            #LiteralExp
		| 'clk' IDENTIFIER                                                  #ClockExp
		| 't' IDENTIFIER									                #TransitionExp
		| 'v' IDENTIFIER NEXT?                                              #VariableExp
		| LPAREN expression RPAREN 									        #ParenExp
		| operator=(NOT | MINUS) expression        				            #UnaryExp
        | expression operator=(MULT | MOD) expression                       #BinaryExp
        | expression operator=(PLUS | MINUS) expression                     #BinaryExp
        | expression operator=(LT | LTE | GT | GTE ) expression             #BinaryExp
        | expression operator=(EQ | NEQ) expression                         #BinaryExp
        | expression '?' expression ':' expression                          #ConditionalExp
		;

LPAREN: '(';
RPAREN: ')';
LSQUARE: '[';
RSQUARE: ']';
NOT: '!';
PLUS: '+';
MINUS: '-';
MULT: '*';
MOD: '%';
LT: '<';
LTE: '<=';
GT: '>';
GTE: '>=';
EQ: '=';
NEQ: '!=';
NEXT: '\'';

NUMBER : [0-9]+;
IDENTIFIER : '[' [a-zA-Z_0-9.]+ ']';

