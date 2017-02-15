package com.example.xx.zoomview_mt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

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

    public static ImageFragment newInstance(Bundle args) {
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_image, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image = (ImageView) rootView.findViewById(R.id.image);
        progress = (ProgressBar) rootView.findViewById(R.id.progress);
        getArgs();
    }

    /**
     * 获取数据
     */
    private void getArgs() {
        Bundle bundle = getArguments();
        thumbUrl = bundle.getString(ImageZoomActivity.THUMB_DATA);
        url = bundle.getString(ImageZoomActivity.ORIGINAL_DATA);
    }


}
