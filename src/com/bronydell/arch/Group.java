//Author: BRONYDELL
//DATE: 31.05.2016
package com.bronydell.arch;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String title;
    private List<Lesson> lessons;

    /**
     * Getter for group title
     *
     * @return Group number
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for group title
     *
     * @param Group number
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for lessons array
     *
     * @return lessons array
     */
    public List<Lesson> getLessons() {
        return lessons;
    }

    /**
     * Setter for lessons array
     *
     * @param lessons array
     */
    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void addLesson(Lesson lesson) {
        if (this.lessons == null) {
            this.lessons = new ArrayList<>();
        }

        this.lessons.add(lesson);
    }
}
