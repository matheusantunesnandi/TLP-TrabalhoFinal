package br.unisul.compiladores.service;

import java.util.ArrayList;
import java.util.List;

import br.unisul.compiladores.odt.ErroSintaticoSemantico;
import br.unisul.compiladores.odt.Literal;
import br.unisul.compiladores.odt.Operacoes;
import br.unisul.compiladores.odt.Simbolo;
import br.unisul.compiladores.odt.TabelaSimbolos;

public class AnalisadorSemantico {

	/**
	 * VARIÁVEIS PARA AÇÃO SEMANTICA Autores: Renato Heinzen, Jonatha Marques
	 */
	private int nivelAtual = 0;
	private int lc = 0;
	private int literal = 0;
	private int deslocamento = 0;
	private int numeroVar = 0; // Número de variáveis
	private int numeroPar = 0; // Número de parâmetros
	private int tipo_identificador = 0; // Pode ser (VARIÁVEL = 1, CONSTANTE = 2, PROCEDURE = 3, RÓTULO = 4,PARÂMETRO =
										// 5);
	private String categoria; // Pode ser VAR, CONST, PROC, ROT, PAR
	private int contexto; // Pode ser (READLN = 1, EXPRESSAO = 2)
	private int endereco_da_constante; // Salva endereço da constante para depois atualizar
	private int nivelProcedure; // Salva nivel atual de procedure

	private boolean houve_par = false; // Salva se houve parametro
	private String nome_identificador; // Salva nome do identificador
	private String nome_identificador_proc; // Salva nome do identificador PROC
	private String nome_identificador_for; // Salva nome do identificador FOR
	private String parte_esquerda; // Salva nome do identificador que recebe a atribuição

	private TabelaSimbolos tabelaSimbolos;

	/**
	 * Pilhas para a analise semântica
	 */
	private ArrayList<Integer> pl_while_dsvs;
	private ArrayList<Integer> pl_while_dsvf;
	private ArrayList<Integer> pl_procedure;
	private ArrayList<Integer> pl_repeat;
	private ArrayList<Integer> pl_procedure_npar;
	private ArrayList<Integer> pl_procedure_retu;
	private ArrayList<Integer> pl_procedure_call;
	private ArrayList<Integer> pl_case_dsvf;
	private ArrayList<Integer> pl_if;
	private ArrayList<Integer> pl_case_dsvt;
	private ArrayList<Integer> pl_case_dsvs;
	private ArrayList<Integer> pl_for_dsvf;
	private ArrayList<Integer> pl_for_controle;
	private ArrayList<Integer> pl_for_dsvs;
	
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

