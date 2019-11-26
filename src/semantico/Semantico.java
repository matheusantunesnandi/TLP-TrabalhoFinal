package semantico;

import javax.swing.JOptionPane;

import tabelaSimbolo.TabelaSimbolos;

@SuppressWarnings({"unused"})
public class Semantico {

	private TabelaSimbolos tabelaSimbolos;
	private Pilha ifs;
	private Pilha whiles;
	private Pilha parametros;
	private Pilha cases;
	private boolean temParametro;
	private int acaoAcumulada = 3;
	private Pilha repeats;
	private Pilha procedures;
	private int nivelAtual = 0;
	private String erro = "";
	private String nomePro = "";
	private int forEndNome = 0;
	private int end_ident = 0;
	private String nome_atribuicao_esquerda = "";
	private String nomeProcedimento = "";
	private int npe = 0;
	private String contexto = "";
	private Pilha fors;
	private int nv;
	private int np;
	private int deslocamento;
	private Instrucoes areaInstrucoes;
	private Literais areaLiterais;
	private MaquinaHipotetica maquinaHipotetica;
	private String penultimo;
	private String antepenultimo;
	private String nomeIdentificador = "";
	private String tipo_identificador = "";
	private InstrucoesMaquinaHipotetica instrucoes;

	private String msgRetornoSemantico = "";

	public String getMsgRetornoSemantico() {
		return msgRetornoSemantico;
	}

	public String getErro() {
		return erro;
	}

	public Semantico()
	{
		this.maquinaHipotetica = new MaquinaHipotetica();
		this.areaInstrucoes = new Instrucoes();
		this.areaLiterais = new Literais();

		this.instrucoes = new InstrucoesMaquinaHipotetica();

	}

