package com.example.cesaramnuelgarcia.solidarios.Activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cesaramnuelgarcia.solidarios.AppSingleton;
import com.example.cesaramnuelgarcia.solidarios.NeederFragments.ChangeProfileFragment;
import com.example.cesaramnuelgarcia.solidarios.NeederFragments.NewsFragment;
import com.example.cesaramnuelgarcia.solidarios.R;
import com.example.cesaramnuelgarcia.solidarios.VolunteerFragments.HomeOptionsFragment;
import com.example.cesaramnuelgarcia.solidarios.VolunteerFragments.OtherOptionsFragment;
import com.example.cesaramnuelgarcia.solidarios.VolunteerFragments.StreetOptionsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainNeederActivity extends AppCompatActivity {

    private Button toSignedUserList;
    private String[] sidebarOptions;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ListView eventsList;
    private String events;
    private JSONArray jsonEvents;
    private String[] eventsTitles;
    private JSONObject user;
    private ArrayList<String> titlesList;
    private ArrayAdapter<String> eventListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_needer);

        sidebarOptions = getResources().getStringArray(R.array.needer_sidebar_options);
        mDrawerLayout = findViewById(R.id.needer_drawer_layout);
        mDrawerList = findViewById(R.id.needer_drawer_list);
        eventsList = findViewById(R.id.eventNeederList);
        titlesList = new ArrayList<String>();

        getEventsFromServer();

        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, sidebarOptions));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.event_list_item, eventsTitles));


        try {
            user = new JSONObject(getIntent().getExtras().getString("user"));
            eventsTitles = getEventsTitles();
            titlesList.addAll(Arrays.asList(eventsTitles));
            eventListAdapter = new ArrayAdapter<String>(this, R.layout.event_list_item, titlesList);
            eventsList.setAdapter(eventListAdapter);

        } catch (JSONException e){
            e.printStackTrace();
        }

        this.toSignedUserList = findViewById(R.id.toSignedUserList);

        this.toSignedUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignedUserList = new Intent(getApplicationContext(), WaitingList.class);
                startActivity(toSignedUserList);
            }
        });
    }

    private String[] getEventsTitles() throws JSONException {
        String[] newsTitles = new String[100];
        for(int i = 0; i < jsonEvents.length(); i ++) {
            newsTitles[i] = jsonEvents.getJSONObject(i).getString("title");
        }
        return newsTitles;
    }

    private void getEventsFromServer() {
        String url = R.string.baseURL + "/events/";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                events = response;

                try{
                    jsonEvents = new JSONArray(events);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), R.string.fail_events_server, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.error_connection, Toast.LENGTH_LONG).show();
            }
        });
        AppSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void setFragment (int id) {
        android.support.v4.app.FragmentManager fragmentManager;
        android.support.v4.app.FragmentTransaction fragmentTransaction;
        switch (id) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                NewsFragment newsFragment = new NewsFragment();
                fragmentTransaction.replace(R.id.fragment, newsFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                HomeOptionsFragment homeOptionsFragment = new HomeOptionsFragment();
                fragmentTransaction.replace(R.id.fragment, homeOptionsFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                StreetOptionsFragment streetOptionsFragment = new StreetOptionsFragment();
                fragmentTransaction.replace(R.id.fragment, streetOptionsFragment);
                fragmentTransaction.commit();
                break;
            case 3:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                OtherOptionsFragment otherOptionsFragment = new OtherOptionsFragment();
                fragmentTransaction.replace(R.id.fragment, otherOptionsFragment);
                fragmentTransaction.commit();
                break;
            case 4:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("user", this.user.toString());
                ChangeProfileFragment changeProfileFragment = new ChangeProfileFragment();
                changeProfileFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment, changeProfileFragment);
                fragmentTransaction.commit();
                break;

        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem (int position) {
            setFragment(position);
            getActionBar().setTitle(sidebarOptions[position]);
        }
    }

}
