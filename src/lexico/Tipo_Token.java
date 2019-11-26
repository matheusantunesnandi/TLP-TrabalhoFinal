package lexico;

import principal.Tela;

public class Tipo_Token {
	
	public Tipo_Token(){
		Token t01=new Token(01,"program",      "Palavra Reservada");
		Token t02=new Token(02,"label",        "Palavra Reservada");
		Token t03=new Token(03,"const",        "Palavra Reservada");
		Token t04=new Token(04,"var",          "Palavra Reservada");
		Token t05=new Token(05,"procedure",    "Palavra Reservada");
		Token t06=new Token(06,"begin",        "Palavra Reservada");
		Token t07=new Token(07,"end",          "Palavra Reservada");
		Token t08=new Token(8, "integer",      "Palavra Reservada");
		Token t09=new Token(9, "array",        "Palavra Reservada");
		Token t10=new Token(10,"of",           "Palavra Reservada");
		Token t11=new Token(11,"call",         "Palavra Reservada");
		Token t12=new Token(12,"goto",         "Palavra Reservada");
		Token t13=new Token(13,"if",           "Palavra Reservada");
		Token t14=new Token(14,"then",         "Palavra Reservada");
		Token t15=new Token(15,"else",         "Palavra Reservada");
		Token t16=new Token(16,"while",        "Palavra Reservada");
		Token t17=new Token(17,"do",           "Palavra Reservada");
		Token t18=new Token(18,"repeat",       "Palavra Reservada");
		Token t19=new Token(19,"until",        "Palavra Reservada");
		Token t20=new Token(20,"readln",       "Palavra Reservada");
		Token t21=new Token(21,"writeln",      "Palavra Reservada");
		Token t27=new Token(27,"for",          "Palavra Reservada");
		Token t28=new Token(28,"to",           "Palavra Reservada");
		Token t29=new Token(29,"case",         "Palavra Reservada");
		
		Token t22=new Token(22,"or",           "Operador Lógico");
		Token t23=new Token(23,"and",          "Operador Lógico");
		Token t24=new Token(24,"not",          "Operador de Negação");
		Token t30=new Token(30,"+",            "Operador de Adição");
		Token t31=new Token(31,"-",            "Operador de Subtração");
		Token t32=new Token(32,"*",            "Operador de Multiplicação");
		Token t33=new Token(33,"/",            "Operador de Divisão");
		Token t40=new Token(40,"=",            "Operador Relacional Igual");
		Token t41=new Token(41,">",            "Operador Relacional Maior");
		Token t42=new Token(42,">=",           "Operador Relacional Maior Igual");
		Token t43=new Token(43,"<",            "Operador Relacional Menor");
		Token t44=new Token(44,"<=",           "Operador Relacional Menor Igual");
		Token t45=new Token(45,"<>",           "Operador Relacional Diferente");
		
		Token t34=new Token(34,"[",            "Abre Colchete");
		Token t35=new Token(35,"]",            "Fecha Colchete");
		Token t36=new Token(36,"(",            "Abre Parentese");
		Token t37=new Token(37,")",            "Fecha Parentese");
		Token t38=new Token(38,":=",           "Atribuição");
		Token t39=new Token(39,":",            "Dois Pontos");		
		Token t46=new Token(46,",",            "Virgula");
		Token t47=new Token(47,";",            "Ponto e Virgula");		
		Token t49=new Token(49,".",            "Ponto");
		Token t50=new Token(50,"..",           "Ponto Ponto");
		Token t51=new Token(51,"$",            "Final de Arquivo");
		
		Tela.AL_p.add(t01);		Tela.AL_p.add(t02);		Tela.AL_p.add(t03);		Tela.AL_p.add(t04);		Tela.AL_p.add(t05);
		Tela.AL_p.add(t06);		Tela.AL_p.add(t07);		Tela.AL_p.add(t08);		Tela.AL_p.add(t09);		Tela.AL_p.add(t10);
		Tela.AL_p.add(t11);		Tela.AL_p.add(t12);		Tela.AL_p.add(t13);		Tela.AL_p.add(t14);		Tela.AL_p.add(t15);
		Tela.AL_p.add(t16);		Tela.AL_p.add(t17);		Tela.AL_p.add(t18);		Tela.AL_p.add(t19);		Tela.AL_p.add(t20);
		Tela.AL_p.add(t21);		Tela.AL_p.add(t27);		Tela.AL_p.add(t28);		Tela.AL_p.add(t29);
				
		Tela.AL_o.add(t22);		Tela.AL_o.add(t23);		Tela.AL_o.add(t24);		Tela.AL_o.add(t30);		Tela.AL_o.add(t31);
		Tela.AL_o.add(t32);		Tela.AL_o.add(t33);		Tela.AL_o.add(t40);		Tela.AL_o.add(t41);		Tela.AL_o.add(t42);
		Tela.AL_o.add(t43);		Tela.AL_o.add(t44);		Tela.AL_o.add(t45);
				
		Tela.AL_s.add(t34);		Tela.AL_s.add(t35);		Tela.AL_s.add(t36);		Tela.AL_s.add(t37);
		Tela.AL_s.add(t38);		Tela.AL_s.add(t39);		Tela.AL_s.add(t46);		Tela.AL_s.add(t47);
		Tela.AL_s.add(t49);		Tela.AL_s.add(t50);		Tela.AL_s.add(t51);				
	}
}
