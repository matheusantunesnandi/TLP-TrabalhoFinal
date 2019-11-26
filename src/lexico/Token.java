package lexico;

public class Token {
	private int codigo;
	private String nome;
	private String desc;
	
	public Token(){
		codigo=0;
		nome=null;
		desc=null;
	}
	
	public Token(int codigo,String nome,String desc){
		this.codigo=codigo;
		this.nome=nome;
		this.desc=desc;
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
	public String toString(){
		return this.codigo+" - "+this.nome+" - "+this.desc;
	}
}
