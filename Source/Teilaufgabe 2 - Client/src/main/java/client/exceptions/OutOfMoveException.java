package client.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutOfMoveException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(OutOfMoveException.class);
	
	public OutOfMoveException() {
		logger.info("You are out of moves!");
	}
}
