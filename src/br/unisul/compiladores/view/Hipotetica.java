package br.unisul.compiladores.view;
	
//Maquina_virtual para a linguagem LMS
//Equipe:Maicon, reinaldo e Fabio - 2003A
//Adaptado e corrigido por Rog�rio Cortina e Charbel Szymanski - 2003B
//Atualizada por Charbel Szymanski em 2016A
//Atualizada por Charbel Szymanski em 2017A
//Atualizada por Charbel Szymanski em 2018B

import javax.swing.JOptionPane;

/**
 * Classe utilizada pela classe "Hipotetica" para armazenar as informa��es 
 * de uma instru��o.
 * Esta classe, bem como as classes "AreaInstrucoes", "AreaLiterais"
 * e "Hipotetica" foi criada por Maicon, Reinaldo e Fabio e adaptada
 * para este aplicativo.
 */
class Tipos{
	public int codigo; 
	public int op1;
	public int op2;
	
  /**
   * Construtor sem par�metros.
   * Todos os atributos s�o inicializados com valores padr�es.
   */
	Tipos(){
	     codigo=0;
	   	 op1=0;
   	 	 op2=0;
   	 }
}

/**
 * Classe utilizada pela classe "Hipotetica" para armazenar a �rea de
 * instru��es.
  * Esta classe, bem como as classes "Tipos", "AreaLiterais"
 * e "Hipotetica" foi criada por Maicon, Reinaldo e Fabio e adaptada
 * para este aplicativo.
 */
class AreaInstrucoes{
	public Tipos AI[]= new Tipos[1000];
	public int LC;
	
    /**
   * Construtor sem par�metros.
   * Todos os atributos s�o inicializados com valores padr�es.
   */
	AreaInstrucoes(){
		for(int i=0; i<1000; i++){
			AI[i]=new Tipos();
		}
	}
}

/**
 * Classe utilizada pela classe "Hipotetica" para armazenar a �rea de
 * literais.
  * Esta classe, bem como as classes "Tipos", "AreaInstrucoes"
 * e "Hipotetica" foi criada por Maicon, Reinaldo e Fabio e adaptada
 * para este aplicativo.
 */
class AreaLiterais{
	public String AL[]= new String[30];
	public int LIT;
}

/**
 * Classe que implementa a m�quina hipot�tica.
 * Esta classe, bem como as classes "Tipos", "AreaInstrucoes"
 * e "AreaLiterais" foi criada por Maicon, Reinaldo e Fabio e adaptada
 * para este aplicativo.
 */
class Hipotetica{
	  public static int MaxInst=1000;
	  public static int MaxList=30;
	  public static int b; //base do segmento
	  public static int topo; //topo da pilha da base de dados
	  public static int p; //apontador de instru��es
	  public static int l; //primeiro operando
	  public static int a; //segundo operando
	  public static int operador; //codigo da instru��o
	  public static int k; //segundo operando
	  public static int num_impr;
	  public static int[] S = new int[1000]; 
	  
    /**
   * Construtor sem par�metros.
   * Os atributos "nv", "np" e "num_impr" s�o inicializados com valores padr�es.
   */	  
	  Hipotetica(){
	  	num_impr=0;
	  }
	      		
	  /**
     * Inicializa a �rea de instru��es.
     */
	  public static void InicializaAI(AreaInstrucoes AI){
	  	for (int i=0;i<MaxInst;i++){ //come�ava de 1
	  		AI.AI[i].codigo=-1;
	  		AI.AI[i].op1=-1;
	  		AI.AI[i].op2=-1;
	  	}
	  	AI.LC=0;
	  }
	  
    /**
     * Inicializa a �rea de literais
     */
	  public static void InicializaAL(AreaLiterais AL){
	  	
	  	for (int i=0;i<MaxList;i++){
	  		AL.AL[i]="";
	  		AL.LIT=0;
	  	}
	  }
	  
