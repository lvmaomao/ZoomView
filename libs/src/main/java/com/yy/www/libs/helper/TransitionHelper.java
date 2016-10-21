package com.yy.www.libs.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.yy.www.libs.TransitionConstant;
import com.yy.www.libs.view.ViewerActivity;

import java.util.ArrayList;

import static com.yy.www.libs.TransitionConstant.PARAMS_ANIM_TYPE;
import static com.yy.www.libs.TransitionConstant.PARAMS_DATA;
import static com.yy.www.libs.TransitionConstant.PARAMS_IMAGE_TYPE;
import static com.yy.www.libs.TransitionConstant.PARAMS_TRANSITIONINDEX;
import static com.yy.www.libs.TransitionConstant.PARAMS_TRANSITIONNAMES;

/**
 * Created by yangyu on 16/10/21.
 */

public abstract class TransitionHelper<T> {

    /**
     * 转场动画的上下文
     */
    private Activity mContext;

    /**
     * 需要展示的数据
     */
    private ArrayList<T> showList;

    /**
     * 共享元素的name
     */
    private ArrayList<String> transitionNames;

    /**
     * 共享元素的view
     */
    private View transtionView;

    /**
     * 是否有关闭动画 默认无动画
     */
    private int anim_type = TransitionConstant.Type.TYPE_HAVE_NOT_ANIM;

    /**
     * 是否有关闭动画 默认无动画
     */
    private int image_type = TransitionConstant.Type.TYPE_IMAGE_NORMAL;

    /**
     * 点击view的位置 默认为0位置
     */
    private int start_position = 0;


    protected TransitionHelper(Activity context) {
        this.mContext = context;
    }


    public ArrayList<T> getShowList() {
        return showList;
    }

    public void setShowList(ArrayList<T> showList) {
        this.showList = showList;
    }

    public ArrayList<String> getTransitionNames() {
        return transitionNames;
    }

    public void setTransitionNames(ArrayList<String> transitionNames) {
        this.transitionNames = transitionNames;
    }

    public View getTranstionView() {
        return transtionView;
    }

    public void setTranstionView(View transtionView) {
        this.transtionView = transtionView;
    }

    public int getAnim_type() {
        return anim_type;
    }

    public void setAnim_type(int anim_type) {
        this.anim_type = anim_type;
    }

    public int getStart_position() {
        return start_position;
    }

    public void setStart_position(int start_position) {
        this.start_position = start_position;
    }

    /**
     * 启动动画
     */
    protected void startActivity() {
        Intent intent = new Intent(mContext, ViewerActivity.class);

        intent.putExtra(PARAMS_DATA, showList);
        intent.putExtra(PARAMS_TRANSITIONNAMES, transitionNames);
        intent.putExtra(PARAMS_ANIM_TYPE, anim_type);
        intent.putExtra(PARAMS_IMAGE_TYPE, image_type);
        intent.putExtra(PARAMS_TRANSITIONINDEX, start_position);

        ActivityOptionsCompat optionsCompat;

        if (Build.VERSION.SDK_INT >= 16) {
            optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(transtionView, 0, 0, transtionView.getWidth(), transtionView.getHeight());
            if (Build.VERSION.SDK_INT >= 21) {
                optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext, transtionView, transitionNames.get(start_position));
            }
            mContext.startActivity(intent, optionsCompat.toBundle());
        } else
            mContext.startActivity(intent);
    }


}
