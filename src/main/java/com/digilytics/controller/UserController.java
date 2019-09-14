package com.digilytics.controller;
import java.io.*;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.digilytics.service.IUserService;

import javax.servlet.http.HttpServletResponse;


/**
 * Controller class for User register and register error download CSV file
 */
@RestController
public class UserController {
	@Autowired
	private IUserService userService;

	/**
	 * POST API to register user
	 * @param file contains CSV file with user details
	 * @return no of rows parsed, no of failed rows and error file path if any
	 * @throws IOException
	 */
	@PostMapping(value = "/register", consumes = "multipart/form-data")
	public ResponseEntity<HashMap> uploadMultipart(@RequestParam("file") MultipartFile file) throws IOException {
		HashMap<String, String> map = userService.addUser(file.getInputStream());
		return ResponseEntity.ok()
				.body(map);
	}

	/**
	 * Download GET API for downloading error csv file
	 * @param filepath with the filename to be downloaded
	 * @param response return the file as response
	 * @throws IOException
	 */
	@GetMapping(value = "/download/{filepath}", produces = "multipart/form-data")
	public void uploadMultipart(@PathVariable String filepath, HttpServletResponse response) throws IOException {

		File file = new File("errors" + "/" + filepath);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
		response.setContentLength((int) file.length());
		BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());
		byte[] buffer = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
		outStream.flush();
		inStream.close();
	}
}