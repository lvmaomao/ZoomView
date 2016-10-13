package com.yy.www.libs;

/**
 * Created by yangyu on 16/10/13.
 */

public class Constant {

    /**
     * 启动图片展示页面的names;
     */
    public static final String PARAMS_TRANSITIONNAMES = "transition_names";
    /**
     * 启动图片展示页面的index;
     */
    public static final String PARAMS_TRANSITIONINDEX = "transition_index";

    /**
     * 展示页面 滑动后的index;
     */
    public static final String PARAMS_RETURNINDEX = "return_index";

    /**
     * 会被recyclerview 回收的
     */
    public static final String PARAMS_OVER_TARGET = "over_target";


    public static class Target {

        public static final int TARGET_SKIP = 1000;
        public static final int TARGET_UN_SKIP = 1001;

    }


}
