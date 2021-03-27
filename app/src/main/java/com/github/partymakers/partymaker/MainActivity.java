package com.github.partymakers.partymaker;

import androidx.appcompat.app.AppCompatActivity;

import com.github.partymakers.partymaker.user.LoginActivity;


import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(true){ // change condition: if the user is not logged in
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}