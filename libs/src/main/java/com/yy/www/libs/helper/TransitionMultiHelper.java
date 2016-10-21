package com.yy.www.libs.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yy.www.libs.TransitionConstant.PARAMS_RETURNINDEX;
import static com.yy.www.libs.TransitionConstant.TRANSITION_NAME_START;

/**
 * Created by yangyu on 16/10/9.
 */

public class TransitionMultiHelper<T> extends TransitionHelper {

    private Bundle transitionState;

    private String transitionName;

    private View transitionView;

    public TransitionMultiHelper(Activity activity) {
        super(activity);
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
        transitionName = (String) getTransitionNames().get(position);
        if (listener.updateView(position) != null)
            transitionView = listener.updateView(position);
    }


    /**
     * multi Image
     *
     * @param urlStrings 位移的描述
     * @param position   点击的位置
     */
    public void startViewerActivity(View view, ArrayList<T> urlStrings, int position) {
        setStart_position(position);
        setShowList(optShowList(urlStrings));
        setTransitionNames(optTransitionNames(urlStrings));
        setTranstionView(view);

        startActivity();
    }

    /**
     * 获取需要展示的内容
     *
     * @param urlStrings
     * @return
     */
    private ArrayList optShowList(ArrayList<T> urlStrings) {
        ArrayList<T> list = new ArrayList<>(urlStrings.size());
        list.addAll(urlStrings);
        return list;
    }

    private ArrayList optTransitionNames(ArrayList<T> urlStrings) {
        ArrayList<String> list = new ArrayList<>(urlStrings.size());
        for (int i = 0; i < urlStrings.size(); i++) {
            list.add(TRANSITION_NAME_START + i);
        }
        return list;
    }

    public interface UpdateTransitionListener {
        View updateView(int position);

        String updateName(int position);
    }

}
