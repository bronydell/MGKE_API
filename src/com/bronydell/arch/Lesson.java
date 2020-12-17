package com.bronydell.arch;

public class Lesson {
    private String lesson;
    private String audience;
    private int number;

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String clas) {
        this.audience = clas;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "\n lesson='" + lesson + '\'' +
                ", \n audience='" + audience + '\'' +
                ", number=" + number +
                "}\n";
    }
}
