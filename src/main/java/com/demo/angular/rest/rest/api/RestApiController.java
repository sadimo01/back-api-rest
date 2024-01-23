package com.demo.angular.rest.rest.api;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.demo.angular.rest.rest.util.CreateExcel;
import com.demo.angular.rest.rest.util.ReportUtil;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class RestApiController {

	private static final String FROMAT = "dd/MM/yyyy";

	private final Model model1 = new Model("date1", LocalDate.now().format(DateTimeFormatter.ofPattern(FROMAT)));
	private final Model model2 = new Model("date2",
			LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern(FROMAT)));
	private final Model model3 = new Model("date3",
			LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern(FROMAT)));

	@GetMapping(value = "/date")
	public Model findData() {
		return model1;

	}

	@GetMapping("/all/{type}")
	public List<?> findAll(@PathVariable String type) {
		List<?> data = null;		
		switch (type) {
		case "Action":
			data = Arrays.asList(model1, model2, model3);
			break;
		case "Select":
			data = Arrays.asList(new Person("SADIK", "Mohamed"), new Person("Golaire", "Thomas"));
			break;
		case "Agregation":
			data = Arrays.asList(new Adresse("Chaussée de Louvain", 214L, "Woluwe Saint Lambet"),
					new Adresse("Chaussée de la Hulpe", 177L, "Watermael-Boitsfort"));
			break;
		default:
			data = new ArrayList<>();
			break;
		}
		return data;

	}

	@GetMapping("/download")
	public ResponseEntity<InputStreamResource> createAndDeliverFile() throws IOException {

		List<Model> list = Arrays.asList(model1, model2, model3);
		File file = CreateExcel.createFile("demoExcel.xlsx", list);
		ByteArrayInputStream in = new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
				.header("Content-Disposition", "attachment; filename=models.xlsx").body(new InputStreamResource(in));

	}

	@GetMapping("/pdf")
	public ResponseEntity<ByteArrayResource> getItemReport() {
		byte[] reportContent;
		reportContent = ReportUtil.getItemReport(Arrays.asList(model1, model2, model3), "pdf");
		ByteArrayResource resource = new ByteArrayResource(reportContent);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.contentLength(resource.contentLength()).header(HttpHeaders.CONTENT_DISPOSITION,
						ContentDisposition.attachment().filename("report.pdf").build().toString())
				.body(resource);
	}

}
