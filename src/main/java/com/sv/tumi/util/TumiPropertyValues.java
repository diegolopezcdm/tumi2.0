package com.sv.tumi.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;


public class TumiPropertyValues {
	private static InputStream inputStream;
	private static Properties prop;
	
	private TumiPropertyValues() throws IOException {
		
		try {
			prop = new Properties();
			String propFileName = "config.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}

	}
	
	public static Properties getInstance() throws IOException{
		
		if(prop==null){
			new TumiPropertyValues();
		}
		return prop;
	}
}