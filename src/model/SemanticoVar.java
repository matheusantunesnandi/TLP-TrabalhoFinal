package model;
/**
 * Esta classe se trata de  um objeto de registro no formato da Tabela de Símbolos.
 *
 */
public class SemanticoVar {

	String nome, categoria;
	int nivel, GA, GB, PROX;

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

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getGA() {
		return GA;
	}

	public void setGA(int gA) {
		GA = gA;
	}

	public int getGB() {
		return GB;
	}

	public void setGB(int gB) {
		GB = gB;
	}

	public int getPROX() {
		return PROX;
	}

	public void setPROX(int pROX) {
		PROX = pROX;
	}

	@Override
	public String toString() {
		return "SemanticoVar [nome=" + nome + ", categoria=" + categoria + ", nivel=" + nivel + ", GA=" + GA + ", GB="
				+ GB + "]";
	}

}
