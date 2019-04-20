package com.example.libraryqa_android;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * 去除可点击文字的下划线
 */

public class NoLineClickSpan extends ClickableSpan {
    public NoLineClickSpan() {
        super();
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        /**set textColor**/
        ds.setColor(ds.linkColor);
        /**Remove the underline**/
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
    }
}
