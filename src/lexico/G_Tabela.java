package lexico;

import principal.Tela;

public class G_Tabela {
	
	int r=0;
	
	public G_Tabela(){
		int n = Tela.AL.size();
		for(int i=0; i<n; i++){
			r=0;
			int m = Tela.AL_p.size();
			for(int j=0; j<m; j++){  //Identifica se o token é uma palavra reservada
				if(Tela.AL.get(i).toLowerCase().equals(Tela.AL_p.get(j).getNome())){
					Token t = new Token();
					t.setCodigo(Tela.AL_p.get(j).getCodigo());
					t.setDesc(Tela.AL_p.get(j).getDesc());
					t.setNome(Tela.AL_p.get(j).getNome());
					Tela.ALfinal.add(t);
					j=m;
					r=1;
				}
			}
			if(r==0){
				m = Tela.AL_o.size();
				for(int j=0; j<m; j++){  //Identifica se o token é um operador
					if(Tela.AL.get(i).equals(Tela.AL_o.get(j).getNome())){
						Token t = new Token();
						t.setCodigo(Tela.AL_o.get(j).getCodigo());
						t.setDesc(Tela.AL_o.get(j).getDesc());
						t.setNome(Tela.AL_o.get(j).getNome());
						Tela.ALfinal.add(t);
						j=m;
						r=1;
					}
				}
				if(r==0){
					m = Tela.AL_s.size();
					for(int j=0; j<m; j++){  //Identifica se o token é um simbulo especial
						if(Tela.AL.get(i).equals(Tela.AL_s.get(j).getNome())){
							Token t = new Token();
							t.setCodigo(Tela.AL_s.get(j).getCodigo());
							t.setDesc(Tela.AL_s.get(j).getDesc());
							t.setNome(Tela.AL_s.get(j).getNome());
							Tela.ALfinal.add(t);
							j=m;
							r=1;
						}
					}
					if(r==0){
						if(Character.isDigit(Tela.AL.get(i).charAt(0))){  // caso seja um inteiro
							Token t = new Token();
							t.setCodigo(26);
							t.setDesc("Inteiro");
							t.setNome(Tela.AL.get(i));
							Tela.ALfinal.add(t);
							r=1;
						}else{
							if(Tela.AL.get(i).charAt(0)=='\"'){  // caso seja um literal
								Token t = new Token();
								t.setCodigo(48);
								t.setDesc("Literal");
								t.setNome(Tela.AL.get(i));
								Tela.ALfinal.add(t);
								r=1;
							}else{  // por fim se for um identificador.
								Token t = new Token();
								t.setCodigo(25);
								t.setDesc("Identificador");
								t.setNome(Tela.AL.get(i));
								Tela.ALfinal.add(t);
								r=1;
							}
						}
					}
				}
			}
		}
	}
}