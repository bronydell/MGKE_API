package com.bronydell.arch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

public class Freaky {

	static public void downloadPage(String filename, Document doc) throws Exception {

		final File f = new File("history/" + filename + ".html");
		FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
	}

	static public void generateJSON(String filename, Day day) {
		Gson gson = new Gson();

		try {
			try (PrintWriter out = new PrintWriter("history/" + filename + ".json")) {
				out.println(gson.toJson(day));
			}
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
