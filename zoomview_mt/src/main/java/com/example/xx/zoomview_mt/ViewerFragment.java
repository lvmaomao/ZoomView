package com.example.xx.zoomview_mt;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;
import com.example.xx.zoomview_mt.utils.GlideRequestListenerAdapter;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by xx on 2016/9/11.
 */
public class ViewerFragment extends Fragment {

    private PhotoViewAttacher mAttacher;

    private View view;
    private ViewSwitcher fade;
    private ImageView thumbnail;

    private ImageView image;

    private String url;

    private boolean hasSharedElementTransition;
    private boolean isTransitionExecuted = false;


    private View sharedElement;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getArguments().getString("image");

        hasSharedElementTransition = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_viewer, container, false);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        image = (ImageView) view.findViewById(R.id.image);
        fade = (ViewSwitcher) view.findViewById(R.id.fade);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sharedElement = thumbnail;
        if (savedInstanceState != null) {
            isTransitionExecuted = savedInstanceState.getBoolean("transition_executed", false);
        }
        loadImage();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("transition_executed", isTransitionExecuted);
        super.onSaveInstanceState(outState);
    }

    View getSharedElement() {
        return sharedElement;
    }

    private void startPostponedEnterTransition() {
        if (hasSharedElementTransition) {
            getActivity().supportStartPostponedEnterTransition();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void loadImage() {
//        if (hasSharedElementTransition && !isTransitionExecuted) {
//            isTransitionExecuted = true;
//            loadThumbnail();

        loadFullImage();
        initPhotoView();
    }

//    private void loadThumbnail() {
//        Glide.with(this).load(thumbnail)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .crossFade(0)
//                .listener(new GlideRequestListenerAdapter<String, GlideDrawable>() {
//                    @Override
//                    protected void onComplete() {
//                        startPostponedEnterTransition();
//                    }
//                })
//                .into(binding.thumbnail);
//    }


    /**
     * 将ImageView 增加 photoView特性
     */
    private void initPhotoView() {
        if (mAttacher == null)
            mAttacher = new PhotoViewAttacher(image);
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (getActivity() != null) {
                    getActivity().supportFinishAfterTransition();
                    mAttacher.cleanup();
                }

            }
        });
    }


    /**
     * 展示图片
     */
    private void loadFullImage() {
        Glide.with(this).load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade(0)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .listener(new GlideRequestListenerAdapter<String, GlideDrawable>() {

//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        changeImageScaleType(resource);
//                        return super.onResourceReady(resource, model, target, isFromMemoryCache, isFirstResource);
//                    }

                    @Override
                    protected void onSuccess(GlideDrawable resource) {
                        sharedElement = image;
                        fadeInFullImage();
                    }
                })
                .into(image);
    }

    private void fadeInFullImage() {
        fade.setDisplayedChild(1);
    }


    /**
     * 长途特殊处理,从后台要宽高比,跳转新页面单独处理
     *
     * @param resource
     */
    void changeImageScaleType(GlideDrawable resource) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm.getDefaultDisplay().getWidth() * 1.0f / wm.getDefaultDisplay().getHeight() * 1.0f
                > resource.getIntrinsicWidth() * 1.0f / resource.getIntrinsicHeight() * 1.0f) {
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);

        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
