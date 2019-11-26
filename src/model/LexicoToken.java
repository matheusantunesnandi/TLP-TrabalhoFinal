package model;

public class LexicoToken {
	private int codigo;
	private String nome;
	private String desc;

	public LexicoToken() {
		codigo = 0;
		nome = null;
		desc = null;
	}

	public LexicoToken(int codigo, String nome, String desc) {
		this.codigo = codigo;
		this.nome = nome;
		this.desc = desc;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String toString() {
		return this.codigo + " - " + this.nome + " - " + this.desc;
	}
}
