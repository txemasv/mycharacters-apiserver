package org.txemasv.mycharacters.apiserver.model;


public class ErrorType {
	
	private String errorMessage;

	public ErrorType(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
