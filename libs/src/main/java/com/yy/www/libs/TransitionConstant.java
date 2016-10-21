package com.yy.www.libs;

/**
 * Created by yangyu on 16/10/13.
 */

public class TransitionConstant {

    /**
     * 启动图片展示页面的names;
     */
    public static final String PARAMS_TRANSITIONNAMES = "transition_names";
    /**
     * 启动图片展示页面的index;
     */
    public static final String PARAMS_TRANSITIONINDEX = "transition_index";
    /**
     * 第二页面需要展示的数据
     */
    public static final String PARAMS_DATA = "transition_data";

    /**
     * 展示页面 滑动后的index;
     */
    public static final String PARAMS_RETURNINDEX = "return_index";


    /**
     * 是否有关闭动画
     */
    public static final String PARAMS_ANIM_TYPE = "anim_type";

    /**
     * 长短图
     */
    public static final String PARAMS_IMAGE_TYPE = "image_type";

    /**
     * 图片的类型
     */
    public static final String PARAMS_IMAGE_SOURCE = "image_source";


    public static class Type {

        public static final int TYPE_FILE = 3001;

        public static final int TYPE_RESID = 3002;

        public static final int TYPE_REMOTE = 3003;

        /**
         * 长图
         */
        public static final int TYPE_IMAGE_LONG = 2001;
        /**
         * 正常图
         */
        public static final int TYPE_IMAGE_NORMAL = 2002;

        /**
         * 不需要关闭动画
         */
        public static final int TYPE_HAVE_NOT_ANIM = 1000;
        /**
         * 需要关闭动画
         */
        public static final int TYPE_HAVE_ANIM = 1001;

    }

    /**
     * 设置transitionName 规则
     */
    public static final String TRANSITION_NAME_START = "transition_";


}
