package model;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import application.PrincipalController;


public class Lexico {
	public static ArrayList<LexicoToken> ALfinal = new ArrayList<LexicoToken>();

	public Lexico(String t) {
		try {
			String p = "";// todo token será "montado" aqui antes de ser analizado e ir pra a tabela final
			int n = t.length();
			int c = 0, ent = 0, y = 0;
			char a = t.charAt(0);
			if (Character.isLetter(a)) {
				c = 1;
			} else {
				if (Character.isDigit(a)) {
					c = 2;
				} else {
					if (a == ':' || a == '>') {
						c = 3;
					} else {
						if (a == '<') {
							c = 4;
						} else {
							if (a == '.') {
								c = 5;
							} else {
								if (a == '\"') {
									c = 6;
								} else {
									if (a == '(') {
										c = 7;
									} else {
										if (a == '$' || a == '[' || a == ']' || a == ')' || a == '*' || a == '/'
												|| a == '-' || a == '+' || a == '=' || a == ';' || a == ',') {
											c = 10;
										} else {
											c = 15;
										}
									}
								}
							}
						}
					}
				}
			}
			for (int i = 0; i < n; i++) {
				a = t.charAt(i);
				switch (c) {
				case 1:// caso o token seja uma letra
					if (p.length() >= 30) {
						JOptionPane.showMessageDialog(null, "Palavras podem conter no máximo 30 caracteres (linha " + (ent + 1) + ")");
						i = n;
					} else {
						p = p + a;
						if (i == n - 1) {
							LexicoGTabela.AL.add(p);
							p = "";
						} else {
							a = t.charAt(i + 1);
							if (Character.isLetter(a) || Character.isDigit(a)) {
								c = 1;
							} else {
								LexicoGTabela.AL.add(p);
								p = "";
								if (a == ':' || a == '>') {
									c = 3;
								} else {
									if (a == '<') {
										c = 4;
									} else {
										if (a == '.') {
											c = 5;
										} else {
											if (a == '\"') {
												c = 6;
											} else {
												if (a == '(') {
													c = 7;
												} else {
													if (a == '$' || a == '[' || a == ']' || a == ')' || a == '*'
															|| a == '/' || a == '-' || a == '+' || a == '=' || a == ';'
															|| a == ',') {
														c = 10;
													} else {
														c = 15;
													}
												}
											}
										}
									}
								}
							}
						}
					}
					break;
				case 2:// monta números
					p = p + a;
					if (i == n - 1) {
						LexicoGTabela.AL.add(p);
						p = "";
					} else {
						a = t.charAt(i + 1);
						if (Character.isDigit(a)) {
							c = 2;
						} else {
							LexicoGTabela.AL.add(p);
							p = "";
							if (Character.isLetter(a)) {
								JOptionPane.showMessageDialog(null, "Uma palavra não pode começar em número (linha " + (ent + 1) + ")");
								i = n;
							} else {
								if (a == ':' || a == '>') {
									c = 3;
								} else {
									if (a == '<') {
										c = 4;
									} else {
										if (a == '.') {
											c = 5;
										} else {
											if (a == '\"') {
												c = 6;
											} else {
												if (a == '(') {
													c = 7;
												} else {
													if (a == '$' || a == '[' || a == ']' || a == ')' || a == '*'
															|| a == '/' || a == '-' || a == '+' || a == '=' || a == ';'
															|| a == ',') {
														c = 10;
													} else {
														c = 15;
													}
												}
											}
										}
									}
								}
							}
						}
					}
					break;
				case 3:// em caso de ":" ou ">"
					p = p + a;
					if (i == n - 1) {
						LexicoGTabela.AL.add(p);
						p = "";
					} else {
						a = t.charAt(i + 1);
						if (a == '=') {
							c = 10;
						} else {
							LexicoGTabela.AL.add(p);
							p = "";
							if (Character.isLetter(a)) {
								c = 1;
							} else {
								if (Character.isDigit(a)) {
									c = 2;
								} else {
									if (a == ':' || a == '>') {
										c = 3;
									} else {
										if (a == '<') {
											c = 4;
										} else {
											if (a == '.') {
												c = 5;
											} else {
												if (a == '\"') {
													c = 6;
												} else {
													if (a == '(') {
														c = 7;
													} else {
														if (a == '$' || a == '[' || a == ']' || a == ')' || a == '*'
																|| a == '/' || a == '-' || a == '+' || a == '='
																|| a == ';' || a == ',') {
															c = 10;
														} else {
															c = 15;
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
					break;
				case 4:// em caso de ">"
					p = p + a;
					if (i == n - 1) {
						LexicoGTabela.AL.add(p);
						p = "";
					} else {
						a = t.charAt(i + 1);
						if (a == '>' || a == '=') {
							c = 10;
						} else {
							LexicoGTabela.AL.add(p);
							p = "";
							if (Character.isLetter(a)) {
								c = 1;
							} else {
								if (Character.isDigit(a)) {
									c = 2;
								} else {
									if (a == ':' || a == '>') {
										c = 3;
									} else {
										if (a == '<') {
											c = 4;
										} else {
											if (a == '.') {
												c = 5;
											} else {
												if (a == '\"') {
													c = 6;
												} else {
													if (a == '(') {
														c = 7;
													} else {
														if (a == '$' || a == '[' || a == ']' || a == ')' || a == '*'
																|| a == '/' || a == '-' || a == '+' || a == '='
																|| a == ';' || a == ',') {
															c = 10;
														} else {
															c = 15;
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
					break;
				case 5:// em caso de "."
					p = p + a;
					if (i == n - 1) {
						LexicoGTabela.AL.add(p);
						p = "";
					} else {
						a = t.charAt(i + 1);
						if (a == '.' && p.length() == 1) {
							c = 5;
						} else {
							LexicoGTabela.AL.add(p);
							p = "";
							if (Character.isLetter(a)) {
								c = 1;
							} else {
								if (Character.isDigit(a)) {
									c = 2;
								} else {
									if (a == ':' || a == '>') {
										c = 3;
									} else {
										if (a == '<') {
											c = 4;
										} else {
											if (a == '.') {
												c = 5;
											} else {
												if (a == '\"') {
													c = 6;
												} else {
													if (a == '(') {
														c = 7;
													} else {
														if (a == '$' || a == '[' || a == ']' || a == ')' || a == '*'
																|| a == '/' || a == '-' || a == '+' || a == '='
																|| a == ';' || a == ',') {
															c = 10;
														} else {
															c = 15;
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
					break;
				case 6:// em caso de literal
					if (a == '\"' && y == 0) {
						p = p + a;
						c = 6;
						y = 1;
					} else {
						if (a != '\"' && i < n - 1) {
							p = p + a;
							c = 6;
						} else {
							if (a != '\"' && i < n) {
								JOptionPane.showMessageDialog(null, "Determine o fim da literal usando \" (aspas)");
								i = n;
							} else {
								if (a == '\"' && y == 1) {
									p = p + a;
									LexicoGTabela.AL.add(p);
									p = "";
									y = 0;
									if (i == n - 1) {
									} else {
										a = t.charAt(i + 1);
										if (Character.isLetter(a)) {
											c = 1;
										} else {
											if (Character.isDigit(a)) {
												c = 2;
											} else {
												if (a == ':' || a == '>') {
													c = 3;
												} else {
													if (a == '<') {
														c = 4;
													} else {
														if (a == '.') {
															c = 5;
														} else {
															if (a == '\"') {
																c = 6;
															} else {
																if (a == '(') {
																	c = 7;
																} else {
																	if (a == '$' || a == '[' || a == ']' || a == ')'
																			|| a == '*' || a == '/' || a == '-'
																			|| a == '+' || a == '=' || a == ';'
																			|| a == ',') {
																		c = 10;
																	} else {
																		c = 15;
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
						}
					}
					break;
				case 7:// em caso de (
					p = p + a;
					if (i == n - 1) {
						LexicoGTabela.AL.add(p);
						p = "";
					} else {
						a = t.charAt(i + 1);
						if (a == '*') {
							c = 8;
						} else {
							LexicoGTabela.AL.add(p);
							p = "";
							if (Character.isLetter(a)) {
								c = 1;
							} else {
								if (Character.isDigit(a)) {
									c = 2;
								} else {
									if (a == ':' || a == '>') {
										c = 3;
									} else {
										if (a == '<') {
											c = 4;
										} else {
											if (a == '.') {
												c = 5;
											} else {
												if (a == '\"') {
													c = 6;
												} else {
													if (a == '(') {
														c = 7;
													} else {
														if (a == '$' || a == '[' || a == ']' || a == ')' || a == '*'
																|| a == '/' || a == '-' || a == '+' || a == '='
																|| a == ';' || a == ',') {
															c = 10;
														} else {
															c = 15;
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
					break;
				case 8:// em caso de comentarios
					p = "";
					if (i == n - 1) {
						JOptionPane.showMessageDialog(null, "Complete o comentário com \"*)\" ");
						i = n;
					} else {
						if (a == '*' && t.charAt(i + 1) == ')') {
							c = 9;
						} else {
							c = 8;
						}
					}
					break;
				case 9:// final de comentario
					p = "";
					if (i == n - 1) {

					} else {
						a = t.charAt(i + 1);
						if (Character.isLetter(a)) {
							c = 1;
						} else {
							if (Character.isDigit(a)) {
								c = 2;
							} else {
								if (a == ':' || a == '>') {
									c = 3;
								} else {
									if (a == '<') {
										c = 4;
									} else {
										if (a == '.') {
											c = 5;
										} else {
											if (a == '\"') {
												c = 6;
											} else {
												if (a == '(') {
													c = 7;
												} else {
													if (a == '$' || a == '[' || a == ']' || a == ')' || a == '*'
															|| a == '/' || a == '-' || a == '+' || a == '=' || a == ';'
															|| a == ',') {
														c = 10;
													} else {
														c = 15;
													}
												}
											}
										}
									}
								}
							}
						}
					}
					break;
				case 10:// caracteres especiais
					p = p + a;
					LexicoGTabela.AL.add(p);
					p = "";
					if (i == n - 1) {
					} else {
						a = t.charAt(i + 1);
						if (Character.isLetter(a)) {
							c = 1;
						} else {
							if (Character.isDigit(a)) {
								c = 2;
							} else {
								if (a == ':' || a == '>') {
									c = 3;
								} else {
									if (a == '<') {
										c = 4;
									} else {
										if (a == '.') {
											c = 5;
										} else {
											if (a == '\"') {
												c = 6;
											} else {
												if (a == '(') {
													c = 7;
												} else {
													if (a == '$' || a == '[' || a == ']' || a == ')' || a == '*'
															|| a == '/' || a == '-' || a == '+' || a == '=' || a == ';'
															|| a == ',') {
														c = 10;
													} else {
														c = 15;
													}
												}
											}
										}
									}
								}
							}
						}
					}
					break;
				default:// espaços, e outros caracteres
					if (a == ' ') {
						if (i == n - 1) {

						} else {
							a = t.charAt(i + 1);
							if (Character.isLetter(a)) {
								c = 1;
							} else {
								if (Character.isDigit(a)) {
									c = 2;
								} else {
									if (a == ':' || a == '>') {
										c = 3;
									} else {
										if (a == '<') {
											c = 4;
										} else {
											if (a == '.') {
												c = 5;
											} else {
												if (a == '\"') {
													c = 6;
												} else {
													if (a == '(') {
														c = 7;
													} else {
														if (a == '$' || a == '[' || a == ']' || a == ')' || a == '*'
																|| a == '/' || a == '-' || a == '+' || a == '='
																|| a == ';' || a == ',') {
															c = 10;
														} else {
															c = 15;
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
						if (i == n - 1) {

						} else {
							// if(a=='\n'){
							ent = ent + 1;
							a = t.charAt(i + 1);
							if (Character.isLetter(a)) {
								c = 1;
							} else {
								if (Character.isDigit(a)) {
									c = 2;
								} else {
									if (a == ':' || a == '>') {
										c = 3;
									} else {
										if (a == '<') {
											c = 4;
										} else {
											if (a == '.') {
												c = 5;
											} else {
												if (a == '\"') {
													c = 6;
												} else {
													if (a == '(') {
														c = 7;
													} else {
														if (a == '$' || a == '[' || a == ']' || a == ')' || a == '*'
																|| a == '/' || a == '-' || a == '+' || a == '='
																|| a == ';' || a == ',') {
															c = 10;
														} else {
															c = 15;
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
			}
			
			PrincipalController.analiseLexicaComSucesso = true;
			
		} catch (IndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null, "Não há código inserido no campo de entrada.");
			PrincipalController.analiseLexicaComSucesso = false;
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}
}