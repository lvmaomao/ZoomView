package com.example.xx.zoomview_mt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by xx on 2016/9/11.
 */
public class OneActivity extends ImageFromActivity {

    ImageView ivShow;
    String url = "http://img.bimg.126.net/photo/K2y0zX93ZxNz84KkysxCfA==/301741175050608631.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_one);

        ivShow = (ImageView) findViewById(R.id.ivShow);
        Glide.with(this)
                .load(url)
                .into(ivShow);
        ivShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startViewerActivity(OneActivity.this, v, url);
            }
        });
    }

    @Override
    String getBackTransitionName(int index) {
        return url;
    }

    @Override
    View getBackTransitionView(int index) {
        return ivShow;
    }


}
