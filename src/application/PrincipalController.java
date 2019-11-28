package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Lexico;
import model.LexicoGTabela;
import model.LexicoTipoToken;
import model.LexicoToken;
import model.MaquinaHipotetica;
import model.Semantico;
import model.SemanticoAcao;
import model.SemanticoInstrucao;
import model.Sintatico;

public class PrincipalController {
	
//	Elementos gerais da interface gráfica:
	public TabPane tabPane = new TabPane();
	public TextArea textAreaEntrada = new TextArea();
	public MenuItem mntmSalvar = new MenuItem();
	
//	Tabela léxica e suas colunas :
	public TableView<LexicoToken> tabelaLexica = new TableView<LexicoToken>();
	public TableColumn<LexicoToken, Integer> ordemLexica = new TableColumn<>("N°");
	public TableColumn<LexicoToken, Integer> codigo = new TableColumn<>("codigo");
	public TableColumn<LexicoToken, String> nome = new TableColumn<>("Token");
	public TableColumn<LexicoToken, String> desc = new TableColumn<>("Descrição");
	
	// Tabela sintática e sua coluna para listar erros, etc:
	public TableView<String> tabelaSintatica = new TableView<>();
	public TableColumn<String, String> errosSintaticos = new TableColumn<>("Erros");

	// Tabela semântica e sua coluna para listar erros, etc:
	public TableView<String> tabelaSemantica = new TableView<>();
	public TableColumn<String, String> errosSemanticos = new TableColumn<>("Erros");

	// Tabela de geração de código e suas colunas:
	public TableView<SemanticoInstrucao> tabelaCodigoIntermediario = new TableView<>();
	public TableColumn<SemanticoInstrucao, Integer> ordemCodInter = new TableColumn<>("N°");
	public TableColumn<SemanticoInstrucao, Integer> codigoTabelaCodInter = new TableColumn<>("Código");
	public TableColumn<SemanticoInstrucao, String> instrucao = new TableColumn<>("Instrução");
	public TableColumn<SemanticoInstrucao, Integer> op1 = new TableColumn<>("OP1");
	public TableColumn<SemanticoInstrucao, Integer> op2 = new TableColumn<>("OP2");
	
//	Variáveis de uso dos métodos gerais:
	public static ArrayList<model.SemanticoInstrucao> AL_Instr = new ArrayList<model.SemanticoInstrucao>();
	public static ArrayList<LexicoToken> ALfinal = new ArrayList<LexicoToken>();
	public static ArrayList<String> AL = new ArrayList<String>();
	public static String AreaLiterais = "";
	
//	Variáveis usadas para o menu "Opções"
	public static boolean salvo = true;
	public static String caminho = "";
	public static File arquivoAberto;
	public static boolean analiseLexicaComSucesso = false;
	public static boolean codigoGeradoComSucesso = false;
	public static boolean analiseSintaticaComSucesso = false;
	public static boolean analiseSemanticaComSucesso = false;
	
	/**
	 * Método para inicializar as configurações das tabelas.
	 */
	private void inicalizarTabelas() {
		
//		Tabela léxica:
		ordemLexica.setCellValueFactory(new PropertyValueFactory<>("ordem"));
		codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		desc.setCellValueFactory(new PropertyValueFactory<>("desc"));

//		Tabela sintática
		errosSintaticos.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
		
//		Tabela semântica
		errosSemanticos.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
		
//		Tabela de código intermediário:
		ordemCodInter.setCellValueFactory(new PropertyValueFactory<>("ordem"));
		codigoTabelaCodInter.setCellValueFactory(new PropertyValueFactory<>("seq"));
		op1.setCellValueFactory(new PropertyValueFactory<>("op1"));
		op2.setCellValueFactory(new PropertyValueFactory<>("op2"));
		instrucao.setCellValueFactory(new PropertyValueFactory<>("cod"));
	}

	public void adicionarColunaTabelaLexicaTeste() {

//		Lexico
		LexicoToken lt = new LexicoToken();
		lt.setCodigo(1);
		lt.setNome("Program");
		lt.setDesc("Palavra reservada");
		
		tabelaLexica.getItems().add(lt);
		
//		Geração de código:
		SemanticoInstrucao si = new SemanticoInstrucao();
		si.setSeq(0);
		si.setCod("CALL");
		si.setOp1(5);
		si.setOp1(8);
		
		tabelaCodigoIntermediario.getItems().add(si);
		
//		Tabela de erros sintaticos
		tabelaSintatica.getItems().add(new String("erro sintatico de teste"));
		
//		Tabela de erros semanticos
		tabelaSemantica.getItems().add(new String("erro semantico de teste"));
	}

	public void resetarCampos() {
		arquivoAberto = null;
		textAreaEntrada.setText("");
		caminho="";
		mntmSalvar.setDisable(true);
		tabelaLexica.getItems().removeAll();
		tabelaSintatica.getItems().removeAll();
		tabelaSemantica.getItems().removeAll();
		tabelaCodigoIntermediario.getItems().removeAll();
		salvo=true;
		analiseLexicaComSucesso = false;
		codigoGeradoComSucesso = false;
		analiseSintaticaComSucesso = false;
		analiseSemanticaComSucesso = false;
	}

