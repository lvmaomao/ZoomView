package com.yy.www.libs.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yy.www.libs.R;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by xx on 2016/9/11.
 */
public class ViewerFragment extends Fragment {

    private PhotoViewAttacher mAttacher;

    private View view;

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
        image = (ImageView) view.findViewById(R.id.image);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        sharedElement = image;
        if (savedInstanceState != null) {
            isTransitionExecuted = savedInstanceState.getBoolean("transition_executed", false);
        }
        loadFullImage();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("transition_executed", isTransitionExecuted);
        super.onSaveInstanceState(outState);
    }

    View getSharedElement() {
        return sharedElement;
    }


    /**
     * 将ImageView 增加 photoView特性
     */
    private void initPhotoView() {
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

    /**
     * 展示图片
     */
    private void loadFullImage() {
        Picasso.with(getActivity())
                .load(url)
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        initPhotoView();
                    }

                    @Override
                    public void onError() {

                    }
                });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAttacher != null) {
            mAttacher.cleanup();
            mAttacher = null;
        }

    }
}
