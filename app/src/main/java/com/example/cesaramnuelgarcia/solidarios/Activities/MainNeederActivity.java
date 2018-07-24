package com.example.cesaramnuelgarcia.solidarios.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cesaramnuelgarcia.solidarios.R;
import com.example.cesaramnuelgarcia.solidarios.VolunteerFragments.HomeOptionsFragment;
import com.example.cesaramnuelgarcia.solidarios.VolunteerFragments.OtherOptionsFragment;
import com.example.cesaramnuelgarcia.solidarios.VolunteerFragments.StreetOptionsFragment;

public class MainNeederActivity extends AppCompatActivity {

    private Button homeHelp;
    private Button streetHelp;
    private Button otherHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_needer);

        this.homeHelp = findViewById(R.id.homeHelp);
        this.streetHelp = findViewById(R.id.streetHelp);
        this.otherHelp = findViewById(R.id.otherHelp);


        this.homeHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(0);
            }
        });

        this.streetHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(1);
            }
        });

        this.otherHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(2);
            }
        });
    }


    private void setFragment (int id) {
        android.support.v4.app.FragmentManager fragmentManager;
        android.support.v4.app.FragmentTransaction fragmentTransaction;
        switch (id) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                HomeOptionsFragment homeOptionsFragment = new HomeOptionsFragment();
                fragmentTransaction.replace(R.id.fragment, homeOptionsFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                StreetOptionsFragment streetOptionsFragment = new StreetOptionsFragment();
                fragmentTransaction.replace(R.id.fragment, streetOptionsFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                OtherOptionsFragment otherOptionsFragment = new OtherOptionsFragment();
                fragmentTransaction.replace(R.id.fragment, otherOptionsFragment);
                fragmentTransaction.commit();
        }
    }
}