	//CASE COM CADA A��O SEMANTICA
	public void semantico(int acao) {
		System.out.println("INICIOU ANALISE SEM�NTICA...");
		switch (acao)
		{
		case 100:	// Reconhecendo o nome do programa
			this.ifs = new Pilha();
			this.whiles = new Pilha();
			this.repeats = new Pilha();
			this.procedures = new Pilha();
			this.parametros = new Pilha();
			this.cases = new Pilha();
			this.fors = new Pilha();
			this.tabelaSimbolos = new TabelaSimbolos();
			MaquinaHipotetica.inicializarInstrucoes(this.areaInstrucoes);
			MaquinaHipotetica.inicicalizaLiterais(this.areaLiterais);
			this.tabelaSimbolos = new TabelaSimbolos();
			this.nv = 0;
			this.deslocamento = 3;
			break;
		case 101:	// Final de programa
			this.instrucoes.insereInstrucao(26, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 26, 0, 0);
			for (int i = 0; i < this.tabelaSimbolos.getTabela().length; i++) {
				if ((this.tabelaSimbolos.getTabela()[i][0] != null) && (this.tabelaSimbolos.getTabela()[i][1].equals("r�tulo"))){
					if (this.tabelaSimbolos.getTabela()[i][4].equals(""))
						break;
					this.erro = "Erro sem�ntico";
					msgRetornoSemantico = "Erro sem�ntico";

					break;
				}

			}

			break;
		case 102:	//Ap�s declara��o de vari�vel
			this.instrucoes.insereInstrucao(24, 0, this.acaoAcumulada);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 24, 0, this.acaoAcumulada);
			this.acaoAcumulada = 3;
			break;
		case 103:	// Ap�s palavra LABEL em declara��o de r�tulo
			this.tipo_identificador = "r�tulo";
			break;
		case 104:	//Encontrado o nome de r�tulo, de vari�vel, ou de par�metro de procedure em declara��o
			this.acaoAcumulada += 1;
			if (this.tipo_identificador.equals("r�tulo")) {
				if (this.tabelaSimbolos.buscar(this.penultimo) != -1) {
					int nivel = Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][2]);
					if (nivel == this.nivelAtual) {
						System.out.println("Erro sem�ntico\nR�tulo j� declarado no mesmo n�vel");
					}
					else
						this.tabelaSimbolos.inserirTabela(this.penultimo, "r�tulo", this.nivelAtual+"", "0", "");
				}
				else {
					this.tabelaSimbolos.inserirTabela(this.penultimo, "r�tulo", this.nivelAtual+"", "0", "");
				}
			}

			if (this.tipo_identificador.equals("vari�vel")) {
				if (this.tabelaSimbolos.buscar(this.penultimo) == -1) {
					this.tabelaSimbolos.inserirTabela(this.penultimo, "vari�vel", this.nivelAtual+"", this.deslocamento+"", "");
					this.deslocamento += 1;
					this.nv += 1;
				} else {
					this.erro = "Erro sem�ntico\nVari�vel \""+this.penultimo+"\" j� foi declarada";
				}
			}

			if (this.tipo_identificador.equals("par�metro")) {
				if (this.tabelaSimbolos.buscar(this.penultimo) != -1) {
					int nivel = Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][2]);
					if (nivel == this.nivelAtual) {
						msgRetornoSemantico = "Erro sem�ntico\nPar�metro j� declarado no mesmo n�vel";
					} else {
						this.tabelaSimbolos.inserirTabela(this.penultimo, "par�metro", this.nivelAtual+"", "", "");
						this.parametros.insereElemento(this.tabelaSimbolos.buscar(this.penultimo));
						this.np += 1;
					}
				} else {
					this.tabelaSimbolos.inserirTabela(this.penultimo, "par�metro", this.nivelAtual+"", "", "");
					this.parametros.insereElemento(this.tabelaSimbolos.buscar(this.penultimo));
					this.np += 1;
				}
			}
			break;
		case 105:	// Reconhecido nome de constante em declara��o
			if (this.tabelaSimbolos.buscar(this.penultimo) != -1) {
				msgRetornoSemantico = "Erro sem�ntico\nConstante j� foi declarada";
			} else {
				this.tabelaSimbolos.inserirTabela(this.penultimo, "constante", this.nivelAtual+"", "0", "0");
				this.end_ident = this.tabelaSimbolos.buscar(this.penultimo);
			}
			break;
		case 106:	// Reconhecido valor de constante em declara��o
			String[][] tabelaSimbolo = this.tabelaSimbolos.getTabela();
			tabelaSimbolo[this.end_ident][3] = this.penultimo;
			this.tabelaSimbolos.setTabela(tabelaSimbolo);
			break;
		case 107:	//Antes de lista de identificadores em declara��o de vari�veis
			this.tipo_identificador = "vari�vel";
			this.nv = 0;
			break;
		case 108:	//Ap�s nome de procedure, em declara��o
			this.deslocamento = 3;
			this.nomePro = this.penultimo;
			this.tabelaSimbolos.inserirTabela(this.penultimo, "procedure", this.nivelAtual+"", this.areaInstrucoes.LC + 1+"", "0");
			possuiParametro(false);
			this.parametros.insereElemento(this.tabelaSimbolos.buscar(this.penultimo));
			this.nivelAtual += 1;
			this.np = 0;
			break;
		case 109:	// Ap�s declara��o de procedure
			if (this.np > 0) {
				String[][] tabelaSimbolo2 = this.tabelaSimbolos.getTabela();
				tabelaSimbolo2[this.tabelaSimbolos.buscar(this.nomePro)][4] = this.np+"";
				for (int i = 0; i < this.np; i++) {
					tabelaSimbolo2[this.parametros.topo()][3] = (-(this.np - i)+"");

					this.parametros.tiraElemento();
				}
				this.tabelaSimbolos.setTabela(tabelaSimbolo2);
			}

			this.instrucoes.insereInstrucao(19, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 19, 0, 0);
			this.procedures.insereElemento(this.areaInstrucoes.LC - 1);

			this.parametros.insereElemento(this.np);
			break;
		case 110:	//Fim de procedure
			this.parametros.tiraElemento();

			this.instrucoes.insereInstrucao(1, 0, this.np + 1);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 1, 0, this.np + 1);

			for (int i = 0; i < this.tabelaSimbolos.getTabela().length; i++) {
				if ((this.tabelaSimbolos.getTabela()[i][0] != null) &&
						(this.tabelaSimbolos.getTabela()[i][1].equals("r�tulo")))
				{
					if (this.tabelaSimbolos.getTabela()[i][4].equals("")) break;
					msgRetornoSemantico = "Erro sem�ntico";

					break;
				}

			}

			this.instrucoes.alteraInstrucao(this.procedures.topo(), 0, this.areaInstrucoes.LC + 1);
			MaquinaHipotetica.alterar(this.areaInstrucoes, this.procedures.topo(), 0, this.areaInstrucoes.LC);
			this.procedures.tiraElemento();

			this.tabelaSimbolos.remover();

			this.nivelAtual -= 1;

			break;
		case 111:	//Antes de par�metros formais de procedures
			this.tipo_identificador = "par�metro";
			possuiParametro(true);
			break;
		case 112:	//Identificador de instru��o rotulada ou comando de atribui��o
			this.nomeIdentificador = this.penultimo;
			break;
		case 113:	// Instru��o rotulada
			this.nomeIdentificador = this.penultimo;
			if (this.tabelaSimbolos.buscar(this.nomeIdentificador) != -1) {
				if (this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nomeIdentificador)][1].equals("r�tulo")) {
					if (!this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nomeIdentificador)][2].equals(this.nivelAtual)) {
						msgRetornoSemantico = "Erro sem�ntico\nR�tulo n�o est� no escopo";
					} else {
						String[][] tabelaSimbolo3 = this.tabelaSimbolos.getTabela();

						tabelaSimbolo3[this.tabelaSimbolos.buscar(this.nomeIdentificador)][3] = this.areaInstrucoes.LC+"";

						if (!tabelaSimbolo3[this.tabelaSimbolos.buscar(this.nomeIdentificador)][4].equals(""))
						{
							String lista = tabelaSimbolo3[this.tabelaSimbolos.buscar(this.nomeIdentificador)][4];
							int qtd = 0;

							for (int i = 0; i < lista.length(); i++) {
								if (lista.charAt(i) == ' ') {
									qtd++;
								}
							}

							int endereco = 0;

							lista = tiraProximo(lista);

							for (int i = 0; i < qtd; i++)
							{
								endereco = Integer.parseInt(proximo(lista));
								lista = tiraProximo(lista);

								this.instrucoes.alteraInstrucao(endereco, 0, this.areaInstrucoes.LC + 1);
								MaquinaHipotetica.alterar(this.areaInstrucoes, endereco, 0, this.areaInstrucoes.LC);
							}

						}

						tabelaSimbolo3[this.tabelaSimbolos.buscar(this.nomeIdentificador)][4] = "";

						this.tabelaSimbolos.setTabela(tabelaSimbolo3);
					}
				}
				else
					this.erro = "Erro sem�ntico: r�tulo n�o est� declarado";
			}
			else {
				this.erro = "Erro sem�ntico: r�tulo n�o est� declarado";
			}

			break;
		case 114:	//Atribui��o parte esquerda
			System.out.println("case 114");
			this.nomeIdentificador = this.penultimo;
			System.out.println(this.nomeIdentificador);
			System.out.println(this.tabelaSimbolos.buscar(this.nomeIdentificador));
			if (this.tabelaSimbolos.buscar(this.nomeIdentificador) != -1) {
				if (!this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nomeIdentificador)][1].equals("vari�vel")) {
					this.erro = "Erro sem�ntico: atribui��o da parte esquerda inv�lida";
					//JOptionPane.showMessageDialog(null, this.erro, "Erro sem�ntico", 0);
				} else {
					this.nome_atribuicao_esquerda = this.nomeIdentificador;
				}
			} else {
				this.erro = "Erro sem�ntico: identificador \""+this.penultimo+"\" n�o encontrado na tabela de s�mbolos";
				//JOptionPane.showMessageDialog(null, this.erro, "Erro sem�ntico", 0);
			}

			break;
		case 115:	// Ap�s express�o em atribui��o
			System.out.println("case 115");
			this.nomeIdentificador = this.penultimo;
			if ((this.nome_atribuicao_esquerda == null) || (this.nome_atribuicao_esquerda.equals(""))) {
				//JOptionPane.showMessageDialog(null, "Erro!", "erro durante a execu��o", 0);
				return;
			}
			int d_nivel = this.nivelAtual - Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nome_atribuicao_esquerda)][2]);
			this.instrucoes.insereInstrucao(4, d_nivel, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nome_atribuicao_esquerda)][3]));
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 4, d_nivel, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nome_atribuicao_esquerda)][3]));
			break;
		case 116:	//Chamada de procedure
			if (this.tabelaSimbolos.buscar(this.penultimo) != -1) {
				if (this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][1].equals("procedure"))
					this.nomeProcedimento = this.penultimo;
			}
			else {
				this.erro = "Erro sem�ntico: procedure \""+this.penultimo+"\" n�o declarada";
			}
			break;
		case 117:	//Ap�s comando call
			this.nomeProcedimento = this.nomePro;
			if (Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nomeProcedimento)][4]) != this.np) {
				this.erro = ("Erro sem�ntico: numero de parametros da procedure " + this.nomeProcedimento + " N�o conferem com o n�mero par�metros passados");
			} else {
				this.instrucoes.insereInstrucao(25, 0, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nomeProcedimento)][3]) + 1);
				this.maquinaHipotetica.incluir(this.areaInstrucoes, 25, 0, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.nomeProcedimento)][3]));
			}
			break;
		case 118:	//Ap�s express�o, em comando call
			this.npe += 1;
			break;
		case 119:	//Comando GOTO
			if ((this.tabelaSimbolos.buscar(this.penultimo) != -1) && (this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][1].equals("r�tulo")))
			{
				if (!this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][2].equals(this.nivelAtual)) {
					this.erro = "Erro sem�ntico: o r�tulo n�o est� declarado no escopo do n�vel";
					//JOptionPane.showMessageDialog(null, this.erro, "Erro sem�ntico", 0);
				} else {
					int op2 = Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][3]);
					if (op2 != 0) {
						this.instrucoes.insereInstrucao(19, 0, 0);
						this.maquinaHipotetica.incluir(this.areaInstrucoes, 19, 0, op2);
					} else {
						this.instrucoes.insereInstrucao(19, 0, 0);
						this.maquinaHipotetica.incluir(this.areaInstrucoes, 19, 0, 0);

						String[][] tabelaSimbolo4 = this.tabelaSimbolos.getTabela();
						int ind = this.tabelaSimbolos.buscar(this.penultimo);

						tabelaSimbolo4[ind][4] = (tabelaSimbolo4[ind][4] + " " + (this.areaInstrucoes.LC - 1));

						this.tabelaSimbolos.setTabela(tabelaSimbolo4);
					}
				}
			}
			else
			{
				this.erro = "Erro sem�ntico: identificador \""+this.penultimo+"\" n�o est� declarado";
			}

			break;
		case 120:	//Ap�s express�o num comando IF
			this.instrucoes.insereInstrucao(20, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 20, 0, 0);
			this.ifs.insereElemento(this.areaInstrucoes.LC - 1);
			break;
		case 121:	//Ap�s instru��o IF
			this.instrucoes.alteraInstrucao(this.ifs.topo(), 0, this.areaInstrucoes.LC + 1);
			MaquinaHipotetica.alterar(this.areaInstrucoes, this.ifs.topo(), 0, this.areaInstrucoes.LC);
			this.ifs.tiraElemento();
			break;
		case 122:	// Ap�s dom�nio do THEN, antes do ELSE
			this.instrucoes.alteraInstrucao(this.ifs.topo(), 0, this.areaInstrucoes.LC + 2);
			MaquinaHipotetica.alterar(this.areaInstrucoes, this.ifs.topo(), 0, this.areaInstrucoes.LC + 1);
			this.ifs.tiraElemento();
			this.instrucoes.insereInstrucao(19, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 19, 0, 0);
			this.ifs.insereElemento(this.areaInstrucoes.LC - 1);
			break;
		case 123:	// Comando WHILE antes da express�o
			this.whiles.insereElemento(this.areaInstrucoes.LC);
			break;
		case 124:	//Comando WHILE depois da express�o
			this.instrucoes.insereInstrucao(20, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 20, 0, 0);
			this.whiles.insereElemento(this.areaInstrucoes.LC - 1);
			break;
		case 125:	// Ap�s comando WHILE
			this.instrucoes.alteraInstrucao(this.whiles.topo(), 0, this.areaInstrucoes.LC + 2);
			MaquinaHipotetica.alterar(this.areaInstrucoes, this.whiles.topo(), 0, this.areaInstrucoes.LC + 1);
			this.whiles.tiraElemento();
			this.instrucoes.insereInstrucao(19, 0, this.whiles.topo());
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 19, 0, this.whiles.topo());
			this.whiles.tiraElemento();
			break;
		case 126:	//Comando REPEAT � in�cio
			this.repeats.insereElemento(this.areaInstrucoes.LC);
			break;
		case 127:	// Comando REPEAT � fim
			this.instrucoes.insereInstrucao(20, 0, this.repeats.topo() + 1);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 20, 0, this.repeats.topo());
			this.repeats.tiraElemento();
			break;
		case 128:	// Comando READLN  in�cio
			this.contexto = "readln";
			break;
		case 129:	// Identificador de vari�vel
			if (this.tabelaSimbolos.buscar(this.penultimo) == -1)
			{
				this.erro = "Erro sem�ntico: identificador \""+this.penultimo+"\" n�o est� declarado";
			}
			else
			{
				int d_nivel2 = this.nivelAtual - Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][2]);

				if (this.contexto.equals("readln")) {
					if (this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][1].equals("vari�vel")) {
						this.instrucoes.insereInstrucao(21, 0, 0);
						this.maquinaHipotetica.incluir(this.areaInstrucoes, 21, 0, 0);
						this.instrucoes.insereInstrucao(4, d_nivel2, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][3]));
						this.maquinaHipotetica.incluir(this.areaInstrucoes, 4, d_nivel2, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][3]));
					}
					else {
						this.erro = "Erro sem�ntico: identificador \""+this.penultimo+"\" n�o � uma vari�vel";
					}
				}

				if (this.contexto.equals("express�o"))
				{
					if (this.tabelaSimbolos.buscar(this.penultimo) == -1)
					{
						this.erro = "Erro sem�ntico: identificador \""+this.penultimo+"\" n�o est� declarado";
					}
					else if ((this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][1].equals("procedure")) || (this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][1].equals("r�tulo")))
					{
						this.erro = "Erro sem�ntico: identificador \""+this.penultimo+"\" n�o � uma constante";
					}
					else if (this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][1].equals("constante")) {
						this.instrucoes.insereInstrucao(3, 0, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][3]));
						this.maquinaHipotetica.incluir(this.areaInstrucoes, 3, 0, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][3]));
					} else {
						this.instrucoes.insereInstrucao(2, d_nivel2, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][3]));
						this.maquinaHipotetica.incluir(this.areaInstrucoes, 2, d_nivel2, Integer.parseInt(this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][3]));
					}

				}

			}

			break;
		case 130:	// WRITELN - ap�s literal na instru��o WRITELN
			MaquinaHipotetica.incluirAreaLiterais(this.areaLiterais, this.penultimo);
			this.areaLiterais.insereLiteral(this.penultimo);
			this.instrucoes.insereInstrucao(23, 0, this.areaLiterais.literais - 1);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 23, 0, this.areaLiterais.literais - 1);
			break;
		case 131:	// WRITELN ap�s express�o
			this.instrucoes.insereInstrucao(22, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 22, 0, 0);
			break;
		case 132:	// Ap�s palavra reservada CASE
			break;
		case 133:	//Ap�s comando CASE
			this.instrucoes.alteraInstrucao(this.cases.topo(), 0, this.areaInstrucoes.LC + 1);
			MaquinaHipotetica.alterar(this.areaInstrucoes, this.cases.topo(), 0, this.areaInstrucoes.LC);
			this.cases.tiraElemento();

			this.instrucoes.insereInstrucao(24, 0, -1);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 24, 0, -1);

			break;
		case 134:	// Ramo do CASE ap�s inteiro, �ltimo da lista
			this.instrucoes.insereInstrucao(28, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 28, 0, 0);

			int ant = Integer.parseInt(this.antepenultimo);
			this.instrucoes.insereInstrucao(3, 0, ant);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 3, 0, ant);

			this.instrucoes.insereInstrucao(15, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 15, 0, 0);

			if (!this.cases.vazia()) {
				this.instrucoes.alteraInstrucao(this.cases.topo(), 0, this.areaInstrucoes.LC + 2);
				MaquinaHipotetica.alterar(this.areaInstrucoes, this.cases.topo(), 0, this.areaInstrucoes.LC + 1);
				this.cases.tiraElemento();
			}

			this.instrucoes.insereInstrucao(20, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 20, 0, 0);
			this.cases.insereElemento(this.areaInstrucoes.LC - 1);

			break;
		case 135:	//Ap�s comando em CASE
			this.instrucoes.alteraInstrucao(this.cases.topo(), 0, this.areaInstrucoes.LC + 2);
			MaquinaHipotetica.alterar(this.areaInstrucoes, this.cases.topo(), 0, this.areaInstrucoes.LC + 1);
			this.cases.tiraElemento();

			this.instrucoes.insereInstrucao(19, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 19, 0, 0);
			this.cases.insereElemento(this.areaInstrucoes.LC - 1);
			break;
		case 136:	//Ramo do CASE: ap�s inteiro
			if (this.cases.cheia()) {
				this.instrucoes.alteraInstrucao(this.cases.topo(), 0, this.areaInstrucoes.LC + 2);
				MaquinaHipotetica.alterar(this.areaInstrucoes, this.cases.topo(), 0, this.areaInstrucoes.LC + 1);
				this.cases.tiraElemento();
			}

			this.instrucoes.insereInstrucao(28, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 28, 0, 0);

			int ant2 = Integer.parseInt(this.antepenultimo);
			this.instrucoes.insereInstrucao(3, 0, ant2);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 3, 0, ant2);

			this.instrucoes.insereInstrucao(15, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 15, 0, 0);

			this.instrucoes.insereInstrucao(29, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 29, 0, 0);

			this.cases.insereElemento(this.areaInstrucoes.LC - 1);
			break;
		case 137:	// Ap�s vari�vel controle comando FOR
			if ((this.tabelaSimbolos.buscar(this.penultimo) != -1) && (this.tabelaSimbolos.getTabela()[this.tabelaSimbolos.buscar(this.penultimo)][1].equals("vari�vel"))) {
				this.forEndNome = this.tabelaSimbolos.buscar(this.penultimo);
			}
			else {
				this.erro = "Erro sem�ntico: vari�vel \""+this.penultimo+"\" n�o declarada";
			}
			break;
		case 138:	//Ap�s express�o valor inicial
			int op1 = this.nivelAtual - Integer.parseInt(this.tabelaSimbolos.getTabela()[this.forEndNome][2]);
			int op2 = Integer.parseInt(this.tabelaSimbolos.getTabela()[this.forEndNome][3]);
			this.instrucoes.insereInstrucao(4, op1, op2);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 4, op1, op2);
			break;
		case 139:	//Ap�s express�o � valor final
			this.fors.insereElemento(this.areaInstrucoes.LC);

			this.instrucoes.insereInstrucao(28, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 28, 0, 0);

			int op12 = this.nivelAtual - Integer.parseInt(this.tabelaSimbolos.getTabela()[this.forEndNome][2]);
			int op22 = Integer.parseInt(this.tabelaSimbolos.getTabela()[this.forEndNome][3]);
			this.instrucoes.insereInstrucao(2, op12, op22);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 2, op12, op22);

			this.instrucoes.insereInstrucao(18, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 18, 0, 0);

			this.fors.insereElemento(this.areaInstrucoes.LC);
			this.instrucoes.insereInstrucao(20, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 20, 0, 0);
			break;
		case 140:	//Ap�s comando em FOR
			int op13 = this.nivelAtual - Integer.parseInt(this.tabelaSimbolos.getTabela()[this.forEndNome][2]);
			int op23 = Integer.parseInt(this.tabelaSimbolos.getTabela()[this.forEndNome][3]);

			this.instrucoes.insereInstrucao(2, op13, op23);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 2, op13, op23);

			this.instrucoes.insereInstrucao(3, 0, 1);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 3, 0, 1);

			this.instrucoes.insereInstrucao(5, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 5, 0, 0);

			this.instrucoes.insereInstrucao(4, op13, op23);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 4, op13, op23);

			this.instrucoes.alteraInstrucao(this.fors.topo(), 0, this.areaInstrucoes.LC + 1);
			MaquinaHipotetica.alterar(this.areaInstrucoes, this.fors.topo(), 0, this.areaInstrucoes.LC + 1);
			this.fors.tiraElemento();

			this.instrucoes.insereInstrucao(19, 0, this.fors.topo() + 1);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 19, 0, this.fors.topo());

			this.instrucoes.insereInstrucao(24, 0, -1);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 24, 0, -1);

			break;
		case 141:	// compara igual
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 15, 0, 0);
			break;
		case 142:	//compara menor
			this.instrucoes.insereInstrucao(13, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 13, 0, 0);
			break;
		case 143:	//compara maior
			this.instrucoes.insereInstrucao(14, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 14, 0, 0);
			break;
		case 144:	//compara maior igual
			this.instrucoes.insereInstrucao(18, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 18, 0, 0);
			break;
		case 145:	// compara menor igual
			this.instrucoes.insereInstrucao(17, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 17, 0, 0);
			break;
		case 146:	//compara diferente
			this.instrucoes.insereInstrucao(16, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 16, 0, 0);
			break;
		case 147:	//Express�o � operando com sinal un�rio
			this.instrucoes.insereInstrucao(9, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 9, 0, 0);
			break;
		case 148:	//Express�o � soma
			this.instrucoes.insereInstrucao(5, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 5, 0, 0);
			break;
		case 149:	//Express�o � subtra��o
			this.instrucoes.insereInstrucao(6, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 6, 0, 0);
			break;
		case 150:	//Express�o � or
			this.instrucoes.insereInstrucao(12, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 12, 0, 0);
			break;
		case 151:	// Express�o � multiplica��o
			this.instrucoes.insereInstrucao(7, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 7, 0, 0);
			break;
		case 152:	// Express�o � divis�o
			this.instrucoes.insereInstrucao(8, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 8, 0, 0);
			break;
		case 153:	// Express�o � and
			this.instrucoes.insereInstrucao(11, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 11, 0, 0);
			break;
		case 154:	//Express�o � inteiro
			int pen = Integer.parseInt(this.penultimo);
			this.instrucoes.insereInstrucao(3, 0, pen);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 3, 0, pen);
			break;
		case 155:	//Express�o � not
			this.instrucoes.insereInstrucao(10, 0, 0);
			this.maquinaHipotetica.incluir(this.areaInstrucoes, 10, 0, 0);
			break;
		case 156:	//Express�o � vari�vel
			this.contexto = "express�o";
			break;
		default:
			System.out.println("Erro com a a��o : " + acao);
		}

		System.out.println(getTabelaSimbolos().toString());
	}

	public String interpreta()
	{
		MaquinaHipotetica.interpretar(this.areaInstrucoes, this.areaLiterais);

		if (this.erro != null && !this.erro.equals("")) {
			return this.erro;
		}

		return "nada";
	}

	public String proximo(String a)
	{
		String aux = "";

		for (int i = 0; i < a.length(); i++) {
			if (a.charAt(i) == ' ') break;
			aux = aux + a.charAt(i);
		}

		return aux;
	}

	public String tiraProximo(String a)
	{
		String aux = "";

		for (int i = 0; i < a.length(); i++) {
			if (a.charAt(i) == ' ') {
				for (int j = i + 1; j < a.length(); j++) {
					aux = aux + a.charAt(j);
				}
				break;
			}
		}

		return aux;
	}


	//GETTERS AND SETTERS
	public void possuiParametro(boolean temParametro) {
		this.temParametro = temParametro;
	}

	public boolean isTemParametro() {
		return this.temParametro;
	}

	public InstrucoesMaquinaHipotetica getInstrucoes() {
		return instrucoes;
	}

	public String getPenultimo()
	{
		return this.penultimo;
	}

	public void setPenultimo(String penultimo)
	{
		this.penultimo = penultimo;
	}

	public void setAntePenultimo(String antepenultimo)
	{
		this.antepenultimo = antepenultimo;
	}

	public String getAntePenultimo()
	{
		return this.antepenultimo;
	}

	public Literais getAreaLiterais() {
		return areaLiterais;
	}

	public void setAreaLiterais(Literais areaLiterais) {
		this.areaLiterais = areaLiterais;
	}

	public TabelaSimbolos getTabelaSimbolos() {
		return tabelaSimbolos;
	}

	public void setTabelaSimbolos(TabelaSimbolos tabelaSimbolos) {
		this.tabelaSimbolos = tabelaSimbolos;
	}


}