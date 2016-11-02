package com.yy.www.libs.helper;

import android.app.Activity;
import android.view.View;

import com.yy.www.libs.bean.ZoomBean;

import java.util.ArrayList;
import java.util.List;

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
    public void startViewerActivity(View view, List<ZoomBean<T>> urlStrings, int position) {

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
    private List optShowList(List<ZoomBean<T>> urlStrings) {
        ArrayList<ZoomBean<T>> list = new ArrayList<>(urlStrings.size());
        list.addAll(urlStrings);
        return list;
    }

    private List optTransitionNames(List<ZoomBean<T>> urlStrings) {
        ArrayList<String> list = new ArrayList<>(urlStrings.size());
        for (int i = 0; i < urlStrings.size(); i++) {
            list.add(TRANSITION_NAME_START + i);
        }
        return list;
    }


}
