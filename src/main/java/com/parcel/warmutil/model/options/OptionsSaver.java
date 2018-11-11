package com.parcel.warmutil.model.options;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.parcel.warmutil.model.helpers.Constants.OPTIONS_FILE_NAME;

public class OptionsSaver {

	private static Logger logger = Logger.getLogger("OptionsSaver");

	public static boolean saveOptions(ProgramOptions options) {
		ObjectMapper mapper = new ObjectMapper();
		try(FileWriter fileWriter = new FileWriter(OPTIONS_FILE_NAME);
			PrintWriter printWriter = new PrintWriter(fileWriter)) {

			String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(options);
			printWriter.print(jsonResult);

			return true;
		} catch (java.io.IOException e) {
			logger.log(Level.SEVERE, "Exception while saving options" , e);
			return false;
		}
	}
}
