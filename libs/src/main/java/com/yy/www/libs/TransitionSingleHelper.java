package com.yy.www.libs;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.view.View;

import com.yy.www.libs.view.ViewerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 针对单个ImageView 的位移
 */
public class TransitionSingleHelper {
    /**
     * 展示页面需要的格式
     */
    private ArrayList<String> transitionNames;

    /**
     * 位移view的desc
     */
    private String transitionName;

    /**
     * 位移的view
     */
    private View transitionView;

    public TransitionSingleHelper() {
    }

    /**
     * 必须实现的回调。设置返回的view和desc
     */
    public SharedElementCallback sharedElementCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            String url = transitionName;
            View view = transitionView;
            sharedElements.clear();
            sharedElements.put(url, view);
        }
    };



    /**
     * single Image
     *
     * @param activity
     */
    public void startViewerActivity(Activity activity,View view,String urlString) {
        this.transitionView = view;
        this.transitionName = urlString;
        if (transitionNames == null) {
            transitionNames = new ArrayList<>();
        } else {
            transitionNames.clear();
        }
        transitionNames.add(urlString);
        Intent intent = new Intent(activity, ViewerActivity.class);
        intent.putExtra("urlStrings", transitionNames);
        ActivityOptionsCompat optionsCompat;

        if (Build.VERSION.SDK_INT >= 16) {
            optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(transitionView, 0, 0, transitionView.getWidth(), transitionView.getHeight());
            if (Build.VERSION.SDK_INT >= 21) {
                optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionView, transitionName);
            }
            activity.startActivity(intent, optionsCompat.toBundle());
        } else
            activity.startActivity(intent);
    }

}
