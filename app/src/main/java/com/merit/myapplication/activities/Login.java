package com.merit.myapplication.activities;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.merit.myapplication.R;

/**
 * Created by merit on 7/10/2015.
 */
public class Login extends FragmentActivity {
    TextView btnSignUpFragment, btnLoginFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initialize();
    }

    private void initialize() {
        // init fragment Signup
        final FragmentSignup fragmentSignup = new FragmentSignup();
        final FragmentLogin fragmentLogin = new FragmentLogin();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentLogin, fragmentLogin);
        fragmentTransaction.commit();

        // init btn
        btnSignUpFragment = (TextView) findViewById(R.id.btnSignUpFragment);
        btnLoginFragment = (TextView) findViewById(R.id.btnLoginFragment);

        btnLoginFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoginFragment.setTextColor(Color.parseColor("#ffffff"));
                btnSignUpFragment.setTextColor(Color.parseColor("#b5b6b8"));

                // change fragment
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLogin, fragmentLogin);
                fragmentTransaction.commit();
            }
        });

        btnSignUpFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set color
                btnSignUpFragment.setTextColor(Color.parseColor("#ffffff"));
                btnLoginFragment.setTextColor(Color.parseColor("#b5b6b8"));

                // change fragment
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentLogin, fragmentSignup);
                fragmentTransaction.commit();
            }
        });
    }
}
