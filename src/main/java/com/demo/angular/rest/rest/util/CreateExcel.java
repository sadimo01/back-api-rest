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
import com.demo.angular.rest.rest.api.Person;

public class CreateExcel {

	private static final Logger log = Logger.getLogger(CreateExcel.class.getName());

	private CreateExcel() {
		throw new IllegalStateException("Utility class");
	}

	public static File createFile(final String fileName, final List<?> models) throws IOException {
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

			for (Object m : models) {
				XSSFRow line = spreadsheet.createRow(countNumberLine++);

				if (m instanceof Model model) {
					addCell(line, 0,model.getCode());
					addCell(line, 1, model.getLibelle());
				}
				if (m instanceof Person p) {
					addCell(line, 0, p.getNom());
					addCell(line, 1, p.getPrenom());
				}
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
		addCell(line, 0, "Column1");
		addCell(line, 1, "Column 2");

	}

	private static void addCell(Row line, int column, String value) {
		Cell cell = line.createCell(column);
		cell.setCellValue(value);
	}

}