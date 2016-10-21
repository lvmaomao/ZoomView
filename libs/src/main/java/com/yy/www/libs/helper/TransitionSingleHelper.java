package com.yy.www.libs.helper;

import android.app.Activity;
import android.view.View;

import java.util.ArrayList;

import static com.yy.www.libs.TransitionConstant.TRANSITION_NAME_START;

/**
 * 针对单个ImageView 的位移
 */
public class TransitionSingleHelper<T> extends TransitionHelper {

    public TransitionSingleHelper(Activity activity) {
        super(activity);
    }


    /**
     * single Image
     */
    public void startViewerActivity(View view, T urlString) {

        setShowList(optShowList(urlString));
        setTransitionNames(optTransitionNames());
        setTranstionView(view);

        startActivity();
    }


    /**
     * 获取需要展示的内容
     *
     * @param urlString
     * @return
     */
    private ArrayList optShowList(T urlString) {
        ArrayList<T> list = new ArrayList<>(1);
        list.add(urlString);
        return list;
    }

    private ArrayList optTransitionNames() {
        ArrayList<String> list = new ArrayList<>(1);
        list.add(TRANSITION_NAME_START + 0);
        return list;
    }


}
