package com.cbstudio.colapicker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SquaredFrameLayout extends FrameLayout{
    public SquaredFrameLayout(Context context) {
        super(context);
    }

    public SquaredFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
