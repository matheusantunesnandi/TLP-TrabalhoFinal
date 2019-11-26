package sintatico;

import lexico.Token;
import principal.Tela;

import java.util.ArrayList;

public class Sintatico {
	
	public static ArrayList<Token> A = null;
	int i=0, a=0, e=0;
	
	@SuppressWarnings("unchecked")
	public Sintatico(){
		
		A=(ArrayList<Token>) Tela.ALfinal.clone();
		
		if(PROGRAMA()){
			Tela.Erro_Sin.removeAll(Tela.Erro_Sin);
			Tela.Erro_Sin.add("Codigo analizado com sucesso !");
		}				
	}
	
	public boolean IDENT(int i){
		boolean b=false;
		try{
			if(A.get(i).getDesc().equals("Identificador")){
				b=true;
			}
		}catch (IndexOutOfBoundsException e) {			
			Erro(1);
			return b;
		}catch (Exception e){
			Erro(0);
		}
		return b;
	}
	public boolean LITERAL(int i){
		boolean b=false;
		try{
			if(A.get(i).getDesc().equals("Literal")){
				b=true;
			}
		}catch (IndexOutOfBoundsException e) {			
			Erro(2);
		}catch (Exception e){
			Erro(0);
		}
		return b;
	}
	public boolean INTEIRO(int i){
		boolean b=false;
		try{
			if(A.get(i).getDesc().equals("Inteiro")){
				b=true;
			}
		}catch (IndexOutOfBoundsException e) {			
			Erro(3);
		}catch (Exception e){
			Erro(0);
		}
		return b;
	}

	public boolean PROGRAMA(){
	boolean b=false;
	a=i;
	try{
		if(A.get(i).getNome().toLowerCase().equals("program")){
			i+=1;
			if(IDENT(i)){
				i+=1;
				if(A.get(i).getNome().toLowerCase().equals(";")){
					//i+=1; //Nao estava aqui
					if(BLOCO()){
						i+=1;
						if(A.get(i).getNome().toLowerCase().equals(".")){
							b=true;
							if(A.size()>i+1){
								Erro(20);
							}
						}else{
							Erro(7);
						}
					}
				}else{
					Erro(21);
				}
			}else{
				Erro(1);
			}
		}else{
			Erro(5);
		}
	}catch (IndexOutOfBoundsException e) {
		i=a;
		Erro(5);
	}catch (Exception e){
		i=a;
		Erro(0);
	}
	return b;
}