	@FXML
	public void novo() {
		int resp = 0;

//		Se o programa não estiver salvo, pergunte. Senão reseta os campos.
		if (!salvo)
			resp = JOptionPane.showConfirmDialog(null, "O programa não foi salvo, deseja fecha-lo mesmo assim?");
		
		if (resp != 0)
			return;
		
		resetarCampos();
	}

	@FXML
	public void abrir() {
		int resp = 0;

//		Se o programa não estiver salvo, pergunte. Senão reseta os campos.
		if (!salvo)
			resp = JOptionPane.showConfirmDialog(null, "O programa não foi salvo, deseja fecha-lo mesmo assim?");
		
		if (resp != 0)
			return;

		selecionarArquivoEntrada();
	}
	
	public void selecionarArquivoEntrada() {
		try {
			FileChooser fc = new FileChooser();
			fc.setTitle("Selecione o código de entrada:");

			File file = fc.showOpenDialog(new Stage());

			if (file.exists() && file.isFile()) {

				caminho = file.getAbsolutePath();

				BufferedReader br = new BufferedReader(new FileReader(file));

				String linha = "", codigo = "";
				while ((linha = br.readLine()) != null) {
					codigo += linha + "\n";
				}

				resetarCampos();

				textAreaEntrada.setText(codigo);
				arquivoAberto = file;
				br.close();
			}
			

		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "Não foi possível abrir o arquivo");
		}
	}

	@FXML
	public void salvar() {
		if (arquivoAberto == null) {
			salvarComo();
			
		} else {
			salvarArquivo(arquivoAberto, textAreaEntrada.getText());
			definirEdicaoSalva();
		}
		
	}

	@FXML
	public void salvarComo() {
			FileChooser fc = new FileChooser();
			fc.setTitle("Escolha o caminho e nome do arquivo a ser salvo:");

			File file = fc.showSaveDialog(new Stage());
			
		    salvarArquivo(file, textAreaEntrada.getText());
		    
		    arquivoAberto = file;

			definirEdicaoSalva();
	}
	
	@FXML
	public void salvarTabelaLexica() {
		
		if (!analiseLexicaComSucesso) {
			JOptionPane.showMessageDialog(null, "Ainda não há tabela léxica gerada");
			return;
		}
		
		FileChooser fc = new FileChooser();
		fc.setTitle("Escolha o caminho e nome do arquivo a ser salvo:");
		fc.setInitialFileName("Tabela_Lexica.csv");

		File file = fc.showSaveDialog(new Stage());
		
//		Construção da string e adição dos títulos das colunas:
		StringBuilder sb = new StringBuilder();
		sb.append(ordemLexica.getText() + ";");
		sb.append(codigo.getText() + ";");
		sb.append(nome.getText() + ";");
		sb.append(desc.getText() + ";");
		sb.append(System.lineSeparator());
		
//		Preenchimento das linhas restante com o conteúdo da tabela:
		for (LexicoToken lt : tabelaLexica.getItems()) {
			sb.append("\"" + lt.getOrdem() + "\";");
			sb.append("\"" + lt.getCodigo() + "\";");
			sb.append("\"" + lt.getNome() + "\";");
			sb.append("\"" + lt.getDesc() + "\";");
			sb.append(System.lineSeparator());
		}
		
		salvarArquivo(file, sb.toString());
	}
	
	@FXML
	public void salvarCodigoIntermediario() {
		
		if (!codigoGeradoComSucesso) {
			JOptionPane.showMessageDialog(null, "Ainda não há código intermediário gerado");
			return;
		}
		
		FileChooser fc = new FileChooser();
		fc.setTitle("Escolha o caminho e nome do arquivo a ser salvo:");
		fc.setInitialFileName("Codigo_Intermediário.csv");

		File file = fc.showSaveDialog(new Stage());
		
//		Construção da string e adição dos títulos das colunas:
		StringBuilder sb = new StringBuilder();
		sb.append(ordemCodInter.getText() + ";");
		sb.append(codigoTabelaCodInter.getText() + ";");
		sb.append(instrucao.getText() + ";");
		sb.append(op1.getText() + ";");
		sb.append(op2.getText() + ";");
		sb.append(System.lineSeparator());
		
//		Preenchimento das linhas restante com o conteúdo da tabela:
		for (SemanticoInstrucao se : tabelaCodigoIntermediario.getItems()) {
			sb.append("\"" + se.getOrdem() + "\";");
			sb.append("\"" + se.getSeq() + "\";");
			sb.append("\"" + se.getCod() + "\";");
			sb.append("\"" + se.getOp1() + "\";");
			sb.append("\"" + se.getOp2() + "\";");
			sb.append(System.lineSeparator());
		}
		
		salvarArquivo(file, sb.toString());
	}

	private void salvarArquivo(File file, String texto) {
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(texto);
			bw.close();
			fw.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	@FXML
	public void sair() {
		Runtime.getRuntime().exit(0);
	}

	@FXML
	public void executarAnalise() {
		inicalizarTabelas();
		
		executarAnaliseSemantica();
	}
	
//	Métodos gerais adiante:
	
	public void executarAnaliseLexica() {
		tabelaLexica.getItems().setAll(new ArrayList<>());
		
		AL = new ArrayList<String>();
		ALfinal = new ArrayList<LexicoToken>();
		
//		Instancia tipoToken para carregar as listas estáticas dentro da classe:
		new LexicoTipoToken();
		
//		Instancia o Lexico para realizar a análise léxica e carregar as váriaveis estáticas para usar no for abaixo:
		new Lexico(textAreaEntrada.getText());
		
//		Instancia o objeto GTabela para carregar a variável ALFinal nesta classe:
		new LexicoGTabela();
		
		for (int i = 0; i < ALfinal.size(); i++) {
			LexicoToken lt = new LexicoToken();
			lt.setOrdem(i + 1);
			lt.setCodigo(ALfinal.get(i).getCodigo());
			lt.setNome(ALfinal.get(i).getNome());
			lt.setDesc(ALfinal.get(i).getDesc());
			
			tabelaLexica.getItems().add(lt);
		}
		
		analiseLexicaComSucesso = true;
	}

	public void executarAnaliseSintatica() {
		tabelaSintatica.getItems().setAll(new ArrayList<>());
		
//		Esvazia os erros sintáticos antes de instânciar a classe novamente, que fará um novo preenchimendo destes:
		Sintatico.Erro_Sin = new ArrayList<String>();
		
		
//		Realiza a análise léxica que preenchera as variáveis necessárias para a análise sintática:
		executarAnaliseLexica();
		
//		Instância um novo Sintatico para executar o construtor e realizar a análise sintática:
		new Sintatico();
		
//		TODO Tentar por um if para retornar true sem a necessidade de alterar o valor toda vez no for abaixo:
		
		for (int i = 0; i < Sintatico.Erro_Sin.size(); i++) {
			
			tabelaSintatica.getItems().add(Sintatico.Erro_Sin.get(i));
			
			analiseSintaticaComSucesso = true;
		}
	}
	
	public void executarAnaliseSemantica() {
//		Remove o que já estava na tabela para preencher novamente com dados novos:
		tabelaSemantica.getItems().setAll(new ArrayList<>());

//		Remove o que já estava na lista para preencher novamente com dados novos:
		SemanticoAcao.Erro_Sem = new ArrayList<String>();
		
		AL_Instr.removeAll(AL_Instr);
		AreaLiterais = "";
		
		executarAnaliseSintatica();
		
		if ("Codigo analizado com sucesso !".equals(Sintatico.Erro_Sin.get(0))) {
			
			new Semantico();
			
			for (int i = 0; i < SemanticoAcao.Erro_Sem.size(); i++) {
				
				tabelaSemantica.getItems().add(SemanticoAcao.Erro_Sem.get(i));
								
				analiseSemanticaComSucesso = true;
			}
		} else {
			SemanticoAcao.Erro_Sem.add("Erro sintático !");
			tabPane.getSelectionModel().select(1);
		}
	}

	public boolean gerarCodigoIntermediario() {
		boolean b = false;
		
//		Primeiro executa a análise:
		executarAnalise();
		
//		Depois gera o código intermediário:
		tabelaCodigoIntermediario.getItems().setAll(new ArrayList<>());
		
		int seq = -1;
		if ("Codigo analizado com sucesso !".equals(tabelaSemantica.getItems().get(0))) {
			for (int i = 0; i < AL_Instr.size(); i++) {
				
				SemanticoInstrucao si = new SemanticoInstrucao();
				si.setOrdem(seq += 1);
				si.setSeq(AL_Instr.get(i).getSeq());
				si.setCod(AL_Instr.get(i).getCod());
				si.setOp1(AL_Instr.get(i).getOp1());
				si.setOp2(AL_Instr.get(i).getOp2());
				
				
				tabelaCodigoIntermediario.getItems().add(si);
				b = true;
			}
		} else {
			if ("Erro sintático !".equals(tabelaSemantica.getItems().get(0))) {
				tabPane.getSelectionModel().select(1);
			} else {
				tabPane.getSelectionModel().select(2);
			}
		}
		codigoGeradoComSucesso = true;
		
		return b;
	}
	
	public void executarNaMaquinaHipotetica() {
		gerarCodigoIntermediario();
		MaquinaHipotetica.Interpreta();
	}
	
	public void definirEdicaoNaoSalva() {
		salvo = false;
		mntmSalvar.setDisable(false);
	}
	
	@FXML
	public void definirEdicaoSalva() {
	    mntmSalvar.setDisable(true);
	    salvo=true;
	}
}
