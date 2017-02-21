//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yy.www.libs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.yy.www.libs.R;

/**
 * 下拉关闭
 */
public class PullBackLayout extends FrameLayout {
    private final ViewDragHelper dragger;
    private final int dragDismissDistance;


    @Nullable
    private PullBackLayout.Callback callback;

    public PullBackLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public PullBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.dragger = ViewDragHelper.create(this, 0.125F, new PullBackLayout.ViewDragCallback());
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PullBackLayout);
        this.dragDismissDistance = (int) typedArray.getDimension(R.styleable.PullBackLayout_dragDismissDistance, 0);
    }

    public void setCallback(@Nullable PullBackLayout.Callback callback) {
        this.callback = callback;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return this.dragger.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {
        try {
            this.dragger.processTouchEvent(event);
        } catch (Exception e) {
        }
        return true;
    }

    public void computeScroll() {
        if (this.dragger.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    private class ViewDragCallback extends android.support.v4.widget.ViewDragHelper.Callback {
        private ViewDragCallback() {
        }

        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return 0;
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            if (top > dragDismissDistance)
                return dragDismissDistance;
            else if (top < -dragDismissDistance)
                return -dragDismissDistance;
            else
                return top;
        }

        public int getViewHorizontalDragRange(View child) {
            return 0;
        }

        public int getViewVerticalDragRange(View child) {
            return PullBackLayout.this.getHeight();
        }

        public void onViewCaptured(View capturedChild, int activePointerId) {
            if (PullBackLayout.this.callback != null) {
                PullBackLayout.this.callback.onPullStart();
            }
        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (PullBackLayout.this.callback != null) {
                float ty = (float) top / (float) PullBackLayout.this.getHeight();
                PullBackLayout.this.callback.onPull(ty);
            }
        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int slop = dragDismissDistance;
            if (Math.abs(releasedChild.getTop()) >= slop) {
                if (PullBackLayout.this.callback != null) {
                    PullBackLayout.this.callback.onPullComplete(releasedChild.getTop());
                }
            } else {
                if (PullBackLayout.this.callback != null) {
                    PullBackLayout.this.callback.onPullCancel();
                }
                PullBackLayout.this.dragger.settleCapturedViewAt(0, 0);
                PullBackLayout.this.invalidate();
            }
        }
    }

    public interface Callback {
        void onPullStart();

        void onPull(float var1);

        void onPullCancel();

        void onPullComplete(int top);
    }
}
