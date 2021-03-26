package com.github.partymakers.partymaker.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.github.partymakers.partymaker.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginSuccessful(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}