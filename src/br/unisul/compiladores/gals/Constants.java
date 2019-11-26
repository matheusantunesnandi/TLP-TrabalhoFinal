package br.unisul.compiladores.gals;

public interface Constants {
	
	int EPSILON  = 0;
    int DOLLAR   = 1;
    
    /**
     * 
     * inicio com t, indica que é uma constante de token
     * 
     */

    int t_TOKEN_ADICAO = 2; //"+"
    int t_TOKEN_SUBTRACAO = 3; //"-"
    int t_TOKEN_MULTIPLICACAO = 4; //"*"
    int t_TOKEN_DIVISAO = 5; //"/"
    int t_TOKEN_ABRE_PARENTESES = 6; //"("
    int t_TOKEN_FECHA_PARENTESES = 7; //")"
    int t_TOKEN_ATRIBUICAO = 8; //":="
    int t_TOKEN_DOIS_PONTOS = 9; //":"
    int t_TOKEN_IGUAL = 10; //"="
    int t_TOKEN_MAIOR = 11; //">"
    int t_TOKEN_MAIOR_IGUAL = 12; //">="
    int t_TOKEN_MENOR = 13; //"<"
    int t_TOKEN_MENOR_IGUAL = 14; //"<="
    int t_TOKEN_DIFERENTE = 15; //"<>"
    int t_TOKEN_VIRGULA = 16; //","
    int t_TOKEN_PONTO_VIRGULA = 17; //";"
    int t_TOKEN_PONTO = 18; //"."
    int t_IDENT = 19;
    int t_INTEIRO = 20;
    int t_LITERAL = 21;
    int t_PROGRAM = 22;
    int t_CONST = 23;
    int t_VAR = 24;
    int t_PROCEDURE = 25;
    int t_BEGIN = 26;
    int t_END = 27;
    int t_INTEGER = 28;
    int t_CALL = 29;
    int t_IF = 30;
    int t_THEN = 31;
    int t_ELSE = 32;
    int t_WHILE = 33;
    int t_DO = 34;
    int t_REPEAT = 35;
    int t_UNTIL = 36;
    int t_READLN = 37;
    int t_WRITELN = 38;
    int t_OR = 39;
    int t_NOT = 40;
    int t_AND = 41;
    int t_FOR = 42;
    int t_TO = 43;
    int t_CASE = 44;
    int t_OF = 45;
    
}