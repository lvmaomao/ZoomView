package com.example.xx.zoomview_mt;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yangyu on 16/9/12.
 */
public class QscViewPager extends ViewPager {


    public QscViewPager(Context context) {
        super(context);
    }

    public QscViewPager(Context context, AttributeSet attrs) {
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
