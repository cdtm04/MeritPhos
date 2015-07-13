package com.merit.myapplication.moduls;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by merit on 7/4/2015.
 */
public class SpannedUserNameTextFormat extends LinkMovementMethod {

    // the method use to settext on textiew under SpannedUserNameTextFormat
    public void setSpannedTextUserName(final Context mContext, TextView textView, final String content) {
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                textEvent(mContext, msg);
            }
        };

        // this method to settext without underline because html code: text-decoration: none can't use
        Spannable s = (Spannable) Html.fromHtml(content);
        for (URLSpan u : s.getSpans(0, s.length(), URLSpan.class)) {
            s.setSpan(new UnderlineSpan() {
                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                    tp.bgColor = Color.parseColor("#ffffff");
                }
            }, s.getSpanStart(u), s.getSpanEnd(u), 0);
        }
        textView.setText(s);

        // set event
        LinkMovementMethodExt linkMovementMethodExt = new LinkMovementMethodExt();
        textView.setMovementMethod(linkMovementMethodExt.getInstance(handler, URLSpan.class));
    }

    // the method use to wrap string userName to SpannedUserNameTextFormat
    protected String getUserNameTextFormat(String userName) {
        return "<a href='" + userName + "'><b>" + userName + "</b></a>";
    }

    // CODE click text Event HERE
    private void textEvent(Context mContext, Message msg) {
        BackgroundColorSpan color = null;
        int what = msg.what;
        if (what == 100) {
            MessageSpan ms = (MessageSpan) msg.obj;
            Object[] spans = (Object[]) ms.getObj();
            TextView view = ms.getView();

            for (Object span : spans) {
                if (span instanceof URLSpan) {
                    int start = Selection.getSelectionStart(view.getText());
                    int end = Selection.getSelectionEnd(view.getText());
                    Spannable _span = (Spannable) view.getText();
                    color = new BackgroundColorSpan(view.getLinkTextColors().getDefaultColor());
                    _span.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    view.setText(_span);

                    // CODE click text Event HERE
                    Toast.makeText(mContext, "User " + ((URLSpan) span).getURL() + " is clicked", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (what == 200) {
            MessageSpan ms = (MessageSpan) msg.obj;
            TextView view = ms.getView();
            Spannable _span = (Spannable) view.getText();
            _span.removeSpan(new BackgroundColorSpan(view.getLinkTextColors().getDefaultColor()));
            view.setText(_span);
        }
    }

    private class LinkMovementMethodExt extends LinkMovementMethod {
        private LinkMovementMethod sInstance;
        private Handler handler = null;
        private Class spanClass = null;

        public MovementMethod getInstance(Handler _handler, Class _spanClass) {
            if (sInstance == null) {
                sInstance = new LinkMovementMethodExt();
                ((LinkMovementMethodExt) sInstance).handler = _handler;
                ((LinkMovementMethodExt) sInstance).spanClass = _spanClass;
            }
            return sInstance;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer,
                                    MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);
                /**
                 * get you interest span
                 */
                Object[] spans = buffer.getSpans(off, off, spanClass);
                if (spans.length != 0) {
                    if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(spans[0]),
                                buffer.getSpanEnd(spans[0]));
                        MessageSpan obj = new MessageSpan();
                        obj.setObj(spans);
                        obj.setView(widget);
                        Message message = handler.obtainMessage();
                        message.obj = obj;
                        message.what = 100;
                        message.sendToTarget();
                        return true;
                    } else if (action == MotionEvent.ACTION_UP) {
                        MessageSpan obj = new MessageSpan();
                        obj.setView(widget);
                        Message message = handler.obtainMessage();
                        message.obj = obj;
                        message.what = 200;
                        message.sendToTarget();
                        return true;
                    }
                }
            }

            return super.onTouchEvent(widget, buffer, event);
        }

        public boolean canSelectArbitrarily() {
            return true;
        }

        public boolean onKeyUp(TextView widget, Spannable buffer, int keyCode,
                               KeyEvent event) {
            return false;
        }
    }

    private class MessageSpan {
        private Object obj;
        private TextView view;

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public TextView getView() {
            return view;
        }

        public void setView(TextView view) {
            this.view = view;
        }
    }
}