    /**
     * Inclui uma instru��o na �rea de instru��es utilizada pela m�quina
     * hipot�tica.
     */
	  public boolean IncluirAI(AreaInstrucoes AI, int c, int o1, int o2) {
	  	boolean aux;
	  	if(AI.LC>=MaxInst)
	  	{
	  		aux=false;
	  	}
	  	else
	  	{
	  		aux=true;
	  		AI.AI[AI.LC].codigo=c;
	  		
	  		if(o1 != -1)
	  		{
	  			AI.AI[AI.LC].op1=o1;
	  		}
	  			
	  		if(c==24)
	  		{
	  			AI.AI[AI.LC].op2=o2;
	  		}
	  			
	  		if(o2!=-1)
	  		{
	  			AI.AI[AI.LC].op2=o2;
	  		}
	  
	  		AI.LC=AI.LC+1;
	  	}
	  	return aux;
	  }
	  
     /**
     * Altera uma instru��o da �rea de instru��es utilizada pela m�quina
     * hipot�tica.
     */
	  public static void AlterarAI(AreaInstrucoes AI, int s, int o1, int o2){
	  	
	  	if (o1!=-1){
	  		AI.AI[s].op1=o1;
	  	}
	  		
	  	if(o2!=-1){
	  		AI.AI[s].op2=o2;
	  	}
	  }

    /**
     * Inclui um literal na �rea de literais utilizada pela m�quina
     * hipot�tica.
     */	  
	  public static boolean IncluirAL(AreaLiterais AL, String literal){
	  	boolean aux;
	  	if (AL.LIT>=MaxList){
	  		aux=false;	  		
	  	}else{
	  		aux=true;
	  		AL.AL[AL.LIT]=literal;
	  		AL.LIT=AL.LIT+1;
	  	}
	  	return aux;	  	
	  }

    /**
     * Utilizada para determinar a base.
     */	  
	  public static int Base(){
	  		int b1;
	  		b1=b;
	  		while(l>0){
	  			b1=S[b1];
	  			l=l-1;
	  		}
	  		return b1;
	  	}
	  
