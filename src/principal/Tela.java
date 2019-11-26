package principal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import lexico.G_Tabela;
import lexico.Lexico;
import lexico.Tipo_Token;
import lexico.Token;
import maq_hipo.Hipotetica;

import sintatico.Sintatico;

import semantico.Semantico;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.wb.swt.SWTResourceManager;

public class Tela extends Shell {

	static Tela shell;

	/********************************************************************************/

	public static ArrayList<semantico.Instrucao> AL_Instr = new ArrayList<semantico.Instrucao>();
	public static String AreaLiterais = "";

	/********************************************************************************/

	public static ArrayList<String> AL = new ArrayList<String>();
	public static ArrayList<String> Erro_Sin = new ArrayList<String>();
	public static ArrayList<String> Erro_Sem = new ArrayList<String>();
	public static ArrayList<Token> AL_p = new ArrayList<Token>();
	public static ArrayList<Token> AL_o = new ArrayList<Token>();
	public static ArrayList<Token> AL_s = new ArrayList<Token>();
	public static ArrayList<Token> ALfinal = new ArrayList<Token>();
	public static TabItem aba_lex;
	public static TabItem aba_sem;
	public static TabItem aba_sin;
	public static TabItem aba_ger;
	public static Table table_lex;
	public static Table table_sin;
	public static Table table_sem;
	public static Table table_ger;
	public static TabFolder tabFolder;
	public static StyledText styledText;
	public static MenuItem mntmSalvar;
	public static boolean salvo = true;
	public static String caminho = "";
	public static int resp;

	public static int seq = -1;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			shell = new Tela(display);
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

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public Tela(final Display display) {
		super(display, SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.TITLE);

		Menu menu = new Menu(this, SWT.BAR);
		setMenuBar(menu);

		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("Principal");

		Menu menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);

