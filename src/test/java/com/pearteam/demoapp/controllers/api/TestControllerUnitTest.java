package com.pearteam.demoapp.controllers.api;

import com.pearteam.demoapp.services.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestController.class)
class TestControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FileService fileService;

	@Test
	public void testMessagePage() throws Exception {
		this.mockMvc.perform(get("/ping")).andExpect(status().isOk())
				.andExpect(content().string("Pong"));
	}

}