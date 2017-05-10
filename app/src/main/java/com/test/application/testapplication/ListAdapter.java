package com.test.application.testapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Класс адаптер для списка результатов запроса.
 */
public class ListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<QueryResult> queryResults;

    public ListAdapter(Context ctx, ArrayList<QueryResult> queryResults) {
        Log.d("testApp", "Инициализация адаптера.");
        this.ctx = ctx;
        this.lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.queryResults = queryResults;
    }

    @Override
    public int getCount() {
        return queryResults.size();
    }

    @Override
    public Object getItem(int position) {
        return queryResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_item, parent, false);
        }

        QueryResult queryResult = getQueryResult(position);
        ((TextView)view.findViewById(R.id.tvTitle)).setText(queryResult.getTitle());
        ((TextView)view.findViewById(R.id.tvAnswersCount)).setText(Integer.toString(queryResult.getAnswersCount()));
        ((TextView)view.findViewById(R.id.tvAddedTime)).setText(queryResult.getAddedTime());
        return view;
    }

    private QueryResult getQueryResult(int position) {
        return (QueryResult)getItem(position);
    }
}
