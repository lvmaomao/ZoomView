package com.example.xx.zoomview_mt;

/**
 * Created by yangyu on 2017/2/20.
 * 图片的动画
 */

public class AnimUtils {

    /**
     * 将指定的ImageView形态(尺寸大小，缩放，旋转，平移，透明度)逐步转化到期望值
     */
//    private void animSourceViewStateTransform(ImageView view, final ViewState vsResult) {
//        if (view == null) return;
//        if (animImageTransform != null) animImageTransform.cancel();
//
//        animImageTransform = ViewState.restoreByAnim(view, vsResult.mTag).addListener(mAnimTransitionStateListener).create();
//
//        // 如果是退出查看操作，动画执行完后，原始被点击的ImageView恢复可见
//        if (animImageTransform != null) {
//            if (vsResult.mTag == ViewState.STATE_ORIGIN) {
//                animImageTransform.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        if (iAfter != null) iAfter.setVisibility(View.VISIBLE);
//                        setVisibility(View.GONE);
//                    }
//                });
//            }
//            animImageTransform.start();
//        }
//    }
//
//    /**
//     * 执行ImageWatcher自身的背景色渐变至期望值[colorResult]的动画
//     */
//    private void animBackgroundTransform(final int colorResult) {
//        if (colorResult == mBackgroundColor) return;
//        if (animBackground != null) animBackground.cancel();
//        final int mCurrentBackgroundColor = mBackgroundColor;
//        animBackground = ValueAnimator.ofFloat(0, 1).setDuration(300);
//        animBackground.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float p = (float) animation.getAnimatedValue();
//                setBackgroundColor(mColorEvaluator.evaluate(p, mCurrentBackgroundColor, colorResult));
//            }
//        });
//        animBackground.start();
//    }


}
