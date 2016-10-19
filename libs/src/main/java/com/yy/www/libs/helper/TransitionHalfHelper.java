package com.yy.www.libs.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.yy.www.libs.Constant;
import com.yy.www.libs.view.ViewerActivity;

import java.util.ArrayList;

import static com.yy.www.libs.Constant.PARAMS_OVER_TARGET;
import static com.yy.www.libs.Constant.PARAMS_TRANSITIONINDEX;
import static com.yy.www.libs.Constant.PARAMS_TRANSITIONNAMES;

/**
 * Created by yangyu on 16/10/9.
 */

public class TransitionHalfHelper {

    private int defPosition = 0;

    private ArrayList<String> transitionNames;

    private Activity mContext;

    public TransitionHalfHelper(Activity activity) {
        this.mContext = activity;
    }

    /**
     * multi Image
     */
    public void startViewerActivity(View view, ArrayList<String> urlStrings, int position) {

        if (transitionNames == null) {
            transitionNames = new ArrayList<>();
        } else {
            transitionNames.clear();
        }
        transitionNames.addAll(urlStrings);

        Intent intent = new Intent(mContext, ViewerActivity.class);
        intent.putExtra(PARAMS_TRANSITIONNAMES, urlStrings);
        intent.putExtra(PARAMS_TRANSITIONINDEX, position);
        intent.putExtra(PARAMS_OVER_TARGET, Constant.Target.TARGET_SKIP);
        ActivityOptionsCompat optionsCompat;

        if (Build.VERSION.SDK_INT >= 16) {
            optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
            if (Build.VERSION.SDK_INT >= 21) {
                optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, view, transitionNames.get(defPosition));
            }
            mContext.startActivity(intent, optionsCompat.toBundle());
        } else
            mContext.startActivity(intent);
    }


}
