package com.pearteam.demoapp.controllers.api;

import com.pearteam.demoapp.dao.NewspaperRepository;
import com.pearteam.demoapp.services.FileService;
import com.pearteam.demoapp.services.ProcessingResponse;
import com.pearteam.demoapp.services.ProcessingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
class FileControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FileService fileService;

	@MockBean
	private NewspaperRepository newspaperRepository;

	@Test
	@DisplayName("Test upload file endpoint")
	void uploadFile() throws Exception {
		//when
		String givenXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
		"<epaperRequest>\n" +
		"  <deviceInfo>\n" +
		"    <screenInfo width=\"1280\" height=\"752\" dpi=\"160\" />\n" +
		"    <appInfo>\n" +
		"      <newspaperName>Gazeta Wyborcza</newspaperName>\n" +
		"    </appInfo>\n" +
		"  </deviceInfo>\n" +
		"</epaperRequest>";
		MockMultipartFile file = new MockMultipartFile(
				"file",
				"GazetaWyborcza.xml",
				MediaType.TEXT_PLAIN_VALUE,
				givenXml.getBytes()
		);
		ProcessingResponse processingResponse = new ProcessingResponse(ProcessingStatus.OK, "Processing Successful!");
		when(fileService.uploadFile(any())).thenReturn(processingResponse);

		//expected
		mockMvc.perform(fileUpload("/api/v1/uploadFile").file(file)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Processing Successful!")));
	}
}