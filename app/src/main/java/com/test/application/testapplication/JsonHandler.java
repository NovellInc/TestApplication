package com.test.application.testapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Класс для работы с JSON данными.
 */
public class JsonHandler {

    /**
     * Разбирает данные JSON.
     * @param jsonObject строка данных в формате JSON.
     * @return возвращает список разобранных данных.
     */
    public static ArrayList<QueryResult> parseData(JSONObject jsonObject){
        Log.d("testApp", "Парсинг данных.");
        ArrayList<QueryResult> list = new ArrayList<>();
        JSONArray array = null;
        try {
            array = jsonObject.getJSONArray("items");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < array.length(); i++){
            JSONObject object = null;
            try {
                object = (JSONObject)array.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (object == null){
                continue;
            }

            try {
                list.add(new QueryResult(object.getString("title"),
                                         object.getInt("answer_count"),
                                         new Date(object.getLong("creation_date"))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
