package com.yy.www.libs.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 避免与PhotoView出现冲突
 */
public class ConflictViewPager extends ViewPager {


    public ConflictViewPager(Context context) {
        super(context);
    }

    public ConflictViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }
}
