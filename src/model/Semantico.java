package model;

import java.util.ArrayList;

import application.PrincipalController;

public class Semantico {
	public static ArrayList<model.SemanticoInstrucao> AL_Instr = new ArrayList<model.SemanticoInstrucao>();
	public static String AreaLiterais = "";

	public static ArrayList<LexicoToken> A = null;
	SemanticoAcao ASem = new SemanticoAcao();
	static int i, a;

	@SuppressWarnings({ "unchecked" })
	public Semantico() {
//		Remove o que já estava na lista para preencher novamente com dados novos:
		SemanticoAcao.Erro_Sem = new ArrayList<String>();

//		Reseta / Zera / Remove todas as instruções:
		Semantico.AL_Instr = new ArrayList<model.SemanticoInstrucao>();
		AreaLiterais = "";

		A = (ArrayList<LexicoToken>) Lexico.ALfinal.clone();
		i = a = 0;
		SemanticoAcao.Zerar();

		if (!PROGRAMA())
			return;

		SemanticoAcao.Erro_Sem = new ArrayList<String>();
		SemanticoAcao.Erro_Sem.add("Codigo analizado com sucesso !");
		PrincipalController.analiseSemanticaComSucesso = true;
	}

	public boolean IDENT(int i) {
		return A.get(i).getDesc().equals("Identificador");
	}

	public boolean LITERAL(int i) {
		return A.get(i).getDesc().equals("Literal");
	}

	public boolean INTEIRO(int i) {
		return A.get(i).getDesc().equals("Inteiro");
	}

	public boolean PROGRAMA() {
		if (A == null || A.isEmpty())
			return false;

		a = i;
		if (!A.get(i).getNome().toLowerCase().equals("program"))
			return false;

		i++;
		;
		if (!IDENT(i) || !SemanticoAcao.Acao(100))
			return false;

		i++;
		;
		if (!A.get(i).getNome().equals(";") || !BLOCO())
			return false;

		i++;
		return A.get(i).getNome().toLowerCase().equals(".") && SemanticoAcao.Acao(101);
	}

	public boolean BLOCO() {
		if (DCLROT() || !DCLCONST(false) || !DCLVAR(false) || !DCLPROC(false))
			return false;

		return CORPO(true);
	}

	public boolean CORPO(boolean q) {
		a = i;

		i++;
		if (!A.get(i).getNome().toLowerCase().equals("begin") || !COMANDO(false) || !REPCOMANDO(false)) {
			i = a;
			return !q;
		}

		i++;
		if (!A.get(i).getNome().toLowerCase().equals("end")) {
			i = a;
			return !q;
		}

		return true;
	}

