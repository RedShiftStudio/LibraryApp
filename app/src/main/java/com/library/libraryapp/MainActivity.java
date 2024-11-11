package com.library.libraryapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.library.libraryapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_ITEM_ID = "selected_item_id";
    private static final String PREFS_NAME = "UserPrefs";
    private static final String IS_LOGGED_IN = "isLoggedIn";

    private int selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isUserLoggedIn()) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt(SELECTED_ITEM_ID);
        } else {
            selectedItem = R.id.home;
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            selectedItem = item.getItemId();
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment(), "home_fragment");
            } else if (item.getItemId() == R.id.account) {
                replaceFragment(new AccountFragment(), "account_fragment");
            }
            return true;
        });

        binding.bottomNavigationView.setSelectedItemId(selectedItem);

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment(), "home_fragment");
        }
    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment, tag);
        fragmentTransaction.commit();
    }

    private boolean isUserLoggedIn() {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getBoolean(IS_LOGGED_IN, false);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID, selectedItem);
    }

}
