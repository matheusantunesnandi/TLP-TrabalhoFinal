package br.unisul.compiladores.odt;

public class Token { 

	private Integer linha;
	private Integer codigo;
	private String token;
	private String descricao;
	
	public Token() {
	}
			
	public Token(Integer linha, Integer codigo, String token, String descricao) {
		this.linha = linha;
		this.codigo = codigo;
		this.token = token;
		this.descricao = descricao;
	}
	
	public Integer getLinha() {
		return linha;
	}
	public void setLinha(Integer linha) {
		this.linha = linha;
	}
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
