package org.txemasv.mycharacters.apiserver.util;


public class SuccessType {
	
	private String success;

	public SuccessType(String successMessage) {
		this.success = successMessage;
	}
	
	public String getSuccessMessage() {
		return success;
	}

	public void setErrorMessage(String successMessage) {
		this.success = successMessage;
	}

}
