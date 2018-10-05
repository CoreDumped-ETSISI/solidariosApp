package com.example.cesaramnuelgarcia.solidarios.NeederFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cesaramnuelgarcia.solidarios.AppSingleton;
import com.example.cesaramnuelgarcia.solidarios.R;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Arrays;

public class NewsFragment extends Fragment {

    private ListView newsList;
    private String news;
    private JSONArray jsonNews;
    private String[] newsTitles;
    private ArrayList<String> titlesList;
    private ArrayAdapter<String> newsListAdapter;

    public NewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newsList = getView().findViewById(R.id.newsList);
        titlesList = new ArrayList<String>();

        getNewsFromServer();

        try {
            jsonNews = new JSONArray(news);
            newsTitles = getNewsTitles();
            titlesList.addAll(Arrays.asList(newsTitles));
            newsListAdapter = new ArrayAdapter<String>(getContext(), R.layout.news_list_item, titlesList);
            newsList.setAdapter(newsListAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    private void getNewsFromServer() {
        String url = R.string.baseURL + "/news/";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                news = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error on connection", Toast.LENGTH_LONG).show();
            }
        });
        AppSingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private String[] getNewsTitles() throws JSONException {
        String[] newsTitles = new String[100];
        for(int i = 0; i < jsonNews.length(); i ++) {
            newsTitles[i] = jsonNews.getJSONObject(i).getString("title");
        }
        return newsTitles;
    }
}
