package com.pearteam.demoapp.controllers.api;

import com.pearteam.demoapp.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/** Temporary controller for checking/testing purposes */
@Controller
public class TestController {
	@Autowired
	FileService fileService;

	@GetMapping("/ping")
	@ResponseBody
	String pingPong() {
		return "Pong";
	}

	@GetMapping("/api/hello")
	@ResponseBody
	public String hello() {
		return "Hello, the time at the server is now " + new Date() + "\n";
	}
}
