package com.yy.www.libs.helper;

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

import static com.yy.www.libs.Constant.PARAMS_RETURNINDEX;
import static com.yy.www.libs.Constant.PARAMS_TRANSITIONINDEX;
import static com.yy.www.libs.Constant.PARAMS_TRANSITIONNAMES;

/**
 * Created by yangyu on 16/10/9.
 */

public class TransitionMultiHelper {

    private ArrayList<String> transitionNames;

    private Bundle transitionState;

    private String transitionName;

    private View transitionView;

    private Activity mContext;

    public TransitionMultiHelper(Activity activity) {
        this.mContext = activity;
    }

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
        int position = transitionState.getInt(PARAMS_RETURNINDEX, 0);
        transitionName = listener.updateName(position);
        if (listener.updateView(position) != null)
            transitionView = listener.updateView(position);
    }


    /**
     * multi Image
     *
     * @param urlStrings 位移的描述
     * @param position   点击的位置
     */
    public void startViewerActivity(View clickView, ArrayList<String> urlStrings, int position) {
        this.transitionView = clickView;
        this.transitionName = urlStrings.get(position);

        if (transitionNames == null) {
            transitionNames = new ArrayList<>();
        } else {
            transitionNames.clear();
        }
        transitionNames.addAll(urlStrings);

        Intent intent = new Intent(mContext, ViewerActivity.class);
        intent.putExtra(PARAMS_TRANSITIONNAMES, urlStrings);
        intent.putExtra(PARAMS_TRANSITIONINDEX, position);

        ActivityOptionsCompat optionsCompat;

        if (Build.VERSION.SDK_INT >= 16) {
            optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(transitionView, 0, 0, transitionView.getWidth(), transitionView.getHeight());
            if (Build.VERSION.SDK_INT >= 21) {
                optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, transitionView, urlStrings.get(position));
            }
            mContext.startActivity(intent, optionsCompat.toBundle());
        } else
            mContext.startActivity(intent);
    }


    public interface UpdateTransitionListener {
        View updateView(int position);

        String updateName(int position);
    }

}
