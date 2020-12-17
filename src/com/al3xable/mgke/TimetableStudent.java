package com.al3xable.mgke;

import com.al3xable.mgke.util.TypeUtil;
import com.bronydell.arch.Day;
import com.bronydell.arch.Freaky;
import com.bronydell.arch.Group;
import com.bronydell.arch.Lesson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TimetableStudent {

    private final static int HEADER_PARAM = 0;
    private final static int GROUP_PARAM = 1;
    private static final int COLUMN_NAME_PARAM = 2;
    private static final String TR_PARAM = "tr";
    private static final String TD_PARAM = "td";


    private static TimetableStudent _instance = null;
    private final String _url_kn = "http://mgke.minsk.edu.by/ru/main.aspx?guid=3841";
    private final String _url_kaz = "http://mgke.minsk.edu.by/ru/main.aspx?guid=3831";
    private Day day;
    private String _date = "";

    public TimetableStudent() {
        //update();
    }

    public static TimetableStudent getInstance() {
        if (_instance == null)
            _instance = new TimetableStudent();

        return _instance;
    }

    /**
     * Check Updates
     *
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
                Freaky.generateJSON(day.getDate() + "-student", day);
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
     *
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
                    Freaky.downloadPage(date1 + "_kaz", document1);
                    Freaky.downloadPage(date2 + "_kn", document2);

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
     *
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

                List<List<Element>> splitTables = new ArrayList<>();
                List<Element> table = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    if (elements.get(i).select("td").size() == 1 && i != 0) {
                        splitTables.add(table);
                        table = new ArrayList<>();
                    }
                    table.add(elements.get(i));
                }

                if (!table.isEmpty()) {
                    splitTables.add(table);
                    table = null;
                }

                for (List<Element> tbl : splitTables) {
                    if (tbl.size() < 3) continue;
                    List<Group> groupList = new ArrayList<>();
                    for (int i = 3; i < tbl.size(); i++) {
                        Element header = tbl.get(HEADER_PARAM);
                        Element groups = tbl.get(GROUP_PARAM);
                        Element columnName = tbl.get(COLUMN_NAME_PARAM);

                        int num = 1;
                        Iterator<Element> tblIter = tbl.get(i).select(TD_PARAM).iterator();
                        while (tblIter.hasNext()) {

                            String lessonName = tblIter.next().text();
                            if (TypeUtil.isInteger(lessonName.trim()) || !tblIter.hasNext())
                                continue;
                            String audience = tblIter.next().text();

                            Lesson lesson = new Lesson();
                            lesson.setNumber(num);
                            lesson.setLesson(lessonName);
                            lesson.setAudience(audience);

                            Group gr = null;
                            if (groupList.size() >= num) {
                                gr = groupList.get(num - 1);
                                gr.addLesson(lesson);
                            } else {
                                gr = new Group();
                                gr.setTitle(groups.select(TD_PARAM).get(num).text());
                                gr.addLesson(lesson);
                                groupList.add(gr);
                            }
                            num++;
                        }

                    }
                    day.getGroups().addAll(groupList);
                }

                System.out.println();
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
