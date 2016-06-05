package com.al3xable.mgke;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bronydell.arch.Day;
import com.bronydell.arch.Freaky;
import com.bronydell.arch.Group;
import com.bronydell.arch.Lesson;

public class TimetableTeacher {
	private static TimetableTeacher _instance = null;
	
	public static TimetableTeacher getInstance() {
		if (_instance == null)
			_instance = new TimetableTeacher();
		
		return _instance;
	}
	
	private String _url = "http://mgke.minsk.edu.by/ru/main.aspx?guid=3821";
    private Day day;
    private String _date = "";
    
    public TimetableTeacher() {
    	//update();
    }
    
    
    /**
     * Check Updates
     * @return success state
     */
    public boolean update() {
    	try {
    		day = new Day();
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
        	System.err.println("No content");
        } else {
            Element tbody = content.select("tbody").first();
            Elements elements = tbody.select("tr");
            
            // Save new date
            String date = content.select("thead").first().select("tr").first().select("td").get(2).text();
            isNew = !_date.equals(date);
            _date = date;
            

            ArrayList<Group> teachers = new ArrayList<>();

            for (int k = 0; k < elements.size(); k++) {
                Elements tds = elements.get(k).select("td"); //Constant
                int num = 1;
                ArrayList<Lesson> pairs = new ArrayList<Lesson>();
                Group teacher = new Group();
                for (int i = 2; i < tds.size(); i = i + 2)
                    if (tds.get(1).text().replace("\u00A0", "")
                            .length() > 0) {
                    	
                        
                        if (tds.get(i).text().length() > 1
                                && i + 1 < tds.size()) {
                        	Lesson lesson = new Lesson();
                        	lesson.setNumber(num);
                        	lesson.setLesson(tds.get(i).text());
                        	lesson.setAudience(tds.get(i + 1).text());
                            pairs.add(lesson);
                        }

                        num++;


                    }
                teacher.setTitle(tds.get(1).text());
                teacher.setLessons(pairs);
                teachers.add(teacher);
            }
            day.setGroups(teachers);
            day.pickADate(_date);
            Freaky.generateJSON(day.getDate()+"-techer", day);
            Freaky.generateJSON("current-techer", day);
        }
        return isNew;
    }
    
    public String getDate() {
    	return _date;
    }
}
