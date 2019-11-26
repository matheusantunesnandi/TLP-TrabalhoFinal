package semantico;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import principal.Tela;

public class AcaoSem {
	
	static int[] IF = new int[10],
		  WHILE = new int[10],
		  REPEAT = new int[10],
		  PROCEDURE = new int[10],
		  CASE = new int[10],
		  CASE2 = new int[10],
		  FOR = new int[10];
	static int n_if=-1, n_while=-1, n_repeat=-1, n_proc=-1, n_case=-1, n_case2=-1, case_dsvs=0, n_for=-1;
	static int nvl=0, topo=2, n_var=0, n_par=0, ponteiro=0;
	static String literal, contexto, atribuicoes, aux_Tipo_Ident;
	static boolean parmtr=false;
	static ArrayList<Var> TS = new ArrayList<Var>();
	static ArrayList<Integer> ponteiro_proc = new ArrayList<Integer>();
//	static ArrayList<String> AL_DSVS = new ArrayList<String>();
	
	public AcaoSem(){}
	
	public static void Zerar(){
		for(int i=0; i<10; i++){
			IF[i] = 0;
			WHILE[i] = 0;
			REPEAT[i] = 0;
			PROCEDURE[i] = 0;
			CASE[i] = 0;
			CASE2[i] = 0;
			FOR[i] = 0;
		}
		n_if=-1;		n_while=-1;		n_repeat=-1;		n_proc=-1;		n_case=-1;		n_case2=-1;		n_for=-1;
		literal="";		contexto="";		atribuicoes="";
		nvl=0;		topo=2;		n_var=0;
		aux_Tipo_Ident="";
		TS.removeAll(TS);
//		AL_DSVS.removeAll(AL_DSVS);
	}
	
