package com.example.cesaramnuelgarcia.solidarios;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.cesaramnuelgarcia.solidarios.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    protected Activity activity;
    protected JSONArray users;

    public ListAdapter(Activity activity, JSONArray users) {
        this.activity = activity;
        this.users = users;
    }
    @Override
    public int getCount() {
        return users.length();
    }

    public void clear(){
        this.users = new JSONArray();
    }

    public void addAll(JSONArray users) {
        for (int i = 0; i < users.length(); i++) {
            try {
                this.users.put(users.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object getItem(int position) {
        try {
            return users.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.user_list_item, null);
        }

        TextView name = v.findViewById(R.id.userName);
        RatingBar ratingBar = v.findViewById(R.id.userValoration);
        try {
            JSONObject dir = (JSONObject) users.get(position);
            name.setText(dir.getString("name").concat(" ").concat(dir.getString("surname")));
            ratingBar.setNumStars(5);
            ratingBar.setRating(dir.getInt("rating"));
        } catch (JSONException e) {
            e.printStackTrace();
        }




        return v;
    }
}
