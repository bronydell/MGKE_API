package com.al3xable.mgke;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bronydell.arch.Day;
import com.bronydell.arch.Freaky;
import com.bronydell.arch.Group;
import com.bronydell.arch.Lesson;

public class TimetableStudent {
	private static TimetableStudent _instance = null;
	
	public static TimetableStudent getInstance() {
		if (_instance == null)
			_instance = new TimetableStudent();
		
		return _instance;
	}
	
	private String _url_kn = "http://mgke.minsk.edu.by/ru/main.aspx?guid=3841";
    private String _url_kaz = "http://mgke.minsk.edu.by/ru/main.aspx?guid=3831";
    private Day day;
    private String _date = "";
    
    
    
    public TimetableStudent() {
    	//update();
    }
    
    /**
     * Check Updates
     * @return success state
     */
    public boolean update() {
    	
    	try {
    		Document document1 = Jsoup.connect(_url_kaz).timeout(10000).get();
        	Document document2 = Jsoup.connect(_url_kn).timeout(10000).get();
    		if (_check(document1, document2)) {

    	        
	    		// Clearing
    			day = new Day();
    			ArrayList<Group> GroupList = new ArrayList<Group>();
    			day.setGroups(GroupList);
				_parse(document1);
				_parse(document2);
				
				//Why?
				//Collections.sort(GroupList); // Groups sort
		        Freaky.generateJSON(day.getDate()+"-student", day);
		        Freaky.generateJSON("current-student", day);
				return true;
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return false;
    }
    
    /**
     * Check Content
     * @return success state
     */
    private boolean _check(Document document1, Document document2) throws IOException {
        if (document1 != null && document2 != null) {
            Element content1 = document1.getElementsByClass("content").first();
            Element content2 = document2.getElementsByClass("content").first();
            
	        if (content1 != null && content2 != null) {
	        	String date1 = content1.select("table").get(0).select("tr").get(0).text();
	        	String date2 = content2.select("table").get(0).select("tr").get(0).text();
	        	try {
					Freaky.downloadPage(date1+"_kaz", document1);
					Freaky.downloadPage(date2+"_kn", document2);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
	            return ((date1.contains(date2) || date2.contains(date1)) && !(_date.equals(date1) || _date.equals(date2)));
	        }
        }
        
		return false;
    }
    
    /**
     * Student parser
     * @param document
     * @throws IOException
     */
    private void _parse(Document document) throws IOException {   
    	Element content = null;
        if (document != null)
            content = document.getElementsByClass("content").first();
        
        if (content == null) {
            System.err.println("Ошибка. Контент не найден");
        } else {
            Elements tables = content.select("table");
            _date = tables.get(0).select("tr").get(0).text();
            day.pickADate(_date);
            for (int k = 1; k < tables.size() + 1; k++) {
                Element e = tables.get(k - 1);
                Elements elements = e.select("tr");
                int groupcount = 0;
                Elements namelab = elements.get(0).select("td"); // Constant
                boolean dated = false;
                
                if (elements.get(0).select("td").size() <= 1) {
                    namelab = elements.get(1).select("td");
                    dated = true;
                }

                // Parse group pairs                
                for (int i = 1; i < namelab.size(); i++) {
                    if (namelab.get(i).text().replace("\u00A0", "").length() > 0) {
                        // Group adding

                        groupcount++;
                        ArrayList<Lesson> pairs = new ArrayList<Lesson>();
                        int position = groupcount * 2 - 1;
                        int num = 1;
                        int j = 2;
                        if (dated)
                            j = 3;

                        for (; j < elements.size(); j++) {
                            Elements tds = elements.get(j).select("td");

                            if (tds.get(position).text().length() > 1
                                    && position + 1 < tds.size()) {
                            	Lesson lesson = new Lesson();
                            	lesson.setNumber(num);
                            	lesson.setLesson(tds.get(position).text());
                            	lesson.setAudience(tds.get(position + 1).text());
                            	pairs.add(lesson);
                            }
                            
                            tds = null;
                            num++;
                            
                        }
                        
                        String group = namelab.get(i).text();
                        Group groupobj = new Group();
                        groupobj.setTitle(group);
                        groupobj.setLessons(pairs);
                        day.getGroups().add(groupobj);
                        pairs = null;
                    }
                }
            }
        }
    }
    
    public boolean existGroup(String group) {
    	for (Group g : day.getGroups()) if (g.getTitle().equals(group)) return true;
    	return false;
    }
    
    public Group getGroupbyName(String group) {
    	for (Group g : day.getGroups()) if (g.getTitle().equals(group)) return g;
    	return null;
    }
    
    public ArrayList<Group> getGroups() {
    	return day.getGroups();
    }
    
    public String getDate() {
    	return _date;
    }
}
