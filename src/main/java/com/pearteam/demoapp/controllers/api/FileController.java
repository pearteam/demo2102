package com.pearteam.demoapp.controllers.api;

import com.pearteam.demoapp.dao.Newspaper;
import com.pearteam.demoapp.dao.NewspaperRepository;
import com.pearteam.demoapp.services.FileService;
import com.pearteam.demoapp.services.ProcessingResponse;
import com.pearteam.demoapp.services.ProcessingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/** Upload files endpoint */
@RestController
@RequestMapping(path = "/api/v1")
public class FileController {
	@Autowired
	FileService fileService;

	@Autowired
	NewspaperRepository newspaperRepository;

	public FileController() {};


	@GetMapping("/newspaper/all")
	public Iterable<Newspaper> getNewspapers(){
		return newspaperRepository.findAll();
	}

	@PostMapping(value = "/uploadFile", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "*")
	public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
		Map<String, Object> response = new HashMap<>();
		ProcessingResponse processingResponse;
		try {
			processingResponse = fileService.uploadFile(file);
		} catch (Exception ex) {
			response.put("error", ex.getMessage());
			return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
		}
		response.put("processingData", processingResponse);
		if (processingResponse.getStatus() != ProcessingStatus.OK) {
			return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
	}
}
