package com.merit.myapplication.instagram;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.merit.myapplication.R;

/**
 * Created by merit on 7/8/2015.
 */
public class DialogLoginInstagram extends Dialog {

    private WebView webView;
    private String mUrl;
    private ProgressDialog pdSpinner;
    private OnDialogLoginInstagram mOnDialogLoginInstagram;

    public DialogLoginInstagram(Context context, String url, OnDialogLoginInstagram mOnDialogLoginInstagram) {
        super(context);
        // remove the title of dialog
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mUrl = url;
        this.mOnDialogLoginInstagram = mOnDialogLoginInstagram;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login_instagram);

        pdSpinner = new ProgressDialog(getContext());
        pdSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdSpinner.setMessage("Loading...");
        webView = (WebView) findViewById(R.id.wbInstagramLogin);
        webView.setWebViewClient(new InstagramWebView());
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl("https://api.instagram.com/oauth/authorize/?client_id=a7bfa891d5a94873bbc7a8ccd7d86e5d&redirect_uri=http://localhost&response_type=code");
        webView.loadUrl(mUrl);

        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    class InstagramWebView extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(ApplicationData.CALLBACK_URL)) {
                String code = url.split("=")[1];
                mOnDialogLoginInstagram.onComplete(code);
                DialogLoginInstagram.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mOnDialogLoginInstagram.onError(description);
            DialogLoginInstagram.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pdSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pdSpinner.dismiss();
        }


    }

    public interface OnDialogLoginInstagram {
        public abstract void onComplete(String code);

        public abstract void onError(String error);
    }
}
