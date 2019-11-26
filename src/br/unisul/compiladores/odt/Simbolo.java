package br.unisul.compiladores.odt;

public class Simbolo {
	
	private int seq;
	private String nome;
	private String categoria;
	private String geralA;
	private String geralB;
	private int nivel;
	private int hash;
	
	
	public Simbolo() {
		
	}
	
	public Simbolo(int seq, String nome, int nivel, String categ, String gA, String gB) {
		setSeq(seq);
		setNome(nome);
		setNivel(nivel);
		setCategoria(categ);
		setGeralA(gA);
		setGeralB(gB);
		setHash(hash(nome, 10));
	}
	
	public Simbolo(Integer seq, String nome, String categoria, Integer nivel,
			String ga, String gb) {
		this.seq = seq;
		this.nome = nome;
		this.categoria = categoria;
		this.nivel = nivel;
		this.geralA = ga;
		this.geralB = gb;
	}
	
	public String toString(){
		return "Nome: "+getNome()+" - "+"Nível: "+getNivel()+" - "+"Categoria: "+getCategoria()
		+" - "+"Geral A: "+getGeralA()+" - "+"Geral B: "+getGeralB();
	}
	
	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getGeralA() {
		return geralA;
	}
	public void setGeralA(String geralA) {
		this.geralA = geralA;
	}
	public String getGeralB() {
		return geralB;
	}
	public void setGeralB(String geralB) {
		this.geralB = geralB;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	
	public int getHash() {
		return hash;
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	public int hash(String key, int tableSize) {

		int hashVal = 0; // uses Horner’s method to evaluate a polynomial
		for (int i = 0; i < key.length(); i++) {
			hashVal = 37 * hashVal + key.charAt(i);
		}
		
		hashVal %= tableSize;
		
		if (hashVal < 0) {
			hashVal += tableSize; // needed if hashVal is negative
			
				
		}
		return hashVal;
	}

}
