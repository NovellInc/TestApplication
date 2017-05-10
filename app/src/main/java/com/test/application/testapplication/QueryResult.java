package com.test.application.testapplication;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс представляет информацию о результате запроса.
 */
public class QueryResult {
    private String Title;
    private int AnswersCount;
    private String AddedTime;

    public QueryResult(String title, int answersCount, Date addedDate) {
        Log.d("testApp", "Конструктор экземпляра QueryResult.");
        Title = title;
        AnswersCount = answersCount;
        AddedTime = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(addedDate);
    }

    public String getTitle() {
        return Title;
    }

    public int getAnswersCount() {
        return AnswersCount;
    }

    public String getAddedTime() {
        return AddedTime;
    }
}