	public boolean COMANDO(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals("for") && !validarSemanticaComandoFor(b)) {

			b = validarSemanticaComandoFor(b);

		} else {
			i = a;
			i++;

			if (A.get(i).getNome().toLowerCase().equals("call")) {
				b = validarSemanticaComandoCall(b);

			} else {
				i = a;
				i++;

				if (A.get(i).getNome().toLowerCase().equals("goto")) {
					b = validarSemanticaComandoGoto(b);

				} else {
					i = a;
					i++;

					if (A.get(i).getNome().toLowerCase().equals("if")) {
						b = validarSemanticaComandoIf(b);

					} else {
						i = a;
						i++;

						if (A.get(i).getNome().toLowerCase().equals("while")) {
							b = validarSemanticaComandoWhile(b);

						} else {
							i = a;
							i++;

							if (A.get(i).getNome().toLowerCase().equals("repeat")) {
								b = validarSemanticaComandoRepeat(b);

							} else {
								i = a;
								i++;

								if (A.get(i).getNome().toLowerCase().equals("readln")) {
									b = validarSemanticaComandoReadln(b);

								} else {
									i = a;
									i++;

									if (A.get(i).getNome().toLowerCase().equals("writeln")) {
										b = validarSemanticaComandoWriteln(b);

									} else {
										i = a;
										i++;

										if (A.get(i).getNome().toLowerCase().equals("case")) {
											b = validarSemanticaComandoCase(b);

										} else {
											i = a;
											i++;

											if (IDENT(i)) {
												if (A.get(i + 1).getNome().toLowerCase().equals(":")) {
													if (RCOMID()) {
														b = true;
													}

												} else {
													SemanticoAcao.Acao(156);

													if (RCOMID()) {
														b = true;

													} else {
														i = a;
														if (q == false) {
															b = true;
														}
													}
												}
											} else {

												i = a;
												if (CORPO(false)) {
													b = true;
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
		if (!b) {
			i = a;
			b = !q;
		}
		return true;
	}

	private boolean validarSemanticaComandoCase(boolean b) {
		if (SemanticoAcao.Acao(132)) {
			if (EXPRESSAO()) {
				i++;
				if (A.get(i).getNome().toLowerCase().equals("of")) {
					if (CONDCASE()) {
						i++;
						if (A.get(i).getNome().toLowerCase().equals("end")) {
							if (SemanticoAcao.Acao(133)) {
								b = true;
							}
						}
					}
				}
			}
		}
		return b;
	}

	private boolean validarSemanticaComandoWriteln(boolean b) {
		i++;
		if (A.get(i).getNome().toLowerCase().equals("(")) {
			if (ITEMSAIDA()) {
				if (REPITEM(false)) {
					i++;
					if (A.get(i).getNome().toLowerCase().equals(")")) {
						b = true;
					}
				}
			}
		}
		return b;
	}

	private boolean validarSemanticaComandoReadln(boolean b) {
		if (SemanticoAcao.Acao(128)) {
			i++;
			if (A.get(i).getNome().toLowerCase().equals("(")) {
				if (VARIAVEL()) {
					if (REPVARIAVEL(false)) {
						i++;
						if (A.get(i).getNome().toLowerCase().equals(")")) {
							SemanticoAcao.Acao(156);
							b = true;
						}
					}
				}
			}
		}
		return b;
	}

	private boolean validarSemanticaComandoRepeat(boolean b) {
		if (SemanticoAcao.Acao(126)) {
			if (COMANDO(true)) {
				i++;
				if (A.get(i).getNome().toLowerCase().equals("until")) {
					if (EXPRESSAO()) {
						if (SemanticoAcao.Acao(127)) {
							b = true;
						}
					}
				}
			}
		}
		return b;
	}

	private boolean validarSemanticaComandoWhile(boolean b) {
		if (SemanticoAcao.Acao(123)) {
			if (EXPRESSAO()) {
				if (SemanticoAcao.Acao(124)) {
					i++;
					if (A.get(i).getNome().toLowerCase().equals("do")) {
						if (COMANDO(false)) {
							if (SemanticoAcao.Acao(125)) {
								b = true;
							}
						}
					}
				}
			}
		}
		return b;
	}

	private boolean validarSemanticaComandoIf(boolean b) {
		if (EXPRESSAO()) {
			if (SemanticoAcao.Acao(120)) {
				i++;
				if (A.get(i).getNome().toLowerCase().equals("then")) {
					if (COMANDO(false)) {
						if (ELSEPARTE(false)) {
							if (SemanticoAcao.Acao(121)) {
								b = true;
							}
						}
					}
				}
			}
		}
		return b;
	}

	private boolean validarSemanticaComandoGoto(boolean b) {
		i++;
		if (IDENT(i)) {
			if (SemanticoAcao.Acao(119)) {
				b = true;
			}
		}
		return b;
	}

	private boolean validarSemanticaComandoCall(boolean b) {
		i++;
		if (IDENT(i)) {
			if (SemanticoAcao.Acao(116)) {
				if (PARAMETROS(false)) {
					if (SemanticoAcao.Acao(117)) {
						b = true;
					}
				}
			}
		}
		return b;
	}

	private boolean validarSemanticaComandoFor(boolean b) {
		i++;
		if (IDENT(i) && SemanticoAcao.Acao(137)) {
			i++;
			if (A.get(i).getNome().toLowerCase().equals(":=") && EXPRESSAO() && SemanticoAcao.Acao(138)) {
				i++;
				if (A.get(i).getNome().toLowerCase().equals("to") && EXPRESSAO() && SemanticoAcao.Acao(139)) {
					i++;
					if (A.get(i).getNome().toLowerCase().equals("do") && COMANDO(false) && SemanticoAcao.Acao(140)) {
						b = true;
					}
				}
			}
		}
		return b;
	}

	public boolean CONDCASE() {
		boolean b = false;
		a = i;
		i++;
		if (INTEIRO(i)) {
			if (RPINTEIRO(false)) {
				if (SemanticoAcao.Acao(136)) {
					i++;
					if (A.get(i).getNome().toLowerCase().equals(":")) {
						if (SemanticoAcao.Acao(134)) {
//							if(ASem.Acao(157)){
							if (COMANDO(true)) {
								if (SemanticoAcao.Acao(135)) {
									if (CONTCASE(false)) {
										b = true;
									}
								}
							}
//							}
						}
					}
				}
			}
		}
		if (!b) {
			i = a;
		}
		return b;
	}

	public boolean CONTCASE(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals(";")) {
			if (CONDCASE()) {
				b = true;
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean DCLCONST(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals("const")) {
			i++;
			if (IDENT(i)) {
				if (SemanticoAcao.Acao(105)) {
					i++;
					if (A.get(i).getNome().toLowerCase().equals("=")) {
						i++;
						if (INTEIRO(i)) {
							if (SemanticoAcao.Acao(106)) {
								i++;
								if (A.get(i).getNome().toLowerCase().equals(";")) {
									if (LDCONST(false)) {
										b = true;
									}
								}
							}
						}
					}
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean DCLPROC(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals("procedure")) {
			i++;
			if (IDENT(i)) {
				if (SemanticoAcao.Acao(108)) {
//					Incrementar nível da procedure após adicionar na Tabela de Símbolos.
					if (DEFPAR(false)) {
						i++;
						if (A.get(i).getNome().toLowerCase().equals(";")) {
							if (SemanticoAcao.Acao(109)) {
								if (BLOCO()) {
									i++;
									if (A.get(i).getNome().toLowerCase().equals(";")) {
										if (SemanticoAcao.Acao(110)) {
											if (DCLPROC(false)) {
												b = true;
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
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean DCLROT() {
		a = i;

		i++;
		if (!A.get(i).getNome().toLowerCase().equals("label") || !SemanticoAcao.Acao(103) || !LID(true)) {
			i = a;
			return false;
		}

		i++;
		return A.get(i).getNome().toLowerCase().equals(";");
	}

	public boolean DCLVAR(boolean q) {
		a = i;

		i++;
		if (!A.get(i).getNome().toLowerCase().equals("var") || !SemanticoAcao.Acao(107) || !LID(true)) {
			i = a;
			return !q;
		}

		i++;
		if (!A.get(i).getNome().toLowerCase().equals(":") || !TIPO()) {
			i = a;
			return !q;
		}

		i++;
		if (!A.get(i).getNome().toLowerCase().equals(";") || !LDVAR(false) || !SemanticoAcao.Acao(102)) {
			i = a;
			return !q;
		}

		return true;
	}

	public boolean DEFPAR(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals("(")) {
			if (SemanticoAcao.Acao(111)) {
				if (LID(true)) {
					i++;
					if (A.get(i).getNome().toLowerCase().equals(":")) {
						i++;
						if (A.get(i).getNome().toLowerCase().equals("integer")) {
							i++;
							if (A.get(i).getNome().toLowerCase().equals(")")) {
								b = true;
							}
						}
					}
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean ELSEPARTE(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (SemanticoAcao.Acao(122)) {
			if (A.get(i).getNome().toLowerCase().equals("else")) {
				if (COMANDO(false)) {
					b = true;
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean EXPRESSAO() {
		boolean b = false;
		if (EXPSINP()) {
			if (REPEXPSIMP(false)) {
				b = true;
			}
		}
		return b;
	}

	public boolean EXPSINP() {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals("+")) {
			if (TERMO()) {
				if (REPEXP(false)) {
					b = true;
				}
			}
		} else {
			if (A.get(i).getNome().toLowerCase().equals("-")) {
				if (TERMO()) {
					if (SemanticoAcao.Acao(147)) {
						if (REPEXP(false)) {
							b = true;
						}
					}
				}
			} else {
				i = a;
				if (TERMO()) {
					if (REPEXP(false)) {
						b = true;
					}
				}
			}
		}
		if (!b) {
			i = a;
		}
		return b;
	}

	public boolean FATOR() {
		boolean b = false;
		a = i;
		i++;
		if (INTEIRO(i)) {
			if (SemanticoAcao.Acao(154)) {
				b = true;
			}
		} else {
			i = a;
			i++;
			if (A.get(i).getNome().toLowerCase().equals("(")) {
				if (EXPRESSAO()) {
					i++;
					if (A.get(i).getNome().toLowerCase().equals(")")) {
						b = true;
					}
				}
			} else {
				i = a;
				i++;
				if (A.get(i).getNome().toLowerCase().equals("not")) {
					if (FATOR()) {
						if (SemanticoAcao.Acao(155)) {
							b = true;
						}
					}
				} else {
					i = a;
					if (SemanticoAcao.Acao(156)) {
						if (VARIAVEL()) {
							b = true;
						}
					}
				}
			}
		}
		if (!b) {
			i = a;
		}
		return b;
	}

	public boolean ITEMSAIDA() {
		boolean b = false;
		a = i;
		i++;
		if (LITERAL(i)) {
			if (SemanticoAcao.Acao(130)) {
				b = true;
			}
		} else {
			i = a;
			if (EXPRESSAO()) {
				if (SemanticoAcao.Acao(131)) {
					b = true;
				}
			}
		}
		if (!b) {
			i = a;
		}
		return b;
	}

	public boolean LDCONST(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (IDENT(i)) {
			if (SemanticoAcao.Acao(105)) {
				i++;
				if (A.get(i).getNome().toLowerCase().equals("=")) {
					i++;
					if (INTEIRO(i)) {
						if (SemanticoAcao.Acao(106)) {
							i++;
							if (A.get(i).getNome().toLowerCase().equals(";")) {
								if (LDCONST(false)) {
									b = true;
								}
							}
						}
					}
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean LDVAR(boolean q) {
		boolean b = false;
		a = i;
		if (LID(q)) {
			i++;
			if (A.get(i).getNome().toLowerCase().equals(":")) {
				if (TIPO()) {
					i++;
					if (A.get(i).getNome().toLowerCase().equals(";")) {
						if (LDVAR(false)) {
							b = true;
						}
					}
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean LID(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (IDENT(i)) {
			if (SemanticoAcao.Acao(104)) {
				if (REPIDENT(false)) {
					b = true;
				}
			}
		}
		if (!b) {
			i = a;
		}
		return b;
	}

	public boolean PARAMETROS(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals("(")) {
			if (EXPRESSAO()) {
				if (SemanticoAcao.Acao(118)) {
					if (REPPAR(false)) {
						i++;
						if (A.get(i).getNome().toLowerCase().equals(")")) {
							b = true;
						}
					}
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean RCOMID() {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals(":")) {
			if (SemanticoAcao.Acao(113)) {
				if (COMANDO(true)) {
					b = true;
				}
			}
		} else {
			i = a;
			if (RVAR(false)) {
				if (SemanticoAcao.Acao(114)) {
					i++;
					if (A.get(i).getNome().toLowerCase().equals(":=")) {
						if (EXPRESSAO()) {
							if (SemanticoAcao.Acao(115)) {
								b = true;
							}
						}
					}
				}
			}
		}
		if (!b) {
			i = a;
		}
		return b;
	}

	public boolean REPCOMANDO(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals(";")) {
			if (COMANDO(true)) {
				if (REPCOMANDO(false)) {
					b = true;
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean REPEXPSIMP(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals("<>")) {
			if (EXPSINP()) {
				if (SemanticoAcao.Acao(146)) {
					b = true;
				}
			}
		} else {
			if (A.get(i).getNome().toLowerCase().equals("<=")) {
				if (EXPSINP()) {
					if (SemanticoAcao.Acao(145)) {
						b = true;
					}
				}
			} else {
				if (A.get(i).getNome().toLowerCase().equals("<")) {
					if (EXPSINP()) {
						if (SemanticoAcao.Acao(142)) {
							b = true;
						}
					}
				} else {
					if (A.get(i).getNome().toLowerCase().equals(">=")) {
						if (EXPSINP()) {
							if (SemanticoAcao.Acao(144)) {
								b = true;
							}
						}
					} else {
						if (A.get(i).getNome().toLowerCase().equals(">")) {
							if (EXPSINP()) {
								if (SemanticoAcao.Acao(143)) {
									b = true;
								}
							}
						} else {
							if (A.get(i).getNome().toLowerCase().equals("=")) {
								if (EXPSINP()) {
									if (SemanticoAcao.Acao(141)) {
										b = true;
									}
								}
							}
						}
					}
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean REPEXP(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals("+")) {
			if (TERMO()) {
				if (SemanticoAcao.Acao(148)) {
					if (REPEXP(false)) {
						b = true;
					}
				}
			}
		} else {
			if (A.get(i).getNome().toLowerCase().equals("-")) {
				if (TERMO()) {
					if (SemanticoAcao.Acao(149)) {
						if (REPEXP(false)) {
							b = true;
						}
					}
				}
			} else {
				if (A.get(i).getNome().toLowerCase().equals("or")) {
					if (TERMO()) {
						if (SemanticoAcao.Acao(150)) {
							if (REPEXP(false)) {
								b = true;
							}
						}
					}
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean REPIDENT(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals(",")) {
			i++;
			if (IDENT(i)) {
				if (SemanticoAcao.Acao(104)) {
					if (REPIDENT(false)) {
						b = true;
					}
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean REPITEM(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals(",")) {
			if (ITEMSAIDA()) {
				if (REPITEM(false)) {
					b = true;
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean REPPAR(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals(",")) {
			if (EXPRESSAO()) {
				if (SemanticoAcao.Acao(118)) {
					if (REPPAR(false)) {
						b = true;
					}
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean REPVARIAVEL(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals(",")) {
			if (VARIAVEL()) {
				if (REPVARIAVEL(false)) {
					b = true;
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean REPTERMO(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals("*")) {
			if (FATOR()) {
				if (SemanticoAcao.Acao(151)) {
					if (REPTERMO(false)) {
						b = true;
					}
				}
			}
		} else {
			if (A.get(i).getNome().toLowerCase().equals("/")) {
				if (FATOR()) {
					if (SemanticoAcao.Acao(152)) {
						if (REPTERMO(false)) {
							b = true;
						}
					}
				}
			} else {
				if (A.get(i).getNome().toLowerCase().equals("and")) {
					if (FATOR()) {
						if (SemanticoAcao.Acao(153)) {
							if (REPTERMO(false)) {
								b = true;
							}
						}
					}
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean RPINTEIRO(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals(",")) {
			i -= 1;
			if (SemanticoAcao.Acao(136)) {
				i++;
				i++;
				if (INTEIRO(i)) {
					if (RPINTEIRO(false)) {
						b = true;
					}
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean RVAR(boolean q) {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals("[")) {
			if (EXPRESSAO()) {
				i++;
				if (A.get(i).getNome().toLowerCase().equals("]")) {
					b = true;
				}
			}
		}
		if (!b) {
			i = a;
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	public boolean TERMO() {
		boolean b = false;
		a = i;
		if (FATOR()) {
			if (REPTERMO(false)) {
				b = true;
			}
		}
		if (!b) {
			i = a;
		}
		return b;
	}

	public boolean TIPO() {
		boolean b = false;
		a = i;
		i++;
		if (A.get(i).getNome().toLowerCase().equals("integer")) {
			b = true;
		} else {
			if (A.get(i).getNome().toLowerCase().equals("array")) {
				i++;
				if (A.get(i).getNome().toLowerCase().equals("[")) {
					i++;
					if (INTEIRO(i)) {
						i++;
						if (A.get(i).getNome().toLowerCase().equals("..")) {
							i++;
							if (INTEIRO(i)) {
								i++;
								if (A.get(i).getNome().toLowerCase().equals("]")) {
									i++;
									if (A.get(i).getNome().toLowerCase().equals("of")) {
										i++;
										if (A.get(i).getNome().toLowerCase().equals("integer")) {
											b = true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (!b) {
			i = a;
		}
		return b;
	}

	public boolean VARIAVEL() {
		boolean b = false;
		a = i;
		i++;
		if (IDENT(i)) {
			if (SemanticoAcao.Acao(129)) {
				if (RVAR(false)) {
					b = true;
				}
			}
		}
		if (!b) {
			i = a;
		}
		return b;
	}
}