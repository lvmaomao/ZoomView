package com.example.xx.zoomview_mt;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.yy.www.libs.widget.ConflictViewPager;
import com.yy.www.libs.widget.PullBackLayout;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by yangyu on 2017/2/15.
 * 图片放大的Activity
 */

public class ImageZoomActivity extends AppCompatActivity implements PullBackLayout.Callback {
    /**
     * 上个页面的imageView集合
     */
    public static final String IMAGE_VIEWS = "image_views";
    /**
     * 上一个页面点击的imageView 位置
     */
    public static final String IMAGE_POSITION = "image_position";
    /**
     * 缩略图
     */
    public static final String IMAGE_THUMB_URL = "image_thumb_url";
    /**
     * 原图
     */
    public static final String IMAGE_URL = "image_url";

    private ValueAnimator bgcAnim;

    private ImageAdapter adapter;

    private ConflictViewPager viewPager;
    protected CircleIndicator indicator;
    private PullBackLayout puller;


    private List<ImageBean> imageViewList;  // imageView引用集合 传递方需要控制数量。
    private List<String> thumbUrlList; //缩略图集合
    private List<String> urlList; //原图集合

    private int startPosition;
    private int mWidth;
    private int mHeight;

    /**
     * 是否已经播放过动画
     */
    public boolean isAnim = false;

    public boolean isAnim() {
        return isAnim;
    }

    public void setAnim(boolean anim) {
        isAnim = anim;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_image_zoom);
        receiveIntent(getIntent());
    }

    //onNewIntent 调用在onStart之前。
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        receiveIntent(getIntent());
    }

    /**
     * 接受上个页面传递的数据
     *
     * @param intent 携带数据的intent
     */
    private void receiveIntent(@NonNull Intent intent) {
        if (intent.hasExtra(IMAGE_VIEWS)) {
            imageViewList = intent.getParcelableArrayListExtra(IMAGE_VIEWS);
            thumbUrlList = intent.getStringArrayListExtra(IMAGE_THUMB_URL);
            urlList = intent.getStringArrayListExtra(IMAGE_URL);
            startPosition = intent.getIntExtra(IMAGE_POSITION, 0);
        } else {
            throw new NullPointerException("please intent imageView and position");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mWidth = dm.widthPixels; // 屏幕宽（像素，如：px）
        mHeight = dm.heightPixels; // 屏幕高（像素，如：px）
        initViewPager();
        initPuller();
        animBackgroundTransform();
    }

    ////////////////////////////////////////////////////
    //// Puller start
    ////////////////////////////////////////////////////
    private void initPuller() {
        puller = (PullBackLayout) findViewById(com.yy.www.libs.R.id.puller);
        if (puller != null) {
            puller.setCallback(this);
        }
    }


    private void initViewPager() {
        adapter = new ImageAdapter();
        viewPager = (ConflictViewPager) findViewById(R.id.viewPager);
        indicator = (CircleIndicator) findViewById(com.yy.www.libs.R.id.indicator);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setCurrentItem(startPosition);
        indicator.setViewPager(viewPager);
    }

    @Override
    public void onPullStart() {

    }

    private static final String TAG = "ImageZoomActivity";

    @Override
    public void onPull(float p) {
        Log.e(TAG, "onPull : " + p);
        setBackgroundColor(mColorEvaluator.evaluate(Math.abs(p), 0xff000000, 0x00000000));
    }

    @Override
    public void onPullCancel() {

    }

    @Override
    public void onPullComplete(int top) {
        closeAct(top);
    }

    ////////////////////////////////////////////////////
    //// Puller end
    ////////////////////////////////////////////////////


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeAct();
    }

    private void closeAct() {
        closeAct(0);
    }

    private void closeAct(int ty) {
        setAnim(false);

        int closePosition = viewPager.getCurrentItem();
        ImageBean closeImageBean = getImageBean(closePosition);
        if (closeImageBean == null) {
            closeImageBean = new ImageBean();
            closeImageBean.translationX = -100f;
            closeImageBean.translationY = -100f;
            closeImageBean.width = mWidth + 200f;
            closeImageBean.height = mHeight + 200f;
            closeImageBean.alpha = 0;
        }
        getCurrentFragment().close(ty, closeImageBean);
    }

    /**
     * 获取 当前的imageBean
     *
     * @param position 当前的postion
     * @return ImageBean
     * @throws Exception 数组越界
     */
    private ImageBean getImageBean(int position) {
        try {
            return imageViewList.get(position);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前的fragment
     *
     * @return
     */
    public ImageFragment getCurrentFragment() {
        return (ImageFragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
    }

    public String getStartThumbUrl() {
        return thumbUrlList.get(startPosition);
    }


    private class ImageAdapter extends FragmentStatePagerAdapter {

        public ImageAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            Bundle arguments = new Bundle();
            //获取当前用户的基本信息 ；传递到下个页面，
            if (position == startPosition) {
                arguments.putParcelable("start_image", imageViewList.get(position));
            }
            arguments.putString("url", urlList.get(position));
            arguments.putString("thumbUrl", thumbUrlList.get(position));

            Fragment fragment = ImageFragment.newInstance(arguments);
            fragment.setArguments(arguments);

            return fragment;
        }

        @Override
        public int getCount() {
            return urlList.size();
        }
    }

    private void setBackgroundColor(int color) {
        puller.setBackgroundColor(color);
    }

    /**
     * 执行ImageWatcher自身的背景色渐变至期望值[colorResult]的动画
     */
    private void animBackgroundTransform() {
        if (bgcAnim != null) bgcAnim.cancel();
        bgcAnim = ValueAnimator.ofFloat(0, 1).setDuration(300);
        bgcAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float p = (float) animation.getAnimatedValue();
                setBackgroundColor(mColorEvaluator.evaluate(p, 0x00000000, 0xff000000));
            }
        });
        bgcAnim.start();
    }

    final TypeEvaluator<Integer> mColorEvaluator = new TypeEvaluator<Integer>() {
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int startColor = startValue;
            int endColor = endValue;

            int alpha = (int) (Color.alpha(startColor) + fraction * (Color.alpha(endColor) - Color.alpha(startColor)));
            int red = (int) (Color.red(startColor) + fraction * (Color.red(endColor) - Color.red(startColor)));
            int green = (int) (Color.green(startColor) + fraction * (Color.green(endColor) - Color.green(startColor)));
            int blue = (int) (Color.blue(startColor) + fraction * (Color.blue(endColor) - Color.blue(startColor)));
            return Color.argb(alpha, red, green, blue);
        }
    };

}
