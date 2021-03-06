package model;

import java.util.ArrayList;

public class LexicoTipoToken {

	public static ArrayList<LexicoToken> AL_p = new ArrayList<LexicoToken>();
	public static ArrayList<LexicoToken> AL_o = new ArrayList<LexicoToken>();
	public static ArrayList<LexicoToken> AL_s = new ArrayList<LexicoToken>();

	public LexicoTipoToken() {
		LexicoToken t01 = new LexicoToken(01, "program", "Palavra Reservada");
		LexicoToken t02 = new LexicoToken(02, "label", "Palavra Reservada");
		LexicoToken t03 = new LexicoToken(03, "const", "Palavra Reservada");
		LexicoToken t04 = new LexicoToken(04, "var", "Palavra Reservada");
		LexicoToken t05 = new LexicoToken(05, "procedure", "Palavra Reservada");
		LexicoToken t06 = new LexicoToken(06, "begin", "Palavra Reservada");
		LexicoToken t07 = new LexicoToken(07, "end", "Palavra Reservada");
		LexicoToken t08 = new LexicoToken(8, "integer", "Palavra Reservada");
		LexicoToken t09 = new LexicoToken(9, "array", "Palavra Reservada");
		LexicoToken t10 = new LexicoToken(10, "of", "Palavra Reservada");
		LexicoToken t11 = new LexicoToken(11, "call", "Palavra Reservada");
		LexicoToken t12 = new LexicoToken(12, "goto", "Palavra Reservada");
		LexicoToken t13 = new LexicoToken(13, "if", "Palavra Reservada");
		LexicoToken t14 = new LexicoToken(14, "then", "Palavra Reservada");
		LexicoToken t15 = new LexicoToken(15, "else", "Palavra Reservada");
		LexicoToken t16 = new LexicoToken(16, "while", "Palavra Reservada");
		LexicoToken t17 = new LexicoToken(17, "do", "Palavra Reservada");
		LexicoToken t18 = new LexicoToken(18, "repeat", "Palavra Reservada");
		LexicoToken t19 = new LexicoToken(19, "until", "Palavra Reservada");
		LexicoToken t20 = new LexicoToken(20, "readln", "Palavra Reservada");
		LexicoToken t21 = new LexicoToken(21, "writeln", "Palavra Reservada");
		LexicoToken t27 = new LexicoToken(27, "for", "Palavra Reservada");
		LexicoToken t28 = new LexicoToken(28, "to", "Palavra Reservada");
		LexicoToken t29 = new LexicoToken(29, "case", "Palavra Reservada");

		LexicoToken t22 = new LexicoToken(22, "or", "Operador Lógico");
		LexicoToken t23 = new LexicoToken(23, "and", "Operador Lógico");
		LexicoToken t24 = new LexicoToken(24, "not", "Operador de Negação");
		LexicoToken t30 = new LexicoToken(30, "+", "Operador de Adição");
		LexicoToken t31 = new LexicoToken(31, "-", "Operador de Subtração");
		LexicoToken t32 = new LexicoToken(32, "*", "Operador de Multiplicação");
		LexicoToken t33 = new LexicoToken(33, "/", "Operador de Divisão");
		LexicoToken t40 = new LexicoToken(40, "=", "Operador Relacional Igual");
		LexicoToken t41 = new LexicoToken(41, ">", "Operador Relacional Maior");
		LexicoToken t42 = new LexicoToken(42, ">=", "Operador Relacional Maior Igual");
		LexicoToken t43 = new LexicoToken(43, "<", "Operador Relacional Menor");
		LexicoToken t44 = new LexicoToken(44, "<=", "Operador Relacional Menor Igual");
		LexicoToken t45 = new LexicoToken(45, "<>", "Operador Relacional Diferente");

		LexicoToken t34 = new LexicoToken(34, "[", "Abre Colchete");
		LexicoToken t35 = new LexicoToken(35, "]", "Fecha Colchete");
		LexicoToken t36 = new LexicoToken(36, "(", "Abre Parentese");
		LexicoToken t37 = new LexicoToken(37, ")", "Fecha Parentese");
		LexicoToken t38 = new LexicoToken(38, ":=", "Atribuição");
		LexicoToken t39 = new LexicoToken(39, ":", "Dois Pontos");
		LexicoToken t46 = new LexicoToken(46, ",", "Virgula");
		LexicoToken t47 = new LexicoToken(47, ";", "Ponto e Virgula");
		LexicoToken t49 = new LexicoToken(49, ".", "Ponto");
		LexicoToken t50 = new LexicoToken(50, "..", "Ponto Ponto");
		LexicoToken t51 = new LexicoToken(51, "$", "Final de Arquivo");

		LexicoTipoToken.AL_p.add(t01);
		LexicoTipoToken.AL_p.add(t02);
		LexicoTipoToken.AL_p.add(t03);
		LexicoTipoToken.AL_p.add(t04);
		LexicoTipoToken.AL_p.add(t05);
		LexicoTipoToken.AL_p.add(t06);
		LexicoTipoToken.AL_p.add(t07);
		LexicoTipoToken.AL_p.add(t08);
		LexicoTipoToken.AL_p.add(t09);
		LexicoTipoToken.AL_p.add(t10);
		LexicoTipoToken.AL_p.add(t11);
		LexicoTipoToken.AL_p.add(t12);
		LexicoTipoToken.AL_p.add(t13);
		LexicoTipoToken.AL_p.add(t14);
		LexicoTipoToken.AL_p.add(t15);
		LexicoTipoToken.AL_p.add(t16);
		LexicoTipoToken.AL_p.add(t17);
		LexicoTipoToken.AL_p.add(t18);
		LexicoTipoToken.AL_p.add(t19);
		LexicoTipoToken.AL_p.add(t20);
		LexicoTipoToken.AL_p.add(t21);
		LexicoTipoToken.AL_p.add(t27);
		LexicoTipoToken.AL_p.add(t28);
		LexicoTipoToken.AL_p.add(t29);

		LexicoTipoToken.AL_o.add(t22);
		LexicoTipoToken.AL_o.add(t23);
		LexicoTipoToken.AL_o.add(t24);
		LexicoTipoToken.AL_o.add(t30);
		LexicoTipoToken.AL_o.add(t31);
		LexicoTipoToken.AL_o.add(t32);
		LexicoTipoToken.AL_o.add(t33);
		LexicoTipoToken.AL_o.add(t40);
		LexicoTipoToken.AL_o.add(t41);
		LexicoTipoToken.AL_o.add(t42);
		LexicoTipoToken.AL_o.add(t43);
		LexicoTipoToken.AL_o.add(t44);
		LexicoTipoToken.AL_o.add(t45);

		LexicoTipoToken.AL_s.add(t34);
		LexicoTipoToken.AL_s.add(t35);
		LexicoTipoToken.AL_s.add(t36);
		LexicoTipoToken.AL_s.add(t37);
		LexicoTipoToken.AL_s.add(t38);
		LexicoTipoToken.AL_s.add(t39);
		LexicoTipoToken.AL_s.add(t46);
		LexicoTipoToken.AL_s.add(t47);
		LexicoTipoToken.AL_s.add(t49);
		LexicoTipoToken.AL_s.add(t50);
		LexicoTipoToken.AL_s.add(t51);
	}
}
