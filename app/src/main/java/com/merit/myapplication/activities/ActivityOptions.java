package com.merit.myapplication.activities;

import android.app.ActivityGroup;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.merit.myapplication.R;
import com.merit.myapplication.instagram.InstagramApp;
import com.merit.myapplication.loaddata.ImageLoader;

import java.util.regex.Matcher;


public class ActivityOptions extends ActivityGroup implements View.OnClickListener {
    private RelativeLayout btnPrivateAccountOption, btnSaveOriginalPhotosOption, btnSaveVideosAfterPostingOption,
            btnFacebookFriendsOption, btnFindContactsOption, btnInviteFriendsOption, btnEditProfileOption,
            btnChangePasswordOption, btnPostsYouveLikedOption, btnLinkedAccountsOption, btnPushNotificationsOption,
            btnCameraOption, btnCellularDataUseOption, btnInstagramHelpCenterOption, btnReportaProblemOption,
            btnAdsOption, btnBlogOption, btnPrivacyPolicyOption, btnTermsofServiceOption,
            btnOpenSourceLibrariesOption, btnClearSearchHistoryOption, btnLogOutOption;
    private RelativeLayout btnBack;
    private RelativeLayout IconPrivateAccountOption, IconSaveOriginalPhotosOption, IconSaveVideosAfterPostingOption;
    private boolean isPrivateAccountOptionOn, isSaveOriginalPhotosOptionOn, isSaveVideosAfterPostingOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        initialize();
    }

    // CODE btnLogOutOption EVENT
    private void btnLogOutOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnLogOutOption
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOptions.this.getParent());
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // reset token
                InstagramApp.instagramApp.resetAccessToken();
                // clear images cache
                new ImageLoader(MainActivity.mainContext).clearCache();

                // move to login activity
                Intent i = new Intent(ActivityOptions.this, Login.class);
                startActivity(i);
                ActivityOptions.this.getParent().finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //Toast.makeText(this, "btnLogOutOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnClearSearchHistoryOption EVENT
    private void btnClearSearchHistoryOptionEvent() {

        // TO DO: SOMETHING WHEN CLICK btnClearSearchHistoryOption

        Toast.makeText(this, "btnClearSearchHistoryOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnOpenSourceLibrariesOption EVENT
    private void btnOpenSourceLibrariesOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnOpenSourceLibrariesOption

        Toast.makeText(this, "btnOpenSourceLibrariesOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnTermsofServiceOption EVENT
    private void btnTermsofServiceOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnTermsofServiceOption

        Toast.makeText(this, "btnTermsofServiceOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnPrivacyPolicyOption EVENT
    private void btnPrivacyPolicyOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnPrivacyPolicyOption

        Toast.makeText(this, "btnPrivacyPolicyOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnBlogOption EVENT
    private void btnBlogOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnBlogOption

        Toast.makeText(this, "btnBlogOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnAdsOption EVENT
    private void btnAdsOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnAdsOption

        Toast.makeText(this, "btnAdsOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnInstagramHelpCenterOption EVENT
    private void btnInstagramHelpCenterOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnInstagramHelpCenterOption

        Toast.makeText(this, "btnInstagramHelpCenterOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnReportaProblemOption EVENT
    private void btnReportaProblemOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnReportaProblemOption

        Toast.makeText(this, "btnReportaProblemOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnCellularDataUseOption EVENT
    private void btnCellularDataUseOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnCellularDataUseOption

        Toast.makeText(this, "btnCellularDataUseOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnCameraOption EVENT
    private void btnCameraOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnCameraOption

        Toast.makeText(this, "btnCameraOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnPushNotificationsOption EVENT
    private void btnPushNotificationsOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnPushNotificationsOption

        Toast.makeText(this, "btnPushNotificationsOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnLinkedAccountsOption EVENT
    private void btnLinkedAccountsOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnLinkedAccountsOption

        Toast.makeText(this, "btnLinkedAccountsOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnPostsYouveLikedOption EVENT
    private void btnPostsYouveLikedOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnPostsYouveLikedOption

        Toast.makeText(this, "btnPostsYouveLikedOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnChangePasswordOption EVENT
    private void btnChangePasswordOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnChangePasswordOption

        Toast.makeText(this, "btnChangePasswordOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnEditProfileOption EVENT
    private void btnEditProfileOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnEditProfileOption

        Toast.makeText(this, "btnEditProfileOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnInviteFriendsOption EVENT
    private void btnInviteFriendsOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnInviteFriendsOption

        Toast.makeText(this, "btnInviteFriendsOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnFindContactsOption EVENT
    private void btnFindContactsOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnFindContactsOption

        Toast.makeText(this, "btnFindContactsOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnFacebookFriendsOption EVENT
    private void btnFacebookFriendsOptionEvent() {
        // TO DO: SOMETHING WHEN CLICK btnFacebookFriendsOption

        Toast.makeText(this, "btnFacebookFriendsOption is clicked", Toast.LENGTH_SHORT).show();
    }

    // CODE btnPrivateAccountOption EVENT
    private void btnPrivateAccountOptionEvent() {
        if (isPrivateAccountOptionOn) {
            // TO DO: SOMETHING WHEN TURN OFF btnPrivateAccountOption

            Toast.makeText(this, "btnPrivateAccountOption is Off", Toast.LENGTH_SHORT).show();
            isPrivateAccountOptionOn = false;
        } else {
            // TO DO: SOMETHING WHEN TURN ON btnPrivateAccountOption

            Toast.makeText(this, "btnPrivateAccountOption is On", Toast.LENGTH_SHORT).show();
            isPrivateAccountOptionOn = true;
        }
    }

    // CODE btnSaveOriginalPhotosOption EVENT
    private void btnSaveOriginalPhotosOptionEvent() {
        if (isSaveOriginalPhotosOptionOn) {
            // TO DO: SOMETHING WHEN TURN OFF btnSaveOriginalPhotosOption

            Toast.makeText(this, "btnSaveOriginalPhotosOption is Off", Toast.LENGTH_SHORT).show();
            isSaveOriginalPhotosOptionOn = false;
        } else {
            // TO DO: SOMETHING WHEN TURN ON btnSaveOriginalPhotosOption

            Toast.makeText(this, "btnSaveOriginalPhotosOption is On", Toast.LENGTH_SHORT).show();
            isSaveOriginalPhotosOptionOn = true;
        }
    }

    // CODE btnSaveVideosAfterPostingOption EVENT
    private void btnSaveVideosAfterPostingOptionEvent() {
        if (isSaveVideosAfterPostingOn) {
            // TO DO: SOMETHING WHEN TURN OFF btnSaveVideosAfterPostingOption

            Toast.makeText(this, "btnSaveVideosAfterPostingOption is Off", Toast.LENGTH_SHORT).show();
            isSaveVideosAfterPostingOn = false;
        } else {
            // TO DO: SOMETHING WHEN TURN ON btnSaveVideosAfterPostingOption

            Toast.makeText(this, "btnSaveVideosAfterPostingOption is On", Toast.LENGTH_SHORT).show();
            isSaveVideosAfterPostingOn = true;
        }
    }

    // set animation for sliding buttons
    private void setAnimationButton(View layoutSetAnimation, boolean isOn) {
        Animation AMIN_FADE = AnimationUtils.loadAnimation(this, R.anim.fadeout_fadein);
        if (isOn) {
            layoutSetAnimation.setBackground(getResources().getDrawable(R.drawable.button_slide_option_on));
        } else {
            layoutSetAnimation.setBackground(getResources().getDrawable(R.drawable.button_slide_option_off));
        }
        layoutSetAnimation.startAnimation(AMIN_FADE);
    }

    // TO DO: INIT OPTION BUTTONS
    private void initialize() {
        // init buttons, set onclick event
        btnBack = (RelativeLayout) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnPrivateAccountOption = (RelativeLayout) findViewById(R.id.btnPrivateAccountOption);
        btnPrivateAccountOption.setOnClickListener(this);

        btnSaveOriginalPhotosOption = (RelativeLayout) findViewById(R.id.btnSaveOriginalPhotosOption);
        btnSaveOriginalPhotosOption.setOnClickListener(this);

        btnSaveVideosAfterPostingOption = (RelativeLayout) findViewById(R.id.btnSaveVideosAfterPostingOption);
        btnSaveVideosAfterPostingOption.setOnClickListener(this);

        btnFacebookFriendsOption = (RelativeLayout) findViewById(R.id.btnFacebookFriendsOption);
        btnFacebookFriendsOption.setOnClickListener(this);

        btnFindContactsOption = (RelativeLayout) findViewById(R.id.btnFindContactsOption);
        btnFindContactsOption.setOnClickListener(this);

        btnInviteFriendsOption = (RelativeLayout) findViewById(R.id.btnInviteFriendsOption);
        btnInviteFriendsOption.setOnClickListener(this);

        btnEditProfileOption = (RelativeLayout) findViewById(R.id.btnEditProfileOption);
        btnEditProfileOption.setOnClickListener(this);

        btnChangePasswordOption = (RelativeLayout) findViewById(R.id.btnChangePasswordOption);
        btnChangePasswordOption.setOnClickListener(this);

        btnPostsYouveLikedOption = (RelativeLayout) findViewById(R.id.btnPostsYouveLikedOption);
        btnPostsYouveLikedOption.setOnClickListener(this);

        btnPushNotificationsOption = (RelativeLayout) findViewById(R.id.btnPushNotificationsOption);
        btnPushNotificationsOption.setOnClickListener(this);

        btnCameraOption = (RelativeLayout) findViewById(R.id.btnCameraOption);
        btnCameraOption.setOnClickListener(this);

        btnCellularDataUseOption = (RelativeLayout) findViewById(R.id.btnCellularDataUseOption);
        btnCellularDataUseOption.setOnClickListener(this);

        btnInstagramHelpCenterOption = (RelativeLayout) findViewById(R.id.btnInstagramHelpCenterOption);
        btnInstagramHelpCenterOption.setOnClickListener(this);

        btnReportaProblemOption = (RelativeLayout) findViewById(R.id.btnReportaProblemOption);
        btnReportaProblemOption.setOnClickListener(this);

        btnAdsOption = (RelativeLayout) findViewById(R.id.btnAdsOption);
        btnAdsOption.setOnClickListener(this);

        btnBlogOption = (RelativeLayout) findViewById(R.id.btnBlogOption);
        btnBlogOption.setOnClickListener(this);

        btnPrivacyPolicyOption = (RelativeLayout) findViewById(R.id.btnPrivacyPolicyOption);
        btnPrivacyPolicyOption.setOnClickListener(this);

        btnTermsofServiceOption = (RelativeLayout) findViewById(R.id.btnTermsofServiceOption);
        btnTermsofServiceOption.setOnClickListener(this);

        btnOpenSourceLibrariesOption = (RelativeLayout) findViewById(R.id.btnOpenSourceLibrariesOption);
        btnOpenSourceLibrariesOption.setOnClickListener(this);

        btnClearSearchHistoryOption = (RelativeLayout) findViewById(R.id.btnClearSearchHistoryOption);
        btnClearSearchHistoryOption.setOnClickListener(this);

        btnLogOutOption = (RelativeLayout) findViewById(R.id.btnLogOutOption);
        btnLogOutOption.setOnClickListener(this);

        btnLinkedAccountsOption = (RelativeLayout) findViewById(R.id.btnLinkedAccountsOption);
        btnLinkedAccountsOption.setOnClickListener(this);

        // init icons of sliding buttons
        IconPrivateAccountOption = (RelativeLayout) findViewById(R.id.IconPrivateAccountOption);
        IconSaveOriginalPhotosOption = (RelativeLayout) findViewById(R.id.IconSaveOriginalPhotosOption);
        IconSaveVideosAfterPostingOption = (RelativeLayout) findViewById(R.id.IconSaveVideosAfterPostingOption);

        // TO DO: GET STATE FROM DATASOURCE AND SET FIRST STATE FOR SLIDING BUTTONS
        isPrivateAccountOptionOn = false;
        isSaveOriginalPhotosOptionOn = false;
        isSaveVideosAfterPostingOn = false;
        // TO DO: GET STATE FROM DATASOURCE AND SET BACKGROUND FOR ICONS OF SLIDING BUTTONS
        IconPrivateAccountOption.setBackground(getResources().getDrawable(R.drawable.button_slide_option_off));
        IconSaveOriginalPhotosOption.setBackground(getResources().getDrawable(R.drawable.button_slide_option_off));
        IconSaveVideosAfterPostingOption.setBackground(getResources().getDrawable(R.drawable.button_slide_option_off));
    }

    // buttons events
    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            ActivityAccountGroup.groupAccountGroup.setRootView();
        } else if (v == btnPrivateAccountOption) {
            btnPrivateAccountOptionEvent();
            setAnimationButton(IconPrivateAccountOption, isPrivateAccountOptionOn);
        } else if (v == btnSaveOriginalPhotosOption) {
            btnSaveOriginalPhotosOptionEvent();
            setAnimationButton(IconSaveOriginalPhotosOption, isSaveOriginalPhotosOptionOn);
        } else if (v == btnSaveVideosAfterPostingOption) {
            btnSaveVideosAfterPostingOptionEvent();
            setAnimationButton(IconSaveVideosAfterPostingOption, isSaveVideosAfterPostingOn);
        } else if (v == btnFacebookFriendsOption) {
            btnFacebookFriendsOptionEvent();
        } else if (v == btnFindContactsOption) {
            btnFindContactsOptionEvent();
        } else if (v == btnInviteFriendsOption) {
            btnInviteFriendsOptionEvent();
        } else if (v == btnEditProfileOption) {
            btnEditProfileOptionEvent();
        } else if (v == btnChangePasswordOption) {
            btnChangePasswordOptionEvent();
        } else if (v == btnPostsYouveLikedOption) {
            btnPostsYouveLikedOptionEvent();
        } else if (v == btnLinkedAccountsOption) {
            btnLinkedAccountsOptionEvent();
        } else if (v == btnPushNotificationsOption) {
            btnPushNotificationsOptionEvent();
        } else if (v == btnCameraOption) {
            btnCameraOptionEvent();
        } else if (v == btnCellularDataUseOption) {
            btnCellularDataUseOptionEvent();
        } else if (v == btnReportaProblemOption) {
            btnReportaProblemOptionEvent();
        } else if (v == btnInstagramHelpCenterOption) {
            btnInstagramHelpCenterOptionEvent();
        } else if (v == btnAdsOption) {
            btnAdsOptionEvent();
        } else if (v == btnBlogOption) {
            btnBlogOptionEvent();
        } else if (v == btnPrivacyPolicyOption) {
            btnPrivacyPolicyOptionEvent();
        } else if (v == btnTermsofServiceOption) {
            btnTermsofServiceOptionEvent();
        } else if (v == btnOpenSourceLibrariesOption) {
            btnOpenSourceLibrariesOptionEvent();
        } else if (v == btnClearSearchHistoryOption) {
            btnClearSearchHistoryOptionEvent();
        } else if (v == btnLogOutOption) {
            btnLogOutOptionEvent();
        }
    }


}
