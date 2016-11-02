package com.example.xx.zoomview_mt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yy.www.libs.TransitionConstant;
import com.yy.www.libs.TransitionManager;
import com.yy.www.libs.bean.ZoomBean;
import com.yy.www.libs.helper.TransitionSingleHelper;

/**
 * Created by xx on 2016/9/11.
 */
public class OneActivity extends AppCompatActivity {
    TransitionSingleHelper t;
    ImageView ivShow;
    String url = "http://img.bimg.126.net/photo/K2y0zX93ZxNz84KkysxCfA==/301741175050608631.jpg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_one);

        ivShow = (ImageView) findViewById(R.id.ivShow);
        Picasso.with(this)
                .load(R.mipmap.ic_launcher)
                .into(ivShow);

        t = new TransitionManager(OneActivity.this).getSingle();
        t.setAnim_type(TransitionConstant.Type.TYPE_HAVE_ANIM);


        ivShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.startViewerActivity(v, new ZoomBean(R.mipmap.ic_launcher, R.mipmap.ic_launcher));
            }
        });
    }


}
