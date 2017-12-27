package org.txemasv.mycharacters.apiserver.util;


public class ErrorType {
	
	private String error;

	public ErrorType(String errorMessage) {
		this.error = errorMessage;
	}
	
	public String getErrorMessage() {
		return error;
	}

	public void setErrorMessage(String errorMessage) {
		this.error = errorMessage;
	}

}
