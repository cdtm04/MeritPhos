package com.merit.myapplication.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.merit.myapplication.R;

/**
 * Created by merit on 7/10/2015.
 */
public class FragmentSignup extends Fragment {
    TextView tvAgreeTerms;
    EditText etEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        initialize(v);
        return v;
    }

    private void initialize(View v) {
        etEmail = (EditText) v.findViewById(R.id.etEmail);
        tvAgreeTerms = (TextView) v.findViewById(R.id.tvAgreeTerms);

        String strAgreeTerms = "By signing up, you agree to our<br><a href = \"https://i.instagram.com/legal/terms/\"><b>Terms</b></a> & <a href = 'https://i.instagram.com/legal/privacy/'><b>Privacy Policy</b></a>";
        tvAgreeTerms.setText(Html.fromHtml(strAgreeTerms));
        tvAgreeTerms.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
