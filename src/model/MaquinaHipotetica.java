package model;

import javax.swing.JOptionPane;

public class MaquinaHipotetica { // Classe que implementa a máquina hipotética.
	public static int MaxInst = 1000;
	public static int MaxList = 300;
	public static int b; // base do segmento
	public static int topo; // topo da pilha da base de dados
	public static int p; // apontador de instruções
	public static int l; // primeiro operando
	public static int a; // segundo operando
	public static int nv = 0; // número de variáveis;
	public static int np = 0; // número de parâmetros;
	public static int operador; // codigo da instrução
	public static int k; // segundo operando
	public static int i;
	public static int num_impr = 0;
	public static int[] S = new int[1000];
	public static int LC = 0;

	/**
	 * @wbp.parser.entryPoint
	 */
	public static int Base() { // Utilizada para determinar a base.
		int b1;
		b1 = b;
		while (l > 0) {
			b1 = S[b1];
			l = l - 1;
		}
		return b1;
	}

	public static void Interpreta() { // Responsável por interpretar as instruções.
		topo = 2;
		b = 0; // registrador base
		p = 0; // aponta proxima instrução
		S[0] = 0; // SL
		S[1] = 0; // DL
		S[2] = 0; // RA
		operador = 0;
		String leitura;
		while (operador != 26) {// Enquanto instrução diferente de Pare
			operador = view.PrincipalGUI.AL_Instr.get(p).getSeq();
			l = view.PrincipalGUI.AL_Instr.get(p).getOp1();
			a = view.PrincipalGUI.AL_Instr.get(p).getOp2();
			p = p + 1;

			switch (operador) {
			case 1:// RETU
				p = S[b + 2];
				topo = b - a;
				b = S[b + 1];
				break;

			case 2:// CRVL
				topo = topo + 1;
				S[topo] = S[Base() + a];
				break;

			case 3: // CRCT
				topo = topo + 1;
				S[topo] = a;
				break;

			case 4:// ARMZ
				S[Base() + a] = S[topo];
				topo = topo - 1;
				break;

			case 5:// SOMA
				S[topo - 1] = S[topo - 1] + S[topo];
				topo = topo - 1;
				break;

			case 6:// SUBT
				S[topo - 1] = S[topo - 1] - S[topo];
				topo = topo - 1;
				break;

			case 7:// MULT
				S[topo - 1] = S[topo - 1] * S[topo];
				topo = topo - 1;
				break;

			case 8: // DIVI
				if (S[topo] == 0) {
					JOptionPane.showMessageDialog(null, "Divisão por zero.", "Erro durante a execução",
							JOptionPane.ERROR_MESSAGE);
					p = 26;
				} else {
					S[topo - 1] = S[topo - 1] / S[topo];
					topo = topo - 1;
				}
				break;

			case 9:// INVR
				S[topo] = -S[topo];
				break;

			case 10: // NEGA
				if (S[topo] == 1) {
					S[topo] = 0;
				} else {
					S[topo] = 1;
				}
				S[topo] = 1 - S[topo];
				break;

			case 11:// CONJ
				if ((S[topo - 1] == 1) && (S[topo] == 1)) {
					S[topo - 1] = 1;
				} else {
					S[topo - 1] = 0;
					topo = topo - 1;
				}
				break;

			case 12:// DISJ
				if ((S[topo - 1] == 1 || S[topo] == 1)) {
					S[topo - 1] = 1;
				} else {
					S[topo - 1] = 0;
					topo = topo - 1;
				}
				break;

			case 13:// CMME
				if (S[topo - 1] < S[topo]) {
					S[topo - 1] = 1;
				} else {
					S[topo - 1] = 0;
				}
				topo = topo - 1;
				break;

			case 14:// CMMA
				if (S[topo - 1] > S[topo]) {
					S[topo - 1] = 1;
				} else {
					S[topo - 1] = 0;
				}
				topo = topo - 1;
				break;

			case 15:// CMIG
				if (S[topo - 1] == S[topo]) {
					S[topo - 1] = 1;
				} else {
					S[topo - 1] = 0;
				}
				topo = topo - 1;
				break;

			case 16:// CMDF
				if (S[topo - 1] != S[topo]) {
					S[topo - 1] = 1;
				} else {
					S[topo - 1] = 0;
				}
				topo = topo - 1;
				break;

			case 17:// CMEI
				if (S[topo - 1] <= S[topo]) {
					S[topo - 1] = 1;
				} else {
					S[topo - 1] = 0;
				}
				topo = topo - 1;
				break;

			case 18:// CMAI
				if (S[topo - 1] >= S[topo]) {
					S[topo - 1] = 1;
				} else {
					S[topo - 1] = 0;
				}
				topo = topo - 1;
				break;

			case 19:// DSVS
				p = a;
				break;

			case 20:// DSVF
				if (S[topo] == 0) {
					p = a;
				}
				topo = topo - 1;
				break;

			case 21:// LEIT
				topo = topo + 1;
				leitura = JOptionPane.showInputDialog("Informe o valor:");
				(S[topo]) = Integer.parseInt(leitura);
				break;

			case 22:// IMPR
				JOptionPane.showMessageDialog(null, S[topo]);
				topo = topo - 1;
				break;

			case 23:// IMPRL
				if (a >= view.PrincipalGUI.AreaLiterais.length()) {
					JOptionPane.showMessageDialog(null, "Literal não encontrado na área dos literais.",
							"Erro durante a execução", JOptionPane.ERROR_MESSAGE);
				} else {
					String mostrar = "";
					for (int i = 0; i < view.PrincipalGUI.AreaLiterais.length(); i++) {
						if (i > l && i < a) {
							mostrar += view.PrincipalGUI.AreaLiterais.charAt(i);
						}
					}
					JOptionPane.showMessageDialog(null, mostrar);
				}
				break;
			case 24:// AMEM
				topo = topo + a;
				break;

			case 25:// CALL
				S[topo + 1] = Base();
				S[topo + 2] = b;
				S[topo + 3] = p;
				b = topo + 1;
				p = a;
				topo = S[topo + 3];
				break;

			case 26:
				// System.exit(0);
				// PARA
				break;

			case 27:
				// NADA
				break;

			case 28:// COPI
				topo = topo + 1;
				S[topo] = S[topo - 1];
				break;

			case 29:// DSVT
				if (S[topo] == 1) {
					p = a;
				}
				topo = topo - 1;
			}
		}
	}
}