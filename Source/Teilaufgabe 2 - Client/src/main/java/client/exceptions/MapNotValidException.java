package client.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapNotValidException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LoggerFactory.getLogger(MapNotValidException.class);

	public MapNotValidException() { 
		logger.info("Map not generated correctly!...");
	}
	
	
	
	
}
