package com.yy.www.libs.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.yy.www.libs.view.ViewerActivity;

import java.util.ArrayList;

import static com.yy.www.libs.Constant.PARAMS_TRANSITIONNAMES;

/**
 * 针对单个ImageView 的位移
 */
public class TransitionSingleHelper {
    /**
     * 展示页面需要的格式
     */
    private ArrayList<String> transitionNames;

    private Activity mContext;

    public TransitionSingleHelper(Activity activity) {
        mContext = activity;
    }


    /**
     * single Image
     */
    public void startViewerActivity(View view, String urlString) {
        if (transitionNames == null) {
            transitionNames = new ArrayList<>();
        } else {
            transitionNames.clear();
        }
        transitionNames.add(urlString);
        Intent intent = new Intent(mContext, ViewerActivity.class);
        intent.putExtra(PARAMS_TRANSITIONNAMES, transitionNames);
        ActivityOptionsCompat optionsCompat;

        if (Build.VERSION.SDK_INT >= 16) {
            optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
            if (Build.VERSION.SDK_INT >= 21) {
                optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, view, urlString);
            }
            mContext.startActivity(intent, optionsCompat.toBundle());
        } else
            mContext.startActivity(intent);
    }

}
