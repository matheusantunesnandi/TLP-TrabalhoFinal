package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
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
	public CheckMenuItem cmiMostrarTabelaSEPilha = new CheckMenuItem();
	
//	Modal
	Stage modalTabelaSEPilha = new Stage();
	
//	Tabela léxica e suas colunas :
	public TableView<LexicoToken> tabelaLexica = new TableView<>();
	public TableColumn<LexicoToken, Integer> ordemLexica = new TableColumn<>("N°");
	public TableColumn<LexicoToken, Integer> codigo = new TableColumn<>("Código");
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
	
//	Variáveis usadas para o menu "Opções"
	public static boolean salvo = true;
	public static File arquivoAberto;
	public static boolean analiseLexicaComSucesso = false;
	public static boolean codigoGeradoComSucesso = false;
	public static boolean analiseSintaticaComSucesso = false;
	public static boolean analiseSemanticaComSucesso = false;
	public static boolean passoAnteriorComErro = false;
	public static int ETAPA_SINTATICA = 1;
	public static int ETAPA_SEMANTICA = 2;
	public static int ETAPA_GER_CODIGO = 3;
	public static int ETAPA_EXECUCAO_M_H = 4;
	public static File PASTA_ENTRADA = new File("./Entradas");
	public static File PASTA_EXPORT = new File("./Export");
	
	public PrincipalController() {
		if (!PASTA_ENTRADA.exists())
			PASTA_ENTRADA.mkdir();

		if (!PASTA_EXPORT.exists())
			PASTA_EXPORT.mkdir();
		
		inicializarModalTabelaSimbolosEPilha();
	}
	
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
		mntmSalvar.setDisable(true);
		salvo=true;
		resetarCamposPreAnalise();
		
