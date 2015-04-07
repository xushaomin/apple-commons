package com.appleframework.commons.pool;

public class PrintImpl implements IPrint {

	private String message;

	public void doPrint() {
		System.out.println(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
