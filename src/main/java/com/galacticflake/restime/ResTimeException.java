package com.galacticflake.restime;

public class ResTimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ResTimeException(String message) {
		this(message, null);
	}
	
	public ResTimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
