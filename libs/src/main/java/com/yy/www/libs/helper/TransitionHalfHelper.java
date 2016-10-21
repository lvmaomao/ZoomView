package com.yy.www.libs.helper;

import android.app.Activity;
import android.view.View;

import java.util.ArrayList;

import static com.yy.www.libs.TransitionConstant.TRANSITION_NAME_START;

/**
 * Created by yangyu on 16/10/9.
 */

public class TransitionHalfHelper<T> extends TransitionHelper {

    private ArrayList<String> transitionNames;

    public TransitionHalfHelper(Activity activity) {
        super(activity);
    }

    /**
     * multi Image
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


}
