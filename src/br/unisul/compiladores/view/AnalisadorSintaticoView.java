package br.unisul.compiladores.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import br.unisul.compiladores.odt.ErroSintaticoSemantico;
import br.unisul.compiladores.odt.Literal;
import br.unisul.compiladores.odt.Operacoes;
import br.unisul.compiladores.odt.Simbolo;
import br.unisul.compiladores.odt.Token;
import br.unisul.compiladores.service.Analisador;
import br.unisul.compiladores.utils.Const;
import br.unisul.compiladores.utils.Utils;

public class AnalisadorSintaticoView extends Shell {
	private static Display display = null;
	private static Shell shell = null;
	private static Button btAnalisar;
	private Table tableTokens;
	private List<Token> tokens;
	private TableColumn tblclmnTipo;
	private TableColumn tblclmnCamin;
	private TableColumn tblclmnCaminho;
	private TableColumn tblclmnValor;
	private static StyledText edtCodigo;
	private Button btLimpar;
	private List<String> listaLinhas;
	private Label lbCodigoDescricao;
	private static MenuItem mntmLxico;
	private static MenuItem mntmSinttico;
	private static MenuItem mntmSemntico;
	private static MenuItem mntmExecutar;
	private final String arqHistorico = "arquivoHistorico.txt";
	private String arqAberto = "";

	private static boolean lexico;
	private static boolean sintatico;
	private static boolean semantico;
	private static boolean executar;
	
	private static Color COLOR_GREEN;
	private static Color COLOR_RED;
	private static Color COLOR_BLACK;
	private static Color COLOR_CYAN;
	