	public static Boolean Acao(int i){
		boolean b = false;
		try{
			switch (i){
			case 100:
				try{
					
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 101:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(26);
					lc.setCod("PARA");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 102:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(24);
					lc.setCod("AMEM");
					lc.setOp1(0);
					lc.setOp2(n_var);
					principal.Tela.AL_Instr.add(lc);
					n_var=0;
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 103:
				try{
					aux_Tipo_Ident="rótulo";
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 104:
				try{
					if(aux_Tipo_Ident.equals("rótulo")){
						if(Varrer(semantico.Semantico.A.get(semantico.Semantico.i).getNome())){
							Var v = new Var();
							v.setNome(semantico.Semantico.A.get(semantico.Semantico.i).getNome());
							v.setCategoria(aux_Tipo_Ident);
							v.setNivel(nvl);
							v.setGA(0);
							v.setGB(0);
							v.setPROX(0);
							TS.add(v);
							b=true;
						}else{
							Erro(1);
						}
					}else{
						if(aux_Tipo_Ident.equals("variável")){
							if(Varrer(semantico.Semantico.A.get(semantico.Semantico.i).getNome())){
								Var v = new Var();
								v.setNome(semantico.Semantico.A.get(semantico.Semantico.i).getNome());
								v.setCategoria(aux_Tipo_Ident);
								v.setNivel(nvl);
								v.setGA(n_var+3);
								v.setGB(0);
								v.setPROX(0);
								TS.add(v);
								n_var+=1;
								b=true;
							}else{
								Erro(1);
							}
						}else{
							if(aux_Tipo_Ident.equals("parametros")){
								if(Varrer(semantico.Semantico.A.get(semantico.Semantico.i).getNome())){
									Var v = new Var();
									v.setNome(semantico.Semantico.A.get(semantico.Semantico.i).getNome());
									v.setCategoria(aux_Tipo_Ident);
									v.setNivel(nvl);
									v.setGA(n_par);
									v.setGB(0);
									v.setPROX(0);
									TS.add(v);
									n_par+=1;
									b=true;
								}else{
									Erro(1);
								}
							}else{
								Erro(0);
							}
						}
					}
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 105:
				try{
					if(Varrer(semantico.Semantico.A.get(semantico.Semantico.i).getNome())){
						Var v = new Var();
						v.setNome(semantico.Semantico.A.get(semantico.Semantico.i).getNome());
						v.setCategoria("constante");
						v.setNivel(nvl);
						v.setGA(0);
						v.setGB(0);
						v.setPROX(0);
						TS.add(v);
						b=true;
					}else{
						Erro(1);
					}
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 106:
				try{
					TS.get(TS.size()-1).setGA(Integer.parseInt(semantico.Semantico.A.get(semantico.Semantico.i).getNome()));
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 107:
				try{
					aux_Tipo_Ident="variável";
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 108:
				try{
					if(Varrer(semantico.Semantico.A.get(semantico.Semantico.i).getNome())){
						Var v = new Var();
						v.setNome(semantico.Semantico.A.get(semantico.Semantico.i).getNome());
						v.setCategoria("procedure");
						v.setNivel(nvl);
						v.setGA(principal.Tela.AL_Instr.size()+1);
						v.setGB(0);
						v.setPROX(0);
						TS.add(v);
						nvl+=1;
						b=true;
					}else{
						Erro(1);
					}
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 109:
				try{
					if(parmtr==true){
						TS.get((TS.size())-(n_par)-1).setGB(n_par);
						int j=1;
						while(n_par>0){
							TS.get(TS.size()-n_par).setGA(-j);
							j+=1;
							n_par-=1;
						}
					}
					Instrucao lc = new Instrucao();
					lc.setSeq(19);
					lc.setCod("DSVS");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					n_proc+=1;
					PROCEDURE[n_proc]=principal.Tela.AL_Instr.size()-1;
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 110:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(1);
					lc.setCod("RETU");
					lc.setOp1(0);
					lc.setOp2(n_par+1);
					principal.Tela.AL_Instr.add(lc);
					nvl-=1;
					principal.Tela.AL_Instr.get(PROCEDURE[n_proc]).setOp2(principal.Tela.AL_Instr.size());
					for(int j=0; j<TS.size(); j++){
						if(TS.get(j).getNivel()>nvl){
							TS.remove(j);
						}
					}
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 111:
				try{
					aux_Tipo_Ident="parametros";
					parmtr=true;
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 112:
				try{
					
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 113:
				try{
					String tkn = semantico.Semantico.A.get(semantico.Semantico.i-1).getNome();
					if(!Varrer(tkn)){
						if("rótulo".equals(Tipo(tkn))){
							int j=Indice(tkn);
							if(TS.get(j).getGA()==0){
								if(j==nvl){
									TS.get(j).setGA(principal.Tela.AL_Instr.size());
									b=true;
								}else{
									Erro(4);
								}
							}else{
								Erro(6);
							}
						}else{
							Erro(5);
						}
					}else{
						Erro(2);
					}
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 114:
				try{
					if(!Varrer(semantico.Semantico.A.get(semantico.Semantico.i).getNome())){
						if(Tipo(semantico.Semantico.A.get(semantico.Semantico.i).getNome()).equals("variável")){
							atribuicoes=semantico.Semantico.A.get(semantico.Semantico.i).getNome();
						}else{
							Erro(3);
						}
					}else{
						Erro(2);
					}
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 115:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(4);
					lc.setCod("ARMZ");
					for(int j=0; j<TS.size(); j++){
						if(atribuicoes.equals(TS.get(j).getNome())){
							lc.setOp1(nvl-(TS.get(j).getNivel()));
							lc.setOp2(TS.get(j).getGA());
						}
					}
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
					Erro(i);
				}
				break;
			case 116:
				try{
					if(Tipo(semantico.Semantico.A.get(semantico.Semantico.i).getNome()).equals("procedure")){
						ponteiro=TS.get(Indice(semantico.Semantico.A.get(semantico.Semantico.i).getNome())).getGA();
					}else{
						if(Varrer(semantico.Semantico.A.get(semantico.Semantico.i).getNome())){
							Erro(8);
						}else{
							Erro(2);
						}
					}
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 117:
				try{
					int temp;
					if(n_par==0){
						temp = Indice(semantico.Semantico.A.get((semantico.Semantico.i)-(n_par)).getNome());
					}else{
						temp = Indice(semantico.Semantico.A.get((semantico.Semantico.i)-(n_par)-2).getNome());
					}
					if(TS.get(temp).getGB() == n_par){
						Instrucao lc = new Instrucao();
						lc.setSeq(25);
						lc.setCod("CALL");
						lc.setOp1(nvl-(TS.get(temp).getNivel()));
						lc.setOp2(TS.get(temp).getGA());
						principal.Tela.AL_Instr.add(lc);
						b=true;
					}else{
						Erro(9);
					}
					n_par=0;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 118:
				try{
					
					n_par+=1;;
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 119:
				try{
					if(!Varrer(semantico.Semantico.A.get(semantico.Semantico.i).getNome())){
						if("rótulo".equals(Tipo(semantico.Semantico.A.get(semantico.Semantico.i).getNome()))){
							int j=Indice(semantico.Semantico.A.get(semantico.Semantico.i).getNome());
							if(j==nvl){
								if(TS.get(j).getGA()!=0){
									Instrucao lc = new Instrucao();
									lc.setSeq(19);
									lc.setCod("DSVS");
									lc.setOp1(0);
									lc.setOp2(TS.get(j).getGA());
									principal.Tela.AL_Instr.add(lc);
								}else{
									Erro(7);
								}
							}else{
								Erro(4);
							}
						}else{
							Erro(5);
						}
					}else{
						Erro(2);
					}
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 120:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(20);
					lc.setCod("DSVF");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					n_if+=1;
					IF[n_if]=principal.Tela.AL_Instr.size()-1;
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 121:
				try{
					principal.Tela.AL_Instr.get(IF[n_if]).setOp2(principal.Tela.AL_Instr.size());
					n_if-=1;
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 122:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(19);
					lc.setCod("DSVS");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					// ainda faz parte do comando principal
					// aqui começaa o else
					principal.Tela.AL_Instr.get(IF[n_if]).setOp2(principal.Tela.AL_Instr.size());
					IF[n_if]=principal.Tela.AL_Instr.size()-1;
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 123:
				try{
					n_while+=1;
					WHILE[n_while]=principal.Tela.AL_Instr.size();
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 124:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(20);
					lc.setCod("DSVF");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					n_while+=1;
					WHILE[n_while]=(principal.Tela.AL_Instr.size()-1);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 125:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(19);
					lc.setCod("DSVS");
					lc.setOp1(0);
					lc.setOp2(WHILE[n_while-1]);
					principal.Tela.AL_Instr.add(lc);
					principal.Tela.AL_Instr.get(WHILE[n_while]).setOp2(principal.Tela.AL_Instr.size());
					n_if-=2;
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 126:
				try{
					n_repeat+=1;
					REPEAT[n_repeat]=principal.Tela.AL_Instr.size();
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 127:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(29);
					lc.setCod("DSVT");
					lc.setOp1(0);
					lc.setOp2(REPEAT[n_repeat]);
					principal.Tela.AL_Instr.add(lc);
					n_repeat-=1;
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 128:
				try{
					contexto="readln";
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 129:
				try{
					String temp = semantico.Semantico.A.get(semantico.Semantico.i).getNome();
					if(!Varrer(temp)){
						if(contexto.equals("readln")){
							if("variável".equals(Tipo(temp))){
								Instrucao lc = new Instrucao();
								lc.setSeq(21);
								lc.setCod("LEIT");
								lc.setOp1(0);
								lc.setOp2(0);
								principal.Tela.AL_Instr.add(lc);
								Instrucao lc2 = new Instrucao();
								lc2.setSeq(4);
								lc2.setCod("ARMZ");
								for(int j=0; j<TS.size(); j++){
									if(semantico.Semantico.A.get(semantico.Semantico.i).getNome().equals(TS.get(j).getNome())){
										lc2.setOp1(nvl-(TS.get(j).getNivel()));
										lc2.setOp2(TS.get(j).getGA());
									}
								}
								principal.Tela.AL_Instr.add(lc2);
								b=true;
							}else{
								Erro(3);
							}
						}else{
							for(int u=0; u<TS.size(); u++){
								if(semantico.Semantico.A.get(semantico.Semantico.i).getNome().equals(TS.get(u).getNome())){
									if(TS.get(u).getCategoria().equals("constante")){
										Instrucao lc = new Instrucao();
										lc.setSeq(3);
										lc.setCod("CRCT");
										lc.setOp1(0);
										lc.setOp2(TS.get(u).getGA());
										principal.Tela.AL_Instr.add(lc);
										b=true;
									}else{
										if(TS.get(u).getCategoria().equals("variável")){
											Instrucao lc = new Instrucao();
											lc.setSeq(2);
											lc.setCod("CRVL");
											lc.setOp1(nvl-(TS.get(u).getNivel()));
											lc.setOp2(TS.get(u).getGA());
											principal.Tela.AL_Instr.add(lc);
											b=true;
										}else{
											if(TS.get(u).getCategoria().equals("parametros")){
												Instrucao lc = new Instrucao();
												lc.setSeq(2);
												lc.setCod("CRVL");
												lc.setOp1(0/*nvl-(TS.get(u).getNivel())*/);
												lc.setOp2(TS.get(u).getGA());
												principal.Tela.AL_Instr.add(lc);
												b=true;
											}else{
												Erro(3);
											}
										}
									}
								}
							}
						}
					}else{
						Erro(2);
					}
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 130:
				try{
					String temp = semantico.Semantico.A.get(semantico.Semantico.i).getNome();
					temp.replaceAll("\"", "");
					Instrucao lc = new Instrucao();
					lc.setSeq(23);
					lc.setCod("IMPRL");
					lc.setOp1(principal.Tela.AreaLiterais.length());
					principal.Tela.AreaLiterais+=temp;
					lc.setOp2(principal.Tela.AreaLiterais.length()-1);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 131:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(22);
					lc.setCod("IMPR");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 132:
				try{
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 133:
				try{
					for(int j=0; j<=n_case2; j++){
						principal.Tela.AL_Instr.get(CASE2[j]).setOp2(principal.Tela.AL_Instr.size());
					}
					n_case2=-1;
					Instrucao lc = new Instrucao();
					lc.setSeq(24);
					lc.setCod("AMEN");
					lc.setOp1(0);
					lc.setOp2(-1);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 134:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(19);
					lc.setCod("DSVS");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					case_dsvs=principal.Tela.AL_Instr.size()-1;
					for(int j=0; j<(n_case+1); j++){
						principal.Tela.AL_Instr.get(CASE[j]).setOp2(principal.Tela.AL_Instr.size());
					}
					n_case=-1;
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 135:
				try{
					Instrucao lc2 = new Instrucao();
					lc2.setSeq(19);
					lc2.setCod("DSVS");
					lc2.setOp1(0);
					lc2.setOp2(0);
					principal.Tela.AL_Instr.add(lc2);
					n_case2+=1;
					CASE2[n_case2]=principal.Tela.AL_Instr.size()-1;
					principal.Tela.AL_Instr.get(case_dsvs).setOp2(principal.Tela.AL_Instr.size());
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 136:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(28);
					lc.setCod("COPI");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					Instrucao lc2 = new Instrucao();
					lc2.setSeq(3);
					lc2.setCod("CRCT");
					lc2.setOp1(0);
					lc2.setOp2(Integer.parseInt(semantico.Semantico.A.get(semantico.Semantico.i).getNome()));
					principal.Tela.AL_Instr.add(lc2);
					Instrucao lc3 = new Instrucao();
					lc3.setSeq(15);
					lc3.setCod("CMIG");
					lc3.setOp1(0);
					lc3.setOp2(0);
					principal.Tela.AL_Instr.add(lc3);
					Instrucao lc4 = new Instrucao();
					lc4.setSeq(29);
					lc4.setCod("DSVT");
					lc4.setOp1(0);
					lc4.setOp2(0);
					principal.Tela.AL_Instr.add(lc4);
					n_case+=1;
					CASE[n_case]=principal.Tela.AL_Instr.size()-1;
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 137:
				try{
					if(!Varrer(semantico.Semantico.A.get(semantico.Semantico.i).getNome())){
						n_for+=1;
						for(int k=0; k<TS.size(); k++){								
							if(semantico.Semantico.A.get(semantico.Semantico.i).getNome().equals(TS.get(k).getNome())){
								if("variável".equals(Tipo(semantico.Semantico.A.get(semantico.Semantico.i).getNome()))){
									FOR[n_for]=k;
								}else{
									Erro(3);
								}
							}
						}
						b=true;
					}else{
						Erro(2);
					}
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 138:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(4);
					lc.setCod("ARMZ");
					lc.setOp1(0);
					lc.setOp2(TS.get(FOR[n_for]).getGA());
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 139:
				try{
					n_for+=1;
					FOR[n_for]=principal.Tela.AL_Instr.size();
					Instrucao lc = new Instrucao();
					lc.setSeq(28);
					lc.setCod("COPI");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					Instrucao lc2 = new Instrucao();
					lc2.setSeq(2);
					lc2.setCod("CRVL");
					lc2.setOp1(0);
					lc2.setOp2(TS.get(FOR[n_for-1]).getGA());
					principal.Tela.AL_Instr.add(lc2);
					Instrucao lc3 = new Instrucao();
					lc3.setSeq(18);
					lc3.setCod("CMAI");
					lc3.setOp1(0);
					lc3.setOp2(0);
					principal.Tela.AL_Instr.add(lc3);
					Instrucao lc4 = new Instrucao();
					lc4.setSeq(20);
					lc4.setCod("DSVF");
					lc4.setOp1(0);
					lc4.setOp2(0);
					principal.Tela.AL_Instr.add(lc4);
					n_for+=1;
					FOR[n_for]=principal.Tela.AL_Instr.size()-1;
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 140:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(2);
					lc.setCod("CRVL");
					lc.setOp1(0);
					lc.setOp2(TS.get(FOR[n_for-2]).getGA());
					principal.Tela.AL_Instr.add(lc);
					Instrucao lc2 = new Instrucao();
					lc2.setSeq(3);
					lc2.setCod("CRCT");
					lc2.setOp1(0);
					lc2.setOp2(1);
					principal.Tela.AL_Instr.add(lc2);
					Instrucao lc3 = new Instrucao();
					lc3.setSeq(5);
					lc3.setCod("SOMA");
					lc3.setOp1(0);
					lc3.setOp2(0);
					principal.Tela.AL_Instr.add(lc3);
					Instrucao lc4 = new Instrucao();
					lc4.setSeq(4);
					lc4.setCod("ARMZ");
					lc4.setOp1(0);
					lc4.setOp2(TS.get(FOR[n_for-2]).getGA());
					principal.Tela.AL_Instr.add(lc4);
					Instrucao lc5 = new Instrucao();
					lc5.setSeq(19);
					lc5.setCod("DSVS");
					lc5.setOp1(0);
					lc5.setOp2(FOR[n_for-1]);
					principal.Tela.AL_Instr.add(lc5);
					principal.Tela.AL_Instr.get(FOR[n_for]).setOp2(principal.Tela.AL_Instr.size());
					Instrucao lc6 = new Instrucao();
					lc6.setSeq(24);
					lc6.setCod("AMEN");
					lc6.setOp1(0);
					lc6.setOp2(-1);
					principal.Tela.AL_Instr.add(lc6);
					n_for-=3;
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 141:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(15);
					lc.setCod("CMIG");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);					
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 142:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(13);
					lc.setCod("CMME");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 143:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(14);
					lc.setCod("CMMA");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 144:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(18);
					lc.setCod("CMAI");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 145:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(17);
					lc.setCod("CMEI");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 146:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(16);
					lc.setCod("CMDF");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 147:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(9);
					lc.setCod("INVR");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 148:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(5);
					lc.setCod("SOMA");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 149:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(6);
					lc.setCod("SUBT");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 150:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(12);
					lc.setCod("DISJ");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 151:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(7);
					lc.setCod("MULT");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 152:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(8);
					lc.setCod("DIVI");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 153:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(11);
					lc.setCod("CONJ");
					lc.setOp1(0);
					lc.setOp2(0);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 154:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(3);
					lc.setCod("CRCT");
					lc.setOp1(0);
					lc.setOp2(Integer.parseInt(semantico.Semantico.A.get(semantico.Semantico.i).getNome()));
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 155:
				try{
					
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 156:
				try{
					contexto="expressão";
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			case 157:
				try{
					Instrucao lc = new Instrucao();
					lc.setSeq(24);
					lc.setCod("AMEN");
					lc.setOp1(0);
					lc.setOp2(-1);
					principal.Tela.AL_Instr.add(lc);
					b=true;
				}catch (Exception e) {
					Erro(i);
				}
				break;
			}
		}catch (Exception e) {
			Erro(i);
		}
		return b;
	}
	
	public static boolean Varrer(String nm){
		boolean b=true;
		for(int i=0; i<TS.size(); i++){
			if(nm.equals(TS.get(i).getNome())){
				b=false;
			}
		}
		return b;
	}
	
	public static String Tipo(String nm){
		String resposta = "nao tem";
		for(int i=0; i<TS.size(); i++){
			if(TS.get(i).getNome().equals(nm)){
				resposta = TS.get(i).getCategoria();
			}
		}
		return resposta;
	}
	
	public static int Indice(String nm){
		int b=-1;
		for(int i=0; i<TS.size(); i++){
			if(nm.equals(TS.get(i).getNome())){
				b=i;
			}
		}
		return b;
	}
	
	public static void Erro(int j){
		switch(j){
		case 1:
			Tela.Erro_Sem.add("O identificador já foi declarado - "+(semantico.Semantico.i+1)+"° token");
			break;
		case 2:
			Tela.Erro_Sem.add("O identificador não foi declarado - "+(semantico.Semantico.i+1)+"° token");
			break;
		case 3:
			Tela.Erro_Sem.add("É possivel atribuir valores apenas a varáveis - "+(semantico.Semantico.i+1)+"° token");
			break;
		case 4:
			Tela.Erro_Sem.add("Você não pode usar um rótulo declarado em um nível diferente - "+(semantico.Semantico.i+1)+"° token");
			break;
		case 5:
			Tela.Erro_Sem.add("Esse identificador não é um rótulo - "+(semantico.Semantico.i+1)+"° token");
			break;
		case 6:
			Tela.Erro_Sem.add("Esse rótulo já foi usado - "+(semantico.Semantico.i+1)+"° token");
			break;
		case 7:
			Tela.Erro_Sem.add("Esse rótulo não aponta para nenhum lugar - "+(semantico.Semantico.i+1)+"° token");
			break;
		case 8:
			Tela.Erro_Sem.add("Esse identificador não é uma procedure - "+(semantico.Semantico.i+1)+"° token");
			break;
		case 9:
			Tela.Erro_Sem.add("A procedure possui um número de parametros diferente - "+(semantico.Semantico.i+1)+"° token");
			break;
		case 100:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 101:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 102:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 103:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 104:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 105:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 106:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 107:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 108:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 109:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 110:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 111:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 112:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 113:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 114:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 115:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 116:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 117:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 118:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 119:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 120:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 121:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 122:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 123:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 124:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 125:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 126:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 127:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 128:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 129:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 130:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 131:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 132:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 133:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 134:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 135:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 136:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 137:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 138:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 139:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 140:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 141:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 142:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 143:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 144:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 145:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 146:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 147:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 148:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 149:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 150:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 151:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 152:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 153:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 154:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 155:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 156:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		case 157:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		default:
			JOptionPane.showMessageDialog(null, "erro na ação "+j);
			break;
		}
	}

}
