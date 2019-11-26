package br.unisul.compiladores.odt;

public class Literal { 

	private Integer seq;
	private String literal;
	
	public Literal() {
	}
	
	public Literal(Integer seq, String literal) {
		this.seq = seq;
		this.literal = literal;
	}
	
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getLiteral() {
		return literal;
	}
	public void setLiteral(String literal) {
		this.literal = literal;
	}
	
}
