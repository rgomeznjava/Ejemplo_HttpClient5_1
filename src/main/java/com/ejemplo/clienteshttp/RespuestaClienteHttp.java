package com.ejemplo.clienteshttp;

import java.io.Serializable;

/**
 *  RESPUESTA CLIENTE 
 *  
 *  OBJETO DE NEGOCIO CON RESPUESTA RECIBIDA DEL CLIENTE DE LLAMADA HTTP
 * 
 * @author GRN
 *
 */
public class RespuestaClienteHttp implements Serializable{

	private static final long serialVersionUID = 6080488136656732240L;

	boolean exitoPeticion = false;
	
	int codigoEstado = 0;
	String mensajeHTTP = "";
	
	//Resultado (Viene en respuesta)
	String resultado = "";
	 
	
	/**
	 * @return the exitoPeticion
	 */
	public boolean isExitoPeticion() {
		return exitoPeticion;
	}
	/**
	 * @param exitoPeticion the exitoPeticion to set
	 */
	public void setExitoPeticion(boolean exitoPeticion) {
		this.exitoPeticion = exitoPeticion;
	}
	/**
	 * @return the codigoEstado
	 */
	public int getCodigoEstado() {
		return codigoEstado;
	}
	/**
	 * @param codigoEstado the codigoEstado to set
	 */
	public void setCodigoEstado(int codigoEstado) {
		this.codigoEstado = codigoEstado;
	}
	/**
	 * @return the resultado
	 */
	public String getResultado() {
		return resultado;
	}
	/**
	 * @param resultado the resultado to set
	 */
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	/**
	 * @return the mensajeHTTP
	 */
	public String getMensajeHTTP() {
		return mensajeHTTP;
	}
	/**
	 * @param mensajeHTTP the mensajeHTTP to set
	 */
	public void setMensajeHTTP(String mensajeHTTP) {
		this.mensajeHTTP = mensajeHTTP;
	}
	
}
