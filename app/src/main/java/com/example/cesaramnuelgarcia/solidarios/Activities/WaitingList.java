package com.example.cesaramnuelgarcia.solidarios.Activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cesaramnuelgarcia.solidarios.AppSingleton;
import com.example.cesaramnuelgarcia.solidarios.ListAdapter;
import com.example.cesaramnuelgarcia.solidarios.R;

import org.json.JSONException;
import org.json.JSONObject;

public class WaitingList extends AppCompatActivity {

    JSONObject results;
    ListView list;
    TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_list);
        list = findViewById(R.id.userList);
        infoTextView = findViewById(R.id.infoTextBehindList);
        getVolunteersSignedUser();

        // TODO: Add list.onItemClickListener
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(results.length() > 3)
            populateList();
        else
            changeTextViewMessageToError();

    }

    private void changeTextViewMessageToError() {
        list.setVisibility(View.INVISIBLE);
        infoTextView.setText(R.string.no_users_signed);
        infoTextView.setVisibility(View.VISIBLE);
    }

    public void populateList() {
        try {
            ListAdapter adapter = new ListAdapter(this, results.getJSONArray("users"));
            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getVolunteersSignedUser() {
        String url = R.string.baseURL + "/vRequest/signedVRequests?id=";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId;
        try {
            JSONObject user = new JSONObject(prefs.getString("user", ""));
            userId = user.getString("_id");
            url.concat(userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                results = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.error_connection, Toast.LENGTH_LONG).show();
            }
        });
        AppSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
