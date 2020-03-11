package model;

import java.util.ArrayList;

public class LexicoGTabela {
	public static ArrayList<String> AL = new ArrayList<String>();

	int r = 0;

	public LexicoGTabela() {
		int n = AL.size();
		for (int i = 0; i < n; i++) {
			r = 0;
			int m = LexicoTipoToken.AL_p.size();
			for (int j = 0; j < m; j++) { // Identifica se o token é uma palavra reservada
				if (AL.get(i).toLowerCase().equals(LexicoTipoToken.AL_p.get(j).getNome())) {
					LexicoToken t = new LexicoToken();
					t.setCodigo(LexicoTipoToken.AL_p.get(j).getCodigo());
					t.setDesc(LexicoTipoToken.AL_p.get(j).getDesc());
					t.setNome(LexicoTipoToken.AL_p.get(j).getNome());
					Lexico.ALfinal.add(t);
					j = m;
					r = 1;
				}
			}
			if (r == 0) {
				m = LexicoTipoToken.AL_o.size();
				for (int j = 0; j < m; j++) { // Identifica se o token é um operador
					if (AL.get(i).equals(LexicoTipoToken.AL_o.get(j).getNome())) {
						LexicoToken t = new LexicoToken();
						t.setCodigo(LexicoTipoToken.AL_o.get(j).getCodigo());
						t.setDesc(LexicoTipoToken.AL_o.get(j).getDesc());
						t.setNome(LexicoTipoToken.AL_o.get(j).getNome());
						Lexico.ALfinal.add(t);
						j = m;
						r = 1;
					}
				}
				if (r == 0) {
					m = LexicoTipoToken.AL_s.size();
					for (int j = 0; j < m; j++) { // Identifica se o token é um simbulo especial
						if (AL.get(i).equals(LexicoTipoToken.AL_s.get(j).getNome())) {
							LexicoToken t = new LexicoToken();
							t.setCodigo(LexicoTipoToken.AL_s.get(j).getCodigo());
							t.setDesc(LexicoTipoToken.AL_s.get(j).getDesc());
							t.setNome(LexicoTipoToken.AL_s.get(j).getNome());
							Lexico.ALfinal.add(t);
							j = m;
							r = 1;
						}
					}
					if (r == 0) {
						if (Character.isDigit(AL.get(i).charAt(0))) { // caso seja um inteiro
							LexicoToken t = new LexicoToken();
							t.setCodigo(26);
							t.setDesc("Inteiro");
							t.setNome(AL.get(i));
							Lexico.ALfinal.add(t);
							r = 1;
						} else {
							if (AL.get(i).charAt(0) == '\"') { // caso seja um literal
								LexicoToken t = new LexicoToken();
								t.setCodigo(48);
								t.setDesc("Literal");
								t.setNome(AL.get(i));
								Lexico.ALfinal.add(t);
								r = 1;
							} else { // por fim se for um identificador.
								LexicoToken t = new LexicoToken();
								t.setCodigo(25);
								t.setDesc("Identificador");
								t.setNome(AL.get(i));
								Lexico.ALfinal.add(t);
								r = 1;
							}
						}
					}
				}
			}
		}
	}
}