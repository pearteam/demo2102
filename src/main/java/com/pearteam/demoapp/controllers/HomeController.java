package com.pearteam.demoapp.controllers;

import com.pearteam.demoapp.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@Autowired
	FileService fileService;

	@RequestMapping(value = "/upload")
	public String upload() {
		return "upload";
	}
}
