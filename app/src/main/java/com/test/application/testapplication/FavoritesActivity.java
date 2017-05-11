package com.test.application.testapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class FavoritesActivity extends AppCompatActivity {

    private ArrayList<QueryResult> favoritesList = new ArrayList<>();

    public static final String FAVORITES = "favorites_list";

    SharedPreferences Favorites;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.favorites_toolbar);
        setSupportActionBar(toolbar);

        Favorites = getSharedPreferences(FAVORITES, this.MODE_PRIVATE);
        for (Object item: Favorites.getAll().values()) {
            try {
                favoritesList.add(JsonHandler.FromJson((String) item));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        final ListView listView = (ListView)findViewById(R.id.lvResultList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QueryResult queryResult = (QueryResult) parent.getItemAtPosition(position);
                Intent intent = new Intent(FavoritesActivity.this, DetailsActivity.class);
                intent.putExtra("link", JsonHandler.ToJson(queryResult));
                startActivity(intent);
            }
        });
        JsonHandler handler = new JsonHandler(listView);
        handler.UpdateView(favoritesList);
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

        if (favoritesList.size() != 0){
            favorites.setTitle(getString(R.string.mi_favorites) + "(" + favoritesList.size() + ")");
            favorites.setEnabled(true);
        } else {
            favorites.setEnabled(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.mi_favorites){

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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
