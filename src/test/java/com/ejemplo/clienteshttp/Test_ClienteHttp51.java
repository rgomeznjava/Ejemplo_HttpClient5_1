package com.ejemplo.clienteshttp;

import java.io.File;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 *   TEST CLIENTE HTTP EJEMPLO (Junit)
 * 
 */
public class Test_ClienteHttp51 {

	//Salto linea
	private static final String NEW_LINE = System.getProperty("line.separator");	
	
	//Propiedades para configuración, pruebas,etc.
	private static Properties properties;

	//Cliente  
	private ClienteHttp51 clienteHttp;
	

	//CARPETA Para archivos xml, json,etc.
	private static  String RUTA_PRUEBAS = "/PRUEBAS/";
		
	
	
	/**
	 *  Inicializar  datos  para  los Test
	 * 
	 * @throws Exception
	 */
	@Before
	public  void _testInicializar() throws Exception {
		
		
		//RUTA_PRUEBAS = "C:/PRUEBAS/";
		
		//Load de fichero
		properties = Utilidades.loadPropertiesFile("application.properties");
		
		//add o sobreescribir parametros
		
		properties.put("URL_PETICION_GET","https://dummy.restapiexample.com/api/v1/employees/");
		properties.put("URL_PETICION_POST","https://xxxxxx/");
		properties.put("URL_PETICION_PUT","https://xxxxxxx/");
		properties.put("URL_PETICION_DELETE","https://xxxxx/");

		properties.put("URL_PETICION_GOOGLE","https://www.google.es");

		//Credenciales (caso de usarlas)
		properties.put("USUARIO","XXXXXX");
		properties.put("PASSWORD","XXXXXX");

	}	
	 
	
	
	
	//@Ignore ("descomentar para ignorar")	
	@Test
	public void prueba_GET_TLS() throws Exception {
					
		System.out.println(NEW_LINE+"TEST ---> PRUEBA GET (HTTPS-TLS) ...");
		
		 
		//url peticion
		String urlPeticion = properties.getProperty("URL_PETICION_GET"); 	 
		
		//CALL HTTP
		clienteHttp = new ClienteHttp51(properties);	

		//Objeto de negocio respuesta cliente 
		RespuestaClienteHttp respuestaCliente = clienteHttp.realizarPeticion_GET(urlPeticion);
		
		//200 Ok, 201 Created, 202 Accepted, 400 Bad request, 406 Not acceptable, 500 Error Server
		//Todos los igual o mayores de 3xx..4xx..5xx seran errores: 
		//Excpeto los que utilicemos para errores de logica negocio  500 Error Server
		//400 Bad Request, 406 Not acceptable a medida o reutilizados
	 
		//El TEST es OK, si trae cod.estado correspodiente a la prueba y  recibe XML con confirmacion true/false
		Assert.assertTrue("TEST PRUEBA GET TLS  NO HA PASADO: SE ESPERABA 200 OK", 
				respuestaCliente.getCodigoEstado()==200 
				&& respuestaCliente.getResultado().contains("Tiger Nixon"));
	}
	
	
	
	
	
	
	@Ignore ("descomentar para ignorar")	 
	@Test
	public void prueba_POST_XML() throws Exception {
		
		System.out.println(NEW_LINE+"TEST ---> PRUEBA POST XML....");
		
		String urlPeticion = properties.getProperty("URL_PETICION_POST"); 	 
		
		//Datos XML a enviar, obtenidos de file
		String nombreArchivoXML = "datos.xml";
		File ficheroDatosXML = new File(RUTA_PRUEBAS + nombreArchivoXML);			
		String datosXML = FileUtils.readFileToString(ficheroDatosXML, "UTF-8");		
		
		//CALL WS
		clienteHttp = new ClienteHttp51(properties);
		//Objeto respuesta de negocio
		RespuestaClienteHttp respuestaCliente  = clienteHttp.realizarPeticion_POST(urlPeticion, datosXML);
			
		//El TEST es OK, si trae cod.estado correspondiente a la prueba 
		Assert.assertTrue("TEST PRUEBA POST NO HA PASADO: SE ESPERABA 201 - Created", respuestaCliente.getCodigoEstado()==201);
	}
	

	@Ignore ("descomentar para ignorar")	
	@Test
	public void prueba_DELETE() throws Exception {
		 	
		System.out.println(NEW_LINE+"TEST ---> BORRAR FORMULARIO DELETE....");
		
		  
		//url peticion
		String urlPeticion = properties.getProperty("URL_PETICION_DELETE"); 	 
					
		//CALL WS
		clienteHttp = new ClienteHttp51(properties);
		
		//Objeto de negocio respuesta cliente 
		RespuestaClienteHttp respuestaCliente = clienteHttp.realizarPeticion_DELETE(urlPeticion);
	
		//El TEST es OK, si trae cod.estado correspodiente a la prueba 
		Assert.assertTrue("TEST BORRAR NO HA PASADO: SE ESPERABA 404 NO CONTENT", respuestaCliente.getCodigoEstado()==404);
	}


	@Ignore ("descomentar para ignorar")	 
	@Test
	public void prueba_modificar_PUT_XML() throws Exception {
		
		System.out.println(NEW_LINE+"TEST ---> MODIFICAR  PUT....");
		 
		String urlPeticion = properties.getProperty("URL_PETICION_PUT"); 	 
		
		//Datos XML a enviar, obtenidos de file
		String nombreArchivoXML = "datos.xml";
		File ficheroDatosXML = new File(RUTA_PRUEBAS + nombreArchivoXML);			
		String datosXML = FileUtils.readFileToString(ficheroDatosXML, "UTF-8");		
	 
		//CALL HTTP PUT
		clienteHttp = new ClienteHttp51(properties);	
		
		//Objeto respuesta de negocio
		RespuestaClienteHttp respuestaCliente  = clienteHttp.realizarPeticion_PUT(urlPeticion, datosXML);
			
		//El TEST es OK, si trae cod.estado correspondiente a la prueba
		Assert.assertTrue("TEST MODIFICAR  NO HA PASADO: SE ESPERABA 200- OK", respuestaCliente.getCodigoEstado()==200);
	}
	
	
	
	
} //_fin
	
