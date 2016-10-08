package com.yy.www.libs.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyu on 16/10/8.
 */

public abstract class ThumbnailActivity extends AppCompatActivity {
    Bundle transitionState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exitElement();
    }

    protected void exitElement() {
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                if (transitionState != null) {
                    int index = transitionState.getInt("index", 0);
                    String url = getBackTransitionName(index);
                    View view = getBackTransitionView(index);
                    sharedElements.clear();
                    sharedElements.put(url, view);
                    transitionState = null;
                }
            }
        });
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        transitionState = new Bundle(data.getExtras());
    }


    /**
     * 设置位移的ViewName
     *
     * @param index
     * @return
     */
    protected abstract String getBackTransitionName(int index);

    /**
     * 设置位移的View
     *
     * @param index
     * @return
     */
    protected abstract View getBackTransitionView(int index);


    /**
     * multi Image
     *
     * @param activity
     * @param view
     * @param urlStrings
     * @param index
     */
    public  void startViewerActivity(Activity activity, View view, ArrayList<String> urlStrings, int index) {

        Intent intent = new Intent(activity, ViewerActivity.class);
        intent.putExtra("urlStrings", urlStrings);
        intent.putExtra("index", index);

        ActivityOptionsCompat optionsCompat;

        if (Build.VERSION.SDK_INT >= 16) {
            optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
            if (Build.VERSION.SDK_INT >= 21) {
                optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, urlStrings.get(index));
            }
            activity.startActivity(intent, optionsCompat.toBundle());
        } else
            activity.startActivity(intent);
    }

    /**
     * single Image
     *
     * @param activity
     * @param view
     * @param urlString
     */
    public  void startViewerActivity(Activity activity, View view, String urlString) {
        Intent intent = new Intent(activity, ViewerActivity.class);
        intent.putExtra("urlString", urlString);

        ActivityOptionsCompat optionsCompat;

        if (Build.VERSION.SDK_INT >= 16) {
            optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
            if (Build.VERSION.SDK_INT >= 21) {
                optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, urlString);
            }
            activity.startActivity(intent, optionsCompat.toBundle());
        } else
            activity.startActivity(intent);
    }
}
