grammar SEDiag;
//clock relation definition language

LINE_COMMENT : '//' .*? '\n' -> skip ;
COMMENT : '/*' .*? '*/' -> skip ;
WS : [ \r\t\n]+ -> skip ;

expression :
          NUMBER                                                            #ValueExp
		| clock                                                             #ReferenceExp
		| transition									                    #ReferenceExp
		| variable NEXT?                                                    #ReferenceExp
		| LPAREN expression RPAREN 									        #ParenExp
		| operator=(NOT | PLUS | MINUS) expression        				    #UnaryExp
        | expression operator=(MULT | DIV | MOD) expression                 #BinaryExp
        | expression operator=(PLUS | MINUS) expression                     #BinaryExp
        | expression operator=(LT | LTE | GT | GTE ) expression             #BinaryExp
        | expression operator=(EQ | NEQ) expression                         #BinaryExp
        | expression '?' expression ':' expression                          #ConditionalExp
		;

clock: 'clk' IDENTIFIER;
variable: 'v' IDENTIFIER;
transition: 't' IDENTIFIER;

literal : NUMBER;

LPAREN: '(';
RPAREN: ')';
LSQUARE: '[';
RSQUARE: ']';
NOT: '!';
PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';
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

