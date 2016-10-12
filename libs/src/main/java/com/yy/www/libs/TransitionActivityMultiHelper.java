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
 * Created by yangyu on 16/10/9.
 */

public class TransitionActivityMultiHelper {

    private ArrayList<String> transitionNames;

    private Bundle transitionState;

    private String transitionName;

    private View transitionView;


    public SharedElementCallback sharedElementCallback = new SharedElementCallback() {
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

    public void update(Intent intent, UpdateTransitionListener listener) {
        transitionState = new Bundle(intent.getExtras());
        int index = transitionState.getInt("index", 0);
        transitionName = listener.updateName(index);
        transitionView = listener.updateView(index);
    }


    /**
     * multi Image
     *
     * @param activity
     * @param view       位移的view
     * @param urlStrings 位移的描述
     * @param position   点击的位置
     */
    public void startViewerActivity(Activity activity, View view, ArrayList<String> urlStrings, int position) {

        this.transitionView = view;
        this.transitionName = urlStrings.get(position);
        if (transitionNames == null) {
            transitionNames = new ArrayList<>();
        } else {
            transitionNames.clear();
        }
        transitionNames.addAll(urlStrings);

        Intent intent = new Intent(activity, ViewerActivity.class);
        intent.putExtra("urlStrings", urlStrings);
        intent.putExtra("index", position);

        ActivityOptionsCompat optionsCompat;

        if (Build.VERSION.SDK_INT >= 16) {
            optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
            if (Build.VERSION.SDK_INT >= 21) {
                optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, urlStrings.get(position));
            }
            activity.startActivity(intent, optionsCompat.toBundle());
        } else
            activity.startActivity(intent);
    }


    public interface UpdateTransitionListener {
        View updateView(int position);

        String updateName(int position);
    }

    UpdateTransitionListener listener;

}
