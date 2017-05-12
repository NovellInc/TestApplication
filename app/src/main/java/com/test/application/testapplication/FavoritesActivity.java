package com.test.application.testapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Класс окна "избранных" ссылок.
 */
public class FavoritesActivity extends AppCompatActivity {

    private ArrayList<QueryResult> favoritesList = new ArrayList<>();
    public static final String FAVORITES = "favorites_list";
    SharedPreferences Favorites;
    private JsonHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.favorites_toolbar);
        setSupportActionBar(toolbar);

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
        handler = new JsonHandler(listView);
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
        handler.UpdateView(favoritesList);

        if (favoritesList.size() != 0){
            favorites.setTitle(getString(R.string.mi_favorites) + "(" + favoritesList.size() + ")");
            favorites.setEnabled(true);
        } else {
            favorites.setTitle(getString(R.string.mi_favorites) + "(" + favoritesList.size() + ")");
            favorites.setEnabled(false);
        }
    }

    /**
     * Обрабатывает событие выбора элемента меню.
     * @param item Элемент меню.
     * @return
     */
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
            handler.UpdateView(favoritesList);
        }

        return super.onOptionsItemSelected(item);
    }

}
