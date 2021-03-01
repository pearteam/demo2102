package com.pearteam.demoapp.dao;

import com.pearteam.demoapp.services.ProcessingStatus;

public class InsertResponse {
	private ProcessingStatus status;
	private String message;

	public InsertResponse(ProcessingStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public ProcessingStatus getStatus() {
		return status;
	}

	public void setStatus(ProcessingStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
