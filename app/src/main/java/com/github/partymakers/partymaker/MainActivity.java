package com.github.partymakers.partymaker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.github.partymakers.partymaker.databinding.ActivityMainBinding;
import com.github.partymakers.partymaker.dashboard.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final private String TAG = MainActivity.class.getSimpleName();
    static final private int REQUEST_CODE_LOGIN = 1;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private ActivityMainBinding dataBinding;
    private Bundle userInfo = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = dataBinding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = dataBinding.tabs;
        tabs.setupWithViewPager(viewPager);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user == null) {
            createLoginIntent();
        } else {
            userInfo.putString("name", user.getDisplayName());
            userInfo.putString("email", user.getEmail());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        replace with switch if you're adding more cases
        if (requestCode == REQUEST_CODE_LOGIN) {
            handleLoginResult(resultCode, data);
        }
    }

    private void createLoginIntent() {
        List<AuthUI.IdpConfig> loginProviders = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(loginProviders)
                        .setTheme(R.style.LoginTheme)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                REQUEST_CODE_LOGIN
        );
    }

    // TODO: login status setter
    private void handleLoginResult(int resultCode, Intent intent) {
        IdpResponse response = IdpResponse.fromResultIntent(intent);
        if (resultCode == RESULT_OK) {
            Log.i(TAG, "Login successful");
            user = firebaseAuth.getCurrentUser();
            userInfo.putString("name", user.getDisplayName());
            userInfo.putString("email", user.getEmail());
            userInfo.putString("status", "Login successful");
        } else if (resultCode == RESULT_CANCELED || response == null) {
            Log.w(TAG, "Login cancelled");
            userInfo.putString("status", "Login cancelled");
        } else {
            String message = "Login failed";
            if (response != null) {
                int errorCode = response.getError().getErrorCode();
                message += ". Error code: " + errorCode;
            }
            Log.e(TAG, message);
            userInfo.putString("status", message);
        }
    }

    public Bundle getUserInfo() {
        return userInfo;
    }
}
