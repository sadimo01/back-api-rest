package com.demo.angular.rest.rest.api;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.angular.rest.rest.util.CreateExcel;
import com.demo.angular.rest.rest.util.ReportUtil;

import net.sf.jasperreports.engine.JasperExportManager;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class RestApiController {

	@GetMapping("/excel/{type}/{data}")
	public ResponseEntity<InputStreamResource> createAndDeliverFile(@PathVariable String type,
			@PathVariable String data) throws IOException {
		List<Model> models = findDatas(data);
		File file = CreateExcel.createFile("demoExcel.xlsx", models);
		ByteArrayInputStream in = new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.header("Content-Disposition", "attachment; filename=models.xlsx").body(new InputStreamResource(in));

	}

	@GetMapping("/pdf/{type}/{data}")
	public ResponseEntity<ByteArrayResource> getItemReport(@PathVariable String type, @PathVariable String data) {
		byte[] reportContent;
		List<Model> models = findDatas(data);
		
		switch (type) {
		case "Sub" -> reportContent = ReportUtil.getItemReport(models, "pdf", "classpath:reports/report.jrxml",
				"report.jasper");
		case "Select" -> reportContent = ReportUtil.getItemReport(models, "pdf",
				"classpath:reports/report-person.jrxml", "report-person.jasper");
		default -> throw new RuntimeException("Unknown report format");
		}
		ByteArrayResource resource = new ByteArrayResource(reportContent);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.contentLength(resource.contentLength()).header(HttpHeaders.CONTENT_DISPOSITION,
						ContentDisposition.attachment().filename("report.pdf").build().toString())
				.body(resource);
	}
	
	
	private List<Model> findDatas(String data)
	{
		List<Model> models = new ArrayList<>();
		int index =0;
		if (Character.isDigit(data.charAt(0))) {
			models = Arrays.asList(new Model("Matricule", data));
		} else {
			List<String> lists = Arrays.stream(Arrays.stream(data.split(" ")).map(String::trim).toArray(String[]::new))
					.toList(); 
			for (String d : lists) {
				models.add(new Model("liste"+index, d));
				index++;
			}

		}
		return models;
	}

}