	public static String pulaLinha = "\r\n";
	public static String valorInicialLinhas = "1"+ pulaLinha;
	public static String contadorLinhas = valorInicialLinhas;
	private static StyledText campoContadorLinhas;
	private Table tabelaSimbolos;
	private Table tabelaLiteral;
	private Table tabelaOperacoes;
	private static List <ErroSintaticoSemantico> listaErrosSintaticosSemanticos;
	private Table tabelaResultados;
	
	
	public static void main(String args[]) {
		try {
			display = Display.getDefault();
			shell = new Shell(display);

			AnalisadorSintaticoView shell = new AnalisadorSintaticoView(display);
			shell.open();
			shell.layout();
		
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exibirNumeroLinhas(int numeroLinhas, StyledText styledText) {
		
		contadorLinhas = "";
		
		for (int i = 1; i <= numeroLinhas; i++) {
			
			contadorLinhas += i + pulaLinha;
			
		}
		
		styledText.setText(contadorLinhas);			
		
	}
	public AnalisadorSintaticoView(final Display display) {
		super(display, SWT.SHELL_TRIM);
		setLayout(null);

		btAnalisar = new Button(this, SWT.NONE);
		btAnalisar.setEnabled(false);
		btAnalisar.setBounds(467, 493, 145, 39);
		btAnalisar.setText("Analisar...");
		btAnalisar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!isLexico()) {
					
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);				
			        messageBox.setText("Aten��o");
			        messageBox.setMessage("Voc� deve selecionar o modo de an�lise l�xica para continuar.");
			        messageBox.open();						
					 
				} else {
					
					analiseLexica();
					
					if (isSintatico() && tableTokens.getItemCount() > 0) {
						
						analiseSintatica();
						
						if (tabelaOperacoes.getItemCount() > 0 && isExecutar() && getListaErrosSintaticosSemanticos().size() == 0) {
							
							Hipotetica MV = new Hipotetica();
							
							AreaInstrucoes AI = new AreaInstrucoes();
							MV.InicializaAI(AI);
							AreaLiterais AL = new AreaLiterais();
							MV.InicializaAL(AL);
							
							for (TableItem item : tabelaLiteral.getItems()) {
								MV.IncluirAL(AL, item.getText(1));
							}
							
							/**
							 * INCLUI PASSANDO COMO PAR�METRO, (AREADEINSTRUCOES, C�DIGO, OP1, OP2 
							 */
							for (TableItem item : tabelaOperacoes.getItems()) {
								
								int CODIGO = Utils.getTransformacao(item.getText(1));
								int OP1 = 0;
								int OP2 = 0;
								
								if (!item.getText(2).equals("-"))
									OP1 = Integer.parseInt(item.getText(2));
								if (!item.getText(3).equals("-"))
									OP2 = Integer.parseInt(item.getText(3));
								
								MV.IncluirAI(AI, CODIGO, OP1, OP2);
							}
							
							MV.Interpreta(AI, AL);

						}	
					}
					
				}
			}

		});
		
		campoContadorLinhas = new StyledText(this, SWT.BORDER | SWT.MULTI);
		campoContadorLinhas.setBounds(4, 36, 24, 477);
		campoContadorLinhas.setEnabled(false);
		campoContadorLinhas.setText(contadorLinhas);
		campoContadorLinhas.setEditable(false);
		campoContadorLinhas.setDoubleClickEnabled(false);
		
		edtCodigo = new StyledText(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		edtCodigo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				btAnalisar.setText(getLabelBotaoAnalisar(edtCodigo.getSelectionText()));
			}
		});
		edtCodigo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.stateMask == SWT.CTRL && e.keyCode == 97){     
					edtCodigo.selectAll();
				}

			}
			@Override
			public void keyReleased(KeyEvent e) {
				btAnalisar.setText(getLabelBotaoAnalisar(edtCodigo.getSelectionText()));
			}
		});
		edtCodigo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (!"".equals(edtCodigo.getText())) {
					btAnalisar.setEnabled(true);
					lbCodigoDescricao.setText("C�digo com " + edtCodigo.getLineCount() + " linhas");
				} else {
					btAnalisar.setEnabled(false);
					lbCodigoDescricao.setText("");
				}
				
				int linhas = edtCodigo.getLineCount();
				
				exibirNumeroLinhas(linhas,campoContadorLinhas);
				int numeroCaracteres = campoContadorLinhas.getCharCount();
				
				if (linhas >= 10) {
					numeroCaracteres = campoContadorLinhas.getCharCount() - 4;
				}
				
				campoContadorLinhas.setSelection(numeroCaracteres);
				
			}
		});
		edtCodigo.setBounds(30, 36, 327, 496);
		
		Button btAbrir = new Button(this, SWT.NONE);
		btAbrir.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
		        String fileName = getFileName();

		        if (fileName != null && !"".equals(fileName)) {
		        	arqAberto = fileName;
		        	processarArquivo(fileName);
		        }
		    }

		});
		btAbrir.setText("Abrir...");
		btAbrir.setBounds(363, 493, 100, 39);
		
		Button btSair = new Button(this, SWT.NONE);
		btSair.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		btSair.setText("Sair");
		btSair.setBounds(699, 493, 75, 39);
		
		btLimpar = new Button(this, SWT.NONE);
		btLimpar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				edtCodigo.setText("");
				contadorLinhas = valorInicialLinhas;
				campoContadorLinhas.setText(contadorLinhas);
				tableTokens.removeAll();
				tableTokens.clearAll();
				selectedLexico(true);
				selectedSintatico(true);
				arqAberto = "";
				lbCodigoDescricao.setText("");
			}
		});
		btLimpar.setText("Limpar");
		btLimpar.setBounds(618, 493, 75, 39);
		
		lbCodigoDescricao = new Label(this, SWT.NONE);
		lbCodigoDescricao.setFont(new Font(display, "Courier New", 14, SWT.BOLD));
		lbCodigoDescricao.setBounds(10, 7, 347, 23);
		
		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);
		
		MenuItem mntmarquivo = new MenuItem(menu, SWT.CASCADE);
		mntmarquivo.setText("&Arquivo");
		
		Menu menu_1 = new Menu(mntmarquivo);
		mntmarquivo.setMenu(menu_1);
		
		MenuItem mntmAbrir = new MenuItem(menu_1, SWT.NONE);
		mntmAbrir.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
		        String fileName = getFileName();

		        if (fileName != null && !"".equals(fileName)) {
		        	processarArquivo(fileName);	
		        }				
			}
		});
		mntmAbrir.setText("A&brir");
		
		MenuItem mntmsalvar = new MenuItem(menu_1, SWT.NONE);
		mntmsalvar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				salvarArquivo(!"".equals(arqAberto) ? arqAberto : arqHistorico);
			}
		});
		mntmsalvar.setText("&Salvar");
		
		MenuItem mntmSalvarComo = new MenuItem(menu_1, SWT.NONE);
		mntmSalvarComo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				String arquivo = saveFileAs();
				
				if (arquivo != null && !"".equals(arquivo)) {
					arqAberto = arquivo;
					salvarArquivo(arqAberto);
				}
				
			}
		});
		mntmSalvarComo.setText("Salvar co&mo...");
		
		new MenuItem(menu_1, SWT.SEPARATOR);
		
		MenuItem mntmSair = new MenuItem(menu_1, SWT.NONE);
		mntmSair.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		mntmSair.setText("Sai&r");
		
		MenuItem mntmModo = new MenuItem(menu, SWT.CASCADE);
		mntmModo.setText("Modo de An\u00E1lise");
		
		Menu menu_3 = new Menu(mntmModo);
		mntmModo.setMenu(menu_3);
		
		mntmLxico = new MenuItem(menu_3, SWT.CHECK);
		mntmLxico.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setLexico(mntmLxico.getSelection());
				checkMenus();
				
			}
		});
		mntmLxico.setSelection(true);
		mntmLxico.setText("L\u00E9xico");
		
		mntmSinttico = new MenuItem(menu_3, SWT.CHECK);
		mntmSinttico.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSintatico(mntmSinttico.getSelection());
				checkMenus();
			}
		});
		mntmSinttico.setSelection(true);
		mntmSinttico.setText("Sint\u00E1tico");
		
		mntmSemntico = new MenuItem(menu_3, SWT.CHECK);
		mntmSemntico.setSelection(true);
		mntmSemntico.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSemantico(mntmSemntico.getSelection());
				checkMenus();
			}
		});
		mntmSemntico.setText("Sem\u00E2ntico");
		
		mntmExecutar = new MenuItem(menu_3, SWT.CHECK);
		mntmExecutar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setExecutar(mntmExecutar.getSelection());
			}
		});
		mntmExecutar.setText("Executar");
		mntmExecutar.setSelection(true);
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setBounds(363, 36, 411, 451);
		
		TabItem tbtmTokens_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmTokens_1.setText("Tokens");
		
		tableTokens = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tbtmTokens_1.setControl(tableTokens);
		tableTokens.setHeaderVisible(true);
		tableTokens.setLinesVisible(true);
		
		tblclmnTipo = new TableColumn(tableTokens, SWT.NONE);
		tblclmnTipo.setWidth(50);
		tblclmnTipo.setText("Linha");
		
		tblclmnCamin = new TableColumn(tableTokens, SWT.NONE);
		tblclmnCamin.setWidth(55);
		tblclmnCamin.setText("C\u00F3digo");
		
		tblclmnCaminho = new TableColumn(tableTokens, SWT.NONE);
		tblclmnCaminho.setWidth(110);
		tblclmnCaminho.setText("Token");
		
		tblclmnValor = new TableColumn(tableTokens, SWT.NONE);
		tblclmnValor.setWidth(155);
		tblclmnValor.setText("Decri\u00E7\u00E3o");
		
		TabItem tbtmTabOperacoes = new TabItem(tabFolder, SWT.NONE);
		tbtmTabOperacoes.setText("Tab. Opera\u00E7\u00F5es");
		
		tabelaOperacoes = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tabelaOperacoes.setLinesVisible(true);
		tabelaOperacoes.setHeaderVisible(true);
		tbtmTabOperacoes.setControl(tabelaOperacoes);
		
		TableColumn tabelaColunaSeq = new TableColumn(tabelaOperacoes, SWT.NONE);
		tabelaColunaSeq.setWidth(50);
		tabelaColunaSeq.setText("Seq.");
		
		TableColumn tabelaColunaCodigo = new TableColumn(tabelaOperacoes, SWT.NONE);
		tabelaColunaCodigo.setWidth(70);
		tabelaColunaCodigo.setText("C\u00F3digo");
		
		TableColumn tabelaColunaOP1 = new TableColumn(tabelaOperacoes, SWT.NONE);
		tabelaColunaOP1.setWidth(50);
		tabelaColunaOP1.setText("Op1");
		
		TableColumn tabelaColunaOP2 = new TableColumn(tabelaOperacoes, SWT.NONE);
		tabelaColunaOP2.setWidth(50);
		tabelaColunaOP2.setText("Op2");
		
		TableColumn tabelaColunaObs = new TableColumn(tabelaOperacoes, SWT.NONE);
		tabelaColunaObs.setWidth(160);
		tabelaColunaObs.setText("Observa\u00E7\u00E3o");
		
		TabItem tbtmTabelaSimbolos = new TabItem(tabFolder, SWT.NONE);
		tbtmTabelaSimbolos.setText("Tab. S\u00EDmbolos");
		
		tabelaSimbolos = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tbtmTabelaSimbolos.setControl(tabelaSimbolos);
		tabelaSimbolos.setHeaderVisible(true);
		tabelaSimbolos.setLinesVisible(true);
		
		TableColumn tableColumn_5 = new TableColumn(tabelaSimbolos, SWT.NONE);
		tableColumn_5.setWidth(50);
		tableColumn_5.setText("Seq.");
		
		TableColumn tableColumn_6 = new TableColumn(tabelaSimbolos, SWT.NONE);
		tableColumn_6.setWidth(70);
		tableColumn_6.setText("Nome");
		
		TableColumn tableColumn_7 = new TableColumn(tabelaSimbolos, SWT.NONE);
		tableColumn_7.setWidth(80);
		tableColumn_7.setText("Categoria");
		
		TableColumn tableColumn_8 = new TableColumn(tabelaSimbolos, SWT.NONE);
		tableColumn_8.setWidth(70);
		tableColumn_8.setText("N\u00EDvel");
		
		TableColumn tableColumn_9 = new TableColumn(tabelaSimbolos, SWT.NONE);
		tableColumn_9.setWidth(55);
		tableColumn_9.setText("GA");
		
		TableColumn tableColumn_10 = new TableColumn(tabelaSimbolos, SWT.NONE);
		tableColumn_10.setWidth(55);
		tableColumn_10.setText("GB");
		
		TabItem tbtmLiteral = new TabItem(tabFolder, SWT.NONE);
		tbtmLiteral.setText("Literal");
		
		tabelaLiteral = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tabelaLiteral.setLinesVisible(true);
		tabelaLiteral.setHeaderVisible(true);
		tbtmLiteral.setControl(tabelaLiteral);
		
		TableColumn tableColumn_11 = new TableColumn(tabelaLiteral, SWT.NONE);
		tableColumn_11.setWidth(50);
		tableColumn_11.setText("Seq.");
		
		TableColumn tableColumn_12 = new TableColumn(tabelaLiteral, SWT.NONE);
		tableColumn_12.setWidth(325);
		tableColumn_12.setText("Literal");
		
		tabelaResultados = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		tabelaResultados.setBounds(4, 538, 772, 77);
		tabelaResultados.setLinesVisible(true);
		tabelaResultados.setHeaderVisible(true);
		
		TableColumn tableColumn = new TableColumn(tabelaResultados, SWT.NONE);
		tableColumn.setWidth(40);
		tableColumn.setText("Seq.");
		
		TableColumn tableColumn_1 = new TableColumn(tabelaResultados, SWT.NONE);
		tableColumn_1.setWidth(50);
		tableColumn_1.setText("Tipo");
		
		TableColumn tableColumn_2 = new TableColumn(tabelaResultados, SWT.NONE);
		tableColumn_2.setWidth(50);
		tableColumn_2.setText("Linha");
		
		TableColumn tableColumn_3 = new TableColumn(tabelaResultados, SWT.NONE);
		tableColumn_3.setWidth(385);
		tableColumn_3.setText("Log de execu\u00E7\u00E3o");
		
		TableColumn tableColumn_4 = new TableColumn(tabelaResultados, SWT.NONE);
		tableColumn_4.setWidth(100);
		tableColumn_4.setText("Esperava");
		
		TableColumn tableColumn_13 = new TableColumn(tabelaResultados, SWT.NONE);
		tableColumn_13.setWidth(100);
		tableColumn_13.setText("Apareceu");

		/**
		 * Se encontra arquivo de historico traz para tela
		 */
		
		configuracoesIniciais();
		
		createContents();
	}

	public static void processarArquivo(String fileName) {
		
		BufferedReader fileReader = null;
		edtCodigo.setText("");
		
		try {
			File file = new File(fileName);
			fileReader = new BufferedReader(new FileReader(file));
			String linha = "";
			
			while ((linha = fileReader.readLine()) != null) {
				edtCodigo.append(linha + "\n");
			}
			
			fileReader.close();
			fileReader = null;
			
			int linhas = edtCodigo.getLineCount();
			exibirNumeroLinhas(linhas,campoContadorLinhas);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void configuracoesIniciais() {
		setLexico(true);
		setSintatico(true);
		setSemantico(true);
		setExecutar(true);

		/**
		 * SE ENCONTRA ARQUIVO DE HISTORICO TRAZ PARA TELA
		 */
		File arq = new File(Const.ARQUIVO_HISTORICO);  
        
		if (arq.exists()) {  
			processarArquivo(Const.ARQUIVO_HISTORICO);
		} 
		
	}
	
	protected void createContents() {
		setText("Analisador");
		setSize(800, 683);
		selectedLexico(true);
		selectedSintatico(true);
	}

	@Override
	protected void checkSubclass() {
	}
	
	private String getLabelBotaoAnalisar (String selection) {
		if (!"".equals(selection)) {
			return "Analisar sele��o...";
		} else {
			return "Analisar";
		}		
	}
	
	private String getFileName() {
	    String caminho = System.getProperty("user.dir");
		FileDialog dialogOpen = new FileDialog(shell, SWT.OPEN);
        dialogOpen.setText("Abrir arquivo");
        dialogOpen.setFilterPath(caminho);
        String[] filterExt = {"*.txt"};
        String[] filterNames = {"Arquivos de texto (*.txt)"};
        dialogOpen.setFilterExtensions(filterExt);
        dialogOpen.setFilterNames(filterNames);
        String fileName = dialogOpen.open();
		return fileName;
	}

	private String saveFileAs() {
	    String caminho = System.getProperty("user.dir");
		FileDialog saveAsDialog = new FileDialog(shell, SWT.SAVE);
        saveAsDialog.setText("Salvar como...");
        saveAsDialog.setFilterPath(caminho);
        String[] filterExt = {"*.txt"};
        String[] filterNames = {"Arquivos de texto (*.txt)"};
        saveAsDialog.setFilterExtensions(filterExt);
        saveAsDialog.setFilterNames(filterNames);
        String fileName = saveAsDialog.open();
		return fileName;
	}	
	
	private void analiseLexica() {
		if ("".equals(edtCodigo.getText())) {
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);				
	        messageBox.setText("Erro");
	        messageBox.setMessage("Voc� deve selecionar um arquivo v�lido ou informar o c�digo para an�lise.");
	        messageBox.open();			
		} else {
			
			populateListaLinhas(arqHistorico);
			
			Analisador analisador = new Analisador();
			List<Token> listaTokens = analisador.iniciarAnaliseLexica(getListaLinhas());
			if (listaTokens.size() > 0) {
				
				tableTokens.removeAll();
				tableTokens.clearAll();
			
				tabelaSimbolos.removeAll();
				tabelaSimbolos.clearAll();
				
				tabelaOperacoes.removeAll();
				tabelaOperacoes.clearAll();
				
				tabelaLiteral.removeAll();
				tabelaLiteral.clearAll();	
				
				tabelaResultados.removeAll();
				tabelaResultados.clearAll();	

				getTokens().clear();
				setTokens(null);
				
				for (Token token : listaTokens) {
					getTokens().add(token);
					String[] linha = new String[] { String.valueOf(token.getLinha()), 
													String.valueOf(token.getCodigo()),
													token.getToken(),
													token.getDescricao()};
					TableItem coluna = new TableItem(tableTokens, SWT.NONE);
					coluna.setText(linha);
				}
			}
		}	
		
	}


	private void analiseSintatica() {
		
		populateListaLinhas(arqHistorico);

		Analisador analisador = new Analisador();
		analisador.iniciarAnaliseSintatica(getTokens(), isSemantico());
		setListaErrosSintaticosSemanticos(analisador.getListaErrosSintaticosSemanticos());
		List<Simbolo> listaSimbolos = analisador.getListaSimbolos();
		List<Operacoes> listaOperacoes = analisador.getListaOperacoes();
		List<Literal> listaLiteral = analisador.getListaLiteral();
		
		if (getListaErrosSintaticosSemanticos().size() > 0) {
			
			for (ErroSintaticoSemantico erro : getListaErrosSintaticosSemanticos()) {
				int countLinha = tabelaResultados.getItemCount() + 1;
				String[] linha = new String[] {String.valueOf(countLinha), Const.RESULTADO_TIPO_ERRO,
	                       					   String.valueOf(erro.getLinha()), (!"".equals(erro.getMensagem()) ? erro.getMensagem() : Const.MENSAGEM_ERRO),
	                       					   erro.getEsperava(), erro.getApareceu()};
				TableItem coluna = new TableItem(tabelaResultados, SWT.NONE, 0);
				coluna.setForeground(COLOR_BLACK);
				coluna.setBackground(COLOR_RED);
				coluna.setText(linha);
				
				for (TableItem item : tableTokens.getItems()) {
					if (Integer.valueOf(item.getText(0)) == erro.getLinha()
							&& erro.getApareceu().equals(item.getText(2))) {
						item.setForeground(COLOR_BLACK);
						item.setBackground(COLOR_RED);
					}
				}				
			}
			
		}
		
		if (listaSimbolos.size() > 0) {
			for (Simbolo simbolo : listaSimbolos) {
				String[] linha = new String[] {String.valueOf(simbolo.getSeq()), simbolo.getNome(), simbolo.getCategoria(), 
						String.valueOf(simbolo.getNivel()), simbolo.getGeralA(), simbolo.getGeralB()};
				TableItem coluna = new TableItem(tabelaSimbolos, SWT.NONE);
				coluna.setText(linha);
			}			
		}
		
		if (listaOperacoes.size() > 0) {
			for (Operacoes operacao : listaOperacoes) {
				String[] linha = new String[] {String.valueOf(operacao.getSeq()), operacao.getCodigo(), operacao.getOp1(), 
						operacao.getOp2(), operacao.getObservacao()};
				TableItem coluna = new TableItem(tabelaOperacoes, SWT.NONE);
				coluna.setText(linha);
			}			
		}

		if (listaLiteral.size() > 0) {
			for (Literal literal : listaLiteral) {
				String[] linha = new String[] {String.valueOf(literal.getSeq()), literal.getLiteral()};
				TableItem coluna = new TableItem(tabelaLiteral, SWT.NONE);
				coluna.setText(linha);
			}			
		}
	}	
	
	public static List<ErroSintaticoSemantico> getListaErrosSintaticosSemanticos() {
		if (listaErrosSintaticosSemanticos == null) {
			listaErrosSintaticosSemanticos = new ArrayList<ErroSintaticoSemantico>();
		}
		return listaErrosSintaticosSemanticos;
	}
	
	public void populateListaLinhas(String arquivo) {
		FileWriter fileTemp = null;
		BufferedWriter bf = null;
		
		try {
			fileTemp = new FileWriter(arquivo, false);
			bf = new BufferedWriter(fileTemp);
			
			bf.write((!"".equals(edtCodigo.getSelectionText()) ? edtCodigo.getSelectionText() : edtCodigo.getText()));
			bf.close();
			
			File file = new File(arquivo);
			BufferedReader fileReader = new BufferedReader(new FileReader(file));

			String linhaTemp = "";
			getListaLinhas().clear();
			setListaLinhas(null);
			
			while ((linhaTemp = fileReader.readLine()) != null) {
				getListaLinhas().add(linhaTemp);
			}
			
			fileReader.close();
			fileReader = null;
			
		} catch (IOException e) {
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);				
	        messageBox.setText("Erro");
	        messageBox.setMessage("Copie o aplicativo para um diret�rio no sistema. N�o execute direto do CD.");
	        messageBox.open();	
	        e.printStackTrace();
		}
	}
	
	public void salvarArquivo(String arquivo) {
		
		FileWriter fileTemp = null;
		BufferedWriter bf = null;
		
		try {
			
			fileTemp = new FileWriter(arquivo, false);
			bf = new BufferedWriter(fileTemp);
			bf.write((!"".equals(edtCodigo.getSelectionText()) ? edtCodigo.getSelectionText() : edtCodigo.getText()));
			bf.close();
			
		} catch (IOException e) {
			MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);				
	        messageBox.setText("Erro");
	        messageBox.setMessage("Copie o aplicativo para um diret�rio no sistema. N�o execute direto do CD.");
	        messageBox.open();	
	        e.printStackTrace();
		}
	}	
	
	public void selectedLexico(boolean selection) {
		mntmLxico.setSelection(selection);
		setLexico(selection);
	}
	public void selectedSintatico(boolean selection) {
		mntmSinttico.setSelection(selection);
		setSintatico(selection);
	}
	
	public List<String> getListaLinhas() {
		if (listaLinhas == null) {
			listaLinhas = new ArrayList<String>();
		}
		return listaLinhas;
	}

	public void setListaLinhas(List<String> listaLinhas) {
		this.listaLinhas = listaLinhas;
	}

	public static boolean isLexico() {
		return lexico;
	}

	public static void setLexico(boolean lexicoParam) {
		lexico = lexicoParam;
	}

	public static boolean isSintatico() {
		return sintatico;
	}

	public static void setSintatico(boolean sintaticoParam) {
		sintatico = sintaticoParam;
	}
	
	public static boolean isSemantico() {
		return semantico;
	}
	
	public static boolean isExecutar() {
		return executar;
	}

	public static void setSemantico(boolean semanticoParam) {
		semantico = semanticoParam;
	}

	public List<Token> getTokens() {
		if (tokens == null) {
			tokens = new ArrayList<Token>();
		}
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}
    
	public static void setListaErrosSintaticosSemanticos(
			List<ErroSintaticoSemantico> listaErrosSintaticosSemanticos) {
		AnalisadorSintaticoView.listaErrosSintaticosSemanticos = listaErrosSintaticosSemanticos;
	}
	
	public static void setExecutar(boolean executar) {
		AnalisadorSintaticoView.executar = executar;
	}
	
	public static void checkMenus() {
		if (!isLexico()) {
			mntmSinttico.setEnabled(false);
			mntmSemntico.setEnabled(false);
			mntmExecutar.setEnabled(false);
			mntmSinttico.setSelection(false);
			mntmSemntico.setSelection(false);
			mntmExecutar.setSelection(false);
			setSintatico(false);
			setSemantico(false);
			setExecutar(false);
		} else if (!isSintatico()) {
			mntmSinttico.setEnabled(true);
			mntmSemntico.setEnabled(false);
			mntmExecutar.setEnabled(false); 
			mntmSemntico.setSelection(false);
			mntmExecutar.setSelection(false);		
			setSemantico(false);
			setExecutar(false);			
		} else if (!isSemantico()) {
			mntmSinttico.setEnabled(true);
			mntmSemntico.setEnabled(true);
			
			mntmExecutar.setEnabled(false);
			mntmExecutar.setSelection(false);	
			setExecutar(false);
		} else {
			mntmSinttico.setEnabled(true);
			mntmSemntico.setEnabled(true);
			mntmExecutar.setEnabled(true);
		}
	}
}
