package com.example.xx.zoomview_mt;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.yy.www.libs.widget.ConflictViewPager;
import com.yy.www.libs.widget.PullBackLayout;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by yangyu on 2017/2/15.
 * 图片放大的Activity
 */

public class ImageZoomActivity extends AppCompatActivity implements PullBackLayout.Callback {
    /**
     * 以下是传递到fragment的信息;
     */
    public static final String THUMB_DATA = "thumb_data";
    public static final String ORIGINAL_DATA = "original_data";
    public static final String IMAGE_DATA = "image_data";
    public static final String DATA_POSITION = "data_position";

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

    private ImageAdapter adapter;

    private ConflictViewPager viewPager;
    private CircleIndicator indicator;
    private PullBackLayout puller;


    private List<ImageBean> imageViewList = new ArrayList<>();  // imageView引用集合 传递方需要控制数量。
    private List<String> thumbUrlList = new ArrayList<>(); //缩略图集合
    private List<String> urlList = new ArrayList<>(); //原图集合

    private int startPosition;

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
        if (intent != null && intent.hasExtra(IMAGE_VIEWS)) {
            imageViewList = (List<ImageBean>) intent.getSerializableExtra(IMAGE_VIEWS);
            thumbUrlList = (List<String>) intent.getSerializableExtra(IMAGE_THUMB_URL);
            urlList = (List<String>) intent.getSerializableExtra(IMAGE_URL);
            startPosition = intent.getIntExtra(IMAGE_POSITION, 0);
        } else {
            throw new NullPointerException("please intent imageView and position");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initViewPager();
        initPuller();
    }

    ////////////////////////////////////////////////////
    //// Puller start
    ////////////////////////////////////////////////////
    private void initPuller() {
        puller = (PullBackLayout) findViewById(com.yy.www.libs.R.id.puller);
        puller.setCallback(this);
        puller.setBackgroundColor(Color.BLACK);
    }


    private void initViewPager() {
        adapter = new ImageAdapter();
        viewPager = (ConflictViewPager) findViewById(R.id.viewPager);
        indicator = (CircleIndicator) findViewById(com.yy.www.libs.R.id.indicator);
        viewPager.setCurrentItem(startPosition);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
    }

    @Override
    public void onPullStart() {

    }

    @Override
    public void onPull(float progress) {
        puller.setAlpha((int) (0xff * (1f - Math.abs(progress))));
    }

    @Override
    public void onPullCancel() {

    }

    @Override
    public void onPullComplete() {
        closeAct();
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
        getCurrentFragment().close(viewPager.getCurrentItem());
    }

    /**
     * 获取当前的fragment
     *
     * @return
     */
    public ImageFragment getCurrentFragment() {
        return (ImageFragment) adapter.instantiateItem(viewPager, viewPager.getCurrentItem());
    }


    private class ImageAdapter extends FragmentStatePagerAdapter {

        public ImageAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            Bundle arguments = new Bundle();
            //获取当前用户的基本信息 ；传递到下个页面，
            arguments.putStringArrayList(THUMB_DATA, (ArrayList<String>) thumbUrlList);
            arguments.putStringArrayList(ORIGINAL_DATA, (ArrayList<String>) urlList);
            arguments.putParcelableArrayList(IMAGE_DATA, (ArrayList<? extends Parcelable>) imageViewList);
            arguments.putInt(DATA_POSITION, position);

            Fragment fragment = ImageFragment.newInstance(arguments);
            fragment.setArguments(arguments);

            return fragment;
        }

        @Override
        public int getCount() {
            return urlList.size();
        }
    }


}