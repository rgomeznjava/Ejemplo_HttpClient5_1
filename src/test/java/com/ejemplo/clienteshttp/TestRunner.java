package com.ejemplo.clienteshttp;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * 	TEST RUNNER: Para lanzar conjunto de  clases Junit Test o Test Suite
 * 
 * @author GRN
 *
 */
public class TestRunner {
	
	//Salto linea
	private static final String NEW_LINE = System.getProperty("line.separator");	

	public static void main(String[] args) throws Exception {
		
	    Result result;
	    
	    //Lanzar Test Junit (add clases)
	    result = JUnitCore.runClasses(com.ejemplo.clienteshttp.Test_ClienteHttp51.class);
	  	
	    //Imprimir resultados
	    StringBuilder sb = new StringBuilder("");
		sb.append(NEW_LINE+"EJECUTADO Test_ClienteHttpEjemplo RESULTADO: "+ result.wasSuccessful() );
		sb.append(NEW_LINE+" ----> Tests ejecutados: "+ result.getRunCount());
		sb.append(NEW_LINE+" ----> Tests ignorados:"+ result.getIgnoreCount());
		sb.append(NEW_LINE+" ----> Tests con error: "+ result.getFailureCount());
		//
		result.getFailures().forEach( f ->  sb.append(NEW_LINE+" ----> Error: "+ f.toString()));
		sb.append(NEW_LINE+NEW_LINE);
		System.out.println(sb.toString());
		//Si hay errores, no se continua realizando test (caso de haberlos)
		if (result.getFailureCount() > 0) {
			System.exit(1);
		}
	}
}  
