package com.github.partymakers.partymaker.user;

import androidx.appcompat.app.AppCompatActivity;
import com.github.partymakers.partymaker.R;

import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginSuccessful(View view) {
        finish(); //login activity won't be called from the stack when user presses the back button
    }
}