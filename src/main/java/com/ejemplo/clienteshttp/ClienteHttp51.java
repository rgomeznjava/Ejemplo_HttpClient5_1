package com.ejemplo.clienteshttp;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


//import org.apache.log4j.Logger;
//import org.slf4j.LoggerFactory;

/* imports version 4.5.13  comparación ---------------------
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
*/



     
/**
 * 	CLIENTE HTTP CLIENT EJEMPLO CRUD
 * 
 * 	- IMPLEMENTA HTTPCLIENT 5.1
 * 
 * @author GRN 
 *
 */
public class ClienteHttp51 {
	
    private static final Logger LOG = LogManager.getLogger(ClienteHttp51.class.getName());
    //private static org.slf4j.Logger LOG = LoggerFactory.getLogger(ClienteHttp4513Ejemplo.class);

	
	private static final int MIN_COD_ERROR_HTTP = 300;
	
	private Properties prop = new Properties();
	
	//Constantes TLS
	public static final String TLSv1_2 = "TLSv1.2";
	public static final String TLSv1_3 = "TLSv1.3";
	
	//Version TLS. Por defecto  TLSv1.3
	private String VERSION_TLS = TLSv1_3; 
	
	//En milisegundos x Defecto (60 segundos)
	private static int CONNECTION_TIME_OUT = 60000;  
	
	//Charset UTF-8
	private static final  String CHARSET_UTF_8 = "UTF-8";	
	
	//Salto linea
	private static String NEW_LINE = System.getProperty("line.separator");	


