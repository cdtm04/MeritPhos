package com.merit.myapplication.moduls;

/**
 * Created by merit on 7/6/2015.
 */

import android.graphics.Color;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.merit.myapplication.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LinkedTextView {
    //public static final Pattern URL_PATTERN = Pattern.compile("((https?|ftp)(:\\/\\/[-_.!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\$,%#]+))");
    //public static final Pattern URL_PATTERN = Pattern.compile("(\\(\\*\\S+\\*\\))");
    //public static final Pattern URL_PATTERN = Pattern.compile("(\\<username\\>)(\\S+)(\\<\\/username\\>)");
    public static final Pattern URL_PATTERN = Pattern.compile("(\\(user\\))(\\S+)(\\(\\/user\\))");

    public interface OnClickListener {
        void onLinkClicked(final String link);

        void onClicked();
    }

    static class SensibleUrlSpan extends URLSpan {
        /**
         * Pattern to match.
         */
        private Pattern mPattern;

        public SensibleUrlSpan(String url, Pattern pattern) {
            super(url);
            mPattern = pattern;
        }

        public boolean onClickSpan(View widget) {
            boolean matched = mPattern.matcher(getURL()).matches();
            if (matched) {
                //super.onClick(widget);
            }
            return matched;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
            ds.setColor(Color.parseColor("#095287"));
            ds.bgColor = Color.WHITE;
            ds.setFakeBoldText(true);
        }
    }

    static class SensibleLinkMovementMethod extends LinkMovementMethod {

        private boolean mLinkClicked;

        private String mClickedLink;

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP) {
                mLinkClicked = false;
                mClickedLink = null;
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    SensibleUrlSpan span = (SensibleUrlSpan) link[0];
                    mLinkClicked = span.onClickSpan(widget);
                    mClickedLink = span.getURL();
                    return mLinkClicked;
                }
            }
            super.onTouchEvent(widget, buffer, event);

            return false;
        }

        public boolean isLinkClicked() {
            return mLinkClicked;
        }

        public String getClickedLink() {
            // delete (user) and (/user) on link
            return mClickedLink.substring(6, mClickedLink.length() - 7);
        }

    }

    public static void autoLink(final TextView view, final String content, final OnClickListener listener) {
        autoLink(view, content, listener, null);
    }

    public static void autoLink(final TextView view, final String content, final OnClickListener listener,
                                final String patternStr) {
        //String text = view.getText().toString();
        Spanned text = Html.fromHtml(content);
        if (TextUtils.isEmpty(text)) {
            return;
        }
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);

        Pattern pattern;
        if (TextUtils.isEmpty(patternStr)) {
            pattern = URL_PATTERN;
        } else {
            pattern = Pattern.compile(patternStr);
        }
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            SensibleUrlSpan urlSpan = new SensibleUrlSpan(matcher.group(0), pattern);
            spannable.setSpan(urlSpan, matcher.start(2), matcher.end(2),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        // Delete (user) and (/user)
        Matcher matcher1 = Pattern.compile("\\(user\\)").matcher(spannable);
        int matchesSoFar = 0;
        while (matcher1.find()) {
            int start = matcher1.start() - matchesSoFar;
            spannable.delete(start, start + 6);
            matchesSoFar = matchesSoFar + 6;
        }
        Matcher matcher2 = Pattern.compile("\\(\\/user\\)").matcher(spannable);
        int matchesSoFar2 = 0;
        while (matcher2.find()) {
            int start = matcher2.start() - matchesSoFar2;
            spannable.delete(start, start + 7);
            matchesSoFar2 = matchesSoFar2 + 7;
        }


        view.setText(spannable, TextView.BufferType.SPANNABLE);


        final SensibleLinkMovementMethod method = new SensibleLinkMovementMethod();
        view.setMovementMethod(method);
        if (listener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (method.isLinkClicked()) {
                        listener.onLinkClicked(method.getClickedLink());
                    } else {
                        listener.onClicked();
                    }
                }
            });
        }
    }

}