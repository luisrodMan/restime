package com.galacticflake.restime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResTimeUtil {

	public static String read(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String responseLine = null;
		StringBuilder buffer = new StringBuilder();
		while ((responseLine=reader.readLine()) != null)
			buffer.append(responseLine).append(System.lineSeparator());
		return buffer.toString();
	}
	
}
