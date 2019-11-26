package model;

import view.PrincipalGUI;

public class LexicoGTabela {

	int r = 0;

	public LexicoGTabela() {
		int n = PrincipalGUI.AL.size();
		for (int i = 0; i < n; i++) {
			r = 0;
			int m = PrincipalGUI.AL_p.size();
			for (int j = 0; j < m; j++) { // Identifica se o token é uma palavra reservada
				if (PrincipalGUI.AL.get(i).toLowerCase().equals(PrincipalGUI.AL_p.get(j).getNome())) {
					LexicoToken t = new LexicoToken();
					t.setCodigo(PrincipalGUI.AL_p.get(j).getCodigo());
					t.setDesc(PrincipalGUI.AL_p.get(j).getDesc());
					t.setNome(PrincipalGUI.AL_p.get(j).getNome());
					PrincipalGUI.ALfinal.add(t);
					j = m;
					r = 1;
				}
			}
			if (r == 0) {
				m = PrincipalGUI.AL_o.size();
				for (int j = 0; j < m; j++) { // Identifica se o token é um operador
					if (PrincipalGUI.AL.get(i).equals(PrincipalGUI.AL_o.get(j).getNome())) {
						LexicoToken t = new LexicoToken();
						t.setCodigo(PrincipalGUI.AL_o.get(j).getCodigo());
						t.setDesc(PrincipalGUI.AL_o.get(j).getDesc());
						t.setNome(PrincipalGUI.AL_o.get(j).getNome());
						PrincipalGUI.ALfinal.add(t);
						j = m;
						r = 1;
					}
				}
				if (r == 0) {
					m = PrincipalGUI.AL_s.size();
					for (int j = 0; j < m; j++) { // Identifica se o token é um simbulo especial
						if (PrincipalGUI.AL.get(i).equals(PrincipalGUI.AL_s.get(j).getNome())) {
							LexicoToken t = new LexicoToken();
							t.setCodigo(PrincipalGUI.AL_s.get(j).getCodigo());
							t.setDesc(PrincipalGUI.AL_s.get(j).getDesc());
							t.setNome(PrincipalGUI.AL_s.get(j).getNome());
							PrincipalGUI.ALfinal.add(t);
							j = m;
							r = 1;
						}
					}
					if (r == 0) {
						if (Character.isDigit(PrincipalGUI.AL.get(i).charAt(0))) { // caso seja um inteiro
							LexicoToken t = new LexicoToken();
							t.setCodigo(26);
							t.setDesc("Inteiro");
							t.setNome(PrincipalGUI.AL.get(i));
							PrincipalGUI.ALfinal.add(t);
							r = 1;
						} else {
							if (PrincipalGUI.AL.get(i).charAt(0) == '\"') { // caso seja um literal
								LexicoToken t = new LexicoToken();
								t.setCodigo(48);
								t.setDesc("Literal");
								t.setNome(PrincipalGUI.AL.get(i));
								PrincipalGUI.ALfinal.add(t);
								r = 1;
							} else { // por fim se for um identificador.
								LexicoToken t = new LexicoToken();
								t.setCodigo(25);
								t.setDesc("Identificador");
								t.setNome(PrincipalGUI.AL.get(i));
								PrincipalGUI.ALfinal.add(t);
								r = 1;
							}
						}
					}
				}
			}
		}
	}
}