	public void acaoSemantica(int ACAO, int cdTokenAtual, String tokenAtual, String tokenAnterior, String lnErro) {

		// Ajustar para usar nossos procedimentos de busca e inserção na tabela de
		// simbolos.
		
		int linha = 0;
		Object local = null;
		lc = getListaOperacoes().size();

		switch (ACAO) {
		case 100:

			tabelaSimbolos = new TabelaSimbolos();
			tabelaSimbolos.iniciaListaSimbolo();

			getListaSimbolos().removeAll(getListaSimbolos());

			numeroVar = 0;
			nivelAtual = 0; // Controla o nível atual
			lc = 1; // Aponta para próxima instrução a ser gerada
			literal = 0; // Ponteiro para área de literais inicia de 0 (zero)
			deslocamento = 3; // Seta deslocamento = 3 em relação a base

			// LIMPAR AS PILHAS
			pl_if = new ArrayList<Integer>();
			pl_while_dsvs = new ArrayList<Integer>();
			pl_while_dsvf = new ArrayList<Integer>();
			pl_repeat = new ArrayList<Integer>();
			pl_procedure = new ArrayList<Integer>();
			pl_procedure_npar = new ArrayList<Integer>();
			pl_procedure_call = new ArrayList<Integer>();
			pl_procedure_retu = new ArrayList<Integer>();
			pl_case_dsvf = new ArrayList<Integer>();
			pl_case_dsvs = new ArrayList<Integer>();
			pl_case_dsvt = new ArrayList<Integer>();
			pl_for_dsvf = new ArrayList<Integer>();
			pl_for_dsvs = new ArrayList<Integer>();
			pl_for_controle = new ArrayList<Integer>();

			break;
		case 101: // PARA
			getListaOperacoes().add(new Operacoes(lc, "PARA", "-", "-", "Fim de programa"));
			if (isCheia(pl_procedure) || isCheia(pl_procedure_retu)) {
				// getTableLog().add(new String("Procedures, ou rótulos existentes sem
				// chamada."));
				getListaErrosSintaticosSemanticos().add(new ErroSintaticoSemantico(Integer.valueOf(lnErro),
						"Procedures, ou rótulos existentes sem chamada."));
			}
			break;
		case 102:
			getListaOperacoes().add(new Operacoes(lc, "AMEM", "-", String.valueOf(numeroVar + deslocamento), ""));
			numeroVar = 0;
			break;
		case 104:
			switch (tipo_identificador) {
			case 1: // CASO SEJA UMA VARIÁVEL
				categoria = "VAR";

				if (SimboloService.buscarSimboloTabelaPorNomeNivelCategoria(tokenAtual, nivelAtual, categoria,
						tabelaSimbolos) == null) {

					Simbolo simbolo = SimboloService.criarSimbolo(getListaSimbolos().size(), tokenAtual, nivelAtual,
							categoria, String.valueOf(numeroVar + deslocamento), "-");
					SimboloService.adicionarSimboloTabela(simbolo, tabelaSimbolos);
					getListaSimbolos().add(simbolo);
					numeroVar++;

				} else {

					System.out.println("token... " + tokenAtual + " linha  " + lnErro);
					getListaErrosSintaticosSemanticos().add(new ErroSintaticoSemantico(Integer.valueOf(lnErro),
							"Erro semântico: " + categoria + ": " + tokenAtual + " -> Já declarado."));
					break;

				}
				break;
			case 5: // CASO SEJA UM PARÂMETRO
				categoria = "PAR";

				if (SimboloService.buscarSimboloTabelaPorNomeNivelCategoria(tokenAtual, nivelAtual, categoria,
						tabelaSimbolos) == null) {

					numeroPar++; // AUMENTA NÚMERO DE PARAMETROS
					Simbolo simbolo = SimboloService.criarSimbolo(getListaSimbolos().size(), tokenAtual, nivelAtual,
							categoria, String.valueOf(nivelProcedure - numeroPar), "-");
					SimboloService.adicionarSimboloTabela(simbolo, tabelaSimbolos);
					getListaSimbolos().add(simbolo);

				} else {

					getListaErrosSintaticosSemanticos().add(new ErroSintaticoSemantico(Integer.valueOf(lnErro),
							"Erro semântico: " + categoria + ": " + tokenAtual + " -> Já declarado."));
					break;

				}

				break;
			}
			break;
		case 105:
			categoria = "CONST";

			if (SimboloService.buscarSimboloTabelaPorNomeNivelCategoria(tokenAtual, nivelAtual, categoria,
					tabelaSimbolos) == null) {
				endereco_da_constante = getListaSimbolos().size();
				Simbolo simbolo = SimboloService.criarSimbolo(getListaSimbolos().size(), tokenAtual, nivelAtual,
						categoria, String.valueOf(numeroVar + deslocamento), "-");
				SimboloService.adicionarSimboloTabela(simbolo, tabelaSimbolos);
				getListaSimbolos().add(simbolo);
				numeroVar++;

			} else {

				getListaErrosSintaticosSemanticos().add(new ErroSintaticoSemantico(Integer.valueOf(lnErro),
						"Erro semântico: " + categoria + ": " + tokenAtual + " -> Já declarado."));
				break;

			}

			break;
		case 106:
			getListaSimbolos().get(endereco_da_constante).setGeralB(tokenAtual);
			break;
		case 107:
			tipo_identificador = 1; // VAR
			break;
		case 108: // PROCEDURE
			categoria = "PROC";
			nome_identificador_proc = tokenAtual;

			if (SimboloService.buscarSimboloTabelaPorNomeNivelCategoria(tokenAtual, nivelAtual, categoria,
					tabelaSimbolos) == null) {
				pl_procedure_npar.add(getListaSimbolos().size());
				Simbolo simbolo = SimboloService.criarSimbolo(getListaSimbolos().size(), tokenAtual, nivelAtual,
						categoria, String.valueOf(lc + 1), "?");
				SimboloService.adicionarSimboloTabela(simbolo, tabelaSimbolos);
				getListaSimbolos().add(simbolo);

			} else {

				getListaErrosSintaticosSemanticos().add(new ErroSintaticoSemantico(Integer.valueOf(lnErro),
						"Erro semântico: " + categoria + ": " + tokenAtual + " -> Já declarado."));
				break;

			}

			houve_par = false;
			numeroPar = 0;
			break;
		case 109: // PROCEDURE
			pl_procedure.add(getListaOperacoes().size()); // INSTRUÇÃO DE DESVIO
			getListaOperacoes().add(new Operacoes(lc, "DSVS", "-", "?",
					"Procedure " + nome_identificador_proc + "(nPar: " + numeroPar + ")"));

			if (houve_par == true) {
				getListaSimbolos().get(findTopo(pl_procedure_npar)).setGeralB("" + numeroPar);
				removeElement(pl_procedure_npar);
			}
			nivelAtual++;
			break;
		case 110: // PROCEDURE

			if (SimboloService.buscarSimboloTabelaPorNome(nome_identificador_proc,
					SimboloService.buscarSimboloTabelaPorCategoria("PROC", tabelaSimbolos)) != null) {

				linha = (SimboloService.buscarSimboloTabelaPorNomeNivelCategoria(nome_identificador_proc,
						nivelProcedure, "PROC", tabelaSimbolos)).getSeq(); // RETORNA O VALOR DA CHAVE

				local = getListaSimbolos().get(linha).getGeralA();

				getListaOperacoes().add(new Operacoes(lc, "RETU", "-", "?", "Retorna para (?)")); // O RETU É GERADO COM
																									// +1 PQ INICIA DE 0
																									// A TABELA
				addElement(pl_procedure_retu, lc);
				getListaOperacoes().get(findTopo(pl_procedure)).setOp2(String.valueOf(lc + 1));
				getListaSimbolos().get(linha).setGeralB(String.valueOf(numeroPar));

				for (int i = getListaSimbolos().size() - 1; i > linha; i--) { // REMOVE DA TABELA DE SIMBOLOS, ELEMENTOS
																				// NÃO MAIS NECESSÁRIOS

					SimboloService.removerSimboloTabela(i, tabelaSimbolos);

					getListaSimbolos().remove(i);
				}
			}

			nivelAtual--;
			removeElement(pl_procedure);
			numeroPar = 0;
			break;
		case 111: // PROCEDURE
			tipo_identificador = 5; // PARÂMETRO
			houve_par = true;
			nivelProcedure = nivelAtual;
			break;
		case 114:
			categoria = "VAR";

			if (SimboloService.buscarSimboloTabelaPorNome(tokenAtual,
					SimboloService.buscarSimboloTabelaPorCategoria(categoria, tabelaSimbolos)) != null) {
				parte_esquerda = tokenAtual;
			} else {
				getListaErrosSintaticosSemanticos().add(new ErroSintaticoSemantico(Integer.valueOf(lnErro),
						"Erro semântico: " + categoria + ": " + tokenAtual + " -> Não declarado."));

				break;
			}
			break;
		case 115: // APÓS ATRIBUIÇÃO

			Simbolo simboloVar = SimboloService.buscarSimboloTabelaPorNome(parte_esquerda,
					SimboloService.buscarSimboloTabelaPorCategoria("VAR", tabelaSimbolos));

			Simbolo simboloPar = SimboloService.buscarSimboloTabelaPorNome(parte_esquerda,
					SimboloService.buscarSimboloTabelaPorCategoria("PAR", tabelaSimbolos));
			if (simboloVar != null) {

				local = simboloVar.getGeralA();
				getListaOperacoes().add(new Operacoes(lc, "ARMZ", String.valueOf(nivelAtual), String.valueOf(local),
						"Armazena em (" + parte_esquerda + ")"));
			}
			if (simboloPar != null) {
				local = simboloPar.getGeralA();
				getListaOperacoes().add(new Operacoes(lc, "ARMZ", String.valueOf(nivelAtual), String.valueOf(local),
						"Armazena em (" + parte_esquerda + ")"));
			}
			break;
		case 116: // CHAMADA DE PROCEDURE - CALL
			nome_identificador_proc = tokenAtual;

			if (SimboloService.buscarSimboloTabelaPorNome(tokenAtual,
					SimboloService.buscarSimboloTabelaPorCategoria("PROC", tabelaSimbolos)) != null) {
				addElement(pl_procedure_call, lc);
			} else {
				getListaErrosSintaticosSemanticos().add(new ErroSintaticoSemantico(Integer.valueOf(lnErro),
						"Erro semântico: PROC: " + tokenAtual + " -> Não declarado."));
				// getTableLog().add(new String("Verifique linhas acima"));
				break;
			}
			break;
		case 117: // APÓS CALL

			Simbolo simboloPar117 = SimboloService.buscarSimboloTabelaPorNome(nome_identificador_proc,
					SimboloService.buscarSimboloTabelaPorCategoria("PROC", tabelaSimbolos));

			if (simboloPar117 != null) {

				if (numeroPar != Integer.parseInt(simboloPar117.getGeralB())) {
					getListaErrosSintaticosSemanticos()
							.add(new ErroSintaticoSemantico(Integer.valueOf(lnErro), "Erro semântico: PROC: "
									+ nome_identificador_proc + " -> Número de parâmetros incorreto."));
					break;
				} else {
					getListaOperacoes().add(new Operacoes(lc, "CALL", "0", simboloPar117.getGeralA(),
							"CALL " + nome_identificador_proc));
					getListaOperacoes().get(findTopo(pl_procedure_retu)).setOp1(String.valueOf(lc + 1));
					getListaOperacoes().get(findTopo(pl_procedure_retu)).setOp2(String.valueOf(lc + 1));
					getListaOperacoes().get(findTopo(pl_procedure_retu))
							.setObservacao("Retorna para (" + (lc + 1) + ")");
					removeElement(pl_procedure_retu);
					removeElement(pl_procedure_call);
				}
			}

			break;
		case 118: // APÓS EXPRESSÃO EM COMANDO CALL, ACUMULA NÚMERO DE PARÂMETROS PARA COMPARAR
					// COM TABELA DE SIMBOLOS
			numeroPar++;
			break;
		case 120: // IF
			getListaOperacoes().add(new Operacoes(lc, "DSVF", "-", "?", "IF"));
			addElement(pl_if, lc);
			break;
		case 121: // IF
			getListaOperacoes().get(findTopo(pl_if)).setOp2(String.valueOf(lc));
			break;
		case 122: // IF (APÓS DOMINIO THEN ANTES DO ELSE)
			getListaOperacoes().get(findTopo(pl_if)).setOp2(String.valueOf(lc + 1));
			removeElement(pl_if);
			addElement(pl_if, lc);
			getListaOperacoes().add(new Operacoes(lc, "DSVS", "-", "?", "ELSE"));
			break;
		case 123: // WHILE ANTES DA EXPRESSÃO
			addElement(pl_while_dsvs, lc); // INSERE NA PILHA DE RETORNO, DSVS
			break;
		case 124: // WHILE DEPOIS DA EXPRESSÃO
			addElement(pl_while_dsvf, lc); // INSERE NA PILHA DO WHILE, DSVF
			getListaOperacoes().add(new Operacoes(lc, "DSVF", "-", "?", "WHILE"));
			break;
		case 125: // WHILE APÓS COMANDO
			getListaOperacoes().get(findTopo(pl_while_dsvf)).setOp2(String.valueOf(lc + 1));
			removeElement(pl_while_dsvf);
			getListaOperacoes().add(new Operacoes(lc, "DSVS", "-", String.valueOf(findTopo(pl_while_dsvs)), "Retorno WHILE"));
			removeElement(pl_while_dsvs);
			break;
		case 126: // REPEAT
			addElement(pl_repeat, lc);
			break;
		case 127: // REPEAT
			getListaOperacoes()
					.add(new Operacoes(lc, "DSVF", "-", String.valueOf(findTopo(pl_repeat)), "Retorno REPEAT"));
			removeElement(pl_repeat);
			break;
		case 128:
			contexto = 1; // READLN
			break;
		case 129:

			Simbolo simboloVar129 = SimboloService.buscarSimboloTabelaPorNome(tokenAtual,
					SimboloService.buscarSimboloTabelaPorCategoria("VAR", tabelaSimbolos));

			Simbolo simboloPar129 = SimboloService.buscarSimboloTabelaPorNome(tokenAtual,
					SimboloService.buscarSimboloTabelaPorCategoria("PAR", tabelaSimbolos));

			Simbolo simboloConst129 = SimboloService.buscarSimboloTabelaPorNome(tokenAtual,
					SimboloService.buscarSimboloTabelaPorCategoria("CONST", tabelaSimbolos));

			switch (contexto) {

			case 1: // CONTEXTO = READLN
				categoria = "VAR";

				if (simboloVar129 != null) {
					getListaOperacoes().add(new Operacoes(lc, "LEIT", "-", "-", "Leitura (" + tokenAtual + ")"));
					lc++;
					linha = simboloVar129.getSeq();
					local = simboloVar129.getGeralA();
					getListaOperacoes()
							.add(new Operacoes(lc, "ARMZ", String.valueOf(nivelAtual - simboloVar129.getNivel()),
									String.valueOf(local), "Armazena (" + tokenAtual + ")"));
				} else if (simboloPar129 != null) {
					getListaOperacoes().add(new Operacoes(lc, "LEIT", "-", "-", "Leitura (" + tokenAtual + ")"));
					lc++;
					linha = simboloPar129.getSeq();
					local = simboloPar129.getGeralA();
					getListaOperacoes().add(new Operacoes(lc, "ARMZ", String.valueOf(simboloPar129.getNivel()),
							String.valueOf(local), "Armazena (" + tokenAtual + ")"));
				} else {
					getListaErrosSintaticosSemanticos().add(new ErroSintaticoSemantico(Integer.valueOf(lnErro),
							"Erro semântico: " + categoria + ": " + tokenAtual + " -> Não declarado."));

				}
				break;
			case 2: // CONTEXTO = EXPRESSAO

				if (simboloConst129 != null) {
					local = simboloConst129.getGeralA();
					getListaOperacoes().add(new Operacoes(lc, "CRCT", String.valueOf(simboloConst129.getNivel()),
							String.valueOf(local), "Carrega (" + tokenAtual + ")"));
				} else if (simboloVar129 != null) {
					local = simboloVar129.getGeralA();
					getListaOperacoes().add(new Operacoes(lc, "CRVL", String.valueOf(simboloVar129.getNivel()),
							String.valueOf(local), "Carrega (" + tokenAtual + ")"));
				} else if (simboloPar129 != null) {
					local = simboloPar129.getGeralA();
					getListaOperacoes().add(new Operacoes(lc, "CRVL", String.valueOf(simboloPar129.getNivel()),
							String.valueOf(local), "Carrega (" + tokenAtual + ")"));
				} else {
					getListaErrosSintaticosSemanticos().add(new ErroSintaticoSemantico(Integer.valueOf(lnErro),
							"Erro semântico: " + categoria + ": " + tokenAtual + " -> Não declarado."));

				}
				break;
			}
			break;
		case 130:
			getListaOperacoes().add(new Operacoes(lc, "IMPRL", "-", String.valueOf(getListaLiteral().size()),
					"Imprime (" + tokenAtual + ")"));
			lc++;
			getListaLiteral().add(new Literal(literal, tokenAtual));
			literal++;
			break;
		case 131:
			getListaOperacoes().add(new Operacoes(lc, "IMPR", "-", "-", "Imprime topo - 1"));
			break;
		case 132: // CASE APÓS PALAVRA RESERVADA CASE
			// addElement(PILHA_CASE_DSVF,LC);
			break;
		case 133: // CASE APÓS COMANDO CASE
			while (isCheia(pl_case_dsvs)) { // RESOLVE SE HOUVER PENDÊNCIAS DE DESVIOS PARA DSVT
				getListaOperacoes().get(findTopo(pl_case_dsvs)).setOp2(String.valueOf(lc));
				removeElement(pl_case_dsvs);
			}
			getListaOperacoes().add(new Operacoes(lc, "AMEM", "-", "-1", "AMEM -1 (limpa)"));
			break;
		case 134: // CASE RAMO DO CASE APÓS INTEIRO, ÚLTIMO DA LISTA
			getListaOperacoes().add(new Operacoes(lc, "COPI", "-", "-", "Duplica topo(copia)"));
			lc++;
			getListaOperacoes().add(new Operacoes(lc, "CRCT", "-", tokenAnterior, "Carrega (" + tokenAnterior + ")"));
			lc++;
			getListaOperacoes().add(new Operacoes(lc, "CMIG", "-", "-", "Compara igual"));
			lc++;

			while (isCheia(pl_case_dsvt)) { // RESOLVE SE HOUVER PENDÊNCIAS DE DESVIOS PARA DSVT
				getListaOperacoes().get(findTopo(pl_case_dsvt)).setOp2(String.valueOf(lc + 1));
				removeElement(pl_case_dsvt);
			}

			getListaOperacoes().add(new Operacoes(lc, "DSVF", "-", "?", "Case"));
			addElement(pl_case_dsvf, lc);
			break;
		case 135: // CASE APÓS COMANDO EM CASE

			while (isCheia(pl_case_dsvf)) { // RESOLVE SE HOUVER PENDÊNCIAS DE DESVIOS PARA DSVF
				getListaOperacoes().get(findTopo(pl_case_dsvf)).setOp2(String.valueOf(lc + 1));
				removeElement(pl_case_dsvf);
			}

			getListaOperacoes().add(new Operacoes(lc, "DSVS", "-", "?", "Case"));
			addElement(pl_case_dsvs, lc);
			break;
		case 136: // CASE RAMO DO CASE: APÓS INTEIRO
			getListaOperacoes().add(new Operacoes(lc, "COPI", "-", "-", "Duplica topo(copia)"));
			lc++;
			getListaOperacoes().add(new Operacoes(lc, "CRCT", "-", tokenAnterior, "Carrega (" + tokenAnterior + ")"));
			lc++;
			getListaOperacoes().add(new Operacoes(lc, "CMIG", "-", "-", "Compara igual"));
			lc++;
			getListaOperacoes().add(new Operacoes(lc, "DSVT", "-", "?", "Case"));
			addElement(pl_case_dsvt, lc);
			break;
		case 137: // FOR
			Simbolo simboloVar137 = SimboloService.buscarSimboloTabelaPorNomeNivelCategoria(tokenAtual, nivelAtual,
					"VAR", tabelaSimbolos);

			if (simboloVar137 != null) {
				local = simboloVar137.getGeralA();
				nome_identificador_for = tokenAtual;
				nome_identificador = tokenAtual;
			} else {
				getListaErrosSintaticosSemanticos().add(new ErroSintaticoSemantico(Integer.valueOf(lnErro),
						"Erro semântico: VAR: " + tokenAtual + " -> Não declarado."));
			}
			break;
		case 138: // FOR

			Simbolo simboloVar138 = SimboloService.buscarSimboloTabelaPorNomeNivelCategoria(nome_identificador,
					nivelAtual, "VAR", tabelaSimbolos);

			linha = simboloVar138.getSeq();
			local = getListaSimbolos().get(linha).getGeralA();
			getListaOperacoes().add(new Operacoes(lc, "ARMZ", String.valueOf(nivelAtual), String.valueOf(local),
					"Armazena (" + nome_identificador + ")"));
			break;
		case 139: // FOR

			Simbolo simboloVar139 = SimboloService.buscarSimboloTabelaPorNomeNivelCategoria(nome_identificador,
					nivelAtual, "VAR", tabelaSimbolos);

			getListaOperacoes().add(new Operacoes(lc, "COPI", "-", "-", "Duplica topo(copia)"));
			addElement(pl_for_dsvs, lc); // RETORNO DO FOR
			lc++;
			linha = simboloVar139.getSeq();
			local = getListaSimbolos().get(linha).getGeralA();
			addElement(pl_for_controle, linha);
			getListaOperacoes().add(new Operacoes(lc, "CRVL", String.valueOf(nivelAtual), String.valueOf(local),
					"Carrega  (" + nome_identificador + ")"));
			lc++;
			getListaOperacoes().add(new Operacoes(lc, "CMAI", "-", "-", "Compara maior ou igual"));
			lc++;
			getListaOperacoes().add(new Operacoes(lc, "DSVF", "-", "?", "FOR"));
			addElement(pl_for_dsvf, lc);
			lc++;
			break;
		case 140: // APÓS FOR
			local = getListaSimbolos().get(findTopo(pl_for_controle)).getGeralA();
			removeElement(pl_for_controle);
			getListaOperacoes().add(new Operacoes(lc, "CRVL", String.valueOf(nivelAtual), String.valueOf(local),
					"Carrega (" + nome_identificador_for + ")"));
			lc++;
			getListaOperacoes().add(new Operacoes(lc, "CRCT", "-", "1", "Carrega (1)"));
			lc++;
			getListaOperacoes().add(new Operacoes(lc, "SOMA", "-", "-", "Soma"));
			lc++;
			getListaOperacoes().add(new Operacoes(lc, "ARMZ", String.valueOf(nivelAtual), String.valueOf(local),
					"Armazena (" + nome_identificador + ")"));
			lc++;
			getListaOperacoes().get(findTopo(pl_for_dsvf)).setOp2(String.valueOf(lc + 1));
			removeElement(pl_for_dsvf);
			getListaOperacoes().add(new Operacoes(lc, "DSVS", "-", String.valueOf(findTopo(pl_for_dsvs)), "FOR"));
			removeElement(pl_for_dsvs);
			lc++;
			getListaOperacoes().add(new Operacoes(lc, "AMEM", "-", "-1", "AMEM -1 (limpa)"));
			break;
		case 141: // = COMPARA IGUAL CMIG
			getListaOperacoes().add(new Operacoes(lc, "CMIG", "-", "-", "Compara igual"));
			break;
		case 142: // < COMPARA MENOR CMME
			getListaOperacoes().add(new Operacoes(lc, "CMME", "-", "-", "Compara menor"));
			break;
		case 143: // > COMPARA MAIOR CMMA
			getListaOperacoes().add(new Operacoes(lc, "CMMA", "-", "-", "Compara maior"));
			break;
		case 144: // >= COMPARA MAIOR OU IGUAL CMAI
			getListaOperacoes().add(new Operacoes(lc, "CMAI", "-", "-", "Compara maior ou igual"));
			break;
		case 145: // <= COMPARA MENOR OU IGUAL CMEI
			getListaOperacoes().add(new Operacoes(lc, "CMEI", "-", "-", "Compara menor"));
			break;
		case 146: // <> COMPARA DIFERENTE CMDF
			getListaOperacoes().add(new Operacoes(lc, "CMDF", "-", "-", "Compara diferente"));
			break;
		case 147: // INVR
			getListaOperacoes().add(new Operacoes(lc, "INVR", "-", "-", "Inverte sinal"));
			break;
		case 148: // SOMA
			getListaOperacoes().add(new Operacoes(lc, "SOMA", "-", "-", "Soma"));
			break;
		case 149: // SUBT
			getListaOperacoes().add(new Operacoes(lc, "SUBT", "-", "-", "Subtração"));
			break;
		case 150: // OR
			getListaOperacoes().add(new Operacoes(lc, "DISJ", "-", "-", "OR"));
			break;
		case 151: // MULT
			getListaOperacoes().add(new Operacoes(lc, "MULT", "-", "-", "Multiplicação"));
			break;
		case 152: // DIV
			getListaOperacoes().add(new Operacoes(lc, "DIV", "-", "-", "Divisão"));
			break;
		case 153: // AND
			getListaOperacoes().add(new Operacoes(lc, "CONJ", "-", "-", "AND"));
			break;
		case 154: // INTEIRO
			getListaOperacoes().add(new Operacoes(lc, "CRCT", "-", tokenAtual, "Carrega (" + tokenAtual + ")"));
			break;
		case 155: // NOT
			getListaOperacoes().add(new Operacoes(lc, "NEGA", "-", "-", "Negação"));
			break;
		case 156: // CONTEXTO = EXPRESSAO
			contexto = 2;
			break;
		case 157:
			break;

		}
	}

	public void addElement(ArrayList<Integer> pilha, int elemento) {
		pilha.add(elemento);
	}

	public void removeElement(ArrayList<Integer> pilha) {
		pilha.remove(pilha.size() - 1);
	}

	public int findTopo(ArrayList<Integer> pilha) {
		return pilha.get(pilha.size() - 1);
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
