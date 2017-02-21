package com.example.xx.zoomview_mt;

import android.animation.Animator;
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

    private static final int STATE_THUMB = 0;
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
    private int statusBarHeight;
    private int mWidth;
    private int mHeight;

    boolean isBeThumb = false;
    boolean isBeFull = false;

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
        //beThumbView();
        //变换中变成全图
        beFullView();
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
        flp.width = (int) originImageBean.width;
        flp.height = (int) originImageBean.height;
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
        ImageBean afterImageBean = originImageBean.clone();
        afterImageBean.translationX = mWidth / 2 - originImageBean.width / 2;
        afterImageBean.translationY = mHeight / 2 - originImageBean.height / 2;
        if (!isBeThumb) {
            playAnim(originImageBean, afterImageBean, STATE_THUMB);
            isBeThumb = true;
        }
    }


    private void beFullView() {
        progress.setVisibility(View.VISIBLE);
        Picasso.with(getActivity())
                .load(thumbUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        //获取真实的宽高比例
                        targetImageBean = originImageBean.clone();
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

                        if (!isBeFull) {
                            playAnim(originImageBean, targetImageBean, STATE_FULL);
                            isBeFull = true;
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


    private void showHDImage() {
        Picasso.with(getActivity())
                .load(url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        image.setImageBitmap(bitmap);
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        image.setImageDrawable(errorDrawable);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        image.setImageDrawable(placeHolderDrawable);
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
     *
     * @param currentItem
     */
    public void close(int currentTY, int currentItem) {
        ImageBean beforeImageBean = targetImageBean == null ? currentImageBean.clone() : targetImageBean.clone();
        beforeImageBean.translationY += currentTY;
        ImageBean afterImageBean = imageViewList.get(currentItem);
        playAnim(beforeImageBean, afterImageBean, STATE_CLOSE);
    }

    private synchronized void playAnim(final ImageBean before, final ImageBean after, final int state) {
        if (animator != null) {
            animator.cancel();
        }
        if (currentImageBean == null) {
            currentImageBean = originImageBean.clone();
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
                        showHDImage();
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


}
