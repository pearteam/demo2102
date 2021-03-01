package com.pearteam.demoapp.controllers.api;

import com.pearteam.demoapp.dao.Newspaper;
import com.pearteam.demoapp.services.NewspaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1")
public class NewspaperController {

	@Autowired
	NewspaperService newspaperService;

	@GetMapping("/newspaper/getAll")
	public ResponseEntity<List<Newspaper>> getNewspapers(
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy){
		List<Newspaper> list = newspaperService.getAll(pageNo, pageSize, sortBy);
		return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/newspaper/findByName")
	public ResponseEntity<List<Newspaper>> findByName(
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(required=false) String name){
		List<Newspaper> list = newspaperService.findByName(pageNo, pageSize, sortBy, name);
		return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/newspaper/findAllWithFilters")
	@CrossOrigin(origins = "*")
	public ResponseEntity<Map<String, Object>> findAllWithFilters(
			@RequestParam(defaultValue = "0") @Min(0) Integer pageNo,
			@RequestParam(defaultValue = "10") @Min(1) @Max(200) Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(required=false) String name,
			@RequestParam(required=false) Integer width,
			@RequestParam(required=false) Integer height,
			@RequestParam(required=false) Integer dpi,
			@RequestParam(required=false) String fileName) {

		Map<String, Object> response = new HashMap<>();
		Page<Newspaper> pageNewspapers;
		try {
			pageNewspapers = newspaperService.findAllWithFilters(pageNo, pageSize, sortBy, name, width, height, dpi, fileName);
		} catch (Exception ex) {
			response.put("error", ex.getMessage());
			return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		response.put("newspapers", pageNewspapers.getContent());
		response.put("current_page", pageNewspapers.getNumber());
		response.put("page_size", pageNewspapers.getNumberOfElements());
		response.put("total", pageNewspapers.getTotalElements());
		response.put("total_pages", pageNewspapers.getTotalPages());
		return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/newspaper/size")
	public Long getNewspapersQty(){
		return newspaperService.getCount();
	}
}
