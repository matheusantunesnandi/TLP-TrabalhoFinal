package br.unisul.compiladores.service;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.unisul.compiladores.gals.Constants;
import br.unisul.compiladores.odt.ErroSintaticoSemantico;
import br.unisul.compiladores.odt.Literal;
import br.unisul.compiladores.odt.Operacoes;
import br.unisul.compiladores.odt.Simbolo;
import br.unisul.compiladores.odt.TabelaSimbolos;
import br.unisul.compiladores.odt.Token;

public class Analisador {
	
	/**
	 * Lista Simbolos
	 */
	private List<Simbolo> listaSimbolos;
	
	/**
	 * Lista Assembly
	 */
	private List<Operacoes> listaOperacoes;
	
	/**
	 * Lista Literal
	 */
	private List<Literal> listaLiteral;
	
	
	/**
	 * Lista de erros sintaticos
	 */
	private List <ErroSintaticoSemantico> listaErrosSintaticosSemanticos;
	
	/**
	 * Método responsável pela analise lexica do código lido em arquivo ou informado manualmente.
	 */
	public List<Token> iniciarAnaliseLexica(List<String> listaLinhas) {
		
		/**
		 * Conteudo da linha
		 */
		String linhaArquivo = "";
		/**
		 * Armazena os tokens
		 */
		String tokenUnico;
		/**
		 * Token único
		 */
		String tokens;
		/**
		 * código do token
		 */
		int hashCodeToken = 0;
		/**
		 * Quantidade de caracteres da linha (Largura)
		 */
		int lengthLinha = 0;
		/**
		 * Estado
		 */
		int estado = 1;
		/**
		 * Contador de linhas
		 */
		int contadorLinha = 0;
		/**
		 * Identificador
		 */
		int identificador = 0;
		
		List<Token> tableTokens = new ArrayList<Token>();

		for (String linha : listaLinhas) {
			linhaArquivo = linha;
			
			linhaArquivo += ' '; 														// Adiciona espaço em branco no final de cada linha.
			lengthLinha = linhaArquivo.length();										// Tamanho da linha.
			tokens = "";																// Reseta o token
			contadorLinha++; 															// Incrementa nova linha
			for(int i=0; i < lengthLinha; i++) {
				tokenUnico = linhaArquivo.substring(i, i + 1);							// Obtem Caracter
				hashCodeToken = tokenUnico.hashCode();									// Nº ascii
				
				switch(estado){
					case 1:
					tokens = tokenUnico;
						if((hashCodeToken >= 65) && (hashCodeToken <= 90) || (hashCodeToken >= 97) && (hashCodeToken <= 122)) { 
							estado = 2; 												// [a..z][A..Z] 
						}
						if(hashCodeToken == 58){estado = 14;}							// :
						if(hashCodeToken == 44){estado = 9;}							// ,
						if(hashCodeToken == 46){estado = 10;}							// .
						if((hashCodeToken >= 48)&&(hashCodeToken <= 57)){estado = 8;}	// 0..9
						if(hashCodeToken == 40){estado = 5;}							// (
						if(hashCodeToken == 61){estado = 17;}							// =
						if(hashCodeToken == 41){estado = 11;}							// ) 	
						if(hashCodeToken == 42){estado = 4;}							// *
						if(hashCodeToken == 43){estado = 16;}							// +
						if(hashCodeToken == 45){estado = 15;}							// -
						if(hashCodeToken == 36){estado = 18;}							// $
						if(hashCodeToken == 59){estado = 19;}							// ;
						if(hashCodeToken == 62){estado = 21;}							// >
						if(hashCodeToken == 60){estado = 22;}							// <
						if(hashCodeToken == 32){estado = 1;}							// "Caracter branco"
						if(hashCodeToken == 39 || hashCodeToken == 34){estado = 23;}	// ' e "
						if(hashCodeToken == 47){estado = 25;} 							// / 							
					break;
					case 2:																// [a..z][A..Z][0..9]
					if(((hashCodeToken >= 65) && (hashCodeToken <= 90)) 
							|| ((hashCodeToken >= 97) && (hashCodeToken <= 122))) {		// [a..z][A..Z]
							estado = 2;
							tokens += tokenUnico;
					}
					if((hashCodeToken >= 48) && (hashCodeToken <= 57)) {				// [0..9]
							estado = 2;
							tokens += tokenUnico;
					}							
					if((hashCodeToken != 65) && (hashCodeToken != 66) && (hashCodeToken != 67)&&
						(hashCodeToken != 68) && (hashCodeToken != 69) && (hashCodeToken != 70) && (hashCodeToken != 71) && (hashCodeToken != 72) && (hashCodeToken != 73)&&
						(hashCodeToken != 74) && (hashCodeToken != 75) && (hashCodeToken != 76) && (hashCodeToken != 77) && (hashCodeToken != 78) && (hashCodeToken != 79)&&
						(hashCodeToken != 80) && (hashCodeToken != 81) && (hashCodeToken != 82) && (hashCodeToken != 83) && (hashCodeToken != 84) && (hashCodeToken != 85)&&
						(hashCodeToken != 86) && (hashCodeToken != 87) && (hashCodeToken != 88) && (hashCodeToken != 89) && (hashCodeToken != 90) && (hashCodeToken != 97)&&
						(hashCodeToken != 98) && (hashCodeToken != 99) && (hashCodeToken != 100) && (hashCodeToken != 101) && (hashCodeToken != 102) && (hashCodeToken != 103)&&
						(hashCodeToken != 104) && (hashCodeToken != 105) && (hashCodeToken != 106) && (hashCodeToken != 107) && (hashCodeToken != 108) && (hashCodeToken != 109)&&
						(hashCodeToken != 110) && (hashCodeToken != 111) && (hashCodeToken != 112) && (hashCodeToken != 113) && (hashCodeToken != 114) && (hashCodeToken != 115) && 
						(hashCodeToken != 116)&& (hashCodeToken != 117) && (hashCodeToken != 118) && (hashCodeToken != 119) && (hashCodeToken != 120) && (hashCodeToken != 121) && 
						(hashCodeToken != 122) && (hashCodeToken != 48) && (hashCodeToken != 49) && (hashCodeToken != 50) && (hashCodeToken != 51) && (hashCodeToken != 52) && 
						(hashCodeToken != 53) && (hashCodeToken != 54) && (hashCodeToken != 55) && (hashCodeToken != 56) && (hashCodeToken != 57)) { // != a-z, A-Z, 0..9							
							estado = 3;
							i--;
					}							
					break;
					case 3: // Identificador e reservadas
						if(tokens.length() > 30) { tableTokens.add(new Token(contadorLinha, 0, tokens, "Identificador maior 30")); identificador = 1; }
						if(tokens.equalsIgnoreCase("program")) { tableTokens.add(new Token(contadorLinha, Constants.t_PROGRAM, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("const")) { tableTokens.add(new Token(contadorLinha, Constants.t_CONST, tokens, "Palavra Reservada")); identificador = 1;}						
						if(tokens.equalsIgnoreCase("var")) { tableTokens.add(new Token(contadorLinha, Constants.t_VAR, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("procedure")) { tableTokens.add(new Token(contadorLinha, Constants.t_PROCEDURE, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("begin")) { tableTokens.add(new Token(contadorLinha, Constants.t_BEGIN, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("end")) { tableTokens.add(new Token(contadorLinha, Constants.t_END, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("integer")) { tableTokens.add(new Token(contadorLinha, Constants.t_INTEGER, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("of")) { tableTokens.add(new Token(contadorLinha, Constants.t_OF, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("call")) { tableTokens.add(new Token(contadorLinha, Constants.t_CALL, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("if")) { tableTokens.add(new Token(contadorLinha, Constants.t_IF, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("then")) { tableTokens.add(new Token(contadorLinha, Constants.t_THEN, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("else")) { tableTokens.add(new Token(contadorLinha, Constants.t_ELSE, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("while")) { tableTokens.add(new Token(contadorLinha, Constants.t_WHILE, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("do")) { tableTokens.add(new Token(contadorLinha, Constants.t_DO, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("repeat")) { tableTokens.add(new Token(contadorLinha, Constants.t_REPEAT, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("until")) { tableTokens.add(new Token(contadorLinha, Constants.t_UNTIL, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("readln")) { tableTokens.add(new Token(contadorLinha, Constants.t_READLN, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("writeln")) { tableTokens.add(new Token(contadorLinha, Constants.t_WRITELN, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("or")) { tableTokens.add(new Token(contadorLinha, Constants.t_OR, tokens, "Operador Lógico")); identificador = 1;}
						if(tokens.equalsIgnoreCase("and")) { tableTokens.add(new Token(contadorLinha, Constants.t_AND, tokens, "Operador Lógico")); identificador = 1;}
						if(tokens.equalsIgnoreCase("not")) { tableTokens.add(new Token(contadorLinha, Constants.t_NOT, tokens, "Operador de Negação")); identificador = 1;}
						if(tokens.equalsIgnoreCase("for")) { tableTokens.add(new Token(contadorLinha, Constants.t_FOR, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("to")) { tableTokens.add(new Token(contadorLinha, Constants.t_TO, tokens, "Palavra Reservada")); identificador = 1;}
						if(tokens.equalsIgnoreCase("case")) { tableTokens.add(new Token(contadorLinha, Constants.t_CASE, tokens, "Palavra Reservada")); identificador = 1;}
						if(identificador == 0) { tableTokens.add(new Token(contadorLinha, Constants.t_IDENT, tokens, "Identificador")); }
						estado = 1;
						tokens = "";
						i--;
						identificador = 0;
					break;
					case 4: // *
						tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_MULTIPLICACAO, tokens, "Operador de Multiplicação"));
						estado = 1;
						tokens = "";
						i--;
					break;
					case 5: // CASE (comentario)
							if(hashCodeToken == 42) { // *.
								estado = 6;
							} else {
								estado = 1;
								tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_ABRE_PARENTESES, tokens, "Abre Parenteses"));
								tokens = "";
								i--;	
							}
					break;
					case 6: // CASE (comentario)
						if(hashCodeToken == 42) { // *
							estado = 7;
						} else {
							estado = 6;
						}
					break;
					case 7: // CASE (comentario)
						if(hashCodeToken == 41) { // )
							tableTokens.add(new Token(contadorLinha, 99, "", "(*  *)  Comentário"));
							estado = 1;
							tokens = "";
						} else { 
							estado = 6;
						}
					break;
					case 8: // CASE (inteiro)
						if((hashCodeToken >= 48) && (hashCodeToken <= 57)) { // 0..9
							estado = 8;
							tokens += tokenUnico;
						} else {
								int numt = 0;
								numt = Integer.parseInt(tokens);
								if(numt > 32767){
									tableTokens.add(new Token(contadorLinha, Constants.t_INTEIRO, String.valueOf(numt), "Fora de escala +-32767"));		
								} else {
									tableTokens.add(new Token(contadorLinha, Constants.t_INTEIRO, String.valueOf(numt), "Inteiro"));	
								}																
								estado = 1;
								i--;									
								tokens = "";
						}					
					break;
					case 9: // ,
						tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_VIRGULA, tokens, "Vírgula"));
						estado = 1;
						tokens = "";
						i--;
					break;
					case 10: // ..
						if(hashCodeToken == 46){
							tokens += tokenUnico;
							tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_PONTO, tokens, "Pontos Seguidos"));
							tokens = "";
							estado = 1;
						} else {
							tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_PONTO, tokens, "Ponto"));
							estado = 1;
							tokens = "";
							i--;
						}							
					break;
					case 11: // )
						tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_FECHA_PARENTESES, tokens, "Fecha Parenteses"));
						estado = 1;
						tokens = "";
						i--;
					break;
					
					case 14: // :
						if(hashCodeToken == 61) {
							estado = 20;
						} else { 
							tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_DOIS_PONTOS, tokens, "Dois Pontos"));
							estado = 1;
							tokens = "";
						}
						i--;
					break;
					case 15: // -
						tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_SUBTRACAO, tokens, "Menos Unário"));
						tokens = "";
						estado = 1;
						i--;
					break;
					case 16: // +
						tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_ADICAO, tokens, "Operador de Adição"));
						estado = 1;
						tokens = "";
						i--;
					break;
					case 17: // =
						tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_IGUAL, tokens, "Sinal de Igualdade"));
						estado = 1;
						tokens = "";
						i--;
					break;
					case 18: // $
						tableTokens.add(new Token(contadorLinha, Constants.DOLLAR, tokens, "Cifrão"));
						estado = 1;
						tokens = "";
						i--;
					break;
					case 19: // ;
						tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_PONTO_VIRGULA, tokens, "Ponto e Vírgula"));
						estado = 1;
						tokens = "";
						i--;
					break;
					case 20: // :=
						tokens += tokenUnico;
						tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_ATRIBUICAO, tokens, "Atribuição"));
						estado = 1;
						tokens = "";
					break;
					case 21: // >=, >							
						if(hashCodeToken == 61) {
							tokens += tokenUnico;
							tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_MAIOR_IGUAL, tokens, "Maior ou Igual"));
							estado = 1;
						} else {
							tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_MAIOR, tokens,  "Sinal de Maior"));
							estado = 1;
							tokens = "";
							i--;
						}									
					break;
					case 22: // <=, <>, <						
						if(hashCodeToken == 61) {
							tokens += tokenUnico;
							tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_MENOR_IGUAL, tokens, "Menor ou Igual"));
							estado = 1;
						}
						if(hashCodeToken == 62){
							tokens += tokenUnico;
							tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_DIFERENTE, tokens, "Diferente"));
							estado = 1;
						}
						if(hashCodeToken != 61 && hashCodeToken != 62){
							tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_MENOR, tokens, "Sinal de Menor"));
							estado = 1;
							tokens = "";
							i--;
						}
					break;
					case 23: // ', " 							
						estado = 24;
						i--;
					break;
					case 24: // 'literal', 'FECHA ASPAS
						if(hashCodeToken != 39 && hashCodeToken != 34){
							tokens += tokenUnico;
							estado = 24;
						} else {
							tokens += tokenUnico;
							tableTokens.add(new Token(contadorLinha, Constants.t_LITERAL, tokens, "Literal"));
							estado = 1;
							tokens = "";
						}
					break;					
					case 25: // /
						tableTokens.add(new Token(contadorLinha, Constants.t_TOKEN_DIVISAO, tokens, "Operador de Divisão"));
						estado = 1;
						i--;
						tokens = "";
					break;
				} // END SWITCH
			} // END FOR			
		} // END FOR LINHAS

		/**
		 * Retorna a lista gerada
		 */
		return tableTokens;
	}

	/**
	 * Método responsável pela analise sintatica dos tokens.
	 */
	public void iniciarAnaliseSintatica(List<Token> tableTokens, boolean isSemantico) {
		
		/**
		 * Pilha de derivações;
		 */
		ArrayList<Integer> PILHA_AS = new ArrayList<Integer>();
		/**
		 * Arrray para inversão da derivação;
		 */
		String [] inverso = null;
		
		/**
		 * Verificação de erro sintático caso aconteça;
		 */
		boolean verificarErroSintatico = false;
		
		/**
		 * armazena o código atual da tabela de tokens;
		 */
		int codigoTokenAtual;
		
		/**
		 * Armazena o token atual da tabela de tokens;
		 */
		String tokenAtual;
		
		/**
		 * Token anterior da tabela de tokens;
		 */
		String tokenAnterior = "";
		
		/**
		 * Linha do erro
		 */
		String lnErro = "";
		
		/**
		 * armazena erro sintatico
		 */
		String erroSintatico = "";
		
		/**
		 * Andamento da análise, usado para pegar o token atual de análise (índice do tokenAtual)
		 */
		int p = 0;
				
		/**
		 * Simbolos armazenados.
		 */
		String [] SIMBOLOS = new String [158];
		
		/**
		 * TOKENS
		 */
		SIMBOLOS [Constants.t_PROGRAM] = "PROGRAM";
		SIMBOLOS [Constants.t_CONST] = "CONST";
		SIMBOLOS [Constants.t_VAR] = "VAR";
		SIMBOLOS [Constants.t_PROCEDURE] = "PROCEDURE";
		SIMBOLOS [Constants.t_BEGIN] = "BEGIN";
		SIMBOLOS [Constants.t_END] = "END";
		SIMBOLOS [Constants.t_INTEGER] = "INTEGER";
		SIMBOLOS [Constants.t_OF] = "OF";
		SIMBOLOS [Constants.t_CALL] = "CALL";
		SIMBOLOS [Constants.t_IF] = "IF";
		SIMBOLOS [Constants.t_THEN] = "THEN";
		SIMBOLOS [Constants.t_ELSE] = "ELSE";
		SIMBOLOS [Constants.t_WHILE] = "WHILE";
		SIMBOLOS [Constants.t_DO] = "DO";
		SIMBOLOS [Constants.t_REPEAT] = "REPEAT";
		SIMBOLOS [Constants.t_UNTIL] = "UNTIL";
		SIMBOLOS [Constants.t_READLN] = "READLN";
		SIMBOLOS [Constants.t_WRITELN] = "WRITELN";
		SIMBOLOS [Constants.t_OR] = "OR";
		SIMBOLOS [Constants.t_AND] = "AND";
		SIMBOLOS [Constants.t_NOT] = "NOT";
		SIMBOLOS [Constants.t_FOR] = "FOR";
		SIMBOLOS [Constants.t_TO] = "TO";
		SIMBOLOS [Constants.t_CASE] = "CASE";
		SIMBOLOS [Constants.t_IDENT] = "IDENT";
		SIMBOLOS [Constants.t_INTEIRO] = "INTEIRO";
		SIMBOLOS [Constants.t_TOKEN_ADICAO] = "+";
		SIMBOLOS [Constants.t_TOKEN_SUBTRACAO] = "-";
		SIMBOLOS [Constants.t_TOKEN_MULTIPLICACAO] = "*";
		SIMBOLOS [Constants.t_TOKEN_DIVISAO] = "/";
		SIMBOLOS [Constants.t_TOKEN_ABRE_PARENTESES] = "(";
		SIMBOLOS [Constants.t_TOKEN_FECHA_PARENTESES] = ")";
		SIMBOLOS [Constants.t_TOKEN_ATRIBUICAO] = ":=";
		SIMBOLOS [Constants.t_TOKEN_DOIS_PONTOS] = ":";
		SIMBOLOS [Constants.t_TOKEN_IGUAL] = "=";
		SIMBOLOS [Constants.t_TOKEN_MAIOR] = ">";
		SIMBOLOS [Constants.t_TOKEN_MAIOR_IGUAL] = ">=";
		SIMBOLOS [Constants.t_TOKEN_MENOR] = "<";
		SIMBOLOS [Constants.t_TOKEN_MENOR_IGUAL] = "<=";
		SIMBOLOS [Constants.t_TOKEN_DIFERENTE] = "<>";
		SIMBOLOS [Constants.t_TOKEN_VIRGULA] = ",";
		SIMBOLOS [Constants.t_TOKEN_PONTO_VIRGULA] = ";";
		SIMBOLOS [Constants.t_LITERAL] = "LITERAL";
		SIMBOLOS [Constants.t_TOKEN_PONTO] = "."; 
		SIMBOLOS [Constants.DOLLAR] = "$";
		
		/**
		 * INÍCIO DOS NÃO TERMINAIS
		 */		
		int primeiroNaoTerminal = 46;
		int primeiroSemantico = 78;

		SIMBOLOS [46] = "<PROGRAMA>";
		SIMBOLOS [47] = "<BLOCO>";
		SIMBOLOS [48] = "<LID>";
		SIMBOLOS [49] = "<REPIDENT>";
		SIMBOLOS [50] = "<DCLCONST>";
		SIMBOLOS [51] = "<LDCONST>";
		SIMBOLOS [52] = "<DCLVAR>";
		SIMBOLOS [53] = "<LDVAR>";
		SIMBOLOS [54] = "<TIPO>";
		SIMBOLOS [55] = "<DCLPROC>";
		SIMBOLOS [56] = "<DEFPAR>";
		SIMBOLOS [57] = "<CORPO>";
		SIMBOLOS [58] = "<REPCOMANDO>";
		SIMBOLOS [59] = "<COMANDO>";
		SIMBOLOS [60] = "<PARAMETROS>";
		SIMBOLOS [61] = "<REFPAR>";
		SIMBOLOS [62] = "<ELSEPARTE>";
		SIMBOLOS [63] = "<VARIAVEL>";
		SIMBOLOS [64] = "<REPVARIAVEL>";
		SIMBOLOS [65] = "<ITEMSAIDA>";
		SIMBOLOS [66] = "<REPITEM>";
		SIMBOLOS [67] = "<EXPRESSAO>";
		SIMBOLOS [68] = "<REPEXPSIMP>";
		SIMBOLOS [69] = "<EXPSIMP>";
		SIMBOLOS [70] = "<REPEXP>";
		SIMBOLOS [71] = "<TERMO>";
		SIMBOLOS [72] = "<REPTERMO>";
		SIMBOLOS [73] = "<FATOR>";
		SIMBOLOS [74] = "<CONDCASE>";
		SIMBOLOS [75] = "<CONTCASE>";
		SIMBOLOS [76] = "<RPINTEIRO>"; 
		
		
		for (int i = 100; i < 157; i++) {
			SIMBOLOS [i] = "(Ação: " + i + ")";
		}
		
		/**
		 * FIRST PRODUCAO
		 */
		Object [] FIRST_PRODUCAO = new Object[90];
 
		FIRST_PRODUCAO[1] = "PROGRAM";//<PROGRAM>
		FIRST_PRODUCAO[2] = "CONST";//<BLOCO>
		FIRST_PRODUCAO[3] = "IDENT";//<LID>
		FIRST_PRODUCAO[4] = "î";//<REPIDENT>
		FIRST_PRODUCAO[5] = ",";//<REPIDENT>
		FIRST_PRODUCAO[6] = "CONST";//<DCLCONST>
		FIRST_PRODUCAO[7] = "î";//<LDCONST>
		FIRST_PRODUCAO[8] = "IDENT";//<LDCONST>
		FIRST_PRODUCAO[9] = "î";//<DCLCONST>
		FIRST_PRODUCAO[10] = "VAR";//<DCLVAR>
		FIRST_PRODUCAO[11] = "î";//<LDVAR>
		FIRST_PRODUCAO[12] = "IDENT";//<LDVAR>
		FIRST_PRODUCAO[13] = "î";//<DCLVAR>
		FIRST_PRODUCAO[14] = "INTEGER";//<TIPO>
		FIRST_PRODUCAO[15] = "PROCEDURE";//<DCLPROC>
		FIRST_PRODUCAO[16] = "î";//<DCLPROC>
		FIRST_PRODUCAO[17] = "î";//<DEFPAR>
		FIRST_PRODUCAO[18] = "(";//<DEFPAR>
		FIRST_PRODUCAO[19] = "BEGIN";//<CORPO>
		FIRST_PRODUCAO[20] = "î";//<REPCOMANDO>
		FIRST_PRODUCAO[21] = ";";//<REPCOMANDO>
		FIRST_PRODUCAO[22] = "IDENT";//<COMANDO>
		FIRST_PRODUCAO[23] = "BEGIN";//<COMANDO>
		FIRST_PRODUCAO[24] = "î";//<COMANDO>
		FIRST_PRODUCAO[25] = "CALL";//<COMANDO>
		FIRST_PRODUCAO[26] = "î";//<PARAMETROS>
		FIRST_PRODUCAO[27] = "(";//<PARAMETROS>
		FIRST_PRODUCAO[28] = "î";//<REPPAR>
		FIRST_PRODUCAO[29] = ",";//<REPPAR>
		FIRST_PRODUCAO[30] = "IF";//<COMANDO>
		FIRST_PRODUCAO[31] = "î";//<ELSEPARTE>
		FIRST_PRODUCAO[32] = "ELSE";//<ELSEPARTE>
		FIRST_PRODUCAO[33] = "WHILE";//<COMANDO>
		FIRST_PRODUCAO[34] = "REPEAT";//<COMANDO>
		FIRST_PRODUCAO[35] = "READLN";//<COMANDO>
		FIRST_PRODUCAO[36] = "IDENT";//<VARIAVEL>
		FIRST_PRODUCAO[37] = "î";//<REPVARIAVEL>
		FIRST_PRODUCAO[38] = ",";//<REPVARIAVEL>
		FIRST_PRODUCAO[39] = "WRITELN";//<COMANDO>
		FIRST_PRODUCAO[40] = "LITERAL";//<ITEMSAIDA>
		FIRST_PRODUCAO[41] = "[NOT] [IDENTIFICADOR] [INTEIRO] [+] [-] [(]";//<ITEMSAIDA>::=<EXPRESSAO>;
		FIRST_PRODUCAO[42] = "î";//<REPITEM>
		FIRST_PRODUCAO[43] = ",";//<REPITEM>
		FIRST_PRODUCAO[44] = "CASE";//<COMANDO>
		FIRST_PRODUCAO[45] = "INTEIRO";//<CONDCASE>
		FIRST_PRODUCAO[46] = ",";//<RPINTEIRO>
		FIRST_PRODUCAO[47] = "î";//<RPINTEIRO>
		FIRST_PRODUCAO[48] = "î";//<CONTCASE>
		FIRST_PRODUCAO[49] = ";";//<CONTCASE>
		FIRST_PRODUCAO[50] = "FOR";//<COMANDO>
		FIRST_PRODUCAO[51] = "NOT IDENT INTEIRO + - (";//<EXPRESSAO>::=<EXPSIMP>...;
		FIRST_PRODUCAO[52] = "î";//<REPEXPSIMP>
		FIRST_PRODUCAO[53] = "=";//<REPEXPSIMP>
		FIRST_PRODUCAO[54] = "<";//<REPEXPSIMP>
		FIRST_PRODUCAO[55] = ">";//<REPEXPSIMP>
		FIRST_PRODUCAO[56] = ">=";//<REPEXPSIMP>
		FIRST_PRODUCAO[57] = "<=";//<REPEXPSIMP>
		FIRST_PRODUCAO[58] = "<>";//<REPEXPSIMP>
		FIRST_PRODUCAO[59] = "+";//<EXPSIMP>
		FIRST_PRODUCAO[60] = "-";//<EXPSIMP>
		FIRST_PRODUCAO[61] = "NOT IDENT INTEIRO (";//<EXPSIMP>::=<TERMO>...;
		FIRST_PRODUCAO[62] = "+";//<REPEXP>
		FIRST_PRODUCAO[63] = "-";//<REPEXP>
		FIRST_PRODUCAO[64] = "OR";//<REPEXP>
		FIRST_PRODUCAO[65] = "î";//<REPEXP>
		FIRST_PRODUCAO[66] = "NOT IDENT INTEIRO (";//<TERMO>::=<FATOR>...;
		FIRST_PRODUCAO[67] = "î";//<REPTERMO>
		FIRST_PRODUCAO[68] = "*";//<REPTERMO>
		FIRST_PRODUCAO[69] = "/";//<REPTERMO>
		FIRST_PRODUCAO[70] = "AND";//<REPTERMO>
		FIRST_PRODUCAO[71] = "INTEIRO";//<FATOR>
		FIRST_PRODUCAO[72] = "(";//<FATOR>
		FIRST_PRODUCAO[73] = "NOT";//<FATOR>
		FIRST_PRODUCAO[74] = "IDENTIFICADOR";//<IDENT>
		
		
		/**
		 * Tabela de DEVIVACAO para enviar para a pilha de analise de tras para frente atraves de um for reverso. 
		 */
		Object [] TABELA_DERIVACAO = new Object[90];
		TABELA_DERIVACAO[0]  = "";
		TABELA_DERIVACAO[1] = "22,19,178,17,47,18,179";
		TABELA_DERIVACAO[2] = "50,52,180,55,57";
		TABELA_DERIVACAO[3] = "19,182,49";
		TABELA_DERIVACAO[4] = "0";
		TABELA_DERIVACAO[5] = "16,19,182,49";
		TABELA_DERIVACAO[6] = "23,19,183,10,20,184,17,51";
		TABELA_DERIVACAO[7] = "0";
		TABELA_DERIVACAO[8] = "19,183,10,20,184,17,51";
		TABELA_DERIVACAO[9] = "0";
		TABELA_DERIVACAO[10] = "24,185,48,9,54,17,53";
		TABELA_DERIVACAO[11] = "0";
		TABELA_DERIVACAO[12] = "48,9,54,17,53";
		TABELA_DERIVACAO[13] = "0";
		TABELA_DERIVACAO[14] = "28";
		TABELA_DERIVACAO[15] = "25,19,186,56,17,187,47,17,188,55";
		TABELA_DERIVACAO[16] = "0";
		TABELA_DERIVACAO[17] = "0";
		TABELA_DERIVACAO[18] = "6,189,48,9,28,7";
		TABELA_DERIVACAO[19] = "26,59,58,27";
		TABELA_DERIVACAO[20] = "0";
		TABELA_DERIVACAO[21] = "17,59,58";
		TABELA_DERIVACAO[22] = "19,192,8,67,193";
		TABELA_DERIVACAO[23] = "57";
		TABELA_DERIVACAO[24] = "0";
		TABELA_DERIVACAO[25] = "29,19,194,60,195";
		TABELA_DERIVACAO[26] = "0";
		TABELA_DERIVACAO[27] = "6,67,196,61,7";
		TABELA_DERIVACAO[28] = "0";
		TABELA_DERIVACAO[29] = "16,67,196,61";
		TABELA_DERIVACAO[30] = "30,67,198,31,59,62,199";
		TABELA_DERIVACAO[31] = "0";
		TABELA_DERIVACAO[32] = "200,32,59";
		TABELA_DERIVACAO[33] = "33,201,67,202,34,59,203";
		TABELA_DERIVACAO[34] = "35,204,59,36,67,205";
		TABELA_DERIVACAO[35] = "37,206,6,63,64,7";
		TABELA_DERIVACAO[36] = "19,207";
		TABELA_DERIVACAO[37] = "0";
		TABELA_DERIVACAO[38] = "16,63,64";
		TABELA_DERIVACAO[39] = "38,6,65,66,7";
		TABELA_DERIVACAO[40] = "21,208";
		TABELA_DERIVACAO[41] = "67,209";
		TABELA_DERIVACAO[42] = "0";
		TABELA_DERIVACAO[43] = "16,65,66";
		TABELA_DERIVACAO[44] = "44,210,67,45,74,27,211"; 
		TABELA_DERIVACAO[45] = "20,76,9,212,59,213,75";
		TABELA_DERIVACAO[46] = "16,214,20,76";
		TABELA_DERIVACAO[47] = "0";
		TABELA_DERIVACAO[48] = "0";
		TABELA_DERIVACAO[49] = "17,74";
		TABELA_DERIVACAO[50] = "42,19,215,8,67,216,43,67,217,34,59,218";
		TABELA_DERIVACAO[51] = "69,68";
		TABELA_DERIVACAO[52] = "0";
		TABELA_DERIVACAO[53] = "10,69,219";
		TABELA_DERIVACAO[54] = "13,69,220";
		TABELA_DERIVACAO[55] = "11,69,221";
		TABELA_DERIVACAO[56] = "12,69,222";
		TABELA_DERIVACAO[57] = "14,69,223";
		TABELA_DERIVACAO[58] = "15,69,224";
		TABELA_DERIVACAO[59] = "2,71,70";
		TABELA_DERIVACAO[60] = "3,71,225,70";
		TABELA_DERIVACAO[61] = "71,70";
		TABELA_DERIVACAO[62] = "2,71,226,70";
		TABELA_DERIVACAO[63] = "3,71,227,70";
		TABELA_DERIVACAO[64] = "39,71,228,70";
		TABELA_DERIVACAO[65] = "0";
		TABELA_DERIVACAO[66] = "73,72";
		TABELA_DERIVACAO[67] = "0";
		TABELA_DERIVACAO[68] = "4,73,229,72";
		TABELA_DERIVACAO[69] = "5,73,230,72";
		TABELA_DERIVACAO[70] = "41,73,231,72";
		TABELA_DERIVACAO[71] = "20,232";
		TABELA_DERIVACAO[72] = "6,67,7";
		TABELA_DERIVACAO[73] = "40,73,233";
		TABELA_DERIVACAO[74] = "234,63";

		/**
		 * A Tabela de REGRA indicará qual linha será colocada na pilha de análise
		 * vinda tabela de DERIVACAO
		 * Primeiro colchetes do vetor é o código do simbolo
		 * Segundo colchetes do vetor é o código do token.
		 * ex: TABELA_REGRA[46][22]
		 * Equivale ao Simbolo 46 - <PROGRAMA> e ao token 22 - PROGRAM, resulta na regra de derivação 1.
		 */
		int [][] TABELA_REGRA = new int [78][46];
		
		TABELA_REGRA[46][Constants.t_PROGRAM]=1;
		TABELA_REGRA[47][Constants.t_CONST]=2;
		TABELA_REGRA[47][Constants.t_VAR]=2;
		TABELA_REGRA[47][Constants.t_PROCEDURE]=2;
		TABELA_REGRA[47][Constants.t_BEGIN]=2;
		TABELA_REGRA[48][Constants.t_IDENT]=3;
		TABELA_REGRA[49][Constants.t_TOKEN_DOIS_PONTOS]=4;
		TABELA_REGRA[49][Constants.t_TOKEN_VIRGULA]=5;
		TABELA_REGRA[50][Constants.t_CONST]=6;
		TABELA_REGRA[50][Constants.t_VAR]=9;
		TABELA_REGRA[50][Constants.t_PROCEDURE]=9;
		TABELA_REGRA[50][Constants.t_BEGIN]=9;
		TABELA_REGRA[51][Constants.t_IDENT]=8;
		TABELA_REGRA[51][Constants.t_VAR]=7;
		TABELA_REGRA[51][Constants.t_PROCEDURE]=7;
		TABELA_REGRA[51][Constants.t_BEGIN]=7;
		TABELA_REGRA[52][Constants.t_VAR]=10;
		TABELA_REGRA[52][Constants.t_PROCEDURE]=13;
		TABELA_REGRA[52][Constants.t_BEGIN]=13;
		TABELA_REGRA[53][Constants.t_IDENT]=12;
		TABELA_REGRA[53][Constants.t_PROCEDURE]=11;
		TABELA_REGRA[53][Constants.t_BEGIN]=11;
		TABELA_REGRA[54][Constants.t_INTEGER]=14;
		TABELA_REGRA[55][Constants.t_PROCEDURE]=15;
		TABELA_REGRA[55][Constants.t_BEGIN]=16;
		TABELA_REGRA[56][Constants.t_TOKEN_ABRE_PARENTESES]=18;
		TABELA_REGRA[56][Constants.t_TOKEN_PONTO_VIRGULA]=17;
		TABELA_REGRA[57][Constants.t_BEGIN]=19;
		TABELA_REGRA[58][Constants.t_END]=20;
		TABELA_REGRA[58][Constants.t_TOKEN_PONTO_VIRGULA]=21;
		TABELA_REGRA[59][Constants.t_IDENT]=22;
		TABELA_REGRA[59][Constants.t_BEGIN]=23;
		TABELA_REGRA[59][Constants.t_END]=24;
		TABELA_REGRA[59][Constants.t_CALL]=25;
		TABELA_REGRA[59][Constants.t_IF]=30;
		TABELA_REGRA[59][Constants.t_ELSE]=24;
		TABELA_REGRA[59][Constants.t_WHILE]=33;
		TABELA_REGRA[59][Constants.t_REPEAT]=34;
		TABELA_REGRA[59][Constants.t_UNTIL]=24;
		TABELA_REGRA[59][Constants.t_READLN]=35;
		TABELA_REGRA[59][Constants.t_WRITELN]=39;
		TABELA_REGRA[59][Constants.t_FOR]=50;
		TABELA_REGRA[59][Constants.t_CASE]=44;
		TABELA_REGRA[59][Constants.t_TOKEN_PONTO_VIRGULA]=24;
		TABELA_REGRA[60][Constants.t_END]=26;
		TABELA_REGRA[60][Constants.t_ELSE]=26;
		TABELA_REGRA[60][Constants.t_UNTIL]=26;
		TABELA_REGRA[60][Constants.t_TOKEN_ABRE_PARENTESES]=27;
		TABELA_REGRA[60][Constants.t_TOKEN_PONTO_VIRGULA]=26;
		TABELA_REGRA[61][Constants.t_TOKEN_FECHA_PARENTESES]=28;
		TABELA_REGRA[61][Constants.t_TOKEN_VIRGULA]=29;
		TABELA_REGRA[62][Constants.t_END]=31;
		TABELA_REGRA[62][Constants.t_ELSE]=32;
		TABELA_REGRA[62][Constants.t_UNTIL]=31;
		TABELA_REGRA[62][Constants.t_TOKEN_PONTO_VIRGULA]=31;
		TABELA_REGRA[63][Constants.t_IDENT]=36;
		TABELA_REGRA[63][Constants.t_LITERAL]=40; 
		TABELA_REGRA[64][Constants.t_TOKEN_FECHA_PARENTESES]=37;
		TABELA_REGRA[64][Constants.t_TOKEN_VIRGULA]=38;
		TABELA_REGRA[65][Constants.t_IDENT]=41;
		TABELA_REGRA[65][Constants.t_NOT]=41;
		TABELA_REGRA[65][Constants.t_INTEIRO]=41;
		TABELA_REGRA[65][Constants.t_TOKEN_ADICAO]=41;
		TABELA_REGRA[65][Constants.t_TOKEN_SUBTRACAO]=41;
		TABELA_REGRA[65][Constants.t_TOKEN_ABRE_PARENTESES]=41;
		TABELA_REGRA[65][Constants.t_LITERAL]=40; 
		TABELA_REGRA[66][Constants.t_TOKEN_FECHA_PARENTESES]=42;
		TABELA_REGRA[66][Constants.t_TOKEN_PONTO_VIRGULA]=43;
		TABELA_REGRA[67][Constants.t_IDENT]=51;
		TABELA_REGRA[67][Constants.t_NOT]=51;
		TABELA_REGRA[67][Constants.t_INTEIRO]=51;
		TABELA_REGRA[67][Constants.t_TOKEN_ADICAO]=51;
		TABELA_REGRA[67][Constants.t_TOKEN_SUBTRACAO]=51;
		TABELA_REGRA[67][Constants.t_TOKEN_ABRE_PARENTESES]=51;
		TABELA_REGRA[68][Constants.t_END]=52;
		TABELA_REGRA[68][Constants.t_OF]=52;
		TABELA_REGRA[68][Constants.t_THEN]=52;
		TABELA_REGRA[68][Constants.t_ELSE]=52;
		TABELA_REGRA[68][Constants.t_DO]=52;
		TABELA_REGRA[68][Constants.t_UNTIL]=52;
		TABELA_REGRA[68][Constants.t_TO]=52;
		TABELA_REGRA[68][Constants.t_TOKEN_FECHA_PARENTESES]=52;
		TABELA_REGRA[68][Constants.t_TOKEN_IGUAL]=53;
		TABELA_REGRA[68][Constants.t_TOKEN_MAIOR]=55;
		TABELA_REGRA[68][Constants.t_TOKEN_MAIOR_IGUAL]=56;
		TABELA_REGRA[68][Constants.t_TOKEN_MENOR]=54;
		TABELA_REGRA[68][Constants.t_TOKEN_MENOR_IGUAL]=57;
		TABELA_REGRA[68][Constants.t_TOKEN_DIFERENTE]=58;
		TABELA_REGRA[68][Constants.t_TOKEN_VIRGULA]=52;
		TABELA_REGRA[68][Constants.t_TOKEN_PONTO_VIRGULA]=52;
		TABELA_REGRA[69][Constants.t_IDENT]=61;
		TABELA_REGRA[69][Constants.t_NOT]=61;
		TABELA_REGRA[69][Constants.t_INTEIRO]=61;
		TABELA_REGRA[69][Constants.t_TOKEN_ADICAO]=59;
		TABELA_REGRA[69][Constants.t_TOKEN_SUBTRACAO]=60;
		TABELA_REGRA[69][Constants.t_TOKEN_ABRE_PARENTESES]=61;
		TABELA_REGRA[70][Constants.t_END]=65;
		TABELA_REGRA[70][Constants.t_OF]=65;
		TABELA_REGRA[70][Constants.t_THEN]=65;
		TABELA_REGRA[70][Constants.t_ELSE]=65;
		TABELA_REGRA[70][Constants.t_DO]=65;
		TABELA_REGRA[70][Constants.t_UNTIL]=65;
		TABELA_REGRA[70][Constants.t_OR]=64;
		TABELA_REGRA[70][Constants.t_TO]=65;
		TABELA_REGRA[70][Constants.t_TOKEN_ADICAO]=62;
		TABELA_REGRA[70][Constants.t_TOKEN_SUBTRACAO]=63;
		TABELA_REGRA[70][Constants.t_TOKEN_FECHA_PARENTESES]=65;
		TABELA_REGRA[70][Constants.t_TOKEN_IGUAL]=65;
		TABELA_REGRA[70][Constants.t_TOKEN_MAIOR]=65;
		TABELA_REGRA[70][Constants.t_TOKEN_MAIOR_IGUAL]=65;
		TABELA_REGRA[70][Constants.t_TOKEN_MENOR]=65;
		TABELA_REGRA[70][Constants.t_TOKEN_MENOR_IGUAL]=65;
		TABELA_REGRA[70][Constants.t_TOKEN_DIFERENTE]=65;
		TABELA_REGRA[70][Constants.t_TOKEN_VIRGULA]=65;
		TABELA_REGRA[70][Constants.t_TOKEN_PONTO_VIRGULA]=65;
		TABELA_REGRA[71][Constants.t_IDENT]=66;
		TABELA_REGRA[71][Constants.t_NOT]=66;
		TABELA_REGRA[71][Constants.t_INTEIRO]=66;
		TABELA_REGRA[71][Constants.t_TOKEN_ABRE_PARENTESES]=66;
		TABELA_REGRA[72][Constants.t_END]=67;
		TABELA_REGRA[72][Constants.t_OF]=67;
		TABELA_REGRA[72][Constants.t_THEN]=67;
		TABELA_REGRA[72][Constants.t_ELSE]=67;
		TABELA_REGRA[72][Constants.t_DO]=67;
		TABELA_REGRA[72][Constants.t_UNTIL]=67;
		TABELA_REGRA[72][Constants.t_OR]=67;
		TABELA_REGRA[72][Constants.t_AND]=70;
		TABELA_REGRA[72][Constants.t_TO]=67;
		TABELA_REGRA[72][Constants.t_TOKEN_ADICAO]=67;
		TABELA_REGRA[72][Constants.t_TOKEN_SUBTRACAO]=67;
		TABELA_REGRA[72][Constants.t_TOKEN_MULTIPLICACAO]=68;
		TABELA_REGRA[72][Constants.t_TOKEN_DIVISAO]=69;
		TABELA_REGRA[72][Constants.t_TOKEN_FECHA_PARENTESES]=67;
		TABELA_REGRA[72][Constants.t_TOKEN_IGUAL]=67;
		TABELA_REGRA[72][Constants.t_TOKEN_MAIOR]=67;
		TABELA_REGRA[72][Constants.t_TOKEN_MAIOR_IGUAL]=67;
		TABELA_REGRA[72][Constants.t_TOKEN_MENOR]=67;
		TABELA_REGRA[72][Constants.t_TOKEN_MENOR_IGUAL]=67;
		TABELA_REGRA[72][Constants.t_TOKEN_DIFERENTE]=67;
		TABELA_REGRA[72][Constants.t_TOKEN_VIRGULA]=67;
		TABELA_REGRA[72][Constants.t_TOKEN_PONTO_VIRGULA]=67;
		TABELA_REGRA[73][Constants.t_IDENT]=74;
		TABELA_REGRA[73][Constants.t_NOT]=73;
		TABELA_REGRA[73][Constants.t_INTEIRO]=71;
		TABELA_REGRA[73][Constants.t_TOKEN_ABRE_PARENTESES]=72;
		TABELA_REGRA[74][Constants.t_INTEIRO]=45;
		TABELA_REGRA[75][Constants.t_END]=48;
		TABELA_REGRA[75][Constants.t_TOKEN_PONTO_VIRGULA]=49;
		TABELA_REGRA[76][Constants.t_TOKEN_DOIS_PONTOS]=47;
		TABELA_REGRA[76][Constants.t_TOKEN_VIRGULA]=46;

		int auxiliar1 = 0;
		for (int i=primeiroNaoTerminal;i<primeiroSemantico;i++){
			for (int j=1;j<primeiroNaoTerminal-1;j++){
				auxiliar1=0;
				auxiliar1 = TABELA_REGRA[i][j];
				if (auxiliar1 == 0)
					TABELA_REGRA[i][j]=0;
			}
		}
		
		if (tableTokens.size() > 0) {
			
			AnalisadorSemantico analisadorSemantico = new AnalisadorSemantico();
			/**
			 * Adiciona cifrão ao topo da pilha
			 */
			PILHA_AS.add(Constants.DOLLAR);
			/**
			 * adiciona <PROGRAMA> ao topo da pilha
			 */
			PILHA_AS.add(primeiroNaoTerminal);

			while (verificarErroSintatico == false){
				
				if (p > 0) {
					tokenAnterior = findToken(tableTokens, p-1, 2).toString();				
				}

				codigoTokenAtual = Integer.parseInt(findToken(tableTokens,p,1).toString());
				tokenAtual = findToken(tableTokens,p,2).toString();
				lnErro = findToken(tableTokens,p,0).toString();
				
				if (Integer.parseInt(findToken(tableTokens, p, 1).toString()) == 99) {
					/**
					 * Caso o token seja um comentário, ele descarta e passa adiante.
					 */
					p++;
					codigoTokenAtual = Integer.parseInt(findToken(tableTokens,p,1).toString());
					tokenAtual = findToken(tableTokens,p,2).toString();
				}
				
				if (Integer.parseInt(findToken(tableTokens, p, 1).toString()) != 99) {

					/**
					 * Verifica não terminal
					 */
					
					if(findTopo(PILHA_AS) >= primeiroNaoTerminal && findTopo(PILHA_AS) <= primeiroSemantico) {

						if (TABELA_REGRA[findTopo(PILHA_AS)][codigoTokenAtual] > 0) {
							if (!TABELA_DERIVACAO[TABELA_REGRA[findTopo(PILHA_AS)][codigoTokenAtual]].toString().equals("0")) {
								/**
								 * monta inverso em array separado pelos espaços em branco, para colocar na pilha inversamente.
								 */
								inverso = TABELA_DERIVACAO[TABELA_REGRA[findTopo(PILHA_AS)][codigoTokenAtual]].toString().split(",");

								/**
								 * Remove do topo da pilha a regra a ser derivada;
								 */
								
								removeElement(PILHA_AS);

								for(int i = inverso.length-1; i >= 0; i--) {

									addElement(PILHA_AS,Integer.parseInt(inverso[i]));//precisa de dois parâmetros, Pilha e Elemento
								}
								
							} else if ("0".equals(TABELA_DERIVACAO[TABELA_REGRA[findTopo(PILHA_AS)][codigoTokenAtual]].toString())) {
								/**
								 * Remove do topo da pilha, regra leva a cadeia vazia;
								 */
								removeElement(PILHA_AS);
							
							}
						} else if (TABELA_REGRA[findTopo(PILHA_AS)][codigoTokenAtual] == 0) {
							
							String auxiliar = " ";
							int topo = findTopo(PILHA_AS);
							
							/**
							 * Percorre TABELA_REGRA a procura de regras para erros
							 */
							for (int i = 0; i < primeiroNaoTerminal-1; i++) {
								if(TABELA_REGRA[topo][i] != 0 && !FIRST_PRODUCAO[TABELA_REGRA[topo][i]].equals("î")) {
									auxiliar += "["+FIRST_PRODUCAO[TABELA_REGRA[topo][i]] + "] ";
								}
							}
							getListaErrosSintaticosSemanticos().add(new ErroSintaticoSemantico(Integer.valueOf(findToken(tableTokens, p, 0)), auxiliar, tokenAtual));
							
						}
					}

					/**
					 * VERIFICA TERMINAL
					 */
					if (findTopo(PILHA_AS) >= 2 && findTopo(PILHA_AS) < 46){
						if (findTopo(PILHA_AS) == codigoTokenAtual){
							/**
							 * Remove do topo da pilha;
							 */
							removeElement(PILHA_AS);
							/**
							 * Incrementa índice para busca de tokens;
							 */
							p++;

						} else {
							getListaErrosSintaticosSemanticos().add(new ErroSintaticoSemantico(Integer.valueOf(findToken(tableTokens, p, 0)), SIMBOLOS[findTopo(PILHA_AS)], tokenAtual));
						}
					}
					
					/**
					 * SE FOR AÇÃO SEMÂNTICA
					 */
					
					if (findTopo(PILHA_AS) >= 100 && findTopo(PILHA_AS) < 236) {
						/**
						 * Remove do topo da pilha a ação semântica, o erro é detectado na ação;
						 */

						if (isSemantico) {
							
							int acaoSemantica = findTopo(PILHA_AS);
							acaoSemantica -= primeiroSemantico;

							getListaSimbolos().removeAll(getListaSimbolos());
							getListaLiteral().removeAll(getListaLiteral());
							getListaOperacoes().removeAll(getListaOperacoes());
							
							analisadorSemantico.acaoSemantica(acaoSemantica, codigoTokenAtual, tokenAtual, tokenAnterior, lnErro);
							getListaErrosSintaticosSemanticos().addAll(analisadorSemantico.getListaErrosSintaticosSemanticos());
						    getListaSimbolos().addAll(analisadorSemantico.getListaSimbolos());
						    getListaLiteral().addAll(analisadorSemantico.getListaLiteral());
						    getListaOperacoes().addAll(analisadorSemantico.getListaOperacoes());
						    
						}
						removeElement(PILHA_AS);
					}
				}
				
				if (getListaErrosSintaticosSemanticos().size() > 0 ) {
					verificarErroSintatico = true;
					JOptionPane.showMessageDialog(null, "A análise encontrou algum erro. Favor verifique a aba de Erros.", "Análise Finalizada com Erros",JOptionPane.ERROR_MESSAGE);
					break;
				}
				
				if (p == tableTokens.size() || findTopo(PILHA_AS) == Constants.DOLLAR){
					verificarErroSintatico = true;
					break;
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "Tabela de tokens vazia!", "Análise Não executada",JOptionPane.WARNING_MESSAGE);
		}
		
	}	

	public String findToken(List<Token> tableTokens, int linha, int coluna) {
		
		String token = "";
		if (coluna == 0) {
			token = String.valueOf(tableTokens.get(linha).getLinha());
		} else if (coluna == 1) {
			token = String.valueOf(tableTokens.get(linha).getCodigo());
		} else if (coluna == 2) {
			token = tableTokens.get(linha).getToken();
		}
		
		return token;
	}
	
	public void addElement(ArrayList<Integer> pilha, int elemento){
		pilha.add(elemento);
	}
	public void removeElement(ArrayList<Integer> pilha){
		pilha.remove(pilha.size()-1);
	}
	public int findTopo(ArrayList<Integer> pilha){
		return pilha.get(pilha.size()-1);
	}
    

	public List<Operacoes> getListaOperacoes() {
		if (listaOperacoes == null) {
			listaOperacoes = new ArrayList<Operacoes>();
		}
		return listaOperacoes;
	}

	public void setListaOperacoes(List<Operacoes> listaOperacoes) {
		this.listaOperacoes = listaOperacoes;
	}

	public List<Literal> getListaLiteral() {
		if (listaLiteral == null) {
			listaLiteral = new ArrayList<Literal>();
		}
		return listaLiteral;
	}

	public void setListaLiteral(List<Literal> listaLiteral) {
		this.listaLiteral = listaLiteral;
	}
	public boolean isVazia(ArrayList<Integer> pilha) {
		if (pilha.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isCheia(ArrayList<Integer> pilha) {
		if (pilha.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<Simbolo> getListaSimbolos() {
		if (listaSimbolos == null) {
			listaSimbolos = new ArrayList<Simbolo>();
		}
		return listaSimbolos;
	}

	public void setListaSimbolos(List<Simbolo> listaSimbolos) {
		this.listaSimbolos = listaSimbolos;
	}
	public List<ErroSintaticoSemantico> getListaErrosSintaticosSemanticos() {
		if (listaErrosSintaticosSemanticos == null) {
			listaErrosSintaticosSemanticos = new ArrayList<ErroSintaticoSemantico>();
		}
		return listaErrosSintaticosSemanticos;
	}

	public void setListaErrosSintaticosSemanticos(List<ErroSintaticoSemantico> listaErrosSintaticosSemanticos) {
		this.listaErrosSintaticosSemanticos = listaErrosSintaticosSemanticos;
	}
	
}