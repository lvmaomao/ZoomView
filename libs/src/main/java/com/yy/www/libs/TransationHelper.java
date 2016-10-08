package com.yy.www.libs;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.view.View;

import com.yy.www.libs.view.ViewerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangyu on 16/10/8.
 */

public class TransationHelper {

    private Bundle transitionState;

    private String transitionName;

    private View transitionView;

    private TransationHelper(Builder builder) {
        transitionName = builder.transitionName;
        transitionView = builder.transitionView;
    }

    SharedElementCallback sharedElementCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (transitionState != null) {
                String url = transitionName;
                View view = transitionView;
                sharedElements.clear();
                sharedElements.put(url, view);
                transitionState = null;
            }
        }


    };

    public void updateView(View view) {
        transitionView = view;
    }

    public void updateName(String name) {
        transitionName = name;
    }

    public void setTransitionState(Intent data) {
        transitionState = new Bundle(data.getExtras());
    }

    public int getIndex() {
        int index = transitionState.getInt("index", 0);
        return index;
    }


    /**
     * multi Image
     *
     * @param activity
     * @param view
     * @param urlStrings
     * @param index
     */
    public void startViewerActivity(Activity activity, View view, ArrayList<String> urlStrings, int index) {

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
    public void startViewerActivity(Activity activity, View view, String urlString) {
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


    public static class Builder {

        private String transitionName;

        private View transitionView;

        /**
         * @param transitionName
         * @return
         */
        public Builder setTransitionName(String transitionName) {
            this.transitionName = transitionName;
            return this;
        }

        public Builder setView(View view) {
            this.transitionView = view;
            return this;
        }

        public TransationHelper build() { // 构建，返回一个新对象
            return new TransationHelper(this);
        }

    }


}
