package com.test.application.testapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
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

    private ArrayList<QueryResult> favoritesList = new ArrayList<>();
    public static final String FAVORITES = "favorites_list";
    SharedPreferences Favorites;
    private JsonHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("testApp", "Инициализация ListActivity.");
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SearchView searchView = (SearchView) findViewById(R.id.svQuery);
        final ListView listView = (ListView)findViewById(R.id.lvResultList);
        handler = new JsonHandler(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QueryResult queryResult = (QueryResult) parent.getItemAtPosition(position);
                Intent intent = new Intent(ListActivity.this, DetailsActivity.class);
                intent.putExtra("link", JsonHandler.ToJson(queryResult));
                startActivity(intent);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    URL url = new URL("https://api.stackexchange.com//2.2/search/advanced?order=desc&sort=activity&body="+query+"&title="+query+"&site=stackoverflow");
                    handler.execute(url);
                } catch (Exception e) {
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
    protected void onResume() {
        super.onResume();
        this.UpdateMenuItems();
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
        favoritesAdd.setEnabled(false);
        favoritesDelete.setEnabled(false);

        this.UpdateMenuItems();

        return true;
    }

    /**
     * Обновление полей меню.
     */
    private void UpdateMenuItems(){
        if (favorites == null){
            return;
        }

        favoritesList.clear();
        Favorites = getSharedPreferences(FAVORITES, this.MODE_PRIVATE);
        for (Object item: Favorites.getAll().values()) {
            try {
                favoritesList.add(JsonHandler.FromJson((String) item));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }

        if (favoritesList.size() != 0){
            favorites.setTitle(getString(R.string.mi_favorites) + "(" + favoritesList.size() + ")");
            favorites.setEnabled(true);
        } else {
            favorites.setTitle(getString(R.string.mi_favorites) + "(" + favoritesList.size() + ")");
            favorites.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.mi_favorites){
            Intent intent = new Intent(ListActivity.this, FavoritesActivity.class);
            startActivity(intent);
        } else
        if (id == R.id.mi_clear_favorites){
            SharedPreferences.Editor editor = Favorites.edit();
            editor.clear();
            editor.apply();
            favoritesList.clear();
            favorites.setTitle(getString(R.string.mi_favorites) + "(" + favoritesList.size() + ")");
            favorites.setEnabled(false);
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        ArrayList<CharSequence> bundleFavorites = savedInstanceState.getCharSequenceArrayList(FAVORITES);
//        for (CharSequence s : bundleFavorites){
//            favoritesList.add(handler.FromJson(s.toString()));
//        }
//        handler.UpdateView(favoritesList);
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        ArrayList<CharSequence> bundleFavorites = new ArrayList<>();
//        for (QueryResult result : favoritesList){
//            bundleFavorites.add(handler.ToJson(result));
//        }
//
//        outState.putCharSequenceArrayList(FAVORITES, bundleFavorites);
//    }
}
