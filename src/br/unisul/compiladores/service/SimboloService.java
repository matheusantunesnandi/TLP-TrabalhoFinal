package br.unisul.compiladores.service;

import java.util.List;
import java.util.stream.Collectors;

import br.unisul.compiladores.odt.Simbolo;
import br.unisul.compiladores.odt.TabelaSimbolos;

public class SimboloService {

	private static List<Simbolo> simbolos;

	public static Simbolo criarSimbolo() {
		return new Simbolo();
	}

	public static Simbolo criarSimbolo(int seq, String nome, int nivel, String categoria, String gA, String gB) {
		return new Simbolo(seq, nome, nivel, categoria, gA, gB);
	}

	// adicionar simbolo
	public static void adicionarSimboloTabela(Simbolo simbolo, TabelaSimbolos tabelaSimbolo) {
		tabelaSimbolo.getListaSimbolos().add(simbolo);
	}

	// busca na lista de simbolos
	public static Simbolo buscarSimboloTabelaPorNome(String nome, TabelaSimbolos tabelaSimbolo) {
		simbolos = tabelaSimbolo.getListaSimbolos();
		Simbolo simboloRetornado = simbolos.stream().filter(simbolo -> simbolo.getNome().equalsIgnoreCase(nome))
				.findFirst().orElse(null);

		return simboloRetornado;
	}
	
	public static Simbolo buscarSimboloTabelaPorNome(String nome, List<Simbolo> simbolosParametro) {
		simbolos = simbolosParametro;
		Simbolo simboloRetornado = simbolos.stream().filter(simbolo -> simbolo.getNome().equalsIgnoreCase(nome))
				.findFirst().orElse(null);

		return simboloRetornado;
	}
	
	public static Simbolo buscarSimboloTabelaPorNomeNivelCategoria(String nome, int nivel, String categoria, TabelaSimbolos tabelaSimbolo) {
		Simbolo simboloRetornado = null;
		simbolos = tabelaSimbolo.getListaSimbolos();
		
		if (simbolos.size() > 0) {
			
			simbolos = buscarSimboloTabelaPorNivel(nivel, simbolos);
			simbolos = buscarSimboloTabelaPorCategoria(categoria, simbolos);
			
			simboloRetornado = buscarSimboloTabelaPorNome(nome, simbolos);
		
		}
		
		return simboloRetornado;

	}

	public static List<Simbolo> buscarSimboloTabelaPorNivel(int nivel, TabelaSimbolos tabelaSimbolo) {
		simbolos = tabelaSimbolo.getListaSimbolos();
		return simbolos.stream().filter(simbolo -> simbolo.getNivel() == nivel).collect(Collectors.toList());
	}

	public static List<Simbolo> buscarSimboloTabelaPorCategoria(String categoria, TabelaSimbolos tabelaSimbolo) {
		simbolos = tabelaSimbolo.getListaSimbolos();
		return simbolos.stream().filter(simbolo -> simbolo.getCategoria().equalsIgnoreCase(categoria))
				.collect(Collectors.toList());
	}
	
	public static List<Simbolo> buscarSimboloTabelaPorNivel(int nivel, List<Simbolo> simbolosParametro) {
		simbolos = simbolosParametro;
		return simbolos.stream().filter(simbolo -> simbolo.getNivel() == nivel).collect(Collectors.toList());
	}

	public static List<Simbolo> buscarSimboloTabelaPorCategoria(String categoria, List<Simbolo> simbolosParametro) {
		simbolos = simbolosParametro;
		return simbolos.stream().filter(simbolo -> simbolo.getCategoria().equalsIgnoreCase(categoria))
				.collect(Collectors.toList());
	}

	public static List<Simbolo> buscarSimboloTabelaPorPosicao(int posicao, TabelaSimbolos tabelaSimbolo) {
		simbolos = tabelaSimbolo.getListaSimbolos();
		return (List<Simbolo>) simbolos.get(posicao);
	}

	// alterar simbolo

	// excluir simbolo

	public static void removerSimboloTabela(Simbolo simbolo, TabelaSimbolos tabelaSimbolos) {
		tabelaSimbolos.getListaSimbolos().remove(simbolo);
	}

	public static void removerSimboloTabela(int posicao, TabelaSimbolos tabelaSimbolos) {
		tabelaSimbolos.getListaSimbolos().remove(posicao);
	}

	

}