	public boolean BLOCO() {
		boolean b=false;
		if(DCLROT(false)&&DCLCONST(false)&&DCLVAR(false)&&DCLPROC(false)&&CORPO(true)){
			b=true;
		}
		return b;
	}
	public boolean CORPO(boolean q){
		boolean b=false;
		a=i; e=0;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("begin")){
				if(COMANDO(false)){
					if(REPCOMANDO(false)){
						i+=1;
						if(A.get(i).getNome().toLowerCase().equals("end")){
							b=true;
						}else{
							Erro(11);
						}
					}
				}
			}else{
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(10);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	
	public boolean COMANDO(boolean q){
		boolean b=false;
		a= i;
		i+=1;
		try{
		if(A.get(i).getNome().toLowerCase().equals("for")){
			i+=1;
			if(IDENT(i)){
				i+=1;
				if(A.get(i).getNome().toLowerCase().equals(":=")){
					if(EXPRESSAO()){
						i+=1;
						if(A.get(i).getNome().toLowerCase().equals("to")){
							if(EXPRESSAO()){
								i+=1;
								if(A.get(i).getNome().toLowerCase().equals("do")){
									if(COMANDO(false)){
										b = true;
									}else{
										Erro(22);
									}									
								}else{
									Erro(22);
								}
							}else{
								Erro(22);
							}							
						}else{
							Erro(22);
						}
					}else{
						Erro(22);
					}
				}else{
					Erro(22);
				}
			}else{
				Erro(22);
			}
		}else{
			i=a;
			i+=1;
			if(A.get(i).getNome().toLowerCase().equals("call")){
				i+=1;
				if(IDENT(i)){
					if(PARAMETROS(false)){
						b=true;
					}else{
						Erro(23);
					}
				}else{
					Erro(23);
				}
			}else{
				i=a;
				i+=1;
				if(A.get(i).getNome().toLowerCase().equals("goto")){
					i+=1;
					if(IDENT(i)){
						b=true;
					}else{
						Erro(24);
					}
				}else{
					i=a;
					i+=1;
					if(A.get(i).getNome().toLowerCase().equals("if")){
						if(EXPRESSAO()){
							i+=1;
							if(A.get(i).getNome().toLowerCase().equals("then")){
								if(COMANDO(false)){
									if(ELSEPARTE(false)){
										b=true;
									}else{
										Erro(25);
									}
								}else{
									Erro(25);
								}
							}else{
								Erro(25);
							}
						}else{
							Erro(25);
						}
					}else{
						i=a;
						i+=1;
						if(A.get(i).getNome().toLowerCase().equals("while")){
							if(EXPRESSAO()){
								i+=1;
								if(A.get(i).getNome().toLowerCase().equals("do")){
									if(COMANDO(false)){
										b=true;
									}else{
										Erro(26);
									}
								}else{
									Erro(26);
								}
							}else{
								Erro(26);
							}
						}else{
							i=a;
							i+=1;
							if(A.get(i).getNome().toLowerCase().equals("repeat")){
								if(COMANDO(true)){
									i+=1;
									if(A.get(i).getNome().toLowerCase().equals("until")){
										if(EXPRESSAO()){
											b=true;
										}else{
											Erro(27);
										}
									}else{
										Erro(27);
									}
								}else{
									Erro(27);
								}
							}else{
								i=a;
								i+=1;
								if(A.get(i).getNome().toLowerCase().equals("readln")){
									i+=1;
									if(A.get(i).getNome().toLowerCase().equals("(")){
										if(VARIAVEL()){
											if(REPVARIAVEL(false)){
												i+=1;
												if(A.get(i).getNome().toLowerCase().equals(")")){
													b=true;
												}else{
													Erro(28);
												}
											}else{
												Erro(28);
											}
										}else{
											Erro(28);
										}
									}else{
										Erro(28);
									}
								}else{
									i=a;
									i+=1;
									if(A.get(i).getNome().toLowerCase().equals("writeln")){
										i+=1;
										if(A.get(i).getNome().toLowerCase().equals("(")){
											if(ITEMSAIDA()){
												if(REPITEM(false)){
													i+=1;
													if(A.get(i).getNome().toLowerCase().equals(")")){
														b=true;
													}else{
														Erro(29);
													}
												}else{
													Erro(29);
												}
											}else{
												Erro(29);
											}
										}else{
											Erro(29);
										}
									}else{
										i=a;
										i+=1;
										if(A.get(i).getNome().toLowerCase().equals("case")){
											if(EXPRESSAO()){
												i+=1;
												if(A.get(i).getNome().toLowerCase().equals("of")){
													if(CONDCASE()){
														i+=1;
														if(A.get(i).getNome().toLowerCase().equals("end")){
															b=true;
														}else{
															Erro(30);
														}
													}else{
														Erro(30);
													}
												}else{
													Erro(30);
												}
											}else{
												Erro(30);
											}
										}else{
											i=a;
											i+=1;
											if(IDENT(i)){
												if(RCOMID()){
													b=true;
												}else{
													i=a;
													if(q==false){
														b=true;
													}
												}
											}else{
												i=a;
												if(CORPO(false)){
													b=true;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if(!b){
			i=a;
			if(q==false){
				b=true;
			}else{
				Erro(8);
			}
		}
		return b;
		}catch (IndexOutOfBoundsException e) {
			Erro(5);
		}catch (Exception e){
			Erro(0);
		}
		return b;
	}
	public boolean CONDCASE(){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(INTEIRO(i)){
				if(RPINTEIRO(false)){
					i+=1;
					if(A.get(i).getNome().toLowerCase().equals(":")){
						if(COMANDO(true)){
							if(CONTCASE(false)){
								b=true;
							}
						}
					}else{
						Erro(9);
					}
				}
			}else{
				Erro(3);
			}
			if(!b){
				i=a;
				Erro(4);			
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean CONTCASE(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals(";")){
				if(CONDCASE()){
					b=true;
				}
			}
			if(!b){
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(6);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean DCLCONST(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("const")){
				i+=1;
				if(IDENT(i)){
					i+=1;
					if(A.get(i).getNome().toLowerCase().equals("=")){
						i+=1;
						if(INTEIRO(i)){
							i+=1;
							if(A.get(i).getNome().toLowerCase().equals(";")){
								if(LDCONST(false)){
									b=true;
								}
							}else{
								Erro(6);
							}
						}else{
							Erro(12);
						}
					}else{
						Erro(12);
					}
				}else{
					Erro(12);
				}
			}else{
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(12);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean DCLPROC(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("procedure")){
				i+=1;
				if(IDENT(i)){
					if(DEFPAR(false)){
						i+=1;
						if(A.get(i).getNome().toLowerCase().equals(";")){
							if(BLOCO()){
								i+=1;
								if(A.get(i).getNome().toLowerCase().equals(";")){
									if(DCLPROC(false)){
										b=true;
									}
								}else{
									Erro(6);
								}
							}else{
								Erro(13);
							}
						}else{
							Erro(6);
						}
					}else{
						Erro(13);
					}
				}else{
					Erro(13);
				}
			}else{
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(13);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean DCLROT(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("label")){
				if(LID(true)){
					i+=1;
					if(A.get(i).getNome().toLowerCase().equals(";")){
						b=true;
					}else{
						Erro(6);
					}
				}else{
					Erro(14);
				}
			}else{
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(14);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean DCLVAR(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("var")){
				if(LID(true)){ //Um ou mais identificadores
					i+=1;
					if(A.get(i).getNome().toLowerCase().equals(":")){
						if(TIPO()){
							i+=1;
							if(A.get(i).getNome().toLowerCase().equals(";")){
								if(LDVAR(false)){
									b=true;
								}else{
									Erro(15);
								}
							}else{
								Erro(6);
							}
						}else{
							Erro(15);
						}
					}else{
						Erro(15);
					}
				}else{
					Erro(15);
				}
			}else{
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(15);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean DEFPAR(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("(")){
				if(LID(true)){
					i+=1;
					if(A.get(i).getNome().toLowerCase().equals(":")){
						i+=1;
						if(A.get(i).getNome().toLowerCase().equals("integer")){
							i+=1;
							if(A.get(i).getNome().toLowerCase().equals(")")){
								b=true;
							}else{
								Erro(33);
							}
						}else{
							Erro(33);
						}
					}else{
						Erro(33);
					}
				}else{
					Erro(33);
				}
			}else{
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(0);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean ELSEPARTE(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("else")){
				if(COMANDO(false)){
					b=true;
				}
			}
			if(!b){
				i=a;
				if(q==false){
					b=true;
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean EXPRESSAO(){
		boolean b=false;
		try{
			if(EXPSINP()){
				if(REPEXPSIMP(false)){
					b=true;
				}
			}
			if(!b){
				Erro(4);
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean EXPSINP(){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("-")||A.get(i).getNome().toLowerCase().equals("+")){
				if(TERMO()){
					if(REPEXP(false)){
						b=true;
					}
				}
			}else{
				i=a;
				if(TERMO()){
					if(REPEXP(false)){
						b=true;
					}
				}
			}
			if(!b){
				i=a;
				Erro(16);
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean FATOR(){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(INTEIRO(i)){
				b=true;
			}else{
				i=a;
				i+=1;
				if(A.get(i).getNome().toLowerCase().equals("(")){
					if(EXPRESSAO()){
						i+=1;
						if(A.get(i).getNome().toLowerCase().equals(")")){
							b=true;
						}
					}
				}else{
					i=a;
					i+=1;
					if(A.get(i).getNome().toLowerCase().equals("not")){
						if(FATOR()){
							b=true;
						}
					}else{
						i=a;
						if(VARIAVEL()){
							b=true;
						}
					}
				}
			}
			if(!b){
				i=a;
				Erro(17);
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean ITEMSAIDA(){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(LITERAL(i)){
				b=true;
			}else{
				i=a;
				if(EXPRESSAO()){
					b=true;
				}
			}
			if(!b){
				i=a;
				Erro(4);
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean LDCONST(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(IDENT(i)){
				i+=1;
				if(A.get(i).getNome().toLowerCase().equals("=")){
					i+=1;
					if(INTEIRO(i)){
						i+=1;
						if(A.get(i).getNome().toLowerCase().equals(";")){
							if(LDCONST(false)){
								b=true;
							}
						}else{
							Erro(6);
						}
					}else{
						Erro(12);
					}
				}else{
					Erro(12);
				}
			}else{
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(18);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean LDVAR(boolean q){
		boolean b=false;
		a=i;
		try{
			if(LID(q)){
				i+=1;
				if(A.get(i).getNome().toLowerCase().equals(":")){
					if(TIPO()){
						i+=1;
						if(A.get(i).getNome().toLowerCase().equals(";")){
							if(LDVAR(false)){
								b=true;
							}
						}else{
							Erro(6);
						}
					}else{
						Erro(15);
					}
				}else{
					Erro(15);
				}
			}else{
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(0);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean LID(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(IDENT(i)){
				if(REPIDENT(false)){
					b=true;
				}
			}
			if(!b){
				i=a;
				if(q==true){
					Erro(4);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean PARAMETROS(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("(")){
				if(EXPRESSAO()){
					if(REPPAR(false)){
						i+=1;
						if(A.get(i).getNome().toLowerCase().equals(")")){
							b=true;
						}
					}
				}else{
					Erro(19);
				}
			}else{
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(19);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean RCOMID(){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals(":")){
				if(COMANDO(true)){
					b=true;
				}
			}else{
				i=a;
				if(RVAR(false)){
					i+=1;
					if(A.get(i).getNome().toLowerCase().equals(":=")){
						if(EXPRESSAO()){
							b=true;
						}
					}
				}
			}
			if(!b){
				i=a;
				Erro(4);
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean REPCOMANDO(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals(";")){
				if(COMANDO(true)){
					if(REPCOMANDO(false)){
						b=true;
					}
				}
			}else{
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(31);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean REPEXPSIMP(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("<>")){
				if(EXPSINP()){
					b=true;
				}
			}else{
				if(A.get(i).getNome().toLowerCase().equals("<=")){
					if(EXPSINP()){
						b=true;
					}
				}else{
					if(A.get(i).getNome().toLowerCase().equals("<")){
						if(EXPSINP()){
							b=true;
						}
					}else{
						if(A.get(i).getNome().toLowerCase().equals(">=")){
							if(EXPSINP()){
								b=true;
							}
						}else{
							if(A.get(i).getNome().toLowerCase().equals(">")){
								if(EXPSINP()){
									b=true;
								}
							}else{
								if(A.get(i).getNome().toLowerCase().equals("=")){
									if(EXPSINP()){
										b=true;
									}
								}
							}
						}
					}
				}
			}
			if(!b){
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(4);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean REPEXP(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("+")){
				if(TERMO()){
					if(REPEXP(false)){
						b=true;
					}
				}
			}else{
				if(A.get(i).getNome().toLowerCase().equals("-")){
					if(TERMO()){
						if(REPEXP(false)){
							b=true;
						}
					}
				}else{
					if(A.get(i).getNome().toLowerCase().equals("or")){
						if(TERMO()){
							if(REPEXP(false)){
								b=true;
							}
						}
					}
				}
			}
			if(!b){
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(4);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean REPIDENT(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals(",")){
				i+=1;
				if(IDENT(i)){
					if(REPIDENT(false)){
						b=true;
					}
				}else{
					Erro(1);
				}
			}if(!b){
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(4);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean REPITEM(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals(",")){
				if(ITEMSAIDA()){
					if(REPITEM(false)){
						b=true;
					}
				}
			}
			if(!b){
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(4);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean REPPAR(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals(",")){
				if(EXPRESSAO()){
					if(REPPAR(false)){
						b=true;
					}
				}
			}
			if(!b){
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(4);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean REPVARIAVEL(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals(",")){
				if(VARIAVEL()){
					if(REPVARIAVEL(false)){
						b=true;
					}
				}
			}
			if(!b){
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(4);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean REPTERMO(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("*")){
				if(FATOR()){
					if(REPTERMO(false)){
						b=true;
					}
				}
			}else{
				if(A.get(i).getNome().toLowerCase().equals("/")){
					if(FATOR()){
						if(REPTERMO(false)){
							b=true;
						}
					}
				}else{
					if(A.get(i).getNome().toLowerCase().equals("and")){
						if(FATOR()){
							if(REPTERMO(false)){
								b=true;
							}
						}
					}
				}
			}
			if(!b){
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(4);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean RPINTEIRO(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals(",")){
				i+=1;
				if(INTEIRO(i)){
					if(RPINTEIRO(false)){
						b=true;
					}
				}
			}
			if(!b){
				i=a;
				if(q==false){
					b=true;
				}else{
					Erro(4);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean RVAR(boolean q){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("[")){
				if(EXPRESSAO()){
					i+=1;
					if(A.get(i).getNome().toLowerCase().equals("]")){
						b=true;
					}
				}
			}
			if(!b){
				i=a;
				if(q==false){
					b=true;					
				}else{
					Erro(4);
				}
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean TERMO(){
		boolean b=false;
		a=i;
		try{
			if(FATOR()){
				if(REPTERMO(false)){
					b=true;
				}
			}
			if(!b){
				i=a;
				Erro(4);
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean TIPO(){
		boolean b=false;
		a=i;
		i+=1;
		try{
			if(A.get(i).getNome().toLowerCase().equals("integer")){
				b=true;
			}else{
				if(A.get(i).getNome().toLowerCase().equals("array")){
					i+=1;
					if(A.get(i).getNome().toLowerCase().equals("[")){
						i+=1;
						if(INTEIRO(i)){
							i+=1;
							if(A.get(i).getNome().toLowerCase().equals("..")){
								i+=1;
								if(INTEIRO(i)){
									i+=1;
									if(A.get(i).getNome().toLowerCase().equals("]")){
										i+=1;
										if(A.get(i).getNome().toLowerCase().equals("of")){
											i+=1;
											if(A.get(i).getNome().toLowerCase().equals("integer")){
												b=true;
											}else{
												Erro(32);
											}
										}else{
											Erro(32);
										}
									}else{
										Erro(32);
									}
								}else{
									Erro(32);
								}
							}else{
								Erro(32);
							}
						}else{
							Erro(32);
						}
					}else{
						Erro(32);
					}
				}else{
					Erro(32);
				}
			}
			if(!b){
				i=a;
				Erro(4);
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	public boolean VARIAVEL(){
		boolean b=false;	a=i;	e=0;
		i+=1;
		try{
			if(IDENT(i)){
				if(RVAR(false)){
					b=true;
				}
			}else{
				Erro(1);
			}
			if(!b){
				i=a;
				Erro(4);
			}
		}catch (IndexOutOfBoundsException e) {
			i=a;
			Erro(5);
		}catch (Exception e){
			i=a;
			Erro(0);
		}
		return b;
	}
	
	public void Erro(int j){
		switch(j){
		case 1:
			Tela.Erro_Sin.add("Defina um identificador - "+(i+1)+"° token");
			break;
		case 2:
			Tela.Erro_Sin.add("Literal Não especificado - "+(i+1)+"° token");
			break;
		case 3:
			Tela.Erro_Sin.add("Inteiro Não especificado - "+(i+1)+"° token");
			break;
		case 4:
			Tela.Erro_Sin.add("Codigo incompleto - "+(i+1)+"° token");
			break;
		case 5:
			Tela.Erro_Sin.add("Inicie o codigo com \"program\" - "+(i+1)+"° token");
			break;
		case 6:
			Tela.Erro_Sin.add("Complete a instrução com \";\" (ponto e vírgula) - "+(i+1)+"° token");
			break;
		case 7:
			Tela.Erro_Sin.add("Finalize o código com \".\" (ponto ) - "+(i+1)+"° token");
			break;
		case 8:
			Tela.Erro_Sin.add("Expecifique um comando - "+(i+1)+"° token");
			break;
		case 9:
			Tela.Erro_Sin.add("Complete a instrução com \":\" (dois pontos) - "+(i+1)+"° token");
			break;
		case 10:
			Tela.Erro_Sin.add("Inicie o corpo com \"begin\" - "+(i+2)+"° token");
			break;
		case 11:
			Tela.Erro_Sin.add("Finalize o corpo com \"end\" - "+(i+1)+"° token");
			break;
		case 12:
			Tela.Erro_Sin.add("Defina a costante - "+(i+1)+"° token");
			break;
		case 13:
			Tela.Erro_Sin.add("Defina uma procedure - "+(i+1)+"° token");
			break;
		case 14:
			Tela.Erro_Sin.add("Defina um label - "+(i+1)+"° token");
			break;
		case 15:
			Tela.Erro_Sin.add("Defina a(s) variável(is) - "+(i+1)+"° token");
			break;
		case 16:
			Tela.Erro_Sin.add("Defina o inicio da espressão - "+(i+1)+"° token");
			break;
		case 17:
			Tela.Erro_Sin.add("Você deve definir uma variável, um inteiro ou uma espressão - "+(i+1)+"° token");
			break;
		case 18:
			Tela.Erro_Sin.add("Defina a atribuição - "+(i+1)+"° token");
			break;
		case 19:
			Tela.Erro_Sin.add("Defina os parametros - "+(i+1)+"° token");
			break;
		case 20:
			Tela.Erro_Sin.add("Não escreva código depois do \"end.\"");
			break;
		case 21:
			Tela.Erro_Sin.add("Termine a primeira linha com \";\" (ponto e virgula) - "+(i+1)+"° token");
			break;
		case 22:
			Tela.Erro_Sin.add("Comando \"for\" incompleto - "+(i+1)+"° token");
			break;
		case 23:
			Tela.Erro_Sin.add("Comando \"call\" incompleto - "+(i+1)+"° token");
			break;
		case 24:
			Tela.Erro_Sin.add("Comando \"goto\" incompleto - "+(i+1)+"° token");
			break;
		case 25:
			Tela.Erro_Sin.add("Comando \"if\" incompleto - "+(i+1)+"° token");
			break;
		case 26:
			Tela.Erro_Sin.add("Comando \"while\" incompleto - "+(i+1)+"° token");
			break;
		case 27:
			Tela.Erro_Sin.add("Comando \"repeat\" incompleto - "+(i+1)+"° token");
			break;
		case 28:
			Tela.Erro_Sin.add("Comando \"readln\" incompleto - "+(i+1)+"° token");
			break;
		case 29:
			Tela.Erro_Sin.add("Comando \"writeln\" incompleto - "+(i+1)+"° token");
			break;
		case 30:
			Tela.Erro_Sin.add("Comando \"case\" incompleto - "+(i+1)+"° token");
			break;
		case 31:
			Tela.Erro_Sin.add("Separe comandos com \";\" (ponto e vigula) - "+(i+1)+"° token");
			break;
		case 32:
			Tela.Erro_Sin.add("Termine o comando \"array\" - "+(i+1)+"° token");
			break;
		case 33:
			Tela.Erro_Sin.add("Passagem de parametros incompleta ou errada - "+(i+1)+"° token");
			break;
		default:
			Tela.Erro_Sin.add("Erro! - "+(i+1)+"° token\n");
			break;
		}
	}
}