package com.test.application.testapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("testApp", "Инициализация ListActivity.");
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });

        // TODO : Запилить запрос к stackoverflow
        SearchView searchView = (SearchView) findViewById(R.id.svQuery);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    URL url = new URL("https://api.stackexchange.com//2.2/search/advanced?order=desc&sort=activity&body="+query+"&title="+query+"&site=stackoverflow");
                    JSONObject response = new JSONObject(ListActivity.RequestSender(url));
                    ArrayList<QueryResult> queryResults = JsonHandler.parseData(response);
                    ListAdapter listAdapter = new ListAdapter(this, queryResults);
                    ListView listView = (ListView)findViewById(R.id.lvResultList);
                    listView.setAdapter(listAdapter);
                    Log.d("testApp", "Пульнул запрос \""+query+"\".");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Отправляет запрос.
     * @param url Строка запроса.
     * @return Строка ответа.
     */
    public static String RequestSender(URL url){
        StringBuilder response = null;
        try {
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            char[] buffer = new char[256];
            int readChars;
            while ((readChars = inputStreamReader.read(buffer)) != -1){
                response.append(buffer, 0, readChars);
            }
            inputStreamReader.close();
        } catch (IOException e) {
            Snackbar.make(this.getCurrentFocus(), "Connection error", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
            Log.d("testApp", "При выполнении запроса произошла ошибка: " + e);
            return null;
        }

        return response.toString();
    }
}
