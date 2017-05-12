package com.test.application.testapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

/**
 * Класс для работы с JSON данными.
 */
public class JsonHandler extends AsyncTask<URL, Void, ArrayList<QueryResult>> {

    private ListView view;
    private ListAdapter listAdapter;

    public JsonHandler(ListView view) {
        this.view = view;
    }

    /**
     * Разбирает данные JSON.
     * @param jsonObject строка данных в формате JSON.
     * @return возвращает список разобранных данных.
     */
    public ArrayList<QueryResult> parseData(JSONObject jsonObject){
        Log.d("testApp", "Парсинг данных.");
        ArrayList<QueryResult> list = new ArrayList<>();
        JSONArray array = null;
        try {
            array = jsonObject.getJSONArray("items");
            Log.d("testApp", "Создан массив.");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < array.length(); i++){
            JSONObject object = null;
            try {
                object = (JSONObject)array.get(i);
                Log.d("testApp", "Извлечен объект.");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (object == null){
                continue;
            }

            try {
                list.add(new QueryResult(object.getString("title"),
                                         object.getInt("answer_count"),
                                         new Date(object.getLong("creation_date") * 1000),
                                         object.getString("link")));
                Log.d("testApp", "Создан результат.");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * Выполняет запрос.
     * @param params Параметры запроса.
     * @return Возвращает результат выполнения запроса.
     */
    @Override
    protected ArrayList<QueryResult> doInBackground(URL... params) {
        Log.d("testApp", "Начало выполнения запроса.");
        StringBuilder response = new StringBuilder();
        try {
            URLConnection connection = params[0].openConnection();
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            char[] buffer = new char[256];
            int readChars;
            while ((readChars = inputStreamReader.read(buffer)) != -1){
                response.append(buffer, 0, readChars);
            }
            inputStreamReader.close();
            Log.d("testApp", "Запрос выполнен.");
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

        try {
            return response == null
                    ? null
                    : this.parseData(new JSONObject(String.valueOf(response)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Пост-обработка запроса.
     * @param queryResults Результат парсинга ответа.
     */
    @Override
    protected void onPostExecute(ArrayList<QueryResult> queryResults) {
        super.onPostExecute(queryResults);

        this.UpdateView(queryResults);
    }

    public void UpdateView(ArrayList<QueryResult> queryResults){
        if (queryResults == null){
            Snackbar.make(view, "Error occurred.", Snackbar.LENGTH_LONG)
                    .show();
            return;
        }

        listAdapter = new ListAdapter(this.view.getContext(), queryResults);
        this.view.setAdapter(listAdapter);
    }

    public static QueryResult FromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, QueryResult.class);
    }

    public static String ToJson(QueryResult queryResult){
        Gson gson = new Gson();
        return gson.toJson(queryResult);
    }
}
