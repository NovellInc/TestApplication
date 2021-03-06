package com.test.application.testapplication;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс представляет информацию о результате запроса.
 */
public class QueryResult {
    private String title;
    private int answersCount;
    private String addedTime;
    private String link;

    public QueryResult(String title, int answersCount, String addedDate, String link) {
        Log.d("testApp", "Конструктор экземпляра QueryResult.");
        this.title = title;
        this.answersCount = answersCount;
        this.addedTime = addedDate;
        this.link = link;
    }

    public QueryResult(String title, int answersCount, Date addedDate, String link) {
        Log.d("testApp", "Конструктор экземпляра QueryResult.");
        this.title = title;
        this.answersCount = answersCount;
        this.addedTime = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(addedDate);
        this.link = link;
    }


    public String getTitle() {
        return title;
    }

    public int getAnswersCount() {
        return answersCount;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public String getLink() { return link; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryResult that = (QueryResult) o;

        return link.equals(that.link);

    }

    @Override
    public int hashCode() {
        return link.hashCode();
    }
}
