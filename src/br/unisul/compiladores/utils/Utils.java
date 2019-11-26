package br.unisul.compiladores.utils;

public class Utils {
	
	public static int getTransformacao(String codigo) {
		if (codigo.equalsIgnoreCase("RETU"))
			return 1;
		if (codigo.equalsIgnoreCase("CRVL"))
			return 2;
		if (codigo.equalsIgnoreCase("CRCT"))
			return 3;
		if (codigo.equalsIgnoreCase("ARMZ"))
			return 4;
		if (codigo.equalsIgnoreCase("SOMA"))
			return 5;
		if (codigo.equalsIgnoreCase("SUBT"))
			return 6;
		if (codigo.equalsIgnoreCase("MULT"))
			return 7;
		if (codigo.equalsIgnoreCase("DIV"))
			return 8;
		if (codigo.equalsIgnoreCase("INVR"))
			return 9;
		if (codigo.equalsIgnoreCase("NEGA"))
			return 10;
		if (codigo.equalsIgnoreCase("CONJ"))
			return 11;
		if (codigo.equalsIgnoreCase("DISJ"))
			return 12;
		if (codigo.equalsIgnoreCase("CMME"))
			return 13;
		if (codigo.equalsIgnoreCase("CMMA"))
			return 14;
		if (codigo.equalsIgnoreCase("CMIG"))
			return 15;
		if (codigo.equalsIgnoreCase("CMDF"))
			return 16;
		if (codigo.equalsIgnoreCase("CMEI"))
			return 17;
		if (codigo.equalsIgnoreCase("CMAI"))
			return 18;
		if (codigo.equalsIgnoreCase("DSVS"))
			return 19;
		if (codigo.equalsIgnoreCase("DSVF"))
			return 20;
		if (codigo.equalsIgnoreCase("LEIT"))
			return 21;
		if (codigo.equalsIgnoreCase("IMPR"))
			return 22;
		if (codigo.equalsIgnoreCase("IMPRL"))
			return 23;
		if (codigo.equalsIgnoreCase("AMEM"))
			return 24;
		if (codigo.equalsIgnoreCase("CALL"))
			return 25;
		if (codigo.equalsIgnoreCase("PARA"))
			return 26;
		if (codigo.equalsIgnoreCase("NADA"))
			return 27;
		if (codigo.equalsIgnoreCase("COPI"))
			return 28;
		if (codigo.equalsIgnoreCase("DSVT"))
			return 29;
		
		return 0;
	}

}
