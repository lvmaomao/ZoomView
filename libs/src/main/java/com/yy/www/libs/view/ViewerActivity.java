package com.yy.www.libs.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yy.www.libs.R;
import com.yy.www.libs.manager.EventManager;
import com.yy.www.libs.widget.ConflictViewPager;
import com.yy.www.libs.widget.PullBackLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 图片展示Activity
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ViewerActivity extends AppCompatActivity implements PullBackLayout.Callback {

    /**
     * 上拉下拉关闭ViewGroup
     */
    private PullBackLayout puller;

    /**
     * 针对ViewDragHelper手势冲突处理
     */
    private ConflictViewPager pager;

    /**
     * viewPager adapter
     */
    private Adapter adapter;

    /**
     * rootView background alpha
     */
    private ColorDrawable background;

    private List<String> mUrlStrings = new ArrayList<>();

    private int mIndex;

    public ViewerFragment getCurrent() {
        return (ViewerFragment) adapter.instantiateItem(pager, pager.getCurrentItem());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        initView();
        getIntentData();
        initPuller();
        initAdapter(mIndex);
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                if (mUrlStrings != null && mUrlStrings.size() > 0) {
                    String url = mUrlStrings.get(pager.getCurrentItem());
                    sharedElements.clear();
                    sharedElements.put(url, getCurrent().getSharedElement());
                }
            }
        });
    }

    private void initView() {
        pager = (ConflictViewPager) findViewById(R.id.pager);
        puller = (PullBackLayout) findViewById(R.id.puller);
    }

    private void getIntentData() {
        //如果是single 位置为0
        mIndex = getIntent().getIntExtra("index", 0);
        mUrlStrings.addAll(getIntent().getStringArrayListExtra("urlStrings"));
        if (mUrlStrings == null || mUrlStrings.size() < 1)
            throw new NullPointerException("Please use the startViewerActivity for intent ViewerActivity");
    }

    private void initPuller() {
        background = new ColorDrawable(Color.BLACK);
        puller.setCallback(this);
        puller.setBackground(background);
    }

    private void initAdapter(int mIndex) {
        adapter = new Adapter();
        pager.setAdapter(adapter);
        pager.setCurrentItem(mIndex);
    }

    @Override
    public void onPullStart() {

    }

    @Override
    public void onPull(float progress) {
        background.setAlpha((int) (0xff * (1f - Math.abs(progress))));

    }

    @Override
    public void onPullCancel() {

    }

    @Override
    public void onPullComplete() {
        supportFinishAfterTransition();
    }


    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    /**
     * 将当前的位置返回；
     */
    @Override
    public void supportFinishAfterTransition() {
        Intent data = new Intent();
        data.putExtra("index", pager.getCurrentItem());
        setResult(RESULT_OK, data);
        EventManager.raiseEvent(pager.getCurrentItem());
        super.supportFinishAfterTransition();
    }

    private class Adapter extends FragmentStatePagerAdapter {

        public Adapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            String url = mUrlStrings.get(position);

            Bundle arguments = new Bundle();
            arguments.putString("image", url);

            Fragment fragment = new ViewerFragment();
            fragment.setArguments(arguments);

            return fragment;
        }

        @Override
        public int getCount() {
            return mUrlStrings.size();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUrlStrings != null) {
            mUrlStrings.clear();
        }
    }
}