		MenuItem mntmNovo = new MenuItem(menu_1, SWT.NONE);
		mntmNovo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					if (salvo == false) {
						resp = JOptionPane.showConfirmDialog(null,
								"O programa não foi salvo, deseja fecha-lo mesmo assim?");
						if (resp == 0) {
							styledText.setText("");
							caminho = "";
							mntmSalvar.setEnabled(false);
							table_lex.removeAll();
							table_sin.removeAll();
							table_sem.removeAll();
							table_ger.removeAll();
							salvo = true;
						}
					} else {
						styledText.setText("");
						caminho = "";
						mntmSalvar.setEnabled(false);
						table_lex.removeAll();
						table_sin.removeAll();
						table_sem.removeAll();
						table_ger.removeAll();
						salvo = true;
					}
				} catch (Exception exception) {

				}
			}
		});
		mntmNovo.setText("Novo");

		MenuItem mntmAbrir = new MenuItem(menu_1, SWT.NONE);
		mntmAbrir.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (salvo == false) {
					int resp = JOptionPane.showConfirmDialog(null,
							"O programa não foi salvo, deseja fecha-lo mesmo assim?");
					if (resp == 0) {
						try {
							FileDialog dlg = new FileDialog(new Shell(), SWT.OPEN);
							String fileName = dlg.open();
							if (fileName != null) {
								File file = new File(fileName);
								caminho = fileName;
								BufferedReader br = new BufferedReader(new FileReader(file));
								String linha = "", codigo = "";
								while ((linha = br.readLine()) != null) {
									codigo += linha + "\n";
								}
								styledText.setText(codigo);
								br.close();
								br = null;
								file = null;
								mntmSalvar.setEnabled(false);
								table_lex.removeAll();
								table_sin.removeAll();
								table_sem.removeAll();
								table_ger.removeAll();
								salvo = true;
							}
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, e);
						}
					}
				} else {
					try {
						FileDialog dlg = new FileDialog(new Shell(), SWT.OPEN);
						String fileName = dlg.open();
						if (fileName != null) {
							File file = new File(fileName);
							caminho = fileName;
							BufferedReader br = new BufferedReader(new FileReader(file));
							String linha = "", codigo = "";
							while ((linha = br.readLine()) != null) {
								codigo += linha + "\n";
							}
							styledText.setText(codigo);
							br.close();
							br = null;
							file = null;
							mntmSalvar.setEnabled(false);
							table_lex.removeAll();
							table_sin.removeAll();
							table_sem.removeAll();
							table_ger.removeAll();
							salvo = true;
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e);
					}
				}
			}
		});
		mntmAbrir.setText("Abrir");

		mntmSalvar = new MenuItem(menu_1, SWT.NONE);
		mntmSalvar.setEnabled(false);
		mntmSalvar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					FileWriter fileWriter = new FileWriter(caminho, false);
					BufferedWriter bw = new BufferedWriter(fileWriter);
					bw.write(styledText.getText());
					bw.close();
					bw = null;
					fileWriter = null;
					mntmSalvar.setEnabled(false);
					salvo = true;
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}
			}
		});
		mntmSalvar.setText("Salvar");

		MenuItem mntmSalvarComo = new MenuItem(menu_1, SWT.NONE);
		mntmSalvarComo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					FileDialog dlg = new FileDialog(new Shell(), SWT.SAVE);
					String fileName = dlg.open();
					if (fileName != null) {
						FileWriter fileWriter = new FileWriter(fileName + ".txt", false);
						caminho = fileName + ".txt";
						BufferedWriter bw = new BufferedWriter(fileWriter);
						bw.write(styledText.getText());
						bw.close();
						bw = null;
						fileWriter = null;
						mntmSalvar.setEnabled(false);
						salvo = true;
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				}
			}
		});
		mntmSalvarComo.setText("Salvar Como");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mntmSair = new MenuItem(menu_1, SWT.NONE);
		mntmSair.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					if (salvo == false) {
						int resp = JOptionPane.showConfirmDialog(null,
								"O programa não foi salvo, deseja fecha-lo mesmo assim?");
						if (resp == 0) {
							display.close();
						}
					} else {
						display.close();
					}
				} catch (Exception exception) {

				}
			}
		});
		mntmSair.setText("Sair");

		MenuItem mntmFunes = new MenuItem(menu, SWT.CASCADE);
		mntmFunes.setText("Funções");

		Menu menu_2 = new Menu(mntmFunes);
		mntmFunes.setMenu(menu_2);

		MenuItem mntmLexico = new MenuItem(menu_2, SWT.NONE);
		mntmLexico.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Lexico();
				tabFolder.setSelection(0);
			}
		});
		mntmLexico.setText("Lexico");

		MenuItem mntmSinttico = new MenuItem(menu_2, SWT.NONE);
		mntmSinttico.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (Sintatico()) {
					tabFolder.setSelection(1);
				}
			}
		});
		mntmSinttico.setText("Sintático");

		MenuItem mntmSemntico = new MenuItem(menu_2, SWT.NONE);
		mntmSemntico.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (Semantico()) {
					tabFolder.setSelection(2);
				}
			}
		});
		mntmSemntico.setText("Semântico");

		MenuItem mntmGeraoCdigo = new MenuItem(menu_2, SWT.NONE);
		mntmGeraoCdigo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (GerCod()) {
					tabFolder.setSelection(3);
				}
			}
		});
		mntmGeraoCdigo.setText("Geração Código");

		Label lblCdigo = new Label(this, SWT.NONE);
		lblCdigo.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL));
		lblCdigo.setBounds(10, 10, 44, 16);
		lblCdigo.setText("Código:");

		styledText = new StyledText(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		styledText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				mntmSalvar.setEnabled(true);
				salvo = false;
			}
		});
		styledText.setBounds(10, 32, 393, 541);

		tabFolder = new TabFolder(this, SWT.NONE);
		tabFolder.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tabFolder.setBounds(409, 10, 439, 563);

		TabItem aba_lex = new TabItem(tabFolder, SWT.NONE);
		aba_lex.setToolTipText("");
		aba_lex.setText("Léxico");

		table_lex = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		aba_lex.setControl(table_lex);
		table_lex.setHeaderVisible(true);
		table_lex.setLinesVisible(true);

		TableColumn tblclmnN = new TableColumn(table_lex, SWT.NONE);
		tblclmnN.setWidth(32);
		tblclmnN.setText("n°");

		TableColumn tblclmnCod = new TableColumn(table_lex, SWT.NONE);
		tblclmnCod.setWidth(43);
		tblclmnCod.setText("Cod");

		TableColumn tblclmnToken = new TableColumn(table_lex, SWT.NONE);
		tblclmnToken.setWidth(181);
		tblclmnToken.setText("Token");

		TableColumn tblclmnDescrio = new TableColumn(table_lex, SWT.NONE);
		tblclmnDescrio.setWidth(169);
		tblclmnDescrio.setText("Descrição");

		aba_sin = new TabItem(tabFolder, SWT.NONE);
		aba_sin.setText("Sintático");

		table_sin = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		aba_sin.setControl(table_sin);
		table_sin.setHeaderVisible(true);
		table_sin.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table_sin, SWT.NONE);
		tblclmnNewColumn.setWidth(425);
		tblclmnNewColumn.setText("Erros");

		aba_sem = new TabItem(tabFolder, SWT.NONE);
		aba_sem.setText("Semantico");

		table_sem = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		aba_sem.setControl(table_sem);
		table_sem.setHeaderVisible(true);
		table_sem.setLinesVisible(true);

		TableColumn tblclmnErros = new TableColumn(table_sem, SWT.NONE);
		tblclmnErros.setWidth(427);
		tblclmnErros.setText("Erros");

		aba_ger = new TabItem(tabFolder, SWT.NONE);
		aba_ger.setText("Ger. Código");

		table_ger = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		aba_ger.setControl(table_ger);
		table_ger.setHeaderVisible(true);
		table_ger.setLinesVisible(true);

		TableColumn tblclmnSeq = new TableColumn(table_ger, SWT.NONE);
		tblclmnSeq.setWidth(26);

		TableColumn tblclmnInstrues = new TableColumn(table_ger, SWT.NONE);
		tblclmnInstrues.setText("cod.");
		tblclmnInstrues.setWidth(35);

		TableColumn tblclmnInstruo = new TableColumn(table_ger, SWT.NONE);
		tblclmnInstruo.setWidth(85);
		tblclmnInstruo.setText("Instrução");

		TableColumn tblclmnOp = new TableColumn(table_ger, SWT.NONE);
		tblclmnOp.setWidth(35);
		tblclmnOp.setText("OP1");

		TableColumn tblclmnOp_1 = new TableColumn(table_ger, SWT.NONE);
		tblclmnOp_1.setWidth(35);
		tblclmnOp_1.setText("OP2");
		createContents();
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Compilador");
		setSize(866, 631);

	}

	public static int exibirDialogoMensagem(Shell pai, String msg, int tipo) {
		MessageBox dialogo = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
		return dialogo.open();
	}

	public static void Lexico() {
		table_lex.removeAll();
		AL.removeAll(AL);
		ALfinal.removeAll(ALfinal);
		@SuppressWarnings("unused")
		Tipo_Token TT = new Tipo_Token();
		@SuppressWarnings("unused")
		Lexico l = new Lexico();
		@SuppressWarnings("unused")
		G_Tabela gt = new G_Tabela();
		for (int i = 0; i < ALfinal.size(); i++) {
			String[] a = { (i + 1 + ""), ALfinal.get(i).getCodigo() + "", ALfinal.get(i).getNome(),
					ALfinal.get(i).getDesc() };
			TableItem ti = new TableItem(table_lex, SWT.NONE);
			ti.setText(a);
		}

	}

	public static boolean Sintatico() {
		boolean b = false;
		table_sin.removeAll();
		Erro_Sin.removeAll(Erro_Sin);
		Lexico();
		@SuppressWarnings("unused")
		Sintatico s = new Sintatico();
		for (int i = 0; i < Erro_Sin.size(); i++) {
			TableItem ti = new TableItem(table_sin, SWT.NONE);
			ti.setText(Erro_Sin.get(i));
			b = true;
		}
		return b;
	}

	@SuppressWarnings("unused")
	public static boolean Semantico() {
		boolean b = false;
		table_sem.removeAll();
		Erro_Sem.removeAll(Erro_Sem);
		AL_Instr.removeAll(AL_Instr);
		AreaLiterais = "";
		Sintatico();
		if ("Codigo analizado com sucesso !".equals(Erro_Sin.get(0))) {
			Semantico s = new Semantico();
			for (int i = 0; i < Erro_Sem.size(); i++) {
				TableItem ti = new TableItem(table_sem, SWT.NONE);
				ti.setText(Erro_Sem.get(i));
				b = true;
			}
		} else {
			Erro_Sem.add("Erro sintático !");
			tabFolder.setSelection(1);
		}
		return b;
	}

	public boolean GerCod() {
		boolean b = false;
		Semantico();
		table_ger.removeAll();
		int seq = -1;
		if ("Codigo analizado com sucesso !".equals(Erro_Sem.get(0))) {
			Hipotetica.Interpreta();
			for (int i = 0; i < AL_Instr.size(); i++) {
				String[] a = { "", "", "", "", "" };
				a[0] = (seq += 1) + "";
				a[1] = AL_Instr.get(i).getSeq() + "";
				a[2] = AL_Instr.get(i).getCod();
				a[3] = AL_Instr.get(i).getOp1() + "";
				a[4] = AL_Instr.get(i).getOp2() + "";
				TableItem ti = new TableItem(table_ger, SWT.NONE);
				ti.setText(a);
				b = true;
			}
		} else {
			if ("Erro sintático !".equals(Erro_Sem.get(0))) {
				tabFolder.setSelection(1);
			} else {
				tabFolder.setSelection(2);
			}
		}
		return b;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
