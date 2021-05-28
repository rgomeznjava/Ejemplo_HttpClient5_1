package com.ejemplo.clienteshttp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utilidades {
	
	
	public static Properties loadPropertiesFile(String nombreProperties) throws Exception {
		
		Properties properties = new Properties();
		
		 try (InputStream input = Utilidades.class.getClassLoader().getResourceAsStream(nombreProperties)) {

	            if (input == null) {
	               
	               throw new Exception("Sorry, unable to find " + nombreProperties);
	            }

	            //load a properties file from class path, inside static method
	            properties.load(input);
	           
	        } catch (IOException ex) {
	        	  throw new Exception("Sorry, unable to find " + nombreProperties + " error:"+ex.getMessage());
	        }
		 return properties;
	}

}
