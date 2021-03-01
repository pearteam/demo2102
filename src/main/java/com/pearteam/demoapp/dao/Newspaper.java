package com.pearteam.demoapp.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Newspaper {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty("id")
	private Integer id;
	private String name;
	private int width;
	private int height;
	private int dpi;
	private String fileName;
	private LocalDateTime timestamp;

	public Newspaper() {}
	public Newspaper(String name, int width, int height, int dpi, String fileName, LocalDateTime timestamp) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.dpi = dpi;
		this.fileName = fileName;
		this.timestamp = timestamp;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getDpi() {
		return dpi;
	}

	public void setDpi(int dpi) {
		this.dpi = dpi;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
