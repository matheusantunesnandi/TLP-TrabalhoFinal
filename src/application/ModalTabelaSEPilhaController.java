package application;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.MaquinaHipotetica;
import model.SemanticoAcao;
import model.SemanticoVar;

public class ModalTabelaSEPilhaController {
	

	// Tabela de Símbolos GUI
	public TableView<SemanticoVar> tabelaSimbolos = new TableView<>();
	public TableColumn<SemanticoVar, String> tsCategoria = new TableColumn<>("Categoria");
	public TableColumn<SemanticoVar, Integer> tsGA = new TableColumn<>("GA");
	public TableColumn<SemanticoVar, Integer> tsGB = new TableColumn<>("GB");
	public TableColumn<SemanticoVar, Integer> tsNivel = new TableColumn<>("Nível");
	public TableColumn<SemanticoVar, String> tsNome = new TableColumn<>("Nome");
	public TableColumn<SemanticoVar, Integer> tsProximo = new TableColumn<>("Próximo");
	

	// Pilha
	public TableView<IntegerTemp> tabelaPilha = new TableView<>();
	public TableColumn<IntegerTemp, Integer> tpPilha = new TableColumn<>("Pilha");
	
	/**
	 * Método para inicializar as configurações das tabelas.
	 */
	private void inicalizarTabelas() {

		// Tabela de Símbolos GUI
		tsCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
		tsGA.setCellValueFactory(new PropertyValueFactory<>("GA"));
		tsGB.setCellValueFactory(new PropertyValueFactory<>("GB"));
		tsNivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));
		tsNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tsProximo.setCellValueFactory(new PropertyValueFactory<>("PROX"));
		
		// Tabela Pilha
		tpPilha.setCellValueFactory(new PropertyValueFactory<>("value"));
	}

	private void resetarCampos() {
		tabelaSimbolos.getItems().setAll(new ArrayList<>());
		tabelaPilha.getItems().setAll(new ArrayList<>());
	}
	
//	TOOD Este método era para ser automático, sem precisar de botão.
	@FXML
	public void atualizar() {
		inicalizarTabelas();
		resetarCampos();
		carregarDadosTabelaSimbolos();
		carregarDadosPilha();
		
	}
	
	private void carregarDadosPilha() {
		// TODO Auto-generated method stub

		for (int i : MaquinaHipotetica.S) {
			
			IntegerTemp it = new IntegerTemp(i);
			tabelaPilha.getItems().add(it);

		}
	}

	public void carregarDadosTabelaSimbolos() {
		tabelaSimbolos.getItems().addAll(SemanticoAcao.TS);
	}

}
