package com.merit.myapplication.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.merit.myapplication.R;
import com.merit.myapplication.instagram.ApplicationData;
import com.merit.myapplication.instagram.DialogLoginInstagram;
import com.merit.myapplication.instagram.InstagramApp;

import java.util.HashMap;

/**
 * Created by merit on 7/11/2015.
 */
public class FragmentLogin extends Fragment {
    RelativeLayout btnLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // init instagramApp


        InstagramApp.instagramApp = new InstagramApp(FragmentLogin.this.getActivity(), ApplicationData.CLIENT_ID, ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        InstagramApp.instagramApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                // if getting token is success open Main ACtivity
                openMainActivity();
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(FragmentLogin.this.getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });

        initialize(v);

        // if instagramApp has token before, just start Main Activity
        if (InstagramApp.instagramApp.hasAccessToken()) {
            openMainActivity();
        }

        return v;
    }

    private void initialize(View v) {
        btnLogin = (RelativeLayout) v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectOrDisconnectUser();
            }
        });
    }

    private void connectOrDisconnectUser() {
        if (InstagramApp.instagramApp.hasAccessToken()) {
            openMainActivity();
        } else {
            // if instagramApp didn't have token before, start dialog login
            InstagramApp.instagramApp.authorize();
        }
    }

    private void openMainActivity() {
        Intent i = new Intent(FragmentLogin.this.getActivity(), MainActivity.class);
        startActivity(i);
        FragmentLogin.this.getActivity().finish();
    }
}
