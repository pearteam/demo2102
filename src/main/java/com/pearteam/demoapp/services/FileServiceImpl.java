package com.pearteam.demoapp.services;

import com.pearteam.demoapp.dao.Newspaper;
import com.pearteam.demoapp.dao.NewspaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/** Processing file service **/
@Service
public class FileServiceImpl implements FileService {

	@Autowired
	private NewspaperRepository newspaperRepository;

	public ProcessingResponse validateXml(String xmlString) {
		URL schemaFile;
		try {
			schemaFile = this.getClass().getResource("/schema1.xsd");
		} catch (Exception e) {
			return new ProcessingResponse(ProcessingStatus.FAIL_ON_VALIDATION, "Missing XSD file: " + e.getMessage());
		}
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new StringReader(xmlString)));
		} catch (SAXException e) {
			return new ProcessingResponse(ProcessingStatus.FAIL_ON_VALIDATION, "SAXException: " + e.getMessage());
		} catch (IOException e) {
			return new ProcessingResponse(ProcessingStatus.FAIL_ON_VALIDATION, "IOException: " + e.getMessage());
		}
		return new ProcessingResponse(ProcessingStatus.OK, null);
	}

	public ProcessingResponse parseXml(String xml) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			return new ProcessingResponse(ProcessingStatus.FAIL_ON_PARSING,"Parser configuration problem " + e.getMessage());
		}
		Document doc;
		try {
			doc = builder.parse(new InputSource(new StringReader(xml)));
		} catch (SAXException e) {
			e.printStackTrace();
			return new ProcessingResponse(ProcessingStatus.FAIL_ON_PARSING, "Parser SAXException " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return new ProcessingResponse(ProcessingStatus.FAIL_ON_PARSING, "Parser IOException " + e.getMessage());
		}

		// normalize XML response
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("deviceInfo").item(0).getChildNodes();
		String newspaperName = null;
		int widthInt = 0;
		int heightInt = 0;
		int dpiInt = 0;
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				switch (eElement.getNodeName()) {
					case "screenInfo" : {
						try {
							widthInt = Integer.parseInt(eElement.getAttribute("width"));
							heightInt = Integer.parseInt(eElement.getAttribute("height"));
							dpiInt = Integer.parseInt(eElement.getAttribute("dpi"));
						} catch (NumberFormatException ex){
							return new ProcessingResponse(ProcessingStatus.FAIL_ON_PARSING, "Problem with parsing width, height or dpi: " + ex.getMessage(), null);
						}
						break;
					}
					case "appInfo" : {
						newspaperName = eElement.getElementsByTagName("newspaperName").item(0).getTextContent();
						break;
					}
				}
			}
		}

		Newspaper newspaper = new Newspaper(newspaperName, widthInt, heightInt, dpiInt, "not_yet", LocalDateTime.now());

		return new ProcessingResponse(ProcessingStatus.OK, null, newspaper);
	}

	public ProcessingResponse insertNewspaperToDatabase(Newspaper newspaper) {
		try {
			newspaperRepository.save(newspaper);
		} catch(Exception ex) {
			return new ProcessingResponse(ProcessingStatus.FAIL_ON_INSERTING_TO_DB, "Insert to newspaper table failed: " +  ex.getMessage());
		}
		return new ProcessingResponse(ProcessingStatus.OK, "Processing Successful! Newspaper added to DB", newspaper);
	}

	public ProcessingResponse processFile(String inputXml, String filename) {

		ProcessingResponse validationResponse = validateXml(inputXml);
		if (validationResponse.getStatus() != ProcessingStatus.OK) {
			return validationResponse;
		}

		ProcessingResponse parsingResponse = parseXml(inputXml);
		if (parsingResponse.getStatus() != ProcessingStatus.OK) {
			return parsingResponse;
		}


		Newspaper newspaper = parsingResponse.getNewspaper();
		newspaper.setFileName(filename);

		ProcessingResponse insertResponse = insertNewspaperToDatabase(newspaper);

		return insertResponse;
	}

	public ProcessingResponse uploadFile(MultipartFile file) {

		ProcessingResponse processingResponse;
		try {
			String xmlContent = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
			processingResponse = processFile(xmlContent, file.getOriginalFilename());
		} catch (Exception e) {
			processingResponse = new ProcessingResponse(ProcessingStatus.FAIL_ON_READING_FILE, "Uploaded file can't be processed: " +  e.getMessage());
		}
		return processingResponse;
	}
}
