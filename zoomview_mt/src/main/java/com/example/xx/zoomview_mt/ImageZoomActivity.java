package com.example.xx.zoomview_mt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.yy.www.libs.widget.ConflictViewPager;
import com.yy.www.libs.widget.PullBackLayout;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by yangyu on 2017/2/15.
 */

public class ImageZoomActivity extends AppCompatActivity {

    public static final String THUMB_DATA = "thumb_data";

    public static final String ORIGINAL_DATA = "original_data";


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

    private ConflictViewPager viewPager;
    private CircleIndicator indicator;
    private PullBackLayout puller;


    private List<ImageView> imageViewList = new ArrayList<>();  // imageView引用集合 传递方需要控制数量。
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
            imageViewList = (List<ImageView>) intent.getSerializableExtra(IMAGE_VIEWS);
            thumbUrlList = (List<String>) intent.getSerializableExtra(IMAGE_THUMB_URL);
            urlList = (List<String>) intent.getSerializableExtra(IMAGE_URL);
            startPosition = intent.getIntExtra(IMAGE_POSITION, -1);
        } else {
            throw new NullPointerException("please intent imageView and position");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initViewPager();

    }

    private void initViewPager() {
        viewPager = (ConflictViewPager) findViewById(R.id.viewPager);
        viewPager.setCurrentItem(startPosition);
        viewPager.setAdapter(new ImageAdapter());
    }

    private class ImageAdapter extends FragmentStatePagerAdapter {

        public ImageAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {

            Bundle arguments = new Bundle();
            ImageView imageView = imageViewList.get(position);
//            ImageBean
            //获取当前用户的基本信息 ；传递到下个页面，
            arguments.putString(THUMB_DATA, thumbUrlList.get(position));
            arguments.putString(ORIGINAL_DATA, urlList.get(position));

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
