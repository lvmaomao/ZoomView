package com.example.xx.zoomview_mt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

/**
 * Created by yangyu on 2017/2/16.
 */

public class ImageFragment extends Fragment {

    /**
     * views
     */
    private View rootView;
    private ImageView image;
    private ProgressBar progress;

    /**
     * data
     */
    private String thumbUrl;
    private String url;
    private ImageBean imageBean;
    private int statusBarHeight;
    private int mWidth;
    private int mHeight;

    boolean isTranslucentStatus = false;


    public static ImageFragment newInstance(Bundle args) {
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_image, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            isTranslucentStatus = true;
        }
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
        showStartImage();
    }

    /**
     * 获取数据
     */
    private void getArgs() {
        Bundle bundle = getArguments();
        thumbUrl = bundle.getString(ImageZoomActivity.THUMB_DATA);
        url = bundle.getString(ImageZoomActivity.ORIGINAL_DATA);
        imageBean = bundle.getParcelable(ImageZoomActivity.IMAGE_DATA);
    }

    /**
     * 将上个页面的图片在此页面创建出来
     */
    private void showStartImage() {
        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) image.getLayoutParams();
        flp.width = imageBean.width;
        flp.height = imageBean.height;
        image.setLayoutParams(flp);
        image.setTranslationX(imageBean.translationX);
        image.setTranslationY(imageBean.translationY - statusBarHeight);
        Picasso.with(getActivity())
                .load(thumbUrl)
                .into(new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        final int sourceDefaultWidth, sourceDefaultHeight, sourceDefaultTranslateX, sourceDefaultTranslateY;
                        //获取真实的宽高比例
                        int resourceImageWidth = bitmap.getWidth();
                        int resourceImageHeight = bitmap.getHeight();
//宽 > 高
                        if (resourceImageWidth * 1f / resourceImageHeight > mWidth * 1f / mHeight) {
                            sourceDefaultWidth = mWidth;
                            sourceDefaultHeight = (int) (sourceDefaultWidth * 1f / resourceImageWidth * resourceImageHeight);
                            sourceDefaultTranslateX = 0;
                            sourceDefaultTranslateY = (mHeight - sourceDefaultHeight) / 2;
                            image.setTag(R.id.image_orientation, "horizontal");
                        } else {
                            //高 > 宽
                            sourceDefaultHeight = mHeight;
                            sourceDefaultWidth = (int) (sourceDefaultHeight * 1f / resourceImageHeight * resourceImageWidth);
                            sourceDefaultTranslateY = 0;
                            sourceDefaultTranslateX = (mWidth - sourceDefaultWidth) / 2;
                            image.setTag(R.id.image_orientation, "vertical");
                        }
                        image.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
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
}
