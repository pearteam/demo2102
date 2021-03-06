package com.pearteam.demoapp.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Newspaper {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@JsonProperty("id")
	private Integer id;
	@NonNull
	private String name;
	@NonNull
	private int width;
	@NonNull
	private int height;
	@NonNull
	private int dpi;
	@NonNull
	private String fileName;
	@NonNull
	private LocalDateTime timestamp;
}
