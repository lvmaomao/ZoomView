package com.yy.www.libs;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yy.www.libs.bean.ImageBean;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by yangyu on 2017/2/16.
 * 图片放大fragment
 */

public class ImageFragment extends LazyFragment implements PhotoViewAttacher.OnPhotoTapListener {

    public static final String TAG = "ImageFragment";

    private static final int ANIM_DURATION = 300;

    private static final int STATE_FULL = 1;
    private static final int STATE_CLOSE = 2;

    /**
     * 动画
     */
    private ValueAnimator animator = null;


    /**
     * views
     */
    private View rootView;
    private ImageView image;
    private PhotoView imageBig;
    private ProgressBar progress;

    /**
     * data
     */
    private String thumbUrl;
    private String url;
    private ImageBean originImageBean;
    private ImageBean currentImageBean;
    private ImageBean targetImageBean;
    private int mWidth;
    private int mHeight;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    ImageZoomActivity activity;

    private int statusBarHeight;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ImageZoomActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_image, container, false);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mWidth = dm.widthPixels; // 屏幕宽（像素，如：px）
        mHeight = dm.heightPixels; // 屏幕高（像素，如：px）
        statusBarHeight = calcStatusBarHeight(activity);
        isPrepared = true;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("ImageFragment", "onViewCreated");
        image = (ImageView) rootView.findViewById(R.id.image);
        imageBig = (PhotoView) rootView.findViewById(R.id.imageBig);
        progress = (ProgressBar) rootView.findViewById(R.id.progress);
        imageBig.setOnPhotoTapListener(this);
        getArgs();
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        //填充各控件的数据
        initThumbImage();
    }

    public static ImageFragment newInstance(Bundle args) {
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 获取数据
     */
    private void getArgs() {
        Bundle bundle = getArguments();
        url = bundle.getString("url");
        thumbUrl = bundle.getString("thumbUrl");
        originImageBean = bundle.getParcelable("start_image");
        if (originImageBean.state == ImageBean.STATE_NORMAL) {
            currentImageBean = originImageBean.clone();
        }
    }

    private void createImageView(ImageBean bean) {
        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) image.getLayoutParams();
        flp.width = (int) bean.width;
        flp.height = (int) bean.height;
        image.setLayoutParams(flp);
        image.setTranslationX(bean.translationX);
        image.setTranslationY(bean.translationY - statusBarHeight);
        imageBig.setTranslationY(statusBarHeight / 2);

    }

    /**
     * 初始化缩略图
     */
    private void initThumbImage() {
        progress.setVisibility(View.VISIBLE);
        if (originImageBean != null) {
            createImageView(originImageBean);
            Picasso.with(getActivity())
                    .load(thumbUrl)
                    .into(image, new Callback() {
                        @Override
                        public void onSuccess() {
                            zoomThumbView();
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
    }

    private void zoomThumbView() {
        Picasso.with(getActivity())
                .load(thumbUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        //获取真实的宽高比例
                        if (originImageBean == null) {
                            targetImageBean = new ImageBean();
                        } else {
                            targetImageBean = originImageBean.clone();
                        }
                        int resourceImageWidth = bitmap.getWidth();
                        int resourceImageHeight = bitmap.getHeight();
                        //宽 > 高
                        if (resourceImageWidth * 1f / resourceImageHeight > mWidth * 1f / mHeight) {
                            targetImageBean.width = mWidth;
                            targetImageBean.height = (int) (targetImageBean.width * 1f / resourceImageWidth * resourceImageHeight);
                            targetImageBean.translationX = 0;
                            targetImageBean.translationY = (mHeight - targetImageBean.height) / 2;
                        } else {
                            //高 > 宽
                            targetImageBean.height = mHeight;
                            targetImageBean.width = (int) (targetImageBean.height * 1f / resourceImageHeight * resourceImageWidth);
                            targetImageBean.translationY = 0;
                            targetImageBean.translationX = (mWidth - targetImageBean.width) / 2;
                        }
                        image.setImageBitmap(bitmap);
                        notifyItemChangedState(false, false);
                        if (activity != null && originImageBean != null && !activity.isAnim && activity.isStartPager()
//                                thumbUrl.equals(activity.getStartThumbUrl())
                                ) { //播放动画 达到指定大小
                            playAnim(originImageBean, targetImageBean, STATE_FULL);
                            activity.setAnim(true);
                        } else { //不播放动画,直接达到指定位置
                            createImageView(targetImageBean);
                            changeImage();
                        }

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });

    }

    /**
     * 切换高清图
     */
    private void changeImage() {
        Picasso.with(activity)
                .load(url)
                .into(imageBig, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageBig.setVisibility(View.VISIBLE);
                        image.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    void notifyItemChangedState(boolean loading, boolean error) {

        if (loading) {
            progress.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
        }
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
     */
    public void close(int currentTY, ImageBean imageBean) {
        //从 目标位置或者变化位置获取启动动画的初始位置
        image.setVisibility(View.VISIBLE);
        imageBig.setVisibility(View.GONE);
        ImageBean beforeImageBean = targetImageBean == null ? currentImageBean.clone() : targetImageBean.clone();
        //对 其实动画的位置进行y轴修正
        beforeImageBean.translationY += currentTY;
        imageBean.translationY -= statusBarHeight;
        playAnim(beforeImageBean, imageBean, STATE_CLOSE);
    }

    private synchronized void playAnim(final ImageBean before, final ImageBean after, final int state) {
        if (animator != null) {
            animator.cancel();
        }
        if (currentImageBean == null) {
            currentImageBean = targetImageBean.clone();
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
                image.setAlpha(before.alpha + (after.alpha - before.alpha) * p);

                currentImageBean.translationX = image.getTranslationX();
                currentImageBean.translationY = image.getTranslationY();
                currentImageBean.scaleX = image.getScaleX();
                currentImageBean.scaleY = image.getScaleY();
                if (before.width != after.width && before.height != after.height
                        && after.width != 0 && after.height != 0) {
                    image.getLayoutParams().width = (int) (before.width + (after.width - before.width) * p);
                    image.getLayoutParams().height = (int) (before.height + (after.height - before.height) * p);
                    image.requestLayout();
                }
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                switch (state) {
                    case STATE_CLOSE:
                        if (getActivity() != null) {
                            getActivity().finish();
                            getActivity().overridePendingTransition(0, 0);
                        }
                        break;
                    case STATE_FULL:
                        changeImage();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        if (activity != null) {
            activity.closeAct();
        }
    }
}
