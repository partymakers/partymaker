package com.github.partymakers.partymaker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.github.partymakers.partymaker.dashboard.SectionsPagerAdapter;
import com.github.partymakers.partymaker.databinding.ActivityMainBinding;
import com.github.partymakers.partymaker.user.UserViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final private String TAG = MainActivity.class.getSimpleName();
    static final private int REQUEST_CODE_LOGIN = 1;

    private UserViewModel userViewModel;
    private ActivityMainBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        if (!userViewModel.isLoggedIn()) {
            createLoginIntent();
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = dataBinding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = dataBinding.tabs;
        tabs.setupWithViewPager(viewPager);
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

    private void handleLoginResult(int resultCode, Intent intent) {
        IdpResponse response = IdpResponse.fromResultIntent(intent);
        userViewModel.handleAuthUiResult(resultCode, response, TAG);
    }
}
