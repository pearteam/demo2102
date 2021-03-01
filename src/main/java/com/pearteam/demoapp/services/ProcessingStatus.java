package com.pearteam.demoapp.services;

public enum ProcessingStatus {
	OK,
	FAIL_ON_INSERTING_TO_DB,
	FAIL_ON_VALIDATION,
	FAIL_ON_PARSING,
	FAIL_ON_READING_FILE
}