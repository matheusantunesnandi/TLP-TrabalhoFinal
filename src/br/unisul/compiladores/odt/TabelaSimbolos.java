package br.unisul.compiladores.odt;

import java.util.ArrayList;
import java.util.List;

public class TabelaSimbolos {
	
	private int codigo;
	private static List<Simbolo> listaSimbolos = new ArrayList<Simbolo>();
	
	public TabelaSimbolos() {
	}
	
	public TabelaSimbolos(ArrayList<Simbolo> listaSimbolos) {
		setCodigo(1);
		setListaSimbolos(listaSimbolos); 
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public static List<Simbolo> getListaSimbolos() {
		return listaSimbolos;
	}

	public static void setListaSimbolos(List<Simbolo> listaSimbolos) {
		TabelaSimbolos.listaSimbolos = listaSimbolos;
	}
	
	public static void iniciaListaSimbolo () {
		TabelaSimbolos.listaSimbolos = new ArrayList<Simbolo>();
	}
	
	

}
