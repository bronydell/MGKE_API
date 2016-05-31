package com.al3xable.mgke;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TimetableTeacher {
	private static TimetableTeacher _instance = null;
	
	public static TimetableTeacher getInstance() {
		if (_instance == null)
			_instance = new TimetableTeacher();
		
		return _instance;
	}
	
	private String _url = "http://mgke.minsk.edu.by/ru/main.aspx?guid=3821";
    
    private String _date = "";
    ArrayList<String> _teachers = new ArrayList<String>();
    HashMap<String, ArrayList<String>> _lessons = new HashMap<String, ArrayList<String>>();
    
    public TimetableTeacher() {
    	update();
    }
    
    
    /**
     * Check Updates
     * @return success state
     */
    public boolean update() {
    	try {
			return _parse(Jsoup.connect(_url).timeout(10000).get());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return false;
    }
    
    private boolean _parse(Document document) {
    	boolean isNew = false;
    	Element content = null;
    	
        if (document != null) content = document.getElementsByClass("content").first();


        if (content == null) {
        	System.err.println("О�?ибка. Контент не найден");
        } else {
            Element tbody = content.select("tbody").first();
            Elements elements = tbody.select("tr");
            
            // Save new date
            String date = content.select("thead").first().select("tr").first().select("td").get(2).text();
            isNew = !_date.equals(date);
            _date = date;
            
            // Clearing
            _teachers = new ArrayList<String>();
            _lessons = new HashMap<String, ArrayList<String>>();
            

            for (int k = 0; k < elements.size(); k++) {
                Elements tds = elements.get(k).select("td"); //Constant
                ArrayList<String> pairs = new ArrayList<String>();
                int num = 1;
                for (int i = 2; i < tds.size(); i = i + 2)
                    if (tds.get(1).text().replace("\u00A0", "")
                            .length() > 0) {


                        if (tds.get(i).text().length() > 1
                                && i + 1 < tds.size()) {
                            pairs.add(num + ". Гр�?ппа "
                                    + tds.get(i).text()
                                    + ". В кабинете "
                                    + tds.get(i + 1).text());
                        }

                        num++;


                    }
                _lessons.put(tds.get(1).text(),pairs);
                _teachers.add(tds.get(1).text());
            }
        }
        
        return isNew;
    }
    
    public boolean existTeacher(String teacher) {
    	for (String t : _teachers)
    		if (t.equals(teacher)) return true;
    	
    	return false;
    }
    
    public ArrayList<String> getTeachers() {
    	return _teachers;
    }
    
    public String getPairsList(String teacher) {
    	if (existTeacher(teacher)) {
	    	String list = "";
	    	for (String s : _lessons.get(teacher)) list += s + "\n";
	    	return list;
    	} else
    		return "Нет ра�?пи�?ания для этого преподавателя";
    }
    
    public String getDate() {
    	return _date;
    }
}
