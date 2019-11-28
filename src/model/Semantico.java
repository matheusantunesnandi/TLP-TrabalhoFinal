package model;

import java.util.ArrayList;

import application.PrincipalController;

public class Semantico {
	public static ArrayList<model.SemanticoInstrucao> AL_Instr = new ArrayList<model.SemanticoInstrucao>();
	public static String AreaLiterais = "";

	public static ArrayList<LexicoToken> A = null;
	SemanticoAcao ASem = new SemanticoAcao();
	static int i, a;

	@SuppressWarnings({ "unchecked", "static-access" })
	public Semantico() {
//		Remove o que já estava na lista para preencher novamente com dados novos:
		SemanticoAcao.Erro_Sem = new ArrayList<String>();
		
//		Reseta / Zera / Remove todas as instruções:
		Semantico.AL_Instr = new ArrayList<model.SemanticoInstrucao>();
		AreaLiterais = "";

		A = (ArrayList<LexicoToken>) PrincipalController.ALfinal.clone();
		i = a = 0;
		ASem.Zerar();

		if (PROGRAMA()) {
			SemanticoAcao.Erro_Sem = new ArrayList<String>();
			SemanticoAcao.Erro_Sem.add("Codigo analizado com sucesso !");
			PrincipalController.analiseSemanticaComSucesso = true;
		}
	}

	public boolean IDENT(int i) {
		boolean b = false;
		if (A.get(i).getDesc().equals("Identificador")) {
			b = true;
		}
		return b;
	}

	public boolean LITERAL(int i) {
		boolean b = false;
		if (A.get(i).getDesc().equals("Literal")) {
			b = true;
		}
		return b;
	}

	public boolean INTEIRO(int i) {
		boolean b = false;
		if (A.get(i).getDesc().equals("Inteiro")) {
			b = true;
		}
		return b;
	}

	@SuppressWarnings("static-access")
	public boolean PROGRAMA() {
		boolean b = false;
		a = i;
		
		if (A == null || A.isEmpty())
			return false;
		
		if (A.get(i).getNome().toLowerCase().equals("program")) {
			i += 1;
			if (IDENT(i)) {
				if (ASem.Acao(100)) {
					i += 1;
					if (A.get(i).getNome().toLowerCase().equals(";")) {
						if (BLOCO()) {
							i += 1;
							if (A.get(i).getNome().toLowerCase().equals(".")) {
								if (ASem.Acao(101)) {
									b = true;
								}
							}
						}
					}
				}
			}
		}
		return b;
	}

	public boolean BLOCO() {
		boolean b = false;
		if (DCLROT(false)) {
			if (DCLCONST(false)) {
				if (DCLVAR(false)) {
					if (DCLPROC(false)) {
						if (CORPO(true)) {
							b = true;
						}
					}
				}
			}
		}
		return b;
	}

