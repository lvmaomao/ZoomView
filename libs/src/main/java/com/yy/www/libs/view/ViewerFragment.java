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
import com.yy.www.libs.util.EnterTransitionCompat;
import com.yy.www.libs.util.SimpleTransitionListener;

import java.io.File;
import java.lang.ref.WeakReference;

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.yy.www.libs.TransitionConstant.PARAMS_DATA;
import static com.yy.www.libs.TransitionConstant.PARAMS_IMAGE_SOURCE;
import static com.yy.www.libs.TransitionConstant.Type.TYPE_FILE;
import static com.yy.www.libs.TransitionConstant.Type.TYPE_REMOTE;
import static com.yy.www.libs.TransitionConstant.Type.TYPE_RESID;

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

    /**
     * type: img。resid。file
     */
    private int imageType;

    /**
     * params
     */
    private String imageUrl;
    private int imageResid;
    private File imageFile;

    private boolean hasSharedElementTransition;
    private boolean isTransitionExecuted = false;


    static class MyHandler extends Handler {
        WeakReference<ViewerFragment> mActivityReference;

        MyHandler(ViewerFragment viewerFragment) {
            mActivityReference = new WeakReference<ViewerFragment>(viewerFragment);
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
        imageType = getArguments().getInt(PARAMS_IMAGE_SOURCE);
        switch (imageType) {
            case TYPE_FILE:
                imageFile = new File(getArguments().getString(PARAMS_DATA));
                break;
            case TYPE_REMOTE:
                imageUrl = getArguments().getString(PARAMS_DATA);
                break;
            case TYPE_RESID:
                imageResid = getArguments().getInt(PARAMS_DATA, 0);
                break;
        }
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
        if (hasSharedElementTransition &&
                !isTransitionExecuted &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            isTransitionExecuted = true;
            loadThumbnail();
            EnterTransitionCompat.addListener(getActivity().getWindow(), new SimpleTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    EnterTransitionCompat.removeListener(getActivity().getWindow(), this);
                    loadFullImage();
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

        switch (imageType) {
            case TYPE_FILE:
                Picasso.with(getActivity())
                        .load(imageFile)
                        .into(thumbnail, getThumbCallback());

                break;
            case TYPE_REMOTE:
                Picasso.with(getActivity())
                        .load(imageUrl)
                        .into(thumbnail, getThumbCallback());
                break;
            case TYPE_RESID:
                Picasso.with(getActivity())
                        .load(imageResid)
                        .into(thumbnail, getThumbCallback());
                break;
        }
    }

    /**
     * 展示图片
     */
    private void loadFullImage() {

        switch (imageType) {
            case TYPE_FILE:
                Picasso.with(getActivity())
                        .load(imageFile)
                        .into(image, getFullCallback());

                break;
            case TYPE_REMOTE:
                Picasso.with(getActivity())
                        .load(imageUrl)
                        .into(image, getFullCallback());
                break;
            case TYPE_RESID:
                Picasso.with(getActivity())
                        .load(imageResid)
                        .into(image, getFullCallback());
                break;
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
