package com.merit.myapplication.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.merit.myapplication.R;
import com.merit.myapplication.instagram.InstagramApp;
import com.merit.myapplication.loaddata.ImageLoader;
import com.merit.myapplication.models.Comment;
import com.merit.myapplication.models.Post;
import com.merit.myapplication.models.User;
import com.merit.myapplication.moduls.ActionBar;
import com.merit.myapplication.moduls.CircleImageView;
import com.merit.myapplication.moduls.LinkedTextView;
import com.merit.myapplication.moduls.ListViewActivityCommentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by merit on 7/19/2015.
 */
public class ActivityComment extends Activity implements ListViewActivityCommentAdapter.OnClickItemCommentListener {
    private Post mPost;
    private String mParentActivity;

    ActionBar abComment;
    ListView lvComments;
    EditText edtComment;
    RelativeLayout btnSendComment, loCaption;

    ArrayList<Comment> mComments = new ArrayList<>();
    ListViewActivityCommentAdapter adapter;

    private boolean mIsLoadingComments = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initialize();
        if (!mIsLoadingComments) {
            loadComments();
        }
    }

    private void initialize() {
        abComment = (ActionBar) findViewById(R.id.abComment);
        lvComments = (ListView) findViewById(R.id.lvComments);
        edtComment = (EditText) findViewById(R.id.edtComment);
        btnSendComment = (RelativeLayout) findViewById(R.id.btnSendComment);
        loCaption = (RelativeLayout) findViewById(R.id.loCaption);

        Intent intent = getIntent();
        mPost = (Post) intent.getSerializableExtra("MPOST");
        mParentActivity = intent.getStringExtra(MainActivity.PARENT);

        showCaption();

        abComment.setButtonRightEnabled(false);
        abComment.setLabelName("COMMENTS");
        abComment.setOnActionBarListener(new ActionBar.OnActionBarListener() {
            @Override
            public void onButtonLeftClick(View v) {
                finish();
            }

            @Override
            public void onButtonRightClick(View v) {

            }
        });

        adapter = new ListViewActivityCommentAdapter(this, mComments, this);

        lvComments.setAdapter(adapter);
        lvComments.setDividerHeight(0);
    }

    private void loadComments() {
        mIsLoadingComments = true;
        final ProgressDialog mDialog = ProgressDialog.show(this, "", "Loading comments...");
        mDialog.setCancelable(false);
        InstagramApp.instagramApp.fetchComments(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == InstagramApp.WHAT_FINALIZE) {
                    mComments.clear();

                    ArrayList<Comment> mCmts = InstagramApp.instagramApp.getComments();
                    for (int i = 0; i < mCmts.size(); i++) {
                        mComments.add(mCmts.get(i));
                    }

                    adapter.notifyDataSetChanged();
                } else if (msg.what == InstagramApp.WHAT_ERROR) {
                    Toast.makeText(ActivityComment.this, "Check your network.", Toast.LENGTH_SHORT).show();
                }
                mIsLoadingComments = false;
                mDialog.dismiss();
                return false;
            }
        }), mPost.getId());
    }

    private void showCaption() {
        if (mPost.getCaptionOfPost() == null) {
            loCaption.setVisibility(View.GONE);
        } else {
            CircleImageView ivAvatar = (CircleImageView) loCaption.findViewById(R.id.ivAvatarOfCaption);
            new ImageLoader(this).DisplayImage(mPost.getUserOfPost().getProfilePicture(), ivAvatar);
            ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAccountActivity(mPost.getUserOfPost().getId());
                }
            });


            String caption = "(user)" + mPost.getUserOfPost().getUserName() + "(/user)" + " " + mPost.getCaptionOfPost().getTextOfCaption() + "<br>" + "<font color='gray'>" + mPost.getCaptionOfPost().getCreatedTimeOfCaption() + "</font>";
            LinkedTextView.autoLink(((TextView) loCaption.findViewById(R.id.tvContentOfCaption)), caption, new LinkedTextView.OnClickListener() {
                @Override
                public void onLinkClicked(String link) {
                    startAccountActivity(mPost.getUserOfPost().getId());
                }

                @Override
                public void onClicked() {

                }
            });

        }
    }

    private void startAccountActivity(String userId) {
        Intent iOpenUserInfoActivity = new Intent(this, ActivityAccount.class);
        iOpenUserInfoActivity.putExtra("ID", userId);
        iOpenUserInfoActivity.putExtra("PARENT", mParentActivity);

        if (mParentActivity.equals(MainActivity.PARENT_HOME)) {
            View view = ActivityHomeGroup.groupHomeGroup.getLocalActivityManager().startActivity("AccountActivity", iOpenUserInfoActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
            ActivityHomeGroup.groupHomeGroup.replaceView(view);
        } else if (mParentActivity.equals(MainActivity.PARENT_ACCOUNT)) {
            View view = ActivityAccountGroup.groupAccountGroup.getLocalActivityManager().startActivity("AccountActivity", iOpenUserInfoActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
            ActivityAccountGroup.groupAccountGroup.replaceView(view);
        }

        finish();
    }

    @Override
    public void onLinkClicked(String link, String cmtUserId) {
        startAccountActivity(cmtUserId);
    }

    @Override
    public void onClickAvatar(View v, String cmtUserId) {
        startAccountActivity(cmtUserId);
    }

    @Override
    public void onTextClicked() {

    }
}
