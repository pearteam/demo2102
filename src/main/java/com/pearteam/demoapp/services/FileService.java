package com.pearteam.demoapp.services;

import com.pearteam.demoapp.dao.Newspaper;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	ProcessingResponse validateXml(String xml);
	ProcessingResponse parseXml(String xml);
	ProcessingResponse processFile(String xml, String filename);
	ProcessingResponse insertNewspaperToDatabase(Newspaper newspaper);
	ProcessingResponse uploadFile(MultipartFile file);
}
