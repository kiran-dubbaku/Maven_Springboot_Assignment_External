package com.intelect.util;


public class CustomErrorType {

    private String errorMessage;
    

    public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public CustomErrorType(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
