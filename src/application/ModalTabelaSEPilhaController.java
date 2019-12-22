package application;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.MaquinaHipotetica;
import model.SemanticoAcao;
import model.SemanticoVar;

public class ModalTabelaSEPilhaController {
	
//	Elementos gerais GUI
	public Button buttonAtualizar = new Button();
	public Button buttonExecutarPP = new Button();

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
		Task<Void> task = new Task<Void>() {
		    @Override
		    protected Void call() throws Exception {
				while (true) {
					inicalizarTabelas();
					resetarCampos();
					carregarDadosTabelaSimbolos();
					carregarDadosPilha();
					
					Thread.sleep(100);
				}
		    }
		};

		Executor exec = Executors.newCachedThreadPool(runnable -> {
		    Thread t = new Thread(runnable);
		    t.setDaemon(true);
		    return t;
		});

//		TODO ALTERAR O correto é que este método seja disparado por EVENTO ao realizar qualquer análise ou execução a partir da outra janela.
		exec.execute(task);
		
		buttonAtualizar.setDisable(true);
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
	

	@FXML
	public void executarPassoAPasso() {
		buttonExecutarPP.setDisable(true);
		resetarCampos();
		
//		TODO executarPassoAPasso EM DESENVOLVIMENTO: Remover?
		MaquinaHipotetica.executarPassoAPasso = true;
		JOptionPane.showMessageDialog(null, "EM DESENVOLVIMENTO (BUGADO) Clique em \"Executar Análise\" novamente na janela principal.");
	}
	
	@FXML
	public void proximoPasso() {
//		TODO proximoPasso EM DESENVOLVIMENTO: Remover?
		MaquinaHipotetica.pause = false;
		
//		TODO Remover bug ao usar o método abaixo quando ele estiver usando Thread. Super lota memória e processador.
//		atualizar();
	}

}