	private void loadProperties() {
		
		 try (InputStream input = ClienteHttp51.class.getClassLoader().getResourceAsStream("app.properties")) {

	         
	            if (input == null) {
	                System.out.println("Sorry, unable to find app.properties");
	                return;
	            }

	            //load a properties file from class path, inside static method
	            this.prop.load(input);
	           
	            if (prop.getProperty("VERSION_TLS")!=null) VERSION_TLS = prop.getProperty("VERSION_TLS");
	            if (prop.getProperty("CONNECTION_TIME_OUT")!=null) CONNECTION_TIME_OUT = Integer.parseInt(prop.getProperty("CONNECTION_TIME_OUT"));

	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	}
	
	
	/**
	 * Constructor
	 * 
	 */
	public ClienteHttp51() {
		
		loadProperties();
	}

	
	
	/**
	 *   REALIZAR PETICION GET
	 * 
	 * @param urlPeticion
	 * @return RespuestaCliente
	 */
	public RespuestaClienteHttp realizarPeticion_GET(String urlPeticion) throws Exception {
	
		//Objeto de negocio, que devolvemos
		RespuestaClienteHttp respCliente = new RespuestaClienteHttp();
		
		String resultado = "";
		
		//Objeto realizar peticion
		CloseableHttpClient httpClient = null;
		
		//Objeto recoge respuesta
		CloseableHttpResponse closeableHttpResponse = null;
		 	
		try {
			
			//-- CONFIGURAR HTTP CLIENT Y HTTP GET
			httpClient = this.configurarCloseableHttpClient();
			
			//Preparar peticion GET
			HttpGet httpGet = new HttpGet(urlPeticion);	
			
			//Ejemplo add cabeceras
			//httpGet.addHeader("usuario", this.usuario);  
							
			//-------- REALIZAR LLAMADA HTTP/S GET ---------------
			
			LOG.info(" ---  realizarPeticion_GET Call: "+ urlPeticion);
			System.out.println(" ---  realizarPeticion_GET Call: "+ urlPeticion);
			
			closeableHttpResponse = httpClient.execute(httpGet);
					
		
			//--------- TRATAR RESPUESTA C�DIGOS Y CONTENIDO RECIBIDO 
				
			int codigoEstado = closeableHttpResponse.getCode();
			String mensajeHttp = closeableHttpResponse.getReasonPhrase();
			LOG.info(" ---  Respuesta peticion GET: " + codigoEstado + " - " +	mensajeHttp);	
			System.out.println(" ---  Respuesta peticion GET: " + codigoEstado + " - " +	mensajeHttp);
			
			//Recogemos el codigo de estado en nuestro objeto negocio
			respCliente.setCodigoEstado(codigoEstado);
			respCliente.setMensajeHTTP(mensajeHttp);
			
			//Ejemplos de codigos HTTP utilizados (a medida o reutilizados):
			
			//200 Ok, 201 Created, 202 Accepted, 400 Bad request, 406 Not acceptable, 500 Error Server
		
			//Todos los igual o mayores de 3xx..4xx..5xx ser�an error: 406 Not acceptable, 500 Error Server
			//excepto 400 Bad Request --> LO UTILIZAMOS COMO ERRORES DE VALIDACION O LOGICA (Error controlado)
		 
			//HAY ERROR SI COD.ESTADO ES  IGUAL O SUPERIOR 300 (Excepto nuestro error de negocio reutilizado el 400) 
			boolean hayErrorRespuesta = (codigoEstado >= MIN_COD_ERROR_HTTP && (codigoEstado != HttpStatus.SC_BAD_REQUEST) );
		
			//CASOS ERROR EN RESPUESTA
			if (hayErrorRespuesta) {
				
				//Lanzamos ERROR: No recogemos respuesta
				throw new HttpResponseException(codigoEstado, mensajeHttp);
			
			} else {
				
				//CASOS OK - CODIGOS x DEBAJO DE 300 ->  200 OK, 201 CREATED, ETC.
				//MAS ERROR A MEDIDA NEGOCIO (400 BAD REQUEST) datos incorrectos.
				
				//Extraer contenido (entity) de la respuesta (closeableHttpResponse)
				HttpEntity entity = closeableHttpResponse.getEntity();
			
				//Error: RESPUESTA SIN CONTENIDO si tiene que venir contenido.
				if (entity == null) {
					
					throw new ClientProtocolException("RESPUESTA SIN CONTENIDO"); 
				
				} else {
					
					//TRATAR RESPUESTA, Obtener String, parsear, etc.				
					LOG.info("--------  RESPUESTA OBTENIDA -----------------");
						
					//OBTENER EL RESULTADO de la respuesta (HttpEntity) en String
					//utilizamos clase de ayuda EntityUtils
					resultado = EntityUtils.toString(entity,CHARSET_UTF_8);	
					
					LOG.info(resultado);
					System.out.println(resultado);
					
					//Setemaos exito y resultado en nuestro objeto negocio
					respCliente.setResultado(resultado);
					
					respCliente.setExitoPeticion(true);		
					
					//Liberar recursos HttpEntity
					EntityUtils.consume(entity);
				}
		
			}
			 	
		} catch (HttpResponseException hre) {		
					
			LOG.error("Error en realizarPeticion_GET(HttpResponseException): " + hre.getMessage());
			
			//ya viene cod.estado y mensaje http
			respCliente.setExitoPeticion(false);
			
			//o lanzar excepcion nueva a medida (controlada en negocio)
			
		} catch (Exception ex) {	
			
			LOG.error("Error GENERAL en realizarPeticion_GET(Exception): " + ex.getMessage());
			
			throw new Exception("Error GENERAL en realizarPeticion_GET: "+ ex.getMessage());
			
		} finally {
			
			 liberarRecurso(closeableHttpResponse);
			
			 liberarRecurso(httpClient);
	    } 
			
		return respCliente;
	}


	 /**
	 * REALIZAR PETICION POST XML
	 * 
	 * @param urlPeticion
	 * @param datosXML
	 * @return
	 * @throws Exception
	 */
	public RespuestaClienteHttp realizarPeticion_POST(String urlPeticion, String datosXML) throws Exception {
		
		
		RespuestaClienteHttp respCliente = new RespuestaClienteHttp();
		
		String resultado = "";
		
		//Objeto realizar peticion
		CloseableHttpClient httpClient = null;
		
		//Objeto recoge respuesta
		CloseableHttpResponse closeableHttpResponse = null;
		 	
		try {
			
			//-- CONFIGURAR HTTP CLIENT Y HTTP GET
			httpClient = this.configurarCloseableHttpClient();

			HttpPost httpPost = new HttpPost(urlPeticion); 
			
			//Add parametros a cabecera
			//httpPost.addHeader("usuario", this.usuario);
			
			//Crear entity (contenido body a enviar) en nuestro caso un StringEntity
			StringEntity myEntity = new StringEntity(datosXML, ContentType.create("application/xml", CHARSET_UTF_8));
			//Add 	
			httpPost.setEntity(myEntity);
			
			//-------- REALIZAR LLAMADA HTTP/S POST ---------------
		
			LOG.info(" ---  realizarPeticion_POST Call: "+ urlPeticion + " Enviando xml: "+ NEW_LINE + datosXML);
		
			closeableHttpResponse = httpClient.execute(httpPost);
				
	
			//--------- TRATAR RESPUESTA C�DIGOS Y CONTENIDO RECIBIDO 
	
			int codigoEstado = closeableHttpResponse.getCode();
			String mensajeHttp = closeableHttpResponse.getReasonPhrase();
			LOG.info(" ---  Respuesta peticion POST: " + codigoEstado + " - " +	mensajeHttp);	
			System.out.println(" ---  Respuesta peticion POST: " + codigoEstado + " - " +	mensajeHttp);
			
			//Recogemos el codigo de estado en nuestro objeto negocio
			respCliente.setCodigoEstado(codigoEstado);
		
			//Ejemplos de codigos HTTP utilizados (a medida o reutilizados):
			
			//200 Ok, 201 Created, 202 Accepted, 400 Bad request, 406 Not acceptable, 500 Error Server
		
			//Todos los igual o mayores de 3xx..4xx..5xx ser�an error: 406 Not acceptable, 500 Error Server
			//excepto 400 Bad Request --> LO UTILIZAMOS COMO ERRORES DE VALIDACION O LOGICA (Error controlado)
		 
			//HAY ERROR SI COD.ESTADO ES  IGUAL O SUPERIOR 300 (Excepto nuestro error de negocio reutilizado el 400) 
			boolean hayErrorRespuesta = (codigoEstado >= MIN_COD_ERROR_HTTP && (codigoEstado != HttpStatus.SC_BAD_REQUEST) );
		
			//CASOS ERROR EN RESPUESTA
			if (hayErrorRespuesta) {
				
				//Lanzamos ERROR: No recogemos respuesta
				throw new HttpResponseException(codigoEstado, mensajeHttp);
			
			} else {
				
				//CASOS OK - CODIGOS x DEBAJO DE 300 ->  200 OK, 201 CREATED, ETC.
				//MAS ERROR A MEDIDA NEGOCIO (400 BAD REQUEST) datos incorrectos.
				
				//Extraer contenido (entity) de la respuesta (closeableHttpResponse)
				HttpEntity entity = closeableHttpResponse.getEntity();
			
			
				//Error: RESPUESTA SIN CONTENIDO si tiene que venir contenido.
				if (entity == null) {
					
					throw new ClientProtocolException("RESPUESTA SIN CONTENIDO"); 
				
				} else {
					
					//TRATAR RESPUESTA, Obtener String, parsear, etc.				
					LOG.info("--------  RESPUESTA OBTENIDA -----------------");
						
					//OBTENER EL RESULTADO de la respuesta (HttpEntity) en String
					//utilizamos clase de ayuda EntityUtils
					resultado = EntityUtils.toString(entity,CHARSET_UTF_8);	
					
					LOG.info(resultado);
	
					//Setemaos exito y resultado en nuestro objeto negocio
					respCliente.setResultado(resultado);
					
					respCliente.setExitoPeticion(true);		
					
					//Liberar recursos HttpEntity
					EntityUtils.consume(entity);
				}
		
			}			
		
		} catch (HttpResponseException hre) {		
						
			LOG.error(" Error en realizarPeticion_POST(HttpResponseException): " + hre.getMessage());
				
			respCliente.setExitoPeticion(false);
			
		} catch (Exception ex) {	
			
			LOG.error(" Error en realizarPeticion_POST(Exception): " + ex.getMessage());
				
			respCliente.setExitoPeticion(false);
					
		} finally {
				
				 liberarRecurso(closeableHttpResponse);
				
				 liberarRecurso(httpClient);
		} 
				
	 return respCliente;
	}

	/**
	 * REALIZAR PETICION PUT XML:  MODIFICACION DE REGISTRO EXISTENTE
	 * 
	 * @param urlPeticion
	 * @param datosXML
	 * @return
	 * @throws Exception
	 */
	public RespuestaClienteHttp realizarPeticion_PUT(String urlPeticion, String datosXML) throws Exception {
		
		
		RespuestaClienteHttp respCliente = new RespuestaClienteHttp();
		
		String resultado = "";
		
		//Objeto realizar peticion
		CloseableHttpClient httpClient = null;
		
		//Objeto recoge respuesta
		CloseableHttpResponse closeableHttpResponse = null;
		 	
		try {
			
			//-- CONFIGURAR HTTP CLIENT 
			httpClient = this.configurarCloseableHttpClient();
	
			HttpPut httpPut = new HttpPut(urlPeticion); 
			
			//Add parametros a cabecera
			//httpPut.addHeader("usuario", this.usuario);
			
			//Crear entity (contenido body a enviar) en nuestro caso un StringEntity
			StringEntity myEntity = new StringEntity(datosXML, ContentType.create("application/xml", CHARSET_UTF_8));
			//Add 	
			httpPut.setEntity(myEntity);
			
			//-------- REALIZAR LLAMADA HTTP/S PUT ---------------
		
			LOG.info(" ---  realizarPeticion_PUT Call: "+ urlPeticion + " Enviando xml: "+ NEW_LINE + datosXML);
		
			closeableHttpResponse = httpClient.execute(httpPut);
				
	
			//--------- TRATAR RESPUESTA C�DIGOS Y CONTENIDO RECIBIDO 
			
			int codigoEstado = closeableHttpResponse.getCode();
			String mensajeHttp = closeableHttpResponse.getReasonPhrase();
			LOG.info(" ---  Respuesta peticion PUT: " + codigoEstado + " - " +	mensajeHttp);	
			System.out.println(" ---  Respuesta peticion PUT: " + codigoEstado + " - " +	mensajeHttp);
			
		
			//Recogemos el codigo de estado en nuestro objeto negocio
			respCliente.setCodigoEstado(codigoEstado);
		
			//Ejemplos de codigos HTTP utilizados (a medida o reutilizados):
			
			//200 Ok, 201 Created, 202 Accepted, 400 Bad request, 406 Not acceptable, 500 Error Server
		
			//Todos los igual o mayores de 3xx..4xx..5xx ser�an error: 406 Not acceptable, 500 Error Server
			//excepto 400 Bad Request --> LO UTILIZAMOS COMO ERRORES DE VALIDACION O LOGICA (Error controlado)
		 
			//HAY ERROR SI COD.ESTADO ES  IGUAL O SUPERIOR 300 (Excepto nuestro error de negocio reutilizado el 400) 
			boolean hayErrorRespuesta = (codigoEstado >= MIN_COD_ERROR_HTTP && (codigoEstado != HttpStatus.SC_BAD_REQUEST) );
		
			//CASOS ERROR EN RESPUESTA
			if (hayErrorRespuesta) {
				
				//Lanzamos ERROR: No recogemos respuesta
				throw new HttpResponseException(codigoEstado, mensajeHttp);
			
			} else {
				
				//CASOS OK - CODIGOS x DEBAJO DE 300 ->  200 OK, 201 CREATED, ETC.
				//MAS ERROR A MEDIDA NEGOCIO (400 BAD REQUEST) datos incorrectos.
				
				//Extraer contenido (entity) de la respuesta (closeableHttpResponse)
				HttpEntity entity = closeableHttpResponse.getEntity();
			
			
				//Error: RESPUESTA SIN CONTENIDO si tiene que venir contenido.
				if (entity == null) {
					
					throw new ClientProtocolException("RESPUESTA SIN CONTENIDO"); 
				
				} else {
					
					//TRATAR RESPUESTA, Obtener String, parsear, etc.				
					LOG.info("--------  RESPUESTA OBTENIDA -----------------");
						
					//OBTENER EL RESULTADO de la respuesta (HttpEntity) en String
					//utilizamos clase de ayuda EntityUtils
					resultado = EntityUtils.toString(entity,CHARSET_UTF_8);	
					
					LOG.info(resultado);
	
					//Setemaos exito y resultado en nuestro objeto negocio
					respCliente.setResultado(resultado);
					
					respCliente.setExitoPeticion(true);		
					
					//Liberar recursos HttpEntity
					EntityUtils.consume(entity);
				}
		
			}			
		
		} catch (HttpResponseException hre) {		
						
			LOG.error(" Error en realizarPeticion_PUT(HttpResponseException): " + hre.getMessage());
				
			respCliente.setExitoPeticion(false);
			
		} catch (Exception ex) {	
			
			LOG.error(" Error en realizarPeticion_PUT(Exception): " + ex.getMessage());
				
			respCliente.setExitoPeticion(false);
					
		} finally {
				
				 liberarRecurso(closeableHttpResponse);
				
				 liberarRecurso(httpClient);
		} 
				
	 return respCliente;
	}
	
	/**
	 *   REALIZAR PETICION DELETE
	 * 
	 * @param urlPeticion
	 * @return RespuestaCliente
	 */
	public RespuestaClienteHttp realizarPeticion_DELETE(String urlPeticion) throws Exception {
	
		RespuestaClienteHttp respCliente = new RespuestaClienteHttp();
		
		String resultado = "";
		
		//Objeto realizar peticion
		CloseableHttpClient httpClient = null;
		
		//Objeto recoge respuesta
		CloseableHttpResponse closeableHttpResponse = null;
		 	
		try {
			
			//-- CONFIGURAR HTTP CLIENT Y HTTP DELETE
			httpClient = this.configurarCloseableHttpClient();
			
			//Preparar peticion DELETE
			HttpDelete httpDelete = new HttpDelete(urlPeticion);
			
			 //Ejemplo add cabeceras
			//httpDelete.addHeader("usuario", this.usuario);
							
			//-------- REALIZAR LLAMADA HTTP/S DELETE ---------------
		
			LOG.info(" ---  realizarPeticion_DELETE Call: "+ urlPeticion);
			
			closeableHttpResponse = httpClient.execute(httpDelete);
					
		
			//--------- TRATAR RESPUESTA C�DIGOS Y CONTENIDO RECIBIDO 
				
			int codigoEstado = closeableHttpResponse.getCode();
			String mensajeHttp = closeableHttpResponse.getReasonPhrase();
			LOG.info(" ---  Respuesta peticion DELETE: " + codigoEstado + " - " +	mensajeHttp);	
			System.out.println(" ---  Respuesta peticion DELETE: " + codigoEstado + " - " +	mensajeHttp);
			
			//Recogemos el codigo de estado en nuestro objeto negocio
			respCliente.setCodigoEstado(codigoEstado);
			
		
			//HAY ERROR SI COD.ESTADO ES  IGUAL O SUPERIOR 300 (Excepto nuestro error a medida 400) 
			boolean hayErrorRespuesta = (codigoEstado >= MIN_COD_ERROR_HTTP && (codigoEstado != HttpStatus.SC_BAD_REQUEST) );
		
			//CASOS ERROR EN RESPUESTA
			if (hayErrorRespuesta) {
				
				//Lanzamos ERROR: No recogemos respuesta
				throw new HttpResponseException(codigoEstado, mensajeHttp);
			
			} else {
				
				//CASOS OK - CODIGOS x DEBAJO DE 300 ->  200 OK, 201 CREATED, ETC.
				//MAS ERROR A MEDIDA NEGOCIO (400 BAD REQUEST) datos incorrectos.
				
				//Extraer contenido (entity) de la respuesta (closeableHttpResponse)
				HttpEntity entity = closeableHttpResponse.getEntity();
			
				//Error: RESPUESTA SIN CONTENIDO si tiene que venir contenido.
				if (entity == null) {
					
					throw new ClientProtocolException("RESPUESTA SIN CONTENIDO"); 
				
				} else {
					 
					LOG.info("--------  RESPUESTA OBTENIDA -----------------");
						
					//OBTENER EL RESULTADO de la respuesta (HttpEntity) en String
					//utilizamos clase de ayuda EntityUtils
					resultado = EntityUtils.toString(entity,CHARSET_UTF_8);	
					
					LOG.info("  resultado.legth: " + resultado.length() + " resultado: " + resultado);
				
					respCliente.setExitoPeticion(true);		
					
					//Liberar recursos HttpEntity
					EntityUtils.consume(entity);
				}
		
			}
						
		} catch (HttpResponseException hre) {		
					
			LOG.error(" Error en realizarPeticion_DELETE(HttpResponseException): " + hre.getMessage());
	
			respCliente.setExitoPeticion(false);
						
		} catch (Exception ex) {	
			
			LOG.error(" Error en realizarPeticion_DELETE(Exception): " + ex.getMessage());
			
			respCliente.setExitoPeticion(false);
			 	
		} finally {
			
			 liberarRecurso(closeableHttpResponse);
			
			 liberarRecurso(httpClient);
	    } 
		
		return respCliente;
	}
	
	/**
	 * 	LIBERAR RECURSOS:
	 * 		 CloseableHttpResponse, CloseableHttpClient
	 * @param obj
	 */
	private void liberarRecurso(Object obj) {
		
		String recurso = "";
		try {			
			if  (obj!=null) {	
				recurso = obj.getClass().getName();
				
				if  (obj instanceof CloseableHttpResponse) {
					((CloseableHttpResponse) obj).close();
				} else if (obj instanceof CloseableHttpClient) {
					((CloseableHttpClient) obj).close();
				}
			}
			
		} catch (Exception e) {		
			LOG.info("Error al cerrar recurso "+ recurso + " Error:" +e.getMessage());
		}
	}

	 
	
	/**
	 * INICIALIZAR TRUST MANAGER PARA SOLO PARA TRAZAR
	 * 
	 * @param sslContext
	 */
	private void inicializarTrusrManagerParaSoloTrazas(SSLContext sslContext)  {
		
		try {
			//solo lo utilizamos  para trazas, en sslContext es donde se configura certificados,etc.
			   TrustManager tm = new X509TrustManager() {
			    	public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			    		LOG.debug(" getAcceptedIssuers():OK");		
						return null;
					}
					public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {	
						LOG.debug(" checkClientTrusted: " + arg0 + " X509Certificate[] encontrados: "+arg0.length + "  auth" +arg1);	
					}
					public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
						LOG.debug("checkServerTrusted: " + arg0 + " X509Certificate[] encontrados: "+arg0.length);
						for (java.security.cert.X509Certificate cert: arg0) { 
							LOG.info(" Tipo:"+ cert.getType()+ " Version:" + cert.getVersion() + " "+cert.getSubjectDN());
							break; //sacamos el primero encontrado
						}	
					}
				}; 
				//solo para trazas
				sslContext.init(null, new TrustManager[]{tm}, null);
				
		} catch (Exception e) {
			LOG.error(" Error al  inicializarTrusrManagerParaSoloTrazas: " + e.getMessage() );	
			//no lanzar, solo es para trazear
		}
	}


	//----------  CONFIGURACION HTTPCLIENT PARA TRABAJAR CON HTTPS, ETC  (YA CONFIGURADO NO TOCAR) ----------------
	/**
	 * 	CONFIGURAR CLOSEABLE HTTPCLIENT
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws KeyStoreException
	 */
	private CloseableHttpClient configurarCloseableHttpClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
		
		//CONFIGURAR OBJETO  closeableHttpClient PARA HTTP Y HTTPS CON VERSION_TLS (Indicada, por defecto TLSv1.3)
	
	   //Aceptar todos los certificados (no los comprueba, no instados en cliente JVM,etc)
	   TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
	  
	   //Crear SSL CONTEXT
	   SSLContext sslContext = SSLContexts.custom().setProtocol(VERSION_TLS).loadTrustMaterial(null, acceptingTrustStrategy).build();
	   SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);	
	
	   //S�lo para mostrar  trazas. (NO UTILIZAR PARA CONFIGURAR CERTIFICADOS, ESO SE HACE EN loadTrustMaterial)
	   this.inicializarTrusrManagerParaSoloTrazas(sslContext);
	
		//CREAR FACTORIA, HTTP  y HTTPS	 
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
																  .register("https", sslsf)
																  .register("http", new PlainConnectionSocketFactory())
																  .build();
		
		//Utilizado BasicHttpClientConnectionManager
		BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
	
		//Crear objeto HTTP CLIENT (Tipo CloseableHttpClient)  YA CONFIGURADO CON TODO LO ANTERIOR
		CloseableHttpClient closeableHttpClient = HttpClients.custom()
															  //_other_.setSSLSocketFactory(sslsf)
															  .setConnectionManager(connectionManager)
															  .setDefaultRequestConfig(
																	  RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_TIME_OUT,TimeUnit.MILLISECONDS) 
												                							.setConnectTimeout(CONNECTION_TIME_OUT,TimeUnit.MILLISECONDS).build()
												                							//_no_.setSocketTimeout(CONNECTION_TIME_OUT,TimeUnit.MILLISECONDS)						
												                )
															  .disableCookieManagement() //desactivar gestion cookies
															  .build();
	
		LOG.info(" ---> CONFIGURADO HTTPCLIENT CON VERSION_TLS:  "+ VERSION_TLS);
		System.out.println(" ---> CONFIGURADO HTTPCLIENT CON VERSION_TLS:  "+ VERSION_TLS);
		return closeableHttpClient;
	}
	
}
