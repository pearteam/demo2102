package com.pearteam.demoapp.services;

import com.pearteam.demoapp.dao.Newspaper;

import java.util.HashMap;
import java.util.Map;

public class ProcessingResponse {
	private ProcessingStatus status;
	private String message;
	private Newspaper newspaper;

	public ProcessingResponse(ProcessingStatus status) {
		this.status = status;
	}

	public ProcessingResponse(ProcessingStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public ProcessingResponse(ProcessingStatus status, String message, Newspaper newspaper) {
		this.status = status;
		this.newspaper = newspaper;
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

	public Newspaper getNewspaper() {
		return newspaper;
	}

	public void setNewspaper(Newspaper newspaper) {
		this.newspaper = newspaper;
	}

	public Map toMap() {
		Map<String, String> map = new HashMap();
		map.put("status", status.toString());
		map.put("message", message);
		return map;
	}
}
