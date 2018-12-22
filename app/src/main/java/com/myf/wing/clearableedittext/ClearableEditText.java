package com.myf.wing.clearableedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by miaoyf 2018/11/22
 */

public class ClearableEditText extends AppCompatEditText {
    private static final int DRAWABLE_LEFT = 0;
    private static final int DRAWABLE_TOP = 1;
    private static final int DRAWABLE_RIGHT = 2;
    private static final int DRAWABLE_BOTTOM = 3;
    private Drawable mClearButtonIcon;
    private int mClearButtonIconWidth = 0;
    private int mClearButtonIconHeight = 0;

    //这个构造函数，用代码new的时候调用，attrs传了null,会使用默认的参数,拿不到xml里定义的参数
    //e.g. ClearableEditText clearableEditText = new ClearableEditText(this)。
    public ClearableEditText(Context context) {
        this(context,null);
    }

    //这个构造函数在使用定义在xml中的ClearableEditText时会被调用.
    //e.g. findViewById(R.id.MyEditText)
    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取图片自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClearableEditTextIcon);
        try {
            mClearButtonIcon = array.getDrawable(R.styleable.ClearableEditTextIcon_clearButtonIcon);
            mClearButtonIconWidth = array.getDimensionPixelSize(R.styleable.ClearableEditTextIcon_clearButtonIconWidth, 30);
            mClearButtonIconHeight = array.getDimensionPixelSize(R.styleable.ClearableEditTextIcon_clearButtonIconHeight, 30);
        } finally {
            array.recycle();
        }
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setClearIconVisible(hasFocus() && text.length() > 0);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        setClearIconVisible(focused && length() > 0);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Drawable drawable = getCompoundDrawables()[DRAWABLE_RIGHT];
                if (drawable != null && event.getX() <= (getWidth() - getPaddingRight())
                        && event.getX() >= (getWidth() - getPaddingRight() - drawable.getBounds().width())) {
                    setText("");
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void setClearIconVisible(boolean visible) {
        //获取图片
        if(mClearButtonIcon == null) {
            //default;
            mClearButtonIcon = getResources().getDrawable(R.drawable.icon_clear);
        }
        Rect realBounds = mClearButtonIcon.getBounds();
        realBounds.right = realBounds.left + Math.round(mClearButtonIconWidth);
        realBounds.bottom = realBounds.top + Math.round(mClearButtonIconHeight);
        mClearButtonIcon.setBounds(realBounds);
        setCompoundDrawables(getCompoundDrawables()[DRAWABLE_LEFT], getCompoundDrawables()[DRAWABLE_TOP],
                visible ? mClearButtonIcon: null, getCompoundDrawables()[DRAWABLE_BOTTOM]);
    }
}