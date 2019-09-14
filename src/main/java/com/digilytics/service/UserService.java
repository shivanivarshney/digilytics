package com.digilytics.service;

import com.digilytics.dao.IUserDAO;
import com.digilytics.entity.User;
import com.digilytics.entity.UserError;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Implementing User service interface
 */
@Service
public class UserService implements IUserService {

	private static String USER_ALREADY_EXISTS = "User already exists";
	private static String INVALID_EMAIL = "Invalid Email";
	private static String INVALID_ROLE = "Invalid Role ";

	@Autowired
	private IUserDAO userDAO;

	@Override
	public HashMap<String, String> addUser(InputStream file) throws IOException {
		int totalRows=0, errorCount=0, i=1;
		String filename = "";

		List roles = userDAO.getAllRoles();
		List<String> presentRoles = new ArrayList<>(roles.size());
		for (Object object : roles) {
			presentRoles.add(object != null ? object.toString() : null);
		}

		CsvMapper mapper = new CsvMapper();
		mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
		MappingIterator<String[]> it = mapper.readerFor(String[].class).readValues(file);
		it.next();

		List<UserError> errors = new ArrayList<>();
		while (it.hasNext()) {
			totalRows++;
			String[] row = it.next();
			User user = new User();
			user.setEmail(row[0]);
			user.setName(row[1]);
			user.setRole(row[2]);
			String error = validateParameters(user, presentRoles);
			if (error.equals("")) {
				try {
					userDAO.addUser(user);
				} catch (DataIntegrityViolationException e) {
					UserError userError = new UserError();
					userError.setError(USER_ALREADY_EXISTS);
					userError.setUser(user);
					errors.add(userError);
					errorCount++;
				}
			} else {
				UserError userError = new UserError();
				userError.setError(error);
				userError.setUser(user);
				errors.add(userError);
				errorCount++;
			}
		}

		if (errors.size() != 0) {
			filename =  createErrorCsvFile(errors);
		}

		HashMap<String, String> map = new LinkedHashMap<>();
		map.put("no_of_rows_parsed", String.valueOf(totalRows));
		map.put("no_of_rows_failed", String.valueOf(errorCount));
		map.put("error_file_url",filename);
		return map;
	}

	/**
	 * Private method to create error csv file with the list of error details
	 * @param users contains list of user and errors details
	 * @return filename of error csv file
	 */
	private String createErrorCsvFile(List<UserError> users) {
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
		String strDate = dateFormat.format(date);

		String filename = "errors/error_"+ strDate;
		File createFile = new File(filename);
		try {
			FileWriter outputFile = new FileWriter(createFile);
			CSVWriter writer = new CSVWriter(outputFile);
			String[] header = {"Email", "Name", "Role", "Error"};
			writer.writeNext(header);
			for (UserError userError: users) {
				String[] error = { userError.getUser().getEmail(), userError.getUser().getName(),
						userError.getUser().getRole(), userError.getError() };
				writer.writeNext(error);
			}
			writer.close();

		}
		 catch (IOException e) {
			e.printStackTrace();
		}
		return createFile.getName();
	}

	/**
	 * Method to validate user rows with correct roles, valid email etc
	 * @param user includes user information
	 * @param roles with list of roles exist
	 * @return error in the row if any
	 */
	private static String validateParameters(User user, List<String> roles)
	{
		List<String> errors = new ArrayList<>();
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
				"[a-zA-Z0-9_+&*-]+)*@" +
				"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
				"A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (!(user.getEmail() == null || pat.matcher(user.getEmail()).matches())) {
			errors.add("Invalid Email");
		}
		String[] userRoles = user.getRole().split("#");
		roles.replaceAll(String::toLowerCase);

		for (String role: userRoles) {
			String modifiedRole = role.trim().toLowerCase();
			if (!roles.contains(modifiedRole)) {
				errors.add("Invalid Role " + role);
			}
		}

		if (errors.size() == 0) {
			return "";
		} else {
			return String.join("#", errors);
		}
	}
}