     /**
     * Respons�vel por interpretar as instru��es.
     */	  
	  public static void Interpreta(AreaInstrucoes AI, AreaLiterais AL){
	  	
	  	topo=0;
	  	b=0; //registrador base
	  	p=0; //aponta pr�xima instru��o
	  	S[1]=0; //SL
	  	S[2]=0; //DL
	  	S[3]=0; //RA
	  	operador=0;
	  	
	  	String leitura;
	  	
	  	while (operador != 26) {//Enquanto instru��o diferente de PARE
	  		
	  		operador=AI.AI[p].codigo;
	  		
	  		
	  		l=AI.AI[p].op1;
	  		a=AI.AI[p].op2;
	  		p=p+1;
	  		
	  		switch (operador){
			       	case 1://RETU
			     		p=S[b+2];
			     		topo=b-a-1; //a = n� de par�metros
			     		b=S[b+1];
				   		break;
			       
			       	case 2://CRVL
			       		topo=topo+1;
			       		S[topo]=S[Base()+a];
			       		break;
			       		
			       	case 3: //CRCT
			       		topo=topo+1;
			       		S[topo]=a;
			       		break;
			       
			       	case 4://ARMZ
			       		S[Base()+a]=S[topo];
			       		topo=topo-1;
			       		break;
			       		
			       	case 5://SOMA
			       		S[topo-1]=S[topo-1]+S[topo];
			       		topo=topo-1;
			       		break;
			       		
			       	case 6://SUBT
			       		S[topo-1]=S[topo-1]-S[topo];
			       		topo=topo-1;
			       		break;
			       		
			       	case 7://MULT
			       		S[topo-1]=S[topo-1]*S[topo];
			       		topo=topo-1;
			       		break;
			       		
			       	case 8: //DIVI
			       		if (S[topo]==0){
			       			JOptionPane.showMessageDialog(null,"Divis�o por zero.","Erro durante a execu��o",JOptionPane.ERROR_MESSAGE);
			       			S[topo-1]=S[topo-1] / S[topo];
			       			topo=topo-1;
			       		}
			       		break;
			       		
			       	case 9://INVR
			       		S[topo]=-S[topo];
			       		break;
			       		
			       	case 10: //NEGA
			       		S[topo]=1-S[topo];
			       		break;
			       		
			       	case 11://CONJ
			       		if((S[topo-1]==1)&&(S[topo]==1)){
			       			S[topo-1]=1;
			       		}else{
			       			S[topo-1]=0;
			       		}
			       		topo=topo-1;
			       		break;
			       		
			       	case 12://DISJ
			       		if((S[topo-1]==1||S[topo]==1)){
			       			S[topo-1]=1;
			       		}else{
			       			S[topo-1]=0;
			       		}
			       		topo=topo-1;
			       		break;
			       	
			       	case 13://CMME
			       		if(S[topo-1]<S[topo]){
			       			S[topo-1]=1;
			       		}else{
			       			S[topo-1]=0;
			       		}
			       		topo=topo-1;
			       		break;
			       		
			       	case 14://CMMA
			       		if(S[topo-1]>S[topo]){
			       			S[topo-1]=1;
			       		}else{
			       			S[topo-1]=0;
			       		}
			       		topo=topo-1;
			       		break;
			       	
			       	case 15://CMIG
			       		if(S[topo-1]==S[topo]){
			       			S[topo-1]=1;
			       		}else{
			       			S[topo-1]=0;
			       		}
			       		topo=topo-1;
			       		break;
			       		
			       	case 16://CMDF
			       		if(S[topo-1]!=S[topo]){
			       			S[topo-1]=1;
			       		}else{
			       			S[topo-1]=0;
			       		}
			       		topo=topo-1;
			       		break;
			       	
			       	case 17://CMEI
			       		if(S[topo-1]<=S[topo]){
			       			S[topo-1]=1;
			       		}else{
			       			S[topo-1]=0;
			       		}
			       		topo=topo-1;
			       		break;
			       	
			       	case 18://CMAI
			       		if(S[topo-1]>=S[topo]){
			       			S[topo-1]=1;
			       		}else{
			       			S[topo-1]=0;
			       		}
			       		topo=topo-1;
			       		 break;
			       		
			       	case 19://DSVS
			       		p=a;
			       		break;
			       		
			       	case 20://DSVF
			       	    if (S[topo]==0){
			       			p=a;
			       		}
			       		topo=topo-1;
			       		
			       		break;
			       		
			       	case 21://LEIT
			       		topo=topo+1;
			       		leitura = JOptionPane.showInputDialog(null,"Informe o valor:","Leitura",JOptionPane.QUESTION_MESSAGE);
			       		//System.out.print("Leia: "); A
			       		(S[topo])=Integer.parseInt(leitura); //problema aqui A
			       		break;
			       		
			       	case 22://IMPR
			       		JOptionPane.showMessageDialog(null,"" + S[topo],"Informa��o",JOptionPane.INFORMATION_MESSAGE);
			       		//System.out.println(S[topo]); A
			       		topo=topo-1;
			       		break;
			       	
			       	case 23://IMPRLIT
			       		if (a>= AL.LIT)
			       		{
			       			JOptionPane.showMessageDialog(null,"Literal n�o encontrado na �rea dos literais.","Erro durante a execu��o",JOptionPane.ERROR_MESSAGE);
			       			//System.out.println("ERRO >> Literal nao encontrada na area"); A
			       		}else{
			       			JOptionPane.showMessageDialog(null,"" + AL.AL[a],"Informa��o",JOptionPane.INFORMATION_MESSAGE);
			       			//System.out.println(AL.AL[a]); A
			       			//AL.LIT++;
			       		}
			       		break;
			       		
			       	case 24://AMEM
			       		topo=topo+a;
			       		break;
			       		
			       	case 25://CALL
			       		int base =Base(); 
			       		//System.out.println("chamando CALL");
			       		//System.out.println("topo+1=" + (topo+1) + " -> " + base);
			       		S[topo+1]=base;
			       		S[topo+2]=b;
			       		S[topo+3]=p;
			       		b=topo+1;
			       		p=a;
			       		break;
			       		
			       	case 26:
			       		//System.exit(0);
			       		//PARA
			       		break;
			       	
			       	case 27:
			       		//NADA
			       		break;
			       	
			       	case 28://COPI
			       		topo=topo+1;
			       		S[topo]=S[topo-1];
			       		break;
			       		
			       	case 29://DSVT
			       		if(S[topo]==1){
			       			p=a;
			       		}
			       		topo=topo-1;
			    }//fim do case
			}//fim do while
		}//fim do procedimento interpreta

	private static void mostraAreaDados() {
		for (int i=topo; i>=0; i--) {
			System.out.println(i+"["+S[i]+"]");
		}
	}
}
