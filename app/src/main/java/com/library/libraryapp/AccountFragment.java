package com.library.libraryapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment {

    public AccountFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        TextView accountEmail = rootView.findViewById(R.id.accountEmail);
        Button logoutButton = rootView.findViewById(R.id.logoutButton);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            accountEmail.setText(currentUser.getEmail());
        }

        logoutButton.setOnClickListener(v -> {
            requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("isLoggedIn", false)
                    .apply();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

            requireActivity().finish();
        });

        return rootView;
    }
}
