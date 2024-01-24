package com.demo.angular.rest.rest.util;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class CsvUtil {
	private static final Logger log = Logger.getLogger(CsvUtil.class.getName());

	private CsvUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static List<String[]> readFileCsv(Path filePath) throws IOException, CsvException {
		 try (Reader reader = Files.newBufferedReader(filePath)) {
		        try (CSVReader csvReader = new CSVReader(reader)) {
		            return csvReader.readAll();
		        }
		    }
		
	}

}
