package com.pearteam.demoapp.services;

import com.pearteam.demoapp.dao.Newspaper;
import com.pearteam.demoapp.dao.NewspaperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class FileServiceIntegrationTest {

	@Autowired
	private FileService fileService;

	@Autowired
	private NewspaperRepository newspaperRepository;

	@BeforeEach
	public void deleteAllBeforeTests() throws Exception {
		newspaperRepository.deleteAll();
	}

	@Test
	@DisplayName("Newspaper data are saved and retrieved from database")
	void savingToDatabase() {
		//given
		Newspaper newspaper = new Newspaper("ABC", 1080, 720, 600, "Test-file", LocalDateTime.now());

		//when
		ProcessingResponse insertResponse = fileService.insertNewspaperToDatabase(newspaper);

		//then
		assertEquals(insertResponse.getStatus(), ProcessingStatus.OK);
		List<Newspaper> newspapers = (List<Newspaper>) newspaperRepository.findAll();
		assertEquals(newspapers.size(), 1);
		Newspaper newspaperFromDb = newspapers.get(0);
		assertEquals(newspaperFromDb.getName(), "ABC");
		assertEquals(newspaperFromDb.getHeight(), 720);
		assertEquals(newspaperFromDb.getWidth(), 1080);
		assertEquals(newspaperFromDb.getDpi(), 600);
		assertEquals(newspaperFromDb.getFileName(), "Test-file");
	}

	@Test
	@DisplayName("File is processed correctly")
	void shouldProcessFile() {
		//given
		String givenXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
				"<epaperRequest>\n" +
				"  <deviceInfo name=\"Browser\" id=\"test@comp\">\n" +
				"    <screenInfo width=\"1280\" height=\"720\" dpi=\"160\" />\n" +
				"    <osInfo name=\"Browser\" version=\"1.0\" />\n" +
				"    <appInfo>\n" +
				"      <newspaperName>ABC</newspaperName>\n" +
				"      <version>1.0</version>\n" +
				"    </appInfo>\n" +
				"  </deviceInfo>\n" +
				"  <getPages editionDefId=\"11\" publicationDate=\"2017-06-06\"/>\n" +
				"</epaperRequest>";
		String fileName = "Test-file";

		//when
		ProcessingResponse fileProcessingResponse = fileService.processFile(givenXml, fileName);
		//then
		assertEquals(fileProcessingResponse.getStatus(), ProcessingStatus.OK);
		List<Newspaper> newspapers = (List<Newspaper>) newspaperRepository.findAll();
		assertEquals(newspapers.size(), 1);
		Newspaper newspaperFromDb = newspapers.get(0);
		assertEquals(newspaperFromDb.getName(), "ABC");
		assertEquals(newspaperFromDb.getHeight(), 720);
		assertEquals(newspaperFromDb.getWidth(), 1280);
		assertEquals(newspaperFromDb.getDpi(), 160);
		assertEquals(newspaperFromDb.getFileName(), "Test-file");
	}
}
