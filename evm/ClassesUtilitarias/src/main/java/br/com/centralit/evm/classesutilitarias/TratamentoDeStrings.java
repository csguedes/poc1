package br.com.centralit.evm.classesutilitarias;

public class TratamentoDeStrings {

	/**
	 * Utiliza regex para retirar espa�os no in�cio e no final da String
	 * @param input
	 * @return
	 */
	public static String retirarEspacosEmBranco(String input) {
		if (input==null) {return "";}
		return input.replaceAll("^\\s+", "").replaceAll("\\s+$", "");		
		
	}
	
}