	public boolean CORPO(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("begin")) {
			if (COMANDO(false)) {
				if (REPCOMANDO(false)) {
					i += 1;
					if (A.get(i).getNome().toLowerCase().equals("end")) {
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

	@SuppressWarnings("static-access")
	public boolean COMANDO(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("for")) {
			i += 1;
			if (IDENT(i)) {
				if (ASem.Acao(137)) {
					i += 1;
					if (A.get(i).getNome().toLowerCase().equals(":=")) {
						if (EXPRESSAO()) {
							if (ASem.Acao(138)) {
								i += 1;
								if (A.get(i).getNome().toLowerCase().equals("to")) {
									if (EXPRESSAO()) {
										if (ASem.Acao(139)) {
											i += 1;
											if (A.get(i).getNome().toLowerCase().equals("do")) {
												if (COMANDO(false)) {
													if (ASem.Acao(140)) {
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
		} else {
			i = a;
			i += 1;
			if (A.get(i).getNome().toLowerCase().equals("call")) {
				i += 1;
				if (IDENT(i)) {
					if (ASem.Acao(116)) {
						if (PARAMETROS(false)) {
							if (ASem.Acao(117)) {
								b = true;
							}
						}
					}
				}
			} else {
				i = a;
				i += 1;
				if (A.get(i).getNome().toLowerCase().equals("goto")) {
					i += 1;
					if (IDENT(i)) {
						if (ASem.Acao(119)) {
							b = true;
						}
					}
				} else {
					i = a;
					i += 1;
					if (A.get(i).getNome().toLowerCase().equals("if")) {
						if (EXPRESSAO()) {
							if (ASem.Acao(120)) {
								i += 1;
								if (A.get(i).getNome().toLowerCase().equals("then")) {
									if (COMANDO(false)) {
										if (ELSEPARTE(false)) {
											if (ASem.Acao(121)) {
												b = true;
											}
										}
									}
								}
							}
						}
					} else {
						i = a;
						i += 1;
						if (A.get(i).getNome().toLowerCase().equals("while")) {
							if (ASem.Acao(123)) {
								if (EXPRESSAO()) {
									if (ASem.Acao(124)) {
										i += 1;
										if (A.get(i).getNome().toLowerCase().equals("do")) {
											if (COMANDO(false)) {
												if (ASem.Acao(125)) {
													b = true;
												}
											}
										}
									}
								}
							}
						} else {
							i = a;
							i += 1;
							if (A.get(i).getNome().toLowerCase().equals("repeat")) {
								if (ASem.Acao(126)) {
									if (COMANDO(true)) {
										i += 1;
										if (A.get(i).getNome().toLowerCase().equals("until")) {
											if (EXPRESSAO()) {
												if (ASem.Acao(127)) {
													b = true;
												}
											}
										}
									}
								}
							} else {
								i = a;
								i += 1;
								if (A.get(i).getNome().toLowerCase().equals("readln")) {
									if (ASem.Acao(128)) {
										i += 1;
										if (A.get(i).getNome().toLowerCase().equals("(")) {
											if (VARIAVEL()) {
												if (REPVARIAVEL(false)) {
													i += 1;
													if (A.get(i).getNome().toLowerCase().equals(")")) {
														ASem.Acao(156);
														b = true;
													}
												}
											}
										}
									}
								} else {
									i = a;
									i += 1;
									if (A.get(i).getNome().toLowerCase().equals("writeln")) {
										i += 1;
										if (A.get(i).getNome().toLowerCase().equals("(")) {
											if (ITEMSAIDA()) {
												if (REPITEM(false)) {
													i += 1;
													if (A.get(i).getNome().toLowerCase().equals(")")) {
														b = true;
													}
												}
											}
										}
									} else {
										i = a;
										i += 1;
										if (A.get(i).getNome().toLowerCase().equals("case")) {
											if (ASem.Acao(132)) {
												if (EXPRESSAO()) {
													i += 1;
													if (A.get(i).getNome().toLowerCase().equals("of")) {
														if (CONDCASE()) {
															i += 1;
															if (A.get(i).getNome().toLowerCase().equals("end")) {
																if (ASem.Acao(133)) {
																	b = true;
																}
															}
														}
													}
												}
											}
										} else {
											i = a;
											i += 1;
											if (IDENT(i)) {
												if (A.get(i + 1).getNome().toLowerCase().equals(":")) {
													if (RCOMID()) {
														b = true;
													}
												} else {
//													if(ASem.Acao(129)){
													ASem.Acao(156);
													if (RCOMID()) {
														b = true;
													} else {
														i = a;
														if (q == false) {
															b = true;
														}
													}
//													}
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
			if (q == false) {
				b = true;
			}
		}
		return b;
	}

	@SuppressWarnings("static-access")
	public boolean CONDCASE() {
		boolean b = false;
		a = i;
		i += 1;
		if (INTEIRO(i)) {
			if (RPINTEIRO(false)) {
				if (ASem.Acao(136)) {
					i += 1;
					if (A.get(i).getNome().toLowerCase().equals(":")) {
						if (ASem.Acao(134)) {
//							if(ASem.Acao(157)){
							if (COMANDO(true)) {
								if (ASem.Acao(135)) {
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
		i += 1;
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

	@SuppressWarnings("static-access")
	public boolean DCLCONST(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("const")) {
			i += 1;
			if (IDENT(i)) {
				if (ASem.Acao(105)) {
					i += 1;
					if (A.get(i).getNome().toLowerCase().equals("=")) {
						i += 1;
						if (INTEIRO(i)) {
							if (ASem.Acao(106)) {
								i += 1;
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

	@SuppressWarnings("static-access")
	public boolean DCLPROC(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("procedure")) {
			i += 1;
			if (IDENT(i)) {
				if (ASem.Acao(108)) {
					if (DEFPAR(false)) {
						i += 1;
						if (A.get(i).getNome().toLowerCase().equals(";")) {
							if (ASem.Acao(109)) {
								if (BLOCO()) {
									i += 1;
									if (A.get(i).getNome().toLowerCase().equals(";")) {
										if (ASem.Acao(110)) {
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

	@SuppressWarnings("static-access")
	public boolean DCLROT(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("label")) {
			if (ASem.Acao(103)) {
				if (LID(true)) {
					i += 1;
					if (A.get(i).getNome().toLowerCase().equals(";")) {
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

	@SuppressWarnings("static-access")
	public boolean DCLVAR(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("var")) {
			if (ASem.Acao(107)) {
				if (LID(true)) {
					i += 1;
					if (A.get(i).getNome().toLowerCase().equals(":")) {
						if (TIPO()) {
							i += 1;
							if (A.get(i).getNome().toLowerCase().equals(";")) {
								if (LDVAR(false)) {
									if (ASem.Acao(102)) {
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

	@SuppressWarnings("static-access")
	public boolean DEFPAR(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("(")) {
			if (ASem.Acao(111)) {
				if (LID(true)) {
					i += 1;
					if (A.get(i).getNome().toLowerCase().equals(":")) {
						i += 1;
						if (A.get(i).getNome().toLowerCase().equals("integer")) {
							i += 1;
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

	@SuppressWarnings("static-access")
	public boolean ELSEPARTE(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (ASem.Acao(122)) {
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

	@SuppressWarnings("static-access")
	public boolean EXPSINP() {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("+")) {
			if (TERMO()) {
				if (REPEXP(false)) {
					b = true;
				}
			}
		} else {
			if (A.get(i).getNome().toLowerCase().equals("-")) {
				if (TERMO()) {
					if (ASem.Acao(147)) {
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

	@SuppressWarnings("static-access")
	public boolean FATOR() {
		boolean b = false;
		a = i;
		i += 1;
		if (INTEIRO(i)) {
			if (ASem.Acao(154)) {
				b = true;
			}
		} else {
			i = a;
			i += 1;
			if (A.get(i).getNome().toLowerCase().equals("(")) {
				if (EXPRESSAO()) {
					i += 1;
					if (A.get(i).getNome().toLowerCase().equals(")")) {
						b = true;
					}
				}
			} else {
				i = a;
				i += 1;
				if (A.get(i).getNome().toLowerCase().equals("not")) {
					if (FATOR()) {
						if (ASem.Acao(155)) {
							b = true;
						}
					}
				} else {
					i = a;
					if (ASem.Acao(156)) {
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

	@SuppressWarnings("static-access")
	public boolean ITEMSAIDA() {
		boolean b = false;
		a = i;
		i += 1;
		if (LITERAL(i)) {
			if (ASem.Acao(130)) {
				b = true;
			}
		} else {
			i = a;
			if (EXPRESSAO()) {
				if (ASem.Acao(131)) {
					b = true;
				}
			}
		}
		if (!b) {
			i = a;
		}
		return b;
	}

	@SuppressWarnings("static-access")
	public boolean LDCONST(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (IDENT(i)) {
			if (ASem.Acao(105)) {
				i += 1;
				if (A.get(i).getNome().toLowerCase().equals("=")) {
					i += 1;
					if (INTEIRO(i)) {
						if (ASem.Acao(106)) {
							i += 1;
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
			i += 1;
			if (A.get(i).getNome().toLowerCase().equals(":")) {
				if (TIPO()) {
					i += 1;
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

	@SuppressWarnings("static-access")
	public boolean LID(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (IDENT(i)) {
			if (ASem.Acao(104)) {
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

	@SuppressWarnings("static-access")
	public boolean PARAMETROS(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("(")) {
			if (EXPRESSAO()) {
				if (ASem.Acao(118)) {
					if (REPPAR(false)) {
						i += 1;
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

	@SuppressWarnings("static-access")
	public boolean RCOMID() {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals(":")) {
			if (ASem.Acao(113)) {
				if (COMANDO(true)) {
					b = true;
				}
			}
		} else {
			i = a;
			if (RVAR(false)) {
				if (ASem.Acao(114)) {
					i += 1;
					if (A.get(i).getNome().toLowerCase().equals(":=")) {
						if (EXPRESSAO()) {
							if (ASem.Acao(115)) {
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
		i += 1;
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

	@SuppressWarnings("static-access")
	public boolean REPEXPSIMP(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("<>")) {
			if (EXPSINP()) {
				if (ASem.Acao(146)) {
					b = true;
				}
			}
		} else {
			if (A.get(i).getNome().toLowerCase().equals("<=")) {
				if (EXPSINP()) {
					if (ASem.Acao(145)) {
						b = true;
					}
				}
			} else {
				if (A.get(i).getNome().toLowerCase().equals("<")) {
					if (EXPSINP()) {
						if (ASem.Acao(142)) {
							b = true;
						}
					}
				} else {
					if (A.get(i).getNome().toLowerCase().equals(">=")) {
						if (EXPSINP()) {
							if (ASem.Acao(144)) {
								b = true;
							}
						}
					} else {
						if (A.get(i).getNome().toLowerCase().equals(">")) {
							if (EXPSINP()) {
								if (ASem.Acao(143)) {
									b = true;
								}
							}
						} else {
							if (A.get(i).getNome().toLowerCase().equals("=")) {
								if (EXPSINP()) {
									if (ASem.Acao(141)) {
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

	@SuppressWarnings("static-access")
	public boolean REPEXP(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("+")) {
			if (TERMO()) {
				if (ASem.Acao(148)) {
					if (REPEXP(false)) {
						b = true;
					}
				}
			}
		} else {
			if (A.get(i).getNome().toLowerCase().equals("-")) {
				if (TERMO()) {
					if (ASem.Acao(149)) {
						if (REPEXP(false)) {
							b = true;
						}
					}
				}
			} else {
				if (A.get(i).getNome().toLowerCase().equals("or")) {
					if (TERMO()) {
						if (ASem.Acao(150)) {
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

	@SuppressWarnings("static-access")
	public boolean REPIDENT(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals(",")) {
			i += 1;
			if (IDENT(i)) {
				if (ASem.Acao(104)) {
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
		i += 1;
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

	@SuppressWarnings("static-access")
	public boolean REPPAR(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals(",")) {
			if (EXPRESSAO()) {
				if (ASem.Acao(118)) {
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
		i += 1;
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

	@SuppressWarnings("static-access")
	public boolean REPTERMO(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("*")) {
			if (FATOR()) {
				if (ASem.Acao(151)) {
					if (REPTERMO(false)) {
						b = true;
					}
				}
			}
		} else {
			if (A.get(i).getNome().toLowerCase().equals("/")) {
				if (FATOR()) {
					if (ASem.Acao(152)) {
						if (REPTERMO(false)) {
							b = true;
						}
					}
				}
			} else {
				if (A.get(i).getNome().toLowerCase().equals("and")) {
					if (FATOR()) {
						if (ASem.Acao(153)) {
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

	@SuppressWarnings("static-access")
	public boolean RPINTEIRO(boolean q) {
		boolean b = false;
		a = i;
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals(",")) {
			i -= 1;
			if (ASem.Acao(136)) {
				i += 1;
				i += 1;
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
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("[")) {
			if (EXPRESSAO()) {
				i += 1;
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
		i += 1;
		if (A.get(i).getNome().toLowerCase().equals("integer")) {
			b = true;
		} else {
			if (A.get(i).getNome().toLowerCase().equals("array")) {
				i += 1;
				if (A.get(i).getNome().toLowerCase().equals("[")) {
					i += 1;
					if (INTEIRO(i)) {
						i += 1;
						if (A.get(i).getNome().toLowerCase().equals("..")) {
							i += 1;
							if (INTEIRO(i)) {
								i += 1;
								if (A.get(i).getNome().toLowerCase().equals("]")) {
									i += 1;
									if (A.get(i).getNome().toLowerCase().equals("of")) {
										i += 1;
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

	@SuppressWarnings("static-access")
	public boolean VARIAVEL() {
		boolean b = false;
		a = i;
		i += 1;
		if (IDENT(i)) {
			if (ASem.Acao(129)) {
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