package com.pearteam.demoapp.services;

import com.pearteam.demoapp.dao.Newspaper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class FileServiceUnitTest {

	@Autowired
	private FileService fileService;

	@Test
	@DisplayName("Xml should pass validation")
	void validateXml() {
		//given:
			String givenXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
					"<epaperRequest>\n" +
					"  <deviceInfo name=\"Browser\" id=\"test@comp\">\n" +
					"    <screenInfo width=\"1280\" height=\"752\" dpi=\"160\" />\n" +
					"    <osInfo name=\"Browser\" version=\"1.0\" />\n" +
					"    <appInfo>\n" +
					"      <newspaperName>abb</newspaperName>\n" +
					"      <version>1.0</version>\n" +
					"    </appInfo>\n" +
					"  </deviceInfo>\n" +
					"  <getPages editionDefId=\"11\" publicationDate=\"2017-06-06\"/>\n" +
					"</epaperRequest>";

		//when:
		ProcessingResponse response = fileService.validateXml(givenXml);

		//then:
		assertEquals(response.getStatus(), ProcessingStatus.OK);
	}

	@Test
	@DisplayName("XML should pass validation with only obligatory fields that will go to database")
	void validateXmlLessFields() {
		//given:
			String givenXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
					"<epaperRequest>\n" +
					"  <deviceInfo>\n" +
					"    <screenInfo width=\"1280\" height=\"752\" dpi=\"160\" />\n" +
					"    <appInfo>\n" +
					"      <newspaperName>abb</newspaperName>\n" +
					"    </appInfo>\n" +
					"  </deviceInfo>\n" +
					"</epaperRequest>";

		//when:
		ProcessingResponse response = fileService.validateXml(givenXml);

		//then:
		assertEquals(response.getStatus(), ProcessingStatus.OK);
	}

	@Test
	@DisplayName("XML should fail validation because screen info attributes aren't not-negative-digits")
	void validateXmlNotDigits() {
		//given:
			String givenXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
					"<epaperRequest>\n" +
					"  <deviceInfo>\n" +
					"    <screenInfo width=\"1280\" height=\"752\" dpi=\"-160\" />\n" +
					"    <appInfo>\n" +
					"      <newspaperName>abb</newspaperName>\n" +
					"    </appInfo>\n" +
					"  </deviceInfo>\n" +
					"</epaperRequest>";

		//when:
		ProcessingResponse response = fileService.validateXml(givenXml);

		//then:
		assertEquals(response.getStatus(), ProcessingStatus.FAIL_ON_VALIDATION);
	}

	@Test
	@DisplayName("XML parsing should retrieve needed data")
	void parseXml() {
		//given:
		String givenXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
				"<epaperRequest>\n" +
				"  <deviceInfo name=\"Browser\" id=\"test@comp\">\n" +
				"    <screenInfo width=\"1280\" height=\"752\" dpi=\"160\" />\n" +
				"    <osInfo name=\"Browser\" version=\"1.0\" />\n" +
				"    <appInfo>\n" +
				"      <newspaperName>abb</newspaperName>\n" +
				"      <version>1.0</version>\n" +
				"    </appInfo>\n" +
				"  </deviceInfo>\n" +
				"  <getPages editionDefId=\"11\" publicationDate=\"2017-06-06\"/>\n" +
				"</epaperRequest>";

		//when:
		Newspaper newspaper = fileService.parseXml(givenXml).getNewspaper();

		//then:
		assertEquals(newspaper.getName(), "abb");
		assertEquals(newspaper.getDpi(), 160);
		assertEquals(newspaper.getWidth(), 1280);
		assertEquals(newspaper.getHeight(), 752);
		assertNotEquals(newspaper.getTimestamp(), null);
	}
}