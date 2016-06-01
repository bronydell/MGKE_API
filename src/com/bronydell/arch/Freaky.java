package com.bronydell.arch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

public class Freaky {
	
	static public void downloadPage(String filename, Document doc) throws Exception {

        final File f = new File("history/"+filename+".html");
        FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
    }
	
	static public void generateJSON(String filename, Day day)
	{
		Gson gson = new Gson();
		try {
			gson.toJson(day, new FileWriter("history/"+filename+".json"));
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
