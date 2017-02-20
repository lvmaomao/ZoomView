package com.example.xx.zoomview_mt;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by yangyu on 2017/2/16.
 * 图片放大fragment
 */

public class ImageFragment extends Fragment {

    private static final int ANIM_DURATION = 300;

    /**
     * 动画
     */
    private ValueAnimator thumbAnim;
    private ValueAnimator fullAnim;
    private ValueAnimator backAnim;
    private ValueAnimator animator = null;


    /**
     * views
     */
    private View rootView;
    private ImageView image;
    private ProgressBar progress;

    /**
     * data
     */
    List<ImageBean> imageViewList;  // imageView引用集合 传递方需要控制数量。
    List<String> thumbUrlList; //缩略图集合
    List<String> urlList; //原图集合
    int position = 0;

    private String thumbUrl;
    private String url;
    private ImageBean originImageBean;
    private ImageBean currentImageBean;
    private ImageBean targetImageBean;
    private int timeLeft; // beThumb的剩余时间
    private int statusBarHeight;
    private int mWidth;
    private int mHeight;

    public static ImageFragment newInstance(Bundle args) {
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_image, container, false);
        statusBarHeight = calcStatusBarHeight(getActivity());
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mWidth = dm.widthPixels; // 屏幕宽（像素，如：px）
        mHeight = dm.heightPixels; // 屏幕高（像素，如：px）
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image = (ImageView) rootView.findViewById(R.id.image);
        progress = (ProgressBar) rootView.findViewById(R.id.progress);
        getArgs();
        //创建imageView位置与前页面相同
        createImageView();
        //缩略图位置在fragment正中间
        beThumbView();
        //变换中变成全图
//        beFullView();
    }

    /**
     * 获取数据
     */
    private void getArgs() {
        Bundle bundle = getArguments();
        thumbUrlList = bundle.getStringArrayList(ImageZoomActivity.THUMB_DATA);
        urlList = bundle.getStringArrayList(ImageZoomActivity.ORIGINAL_DATA);
        imageViewList = bundle.getParcelableArrayList(ImageZoomActivity.IMAGE_DATA);
        position = bundle.getInt(ImageZoomActivity.DATA_POSITION);

        if (thumbUrlList != null && urlList != null && imageViewList != null) {
            originImageBean = imageViewList.get(position);
            thumbUrl = thumbUrlList.get(position);
            url = urlList.get(position);
        } else {
            throw new NullPointerException("data not be null");
        }

        if (originImageBean != null) {
            originImageBean.translationY -= statusBarHeight;
        }
    }

    private void createImageView() {
        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) image.getLayoutParams();
        flp.width = originImageBean.width;
        flp.height = originImageBean.height;
        image.setLayoutParams(flp);
        image.setTranslationX(originImageBean.translationX);
        image.setTranslationY(originImageBean.translationY);
        Picasso.with(getActivity())
                .load(thumbUrl)
                .into(image);
    }

    /**
     * 缩略图变化
     */
    private void beThumbView() {
        progress.setVisibility(View.VISIBLE);
//        thumbAnim = ValueAnimator.ofFloat(0, 1).setDuration(ANIM_DURATION);
//        final float targetX = mWidth / 2 - originImageBean.width / 2;
//        final float targetY = mHeight / 2 - originImageBean.height / 2;
//        currentImageBean = originImageBean;
//        thumbAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float p = (float) animation.getAnimatedValue();
//                currentImageBean.translationX = originImageBean.translationX + (targetX - originImageBean.translationX) * p;
//                currentImageBean.translationY = originImageBean.translationY + (targetY - originImageBean.translationY) * p;
//                timeLeft = (int) (ANIM_DURATION - ANIM_DURATION * p);
//                image.setTranslationX(currentImageBean.translationX);
//                image.setTranslationY(currentImageBean.translationY);
//                if (originImageBean.translationX != targetX) {
//                    image.requestLayout();
//                }
//            }
//        });
//        thumbAnim.start();
        ImageBean afterImageBean = originImageBean;
        afterImageBean.translationX = mWidth / 2 - originImageBean.width / 2;
        afterImageBean.translationY = mHeight / 2 - originImageBean.height / 2;
        playAnim(originImageBean, afterImageBean);
    }


    private void playAnim(final ImageBean before, final ImageBean after) {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
        if (currentImageBean == null) {
            currentImageBean = new ImageBean();
        }
        animator = ValueAnimator.ofFloat(0, 1).setDuration(ANIM_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float p = (float) animation.getAnimatedValue();
                image.setTranslationX(before.translationX + (after.translationX - before.translationX) * p);
                image.setTranslationY(before.translationY + (after.translationY - before.translationY) * p);
                image.setScaleX(before.scaleX + (after.scaleX - before.scaleX) * p);
                image.setScaleY(before.scaleY + (after.scaleY - before.scaleY) * p);

                currentImageBean.translationX = image.getTranslationX();
                currentImageBean.translationY = image.getTranslationY();
                currentImageBean.scaleX = image.getScaleX();
                currentImageBean.scaleY = image.getScaleY();
//                image.setRotation((before.rotation + (after.rotation - before.rotation) * p) % 360);
//                image.setAlpha((before.alpha + (after.alpha - before.alpha) * p));
                if (before.width != after.width && before.height != after.height
                        && after.width != 0 && after.height != 0) {
                    image.getLayoutParams().width = (int) (before.width + (after.width - before.width) * p);
                    image.getLayoutParams().height = (int) (before.height + (after.height - before.height) * p);
                    image.requestLayout();
                }
            }
        });
        animator.start();
    }


    private void beFullView() {
        Picasso.with(getActivity())
                .load(url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        //获取真实的宽高比例
                        targetImageBean = new ImageBean();
                        targetImageBean.width = bitmap.getWidth();
                        targetImageBean.height = bitmap.getHeight();
                        //宽 > 高
                        if (targetImageBean.width * 1f / targetImageBean.height > mWidth * 1f / mHeight) {
                            targetImageBean.width = mWidth;
                            targetImageBean.height = (int) (targetImageBean.width * 1f / targetImageBean.width * targetImageBean.height);
                            targetImageBean.translationX = 0;
                            targetImageBean.translationY = (mHeight - targetImageBean.height) / 2;
                            image.setTag(R.id.image_orientation, "horizontal");
                        } else {
                            //高 > 宽
                            targetImageBean.height = mHeight;
                            targetImageBean.width = (int) (targetImageBean.height * 1f / targetImageBean.height * targetImageBean.width);
                            targetImageBean.translationY = 0;
                            targetImageBean.translationX = (mWidth - targetImageBean.width) / 2;
                            image.setTag(R.id.image_orientation, "vertical");
                        }
                        image.setImageBitmap(bitmap);
                        notifyItemChangedState(false, false);

//                        if (thumbAnim != null && thumbAnim.isRunning()) {
//                            thumbAnim.cancel();
//                        }
                        fullAnim = ValueAnimator.ofFloat(0, 1).setDuration(Math.max(timeLeft, 150)); //最短150毫秒
                        fullAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float p = (float) animation.getAnimatedValue();
                                image.setTranslationX(currentImageBean.translationX + (targetImageBean.translationX - currentImageBean.translationX) * p);
                                image.setTranslationY(currentImageBean.translationY + (targetImageBean.translationY - currentImageBean.translationY) * p);
                                if (currentImageBean.width != targetImageBean.width && currentImageBean.height != targetImageBean.height
                                        && targetImageBean.width != 0 && targetImageBean.height != 0) {
                                    image.getLayoutParams().width = (int) (currentImageBean.width + (targetImageBean.width - currentImageBean.width) * p);
                                    image.getLayoutParams().height = (int) (currentImageBean.height + (targetImageBean.height - currentImageBean.height) * p);
                                    image.requestLayout();
                                }
                            }
                        });
                        fullAnim.start();

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        notifyItemChangedState(true, false);

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        notifyItemChangedState(false, image.getDrawable() == null);
                    }
                });
    }

    void notifyItemChangedState(boolean loading, boolean error) {
        if (loading) {
            progress.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
        }
//        ImageView errorView = (ImageView) itemView.getChildAt(2);
//        errorView.setAlpha(1f);
//        errorView.setVisibility(error ? View.VISIBLE : View.GONE);
    }


    public static int calcStatusBarHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    /**
     * 关闭方法
     *
     * @param currentItem
     */
    public void close(int currentItem) {

        final ImageBean pullImageBean = targetImageBean;
        pullImageBean.translationY = image.getTranslationY();
        originImageBean = imageViewList.get(currentItem);

        if (fullAnim.isRunning()) {
            fullAnim.cancel();
        }

        backAnim = ValueAnimator.ofFloat(0, 1).setDuration(ANIM_DURATION); //最短150毫秒
        backAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float p = (float) animation.getAnimatedValue();
                image.setTranslationX(pullImageBean.translationX + (originImageBean.translationX - pullImageBean.translationX) * p);
                image.setTranslationY(pullImageBean.translationY + (originImageBean.translationY - pullImageBean.translationY) * p);
                if (pullImageBean.width != targetImageBean.width && pullImageBean.height != targetImageBean.height
                        && targetImageBean.width != 0 && targetImageBean.height != 0) {
                    image.getLayoutParams().width = (int) (pullImageBean.width + (originImageBean.width - pullImageBean.width) * p);
                    image.getLayoutParams().height = (int) (pullImageBean.height + (originImageBean.height - pullImageBean.height) * p);
                    image.requestLayout();
                }
                if (getActivity() != null && p == 1) {
                    getActivity().finish();
                }
            }
        });
        backAnim.start();

    }


}
