package com.yy.www.libs.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.yy.www.libs.R;
import com.yy.www.libs.bean.ZoomBean;
import com.yy.www.libs.widget.ConflictViewPager;
import com.yy.www.libs.widget.PullBackLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

import static com.yy.www.libs.TransitionConstant.PARAMS_ANIM_TYPE;
import static com.yy.www.libs.TransitionConstant.PARAMS_DATA;
import static com.yy.www.libs.TransitionConstant.PARAMS_IMAGE_SOURCE;
import static com.yy.www.libs.TransitionConstant.PARAMS_IMAGE_TYPE;
import static com.yy.www.libs.TransitionConstant.PARAMS_RETURNINDEX;
import static com.yy.www.libs.TransitionConstant.PARAMS_TRANSITIONINDEX;
import static com.yy.www.libs.TransitionConstant.PARAMS_TRANSITIONNAMES;
import static com.yy.www.libs.TransitionConstant.Type.TYPE_FILE;
import static com.yy.www.libs.TransitionConstant.Type.TYPE_HAVE_NOT_ANIM;
import static com.yy.www.libs.TransitionConstant.Type.TYPE_IMAGE_NORMAL;

/**
 * 图片展示Activity
 */
public class ViewerActivity<T> extends AppCompatActivity implements PullBackLayout.Callback {

    /**
     * 上拉下拉关闭ViewGroup
     */
    private PullBackLayout puller;

    /**
     * 针对ViewDragHelper手势冲突处理
     */
    private ConflictViewPager pager;

    CircleIndicator indicator;

    /**
     * viewPager adapter
     */
    private Adapter adapter;

    /**
     * rootView background alpha
     */
    private ColorDrawable background;

    private List<ZoomBean<T>> showList = new ArrayList<>();

    private List<String> transitionNames = new ArrayList<>();

    private int mIndex;

    /**
     * 关闭方法
     */
    private int anim_type;
    private int image_type;


    public ViewerFragment getCurrent() {
        return (ViewerFragment) adapter.instantiateItem(pager, pager.getCurrentItem());
    }

    public ViewerFragment getFirst() {
        return (ViewerFragment) adapter.instantiateItem(pager, mIndex);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        initView();
        getIntentData();
        initPuller();
        initAdapter(mIndex);
        //Support library version of postponeEnterTransition() that works only on API 21 and later.
        //延迟过渡动画
        supportPostponeEnterTransition();

        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                try {
                    if (transitionNames != null && transitionNames.size() > 0) {
                        sharedElements.clear();
                        sharedElements.put(transitionNames.get(pager.getCurrentItem()), getCurrent().getSharedElement());
                    }
                } catch (NullPointerException e) {
                    Log.e("ViewerActivity", "onMapSharedElements : " + e.toString());
                }
            }
        });
    }

    private void initView() {
        pager = (ConflictViewPager) findViewById(R.id.pager);
        puller = (PullBackLayout) findViewById(R.id.puller);
        indicator = (CircleIndicator) findViewById(R.id.indicator);

    }


    private void getIntentData() {
        mIndex = getIntent().getIntExtra(PARAMS_TRANSITIONINDEX, 0);
        anim_type = getIntent().getIntExtra(PARAMS_ANIM_TYPE, TYPE_IMAGE_NORMAL);
        image_type = getIntent().getIntExtra(PARAMS_IMAGE_TYPE, TYPE_HAVE_NOT_ANIM);
        transitionNames.addAll(getIntent().getStringArrayListExtra(PARAMS_TRANSITIONNAMES));
        showList.addAll((Collection<? extends ZoomBean<T>>) getIntent().getSerializableExtra(PARAMS_DATA));
    }

    private void initPuller() {
//        background = new ColorDrawable(Color.BLACK);
        puller.setCallback(this);
        puller.setBackgroundColor(Color.BLACK);
    }

    private void initAdapter(int mIndex) {
        adapter = new Adapter();
        pager.setAdapter(adapter);
        pager.setCurrentItem(mIndex);
        indicator.setViewPager(pager);
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
    public void onPullComplete(int top) {
        closeAct();
    }


    @Override
    public void onBackPressed() {
        closeAct();
    }

    /**
     * 将当前的位置返回；
     */
    @Override
    public void supportFinishAfterTransition() {
        Intent data = new Intent();
        data.putExtra(PARAMS_RETURNINDEX, pager.getCurrentItem());
        setResult(RESULT_OK, data);
        super.supportFinishAfterTransition();
    }

    private class Adapter extends FragmentStatePagerAdapter {

        public Adapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            ZoomBean<T> zoomBean = showList.get(position);

            Bundle arguments = new Bundle();
            arguments.putSerializable(PARAMS_DATA, zoomBean);
            arguments.putInt(PARAMS_IMAGE_SOURCE, TYPE_FILE);

            Fragment fragment = new ViewerFragment();
            fragment.setArguments(arguments);

            return fragment;
        }

        @Override
        public int getCount() {
            return showList.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (showList != null) {
            showList.clear();
        }
        if (transitionNames != null) {
            transitionNames.clear();
        }
    }

    public void closeAct() {
        if (anim_type == TYPE_HAVE_NOT_ANIM) {
            finish();
        } else {
            supportFinishAfterTransition();
        }
    }
}
