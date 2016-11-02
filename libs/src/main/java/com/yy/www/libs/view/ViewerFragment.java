package com.yy.www.libs.view;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yy.www.libs.R;
import com.yy.www.libs.bean.ZoomBean;
import com.yy.www.libs.util.EnterTransitionCompat;
import com.yy.www.libs.util.SimpleTransitionListener;

import java.io.File;
import java.lang.ref.WeakReference;

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.yy.www.libs.TransitionConstant.PARAMS_DATA;

/**
 * Created by xx on 2016/9/11.
 */
public class ViewerFragment extends Fragment {

    private static final int SUCCESS = 200;

    private MyHandler myHandler = new MyHandler(this);

    private PhotoViewAttacher mAttacher;

    /**
     * views
     */
    private View view;
    private ImageView image;
    private ImageView thumbnail;
    private ViewSwitcher fade;
    private View sharedElement;

    ZoomBean zoomBean;

    private boolean hasSharedElementTransition;
    private boolean isTransitionExecuted = false;


    static class MyHandler extends Handler {
        WeakReference<ViewerFragment> mActivityReference;

        MyHandler(ViewerFragment viewerFragment) {
            mActivityReference = new WeakReference<>(viewerFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            final ViewerFragment fragment = mActivityReference.get();
            if (fragment != null) {
                if (msg.what == SUCCESS) {
                    fragment.sharedElement = fragment.image;
                    fragment.initPhotoView();
                    fragment.fadeInFullImage();
                }
            }
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zoomBean = (ZoomBean) getArguments().getSerializable(PARAMS_DATA);
        hasSharedElementTransition = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_viewer, container, false);
        image = (ImageView) view.findViewById(R.id.image);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
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

    private void loadImage() {
        if (getActivity() != null &&
                hasSharedElementTransition &&
                !isTransitionExecuted) {
            isTransitionExecuted = true;
            loadThumbnail();
            EnterTransitionCompat.addListener(getActivity().getWindow(), new SimpleTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    if (getActivity() != null) {
                        EnterTransitionCompat.removeListener(getActivity().getWindow(), this);
                        loadFullImage();
                    }
                }
            });
        } else {
            loadFullImage();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("transition_executed", isTransitionExecuted);
        super.onSaveInstanceState(outState);
    }

    View getSharedElement() {
        return sharedElement;
    }

    private void loadThumbnail() {

        if (zoomBean.getThumb() instanceof File) {
            Picasso.with(getActivity())
                    .load((File) zoomBean.getThumb())
                    .into(thumbnail, getThumbCallback());
        } else if (zoomBean.getThumb() instanceof String) {
            Picasso.with(getActivity())
                    .load((String) zoomBean.getThumb())
                    .into(thumbnail, getThumbCallback());
        } else if (zoomBean.getThumb() instanceof Integer) {
            Picasso.with(getActivity())
                    .load((Integer) zoomBean.getThumb())
                    .into(thumbnail, getThumbCallback());
        }

    }

    /**
     * 展示图片
     */
    private void loadFullImage() {

        if (zoomBean.getImage() instanceof File) {
            Picasso.with(getActivity())
                    .load((File) zoomBean.getImage())
                    .into(image, getFullCallback());
        } else if (zoomBean.getImage() instanceof String) {
            Picasso.with(getActivity())
                    .load((String) zoomBean.getImage())
                    .into(image, getFullCallback());
        } else if (zoomBean.getImage() instanceof Integer) {
            Picasso.with(getActivity())
                    .load((Integer) zoomBean.getImage())
                    .into(image, getFullCallback());
        }

    }

    @NonNull
    private Callback getThumbCallback() {
        return new Callback() {
            @Override
            public void onSuccess() {
                startPostponedEnterTransition();
            }

            @Override
            public void onError() {

            }
        };
    }

    @NonNull
    private Callback getFullCallback() {
        return new Callback() {
            @Override
            public void onSuccess() {
                myHandler.sendEmptyMessage(SUCCESS);
            }

            @Override
            public void onError() {

            }
        };
    }

    private void startPostponedEnterTransition() {
        if (hasSharedElementTransition && getActivity() != null) {
            getActivity().supportStartPostponedEnterTransition();
        }
    }

    /**
     * 将ImageView 增加 photoView特性
     */
    public void initPhotoView() {
        if (mAttacher == null)
            mAttacher = new PhotoViewAttacher(image);
        mAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (getActivity() != null)
                    ((ViewerActivity) getActivity()).closeAct();
            }
        });
    }

    public void fadeInFullImage() {
        fade.setDisplayedChild(1);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
        if (mAttacher != null) {
            mAttacher.cleanup();
            mAttacher = null;
        }

    }
}
