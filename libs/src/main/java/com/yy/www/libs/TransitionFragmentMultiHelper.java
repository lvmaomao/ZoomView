package com.yy.www.libs;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.yy.www.libs.view.ViewerActivity;

import java.util.ArrayList;

import static com.yy.www.libs.Constant.PARAMS_OVER_TARGET;
import static com.yy.www.libs.Constant.PARAMS_TRANSITIONINDEX;
import static com.yy.www.libs.Constant.PARAMS_TRANSITIONNAMES;

/**
 * Created by yangyu on 16/10/9.
 */

public class TransitionFragmentMultiHelper {

    private ArrayList<String> transitionNames;

    private View transitionView;

    /**
     * multi Image
     *
     * @param activity
     * @param urlStrings 位移的描述
     * @param position   点击的位置
     */
    public void startViewerActivity(Activity activity, View view, ArrayList<String> urlStrings, int position) {

        this.transitionView = view;
        if (transitionNames == null) {
            transitionNames = new ArrayList<>();
        } else {
            transitionNames.clear();
        }
        transitionNames.addAll(urlStrings);

        Intent intent = new Intent(activity, ViewerActivity.class);
        intent.putExtra(PARAMS_TRANSITIONNAMES, urlStrings);
        intent.putExtra(PARAMS_TRANSITIONINDEX, position);
        intent.putExtra(PARAMS_OVER_TARGET, Constant.Target.TARGET_SKIP);

        ActivityOptionsCompat optionsCompat;

        if (Build.VERSION.SDK_INT >= 16) {
            optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(transitionView, 0, 0, transitionView.getWidth(), transitionView.getHeight());
            if (Build.VERSION.SDK_INT >= 21) {
                optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionView, urlStrings.get(position));
            }
            activity.startActivity(intent, optionsCompat.toBundle());
        } else
            activity.startActivity(intent);
    }


}
