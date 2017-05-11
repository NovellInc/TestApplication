package com.test.application.testapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Экран детальной информации.
 */
public class DetailsActivity extends AppCompatActivity {

    private ArrayList<QueryResult> favoritesList = new ArrayList<>();
    private String link;
    private QueryResult queryResult;

    public static final String FAVORITES = "favorites_list";

    SharedPreferences Favorites;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("testApp", "Инициализация DetailsActivity.");
        setContentView(R.layout.details_activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);

        link = getIntent().getExtras().getString("link");
        queryResult = JsonHandler.FromJson(link);

        Favorites = getSharedPreferences(FAVORITES, this.MODE_PRIVATE);
        Collection<?> favoritesCollection = Favorites.getAll().values();
        for (Object item: favoritesCollection) {
            try {
                favoritesList.add(JsonHandler.FromJson((String) item));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        WebView webView = (WebView)findViewById(R.id.wvPage);
        webView.loadUrl(queryResult.getLink());
    }

    private MenuItem favorites;
    private MenuItem favoritesAdd;
    private MenuItem favoritesDelete;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);

        favoritesAdd = menu.findItem(R.id.mi_add_to_favorites);
        favorites = menu.findItem(R.id.mi_favorites);
        favoritesDelete = menu.findItem(R.id.mi_delete_from_favorites);
        favoritesAdd.setEnabled(true);

        if (favoritesList.size() != 0){
            favorites.setTitle(getString(R.string.mi_favorites) + "(" + favoritesList.size() + ")");
            favorites.setEnabled(true);
            favoritesDelete.setEnabled(true);
        } else {
            favorites.setEnabled(false);
            favoritesDelete.setEnabled(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.mi_add_to_favorites){
            if (favoritesList.size() == 0 || !favoritesList.contains(queryResult)){
                Editor editor = Favorites.edit();
                editor.putString(this.queryResult.getLink(), JsonHandler.ToJson(queryResult));
                editor.apply();
                favoritesList.add(queryResult);
                favorites.setTitle(getString(R.string.mi_favorites) + "(" + favoritesList.size() + ")");
            }
        } else
        if (id == R.id.mi_delete_from_favorites){
            int queryResultFoundIndex = favoritesList.indexOf(queryResult);
            if (queryResultFoundIndex >= 0){
                Editor editor = Favorites.edit();
                editor.remove(this.queryResult.getLink());
                editor.apply();
                favoritesList.remove(queryResultFoundIndex);
                favorites.setTitle(getString(R.string.mi_favorites) + "(" + favoritesList.size() + ")");
            }
        } else
        if (id == R.id.mi_favorites){
            Intent intent = new Intent(DetailsActivity.this, FavoritesActivity.class);
            startActivity(intent);
        } else
        if (id == R.id.mi_clear_favorites){
            Editor editor = Favorites.edit();
            editor.clear();
            editor.apply();
            favoritesList.clear();
            favorites.setTitle(getString(R.string.mi_favorites) + "(" + favoritesList.size() + ")");
        }

        if (favoritesList.size() == 0 ){
            favorites.setEnabled(false);
            favoritesDelete.setEnabled(false);
        } else {
            favorites.setEnabled(true);
            favoritesDelete.setEnabled(true);
        }

        return super.onOptionsItemSelected(item);
    }
}
