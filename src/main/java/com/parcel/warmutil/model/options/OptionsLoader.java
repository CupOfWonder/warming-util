package com.parcel.warmutil.model.options;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.parcel.warmutil.model.helpers.Constants.OPTIONS_FILE_NAME;

public class OptionsLoader {

	private static Logger logger = Logger.getLogger("OptionsLoader");

	@Nullable
	public static ProgramOptions loadOptions() {
		try(FileReader reader = new FileReader(OPTIONS_FILE_NAME); BufferedReader bufferedReader = new BufferedReader(reader)) {
			StringBuilder builder = new StringBuilder();
			bufferedReader.lines().forEach(builder::append);

			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(builder.toString(), ProgramOptions.class);
		} catch (FileNotFoundException e) {
			logger.log(Level.INFO, "Options file not found! Using default options");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error during options load", e);
		}
		return null;
	}
}
