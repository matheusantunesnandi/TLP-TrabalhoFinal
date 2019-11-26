package br.unisul.compiladores.odt;

public class ErroSintaticoSemantico {

	private Integer linha;
	private String esperava;
	private String apareceu;
	private String mensagem;
	
	public ErroSintaticoSemantico() {
	}
	
	public ErroSintaticoSemantico(Integer linha, String esperava, String apareceu) {
		this.linha = linha;
		this.esperava = esperava;
		this.apareceu = apareceu;
	}
	
	public ErroSintaticoSemantico(Integer linha, String mensagem) {
		this.linha = linha;
		this.mensagem = mensagem;
		this.esperava = "";
		this.apareceu = "";		
	}
	
	public Integer getLinha() {
		return linha;
	}
	public void setLinha(Integer linha) {
		this.linha = linha;
	}
	public String getEsperava() {
		return esperava;
	}
	public void setEsperava(String esperava) {
		this.esperava = esperava;
	}
	public String getApareceu() {
		return apareceu;
	}
	public void setApareceu(String apareceu) {
		this.apareceu = apareceu;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
}