//		TODO Remover
//		cmiMostrarTabelaSEPilha.setDisable(true);
	}

	private void resetarCamposPreAnalise() {
		tabelaLexica.getItems().setAll(new ArrayList<>());
		tabelaSintatica.getItems().setAll(new ArrayList<>());
		tabelaSemantica.getItems().setAll(new ArrayList<>());
		tabelaCodigoIntermediario.getItems().setAll(new ArrayList<>());
		analiseLexicaComSucesso = false;
		codigoGeradoComSucesso = false;
		analiseSintaticaComSucesso = false;
		analiseSemanticaComSucesso = false;
		passoAnteriorComErro = false;
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
			fc.setInitialDirectory(PASTA_ENTRADA);

			File file = fc.showOpenDialog(new Stage());

			if (file.exists() && file.isFile()) {

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
			JOptionPane.showMessageDialog(null, "Não foi possível abrir o arquivo.");
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
			fc.setInitialDirectory(PASTA_ENTRADA);

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
		fc.setInitialDirectory(PASTA_EXPORT);

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
		fc.setInitialDirectory(PASTA_EXPORT);

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
		
		resetarCamposPreAnalise();
		
		inicalizarTabelas();
		
		executarAnaliseSemantica();
		
		// Simula o próximo passo para validar a ação anterior (Semântica):
		passoAnteriorComSucesso(ETAPA_GER_CODIGO);
		
//		Reinicializa a modal para carregar os dados:
		inicializarModalTabelaSimbolosEPilha();
	}
	
//	Métodos gerais adiante:
	
	public void executarAnaliseLexica() {
		LexicoGTabela.AL = new ArrayList<String>();
		Lexico.ALfinal = new ArrayList<LexicoToken>();
		
//		Instancia tipoToken para carregar as listas estáticas dentro da classe:
		new LexicoTipoToken();
		
//		Instancia o Lexico para realizar a análise léxica e carregar as váriaveis estáticas para usar no for abaixo:
		new Lexico(textAreaEntrada.getText());
		
//		Instancia o objeto GTabela para carregar a variável Lexico.ALfinal nesta classe:
		new LexicoGTabela();
		
		for (int i = 0; i < Lexico.ALfinal.size(); i++) {
			LexicoToken lt = new LexicoToken();
			lt.setOrdem(i + 1);
			lt.setCodigo(Lexico.ALfinal.get(i).getCodigo());
			lt.setNome(Lexico.ALfinal.get(i).getNome());
			lt.setDesc(Lexico.ALfinal.get(i).getDesc());
			
			tabelaLexica.getItems().add(lt);
		}
		
		tabPane.getSelectionModel().select(0);
	}

	public void executarAnaliseSintatica() {
//		Realiza a análise léxica que preenchera as variáveis necessárias para a análise sintática:
		executarAnaliseLexica();

		if(!passoAnteriorComSucesso(ETAPA_SINTATICA))
			return;
		
//		Instância um novo Sintatico para executar o construtor e realizar a análise sintática:
		new Sintatico();
		
//		Adiciona os erros sintáticos na tabela da interface gráfica.
		for (int i = 0; i < Sintatico.Erro_Sin.size(); i++) {
			tabelaSintatica.getItems().add(Sintatico.Erro_Sin.get(i));
		}
	}
	
	public void executarAnaliseSemantica() {
		executarAnaliseSintatica();

		if (!passoAnteriorComSucesso(ETAPA_SEMANTICA))
			return;

//		Instância a classe Semantico para realizar a análise semântica do construtor:
		new Semantico();

//		Adiciona os erros semânticos na tabela da interface gráfica.
		for (int i = 0; i < SemanticoAcao.Erro_Sem.size(); i++) {
			tabelaSemantica.getItems().add(SemanticoAcao.Erro_Sem.get(i));
		}
	}

	public void gerarCodigoIntermediario() {
		
//		TODO Tratar para não executar caso já tenha sido realizar a análise antes:
//		Primeiro executa a análise:
		executarAnalise();
		
		if (!passoAnteriorComSucesso(ETAPA_GER_CODIGO))
			return;

		int seq = -1;
		for (int i = 0; i < Semantico.AL_Instr.size(); i++) {
			
			SemanticoInstrucao si = new SemanticoInstrucao();
			si.setOrdem(seq += 1);
			si.setSeq(Semantico.AL_Instr.get(i).getSeq());
			si.setCod(Semantico.AL_Instr.get(i).getCod());
			si.setOp1(Semantico.AL_Instr.get(i).getOp1());
			si.setOp2(Semantico.AL_Instr.get(i).getOp2());
			
			
			tabelaCodigoIntermediario.getItems().add(si);
			codigoGeradoComSucesso = true;
		}
		
		tabPane.getSelectionModel().select(3);
	}
	
	public void executarNaMaquinaHipotetica() {
		gerarCodigoIntermediario();
		
		if (passoAnteriorComSucesso(ETAPA_GER_CODIGO))
			MaquinaHipotetica.Interpreta();
	}

	private boolean passoAnteriorComSucesso(int etapaAtual) {
		if (passoAnteriorComErro)
			return false;
		
		if (!analiseLexicaComSucesso && etapaAtual == ETAPA_SINTATICA) {
			JOptionPane.showMessageDialog(null, "A análise léxica não foi realizada com sucesso.");
			tabPane.getSelectionModel().select(0);
			passoAnteriorComErro = true;
			return false;
			
		} else if (!analiseSintaticaComSucesso && etapaAtual == ETAPA_SEMANTICA) {
			JOptionPane.showMessageDialog(null, "A análise sintática não foi realizada com sucesso");
			tabPane.getSelectionModel().select(1);
			passoAnteriorComErro = true;
			return false;
			
		} else if (!analiseSemanticaComSucesso && etapaAtual == ETAPA_GER_CODIGO) {
			JOptionPane.showMessageDialog(null, "A análise semântica não foi realizada com sucesso");
			tabPane.getSelectionModel().select(2);
			passoAnteriorComErro = true;
			return false;
			
		} else if (!codigoGeradoComSucesso && etapaAtual == ETAPA_EXECUCAO_M_H) {
			JOptionPane.showMessageDialog(null, "O código intermediário não foi gerado com sucesso");
			tabPane.getSelectionModel().select(3);
			passoAnteriorComErro = true;
			return false;
		}
		
		return true;
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
	
	@FXML
	public void mostrarTabelaDeSimbolosEPilha() {
		if (modalTabelaSEPilha.isShowing()) {
			modalTabelaSEPilha.close();
			
		} else {
		    modalTabelaSEPilha.show();
		}
	}

	private void inicializarModalTabelaSimbolosEPilha() {
		try {
//			TODO Validar forma correta de fazer isto:
			modalTabelaSEPilha = new Stage();
			
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("ModalTabelaSEPilha.fxml"));
		    modalTabelaSEPilha.setScene(new Scene(root));
		    modalTabelaSEPilha.setTitle("Tabela de Símbolos e Pilha");
		    modalTabelaSEPilha.initModality(Modality.WINDOW_MODAL);
		    modalTabelaSEPilha.setOnCloseRequest(event -> {
		        cmiMostrarTabelaSEPilha.setSelected(false);
		    });
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Erro ao abrir modal da Tabela de Símbolos e Pilha.");
			e.printStackTrace();
		}
	}
}
