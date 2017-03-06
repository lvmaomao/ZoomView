package com.example.xx.zoomview_mt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xx.zoomview_mt.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyu on 16/10/12.
 */
public class TestFragment extends Fragment implements TestAdapter.onImageViewClickListener {


    LinearLayoutManager manager;

    RecyclerView rvImage;

    List<String> urls;

    public static TestFragment getInstance() {

        return new TestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        rvImage = (RecyclerView) view.findViewById(R.id.rvImage);
        initDummy();
        initRv();
        return view;
    }

    private void initDummy() {
        if (urls == null)
            urls = new ArrayList<>();
        urls.add("http://img.my.csdn.net/uploads/201701/06/1483664940_9893.jpg");
        urls.add("http://img.my.csdn.net/uploads/201701/06/1483664940_3308.jpg");
        urls.add("http://img.my.csdn.net/uploads/201701/06/1483664927_3920.png");
        urls.add("http://img.my.csdn.net/uploads/201701/06/1483664926_8360.png");
        urls.add("http://img.my.csdn.net/uploads/201701/06/1483664926_6184.png");
        urls.add("http://img.my.csdn.net/uploads/201701/06/1483664925_8382.png");
        urls.add("http://img.my.csdn.net/uploads/201701/06/1483664925_2087.jpg");
        urls.add("http://img.my.csdn.net/uploads/201701/06/1483664777_5730.png");
        urls.add("http://img.my.csdn.net/uploads/201701/06/1483664741_1378.jpg");
    }

    private void initRv() {
        manager = new LinearLayoutManager(getActivity());
        rvImage.setLayoutManager(manager);
        rvImage.setAdapter(new TestAdapter(getActivity(), this, urls));
    }

    @Override
    public void onImageClick(View v, int position) {

    }


}
