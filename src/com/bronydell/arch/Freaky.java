package com.bronydell.arch;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;

public class Freaky {
	
	static public void downloadPage(String filename, Document doc) throws Exception {

        final File f = new File("history/"+filename+".html");
        FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
    }
	
	static public void generateJSON(String filename, Day day)
	{
		Gson gson = new Gson();
		
        try {
        	final File f = new File("current.json");
			FileUtils.writeStringToFile(f, gson.toJson(day), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
