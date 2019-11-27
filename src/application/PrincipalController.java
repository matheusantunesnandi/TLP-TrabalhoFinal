package application;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public TextArea textaAreaEntrada = new TextArea();
	
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
	public TableColumn<SemanticoInstrucao, Integer> op1 = new TableColumn<>("OP1");
	public TableColumn<SemanticoInstrucao, Integer> op2 = new TableColumn<>("OP2");
	public TableColumn<SemanticoInstrucao, String> instrucao = new TableColumn<>("Instrução");
	
//	Variáveis de uso dos métodos gerais:
	public static ArrayList<model.SemanticoInstrucao> AL_Instr = new ArrayList<model.SemanticoInstrucao>();
	public static ArrayList<LexicoToken> ALfinal = new ArrayList<LexicoToken>();
	public static ArrayList<String> AL = new ArrayList<String>();
	public static String AreaLiterais = "";
	
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
		tabelaLexica.getItems().removeAll();
		tabelaSintatica.getItems().removeAll();
		tabelaSemantica.getItems().removeAll();
		tabelaCodigoIntermediario.getItems().removeAll();
		
		// TODO adicionar restos dos campos, migrar do anterior
	}

	@FXML
	public void novo() {
		System.out.println("novo");
//		TODO novo
	}

	@FXML
	public void abrir() {
		System.out.println("abrir");
//		TODO abrir
	}

	@FXML
	public void salvar() {
		System.out.println("salvar");
//		TODO salvar

	}

	@FXML
	public void salvarComo() {
		System.out.println("salvarComo");
//		TODO salvarComo

	}

	@FXML
	public void sair() {
		System.out.println("sair");
//		TODO sair

	}

	@FXML
	public void executarAnalise() {
		System.out.println("executarAnalise");

		inicalizarTabelas();
		
		executarAnaliseSemantica();
	}
	
//	Métodos gerais:

	public void executarAnaliseLexica() {
		tabelaLexica.getItems().removeAll();
		
		AL = new ArrayList<String>();
		ALfinal = new ArrayList<LexicoToken>();
		
//		Instancia tipoToken para carregar as listas estáticas dentro da classe:
		new LexicoTipoToken();
		
//		Instancia o Lexico para realizar a análise léxica e carregar as váriaveis estáticas para usar no for abaixo:
		new Lexico(textaAreaEntrada.getText());
		
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

	}

	public boolean executarAnaliseSintatica() {
		boolean b = false;
		
		tabelaSintatica.getItems().removeAll();
		
//		Esvazia os erros sintáticos antes de instânciar a classe novamente, que fará um novo preenchimendo destes:
		Sintatico.Erro_Sin = new ArrayList<String>();
		
		
//		Realiza a análise léxica que preenchera as variáveis necessárias para a análise sintática:
		executarAnaliseLexica();
		
//		Instância um novo Sintatico para executar o construtor e realizar a análise sintática:
		new Sintatico();
		
//		TODO Tentar por um if para retornar true sem a necessidade de alterar o valor toda vez no for abaixo:
		
		for (int i = 0; i < Sintatico.Erro_Sin.size(); i++) {
			
			tabelaSintatica.getItems().add(Sintatico.Erro_Sin.get(i));
			
			b = true;
		}
		
		return b;
	}
	
	public boolean executarAnaliseSemantica() {
		boolean b = false;
		
//		Remove o que já estava na tabela para preencher novamente com dados novos:
		tabelaSemantica.getItems().removeAll();
		

//		Remove o que já estava na lista para preencher novamente com dados novos:
		SemanticoAcao.Erro_Sem = new ArrayList<String>();
		
		AL_Instr.removeAll(AL_Instr);
		AreaLiterais = "";
		
		executarAnaliseSintatica();
		
		if ("Codigo analizado com sucesso !".equals(Sintatico.Erro_Sin.get(0))) {
			
			new Semantico();
			
			for (int i = 0; i < SemanticoAcao.Erro_Sem.size(); i++) {
				
				tabelaSemantica.getItems().add(SemanticoAcao.Erro_Sem.get(i));
				
				b = true;
			}
		} else {
			SemanticoAcao.Erro_Sem.add("Erro sintático !");
			tabPane.getSelectionModel().select(1);
		}
		return b;
	}

	public boolean gerarCodigoIntermediario() {
		System.out.println("gerarCodigoIntermediario");
		

		boolean b = false;
		
//		Primeiro executa a análise:
		executarAnalise();
		
//		Depois gera o código intermediário:
//		TODO gerarCodigoIntermediario (Validar)

		tabelaCodigoIntermediario.getItems().removeAll();
		int seq = -1;
		if ("Codigo analizado com sucesso !".equals(tabelaSemantica.getItems().get(0))) {
			MaquinaHipotetica.Interpreta();
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
		return b;
	}
}
