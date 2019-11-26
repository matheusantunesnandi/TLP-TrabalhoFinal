package br.unisul.compiladores.odt;

public class Operacoes {

	private Integer seq;
	private String codigo;
	private String op1;
	private String op2;
	private String observacao;
	
	public Operacoes() {
	}
	 
	public Operacoes(Integer seq, String codigo, String op1, String op2, String observacao) {
		this.seq = seq;
		this.codigo = codigo;
		this.op1 = op1;
		this.op2 = op2;
		this.observacao = observacao;
	}
	
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getOp1() {
		return op1;
	}
	public void setOp1(String op1) {
		this.op1 = op1;
	}
	public String getOp2() {
		return op2;
	}
	public void setOp2(String op2) {
		this.op2 = op2;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
}
