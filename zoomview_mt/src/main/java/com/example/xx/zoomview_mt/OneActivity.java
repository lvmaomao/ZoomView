package com.example.xx.zoomview_mt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.xx.zoomview_mt.ImageZoomActivity.IMAGE_THUMB_URL;
import static com.example.xx.zoomview_mt.ImageZoomActivity.IMAGE_URL;
import static com.example.xx.zoomview_mt.ImageZoomActivity.IMAGE_VIEWS;

/**
 * Created by xx on 2016/9/11.
 */
public class OneActivity extends AppCompatActivity {
    ImageView ivShow;
    List<ImageBean> ivs = new ArrayList<>();
    List<String> url = new ArrayList<>();
    List<String> thumbUrl = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_one);

        ivShow = (ImageView) findViewById(R.id.ivShow);
        Picasso.with(this)
                .load("http://img.my.csdn.net/uploads/201701/17/1484647899_2806.jpg")
                .into(ivShow);

        ivShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivs.add(new ImageBean(ivShow));
                url.add("http://img.my.csdn.net/uploads/201701/06/1483664940_9893.jpg");
                thumbUrl.add("http://img.my.csdn.net/uploads/201701/17/1484647899_2806.jpg");

                Intent intent = new Intent(OneActivity.this, ImageZoomActivity.class);
                intent.putExtra(IMAGE_VIEWS, (Serializable) ivs);
                intent.putStringArrayListExtra(IMAGE_THUMB_URL, (ArrayList<String>) thumbUrl);
                intent.putStringArrayListExtra(IMAGE_URL, (ArrayList<String>) url);
                //默认位置为0 所以不传递 startPosition
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }


}
