package com.demo.angular.rest.rest.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.demo.angular.rest.rest.api.Model;

public class CreateExcel {

	private static final Logger log = Logger.getLogger(CreateExcel.class.getName());

	private CreateExcel() {
		throw new IllegalStateException("Utility class");
	}

	public static File createFile(final String fileName, final List<Model> models) throws IOException {
		File resultFile = null;
		try {
			Path repertoireTemporaire = Files.createTempDirectory("liste-model");

			// Créer, dans ce répertoire, un fichier avec le nom spécifié
			resultFile = new File(repertoireTemporaire.toFile(), fileName);
		} catch (IOException e) {
			throw new IOException("Problem");
		}
		try (XSSFWorkbook workbook = new XSSFWorkbook();
				OutputStream stream = Files.newOutputStream(resultFile.toPath())) {
			XSSFSheet spreadsheet = workbook.createSheet("List of Models");
			int countNumberLine = 0;

			addHeader(spreadsheet, countNumberLine++); 

			for (Model m : models) {
				XSSFRow line = spreadsheet.createRow(countNumberLine++);

				addCell(line, 0, m.getCode());
				addCell(line, 1, m.getLibelle());
			}
			workbook.write(stream);
			return resultFile;

		} catch (IOException e) {
			log.log(Level.SEVERE, "problem  {}", fileName);
		}
		return resultFile;
	}

	private static void addHeader(XSSFSheet spreadsheet, int countNumberLine) {
		XSSFRow line = spreadsheet.createRow(countNumberLine);
		addCell(line, 0, "code");
		addCell(line, 1, "description");

	}

	private static void addCell(Row line, int column, String value) {
		Cell cell = line.createCell(column);
		cell.setCellValue(value);
	}

}