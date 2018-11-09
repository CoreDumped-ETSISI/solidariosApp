package com.example.cesaramnuelgarcia.solidarios.Activities;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cesaramnuelgarcia.solidarios.R;

public class MainScreen extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    private Toolbar toolbar;

    private Fragment actualFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        setupToolbar();
        setupDrawer();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        this.drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (this.drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * Setup ToolBar Activity Main
     */
    private void setupToolbar() {
        this.toolbar = findViewById(R.id.toolbar_ActMainScreen);

        setSupportActionBar(this.toolbar);

    }

    /**
     * Setup Drawer Activity Main
     */
    private void setupDrawer() {
        this.mDrawerLayout = findViewById(R.id.drawerLayout_ActMainScreen);
        this.nvDrawer = findViewById(R.id.navDrawer_ActMainScreen);

        //Setup Navigation ClickListener
        this.nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return false;
            }
        });

        //Setup Navigation is open/close
        this.drawerToggle = new ActionBarDrawerToggle(this, this.mDrawerLayout, this.toolbar, R.string.drawerOpen, R.string.drawerClose);
        this.mDrawerLayout.addDrawerListener(this.drawerToggle);
        this.drawerToggle.setDrawerIndicatorEnabled(true);
        this.drawerToggle.syncState();

    }



    /**
     * Management of drawer menuItem selected
     */
    private void selectDrawerItem(MenuItem menuItem) {

        if(menuItem.getTitle().toString().compareTo(getString(R.string.help))==0) {
            int size = nvDrawer.getMenu().size();
            for (int i = 0; i < size; i++) {
                nvDrawer.getMenu().getItem(i).setChecked(false);
            }
            menuItem.setChecked(true);
            initFragmentHelp();
            this.mDrawerLayout.closeDrawer(Gravity.START, true);

        } else if(menuItem.getTitle().toString().compareTo(getString(R.string.event))==0) {
            int size = nvDrawer.getMenu().size();
            for (int i = 0; i < size; i++) {
                nvDrawer.getMenu().getItem(i).setChecked(false);
            }
            menuItem.setChecked(true);
            initFragmentEvent();
            this.mDrawerLayout.closeDrawer(Gravity.START, true);

        } else if(menuItem.getTitle().toString().compareTo(getString(R.string.disconnect))==0) {
            this.logOut();
            this.mDrawerLayout.closeDrawer(Gravity.START, true);
        }

        Log.d("selectDrawerItem", menuItem.getTitle().toString());
    }


    private void initFragmentHelp() {
        Toast.makeText(this, "Go to Help", Toast.LENGTH_SHORT).show();
        /*
        this.actualFragment = Fragment.instantiate(this, "");
        getFragmentManager().beginTransaction().replace(R.id.content_frame, this.actualFragment).commitAllowingStateLoss();
        */
    }

    private void initFragmentEvent() {
        Toast.makeText(this, "Go to Event", Toast.LENGTH_SHORT).show();
        /*
        this.actualFragment = Fragment.instantiate(this, "");
        getFragmentManager().beginTransaction().replace(R.id.content_frame, this.actualFragment).commitAllowingStateLoss();
        */
    }

    private void logOut() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("loggedUser", null);
        editor.apply();

        onBackPressed();

    }
}
