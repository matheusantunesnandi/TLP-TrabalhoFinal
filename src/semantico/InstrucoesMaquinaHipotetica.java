package semantico;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Cria as instru��es e manipula a tabela de instru��es que aparecem na tela principal
 *
 */
public class InstrucoesMaquinaHipotetica {
	private JTable table;
	private DefaultTableModel abas;
	private String[] instrucoes;
	private int pt;
	private String aux;

	public InstrucoesMaquinaHipotetica()
	{
		this.instrucoes = new String[30];
		this.instrucoes[1] = "RETU";	//Retorno de procedure
		this.instrucoes[2] = "CRVL";	//Carrega valor na pilha
		this.instrucoes[3] = "CRCT";	//Carrega constante na pilha
		this.instrucoes[4] = "ARMZ";	//Armazena conte�do da pilha (topo) no endere�o dado
		this.instrucoes[5] = "SOMA";	//Opera��o soma c/ elementos do topo e sub-topo
		this.instrucoes[6] = "SUBT";	//Opera��o de subtra��o
		this.instrucoes[7] = "MULT";	//Opera��o de multiplica��o
		this.instrucoes[8] = "DIVI";	//Opera��o de divis�o inteira
		this.instrucoes[9] = "INVR";	//Inverte sinal
		this.instrucoes[10] = "NEGA";	//Opera��o de nega��o
		this.instrucoes[11] = "CONJ";	//Opera��o de AND
		this.instrucoes[12] = "DISJ";	//Opera��o de OR
		this.instrucoes[13] = "CMME";	//Compara menor
		this.instrucoes[14] = "CMMA";	//Compara maior
		this.instrucoes[15] = "CMIG";	//Compara igual
		this.instrucoes[16] = "CMDF";	//Compara diferente
		this.instrucoes[17] = "CMEI";	//Compara menor igual
		this.instrucoes[18] = "CMAI";	//Compara maior igual
		this.instrucoes[19] = "DSVS";	//Desviar sempre
		this.instrucoes[20] = "DSVF";	//Desviar se falso
		this.instrucoes[21] = "LEIT";	//Leitura
		this.instrucoes[22] = "IMPR";	//Imprimir topo da pilha
		this.instrucoes[23] = "IMPRL";	//Imprimir literal extra�do da �rea de literais
		this.instrucoes[24] = "AMEM";	//Alocar espa�o na �rea de dados
		this.instrucoes[25] = "CALL";	//Chamada da procedure �a� no n�vel �L�
		this.instrucoes[26] = "PARA";	//Finaliza a execu��o
		this.instrucoes[27] = "NADA";	//N�o faz nada, continua a execu��o
		this.instrucoes[28] = "COPI";	//Duplica o topo da pilha
		this.instrucoes[29] = "DSVT";	//Desvia se verdadeiro

		this.pt = 0;

		this.aux = "Instrucao - OP1 - OP2";

		inicio();
	}

	//envia a tabela para o frame
	public DefaultTableModel getModTab() {
		return abas;
	}

	//cria a tabela para armazenar a tabela de instru��es
	public void inicio() {

		this.abas = new DefaultTableModel();
		this.abas.setColumnCount(3);
		this.abas.setRowCount(400);
		this.abas.setColumnIdentifiers(new String[] { "Instru��o", "Opera��o 1", "Opera��o 2" });

		this.table = new JTable();
		this.table.setModel(this.abas);
	}

	//insere uma instru��o na tabela
	public void insereInstrucao(int indice, int op1, int op2)
	{
		this.table.setValueAt(this.pt + 1 + ". " + this.instrucoes[indice], this.pt, 0);
		this.table.setValueAt(Integer.valueOf(op1), this.pt, 1);
		this.table.setValueAt(Integer.valueOf(op2), this.pt, 2);
		this.pt += 1;
	}

	//altera uma instru��o na tabela
	public void alteraInstrucao(int indice, int op1, int op2) {
		this.table.setValueAt(Integer.valueOf(op1), indice, 1);
		this.table.setValueAt(Integer.valueOf(op2), indice, 2);
	}

	//carrega instru��es na tabela
	public void carregaInstrucoes() {
		String indice = "";
		Integer op1 = Integer.valueOf(0); Integer op2 = Integer.valueOf(0);
		for (int i = 0; i < this.pt; i++) {
			indice = (String)this.table.getValueAt(i, 0);
			op1 = (Integer)this.table.getValueAt(i, 1);
			op2 = (Integer)this.table.getValueAt(i, 2);
			this.aux = (this.aux + "\n" + indice + " - " + op1 + " - " + op2);
		}
	}

	//limpa a tabela
	public void limpar() {
		for (int i = 0; i < 400; i++) {
			this.table.setValueAt("", i, 0);
			this.table.setValueAt("", i, 1);
			this.table.setValueAt("", i, 2);
		}
	}
}