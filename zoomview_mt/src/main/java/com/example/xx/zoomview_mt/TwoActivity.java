package com.example.xx.zoomview_mt;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.example.xx.zoomview_mt.nineGridView.NineGridImageView;
import com.example.xx.zoomview_mt.nineGridView.NineGridImageViewAdapter;
import com.squareup.picasso.Picasso;
import com.yy.www.libs.view.ThumbnailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyu on 16/9/13.
 */
public class TwoActivity extends ThumbnailActivity {

    NineGridImageView nineGridImageView;

    private List<String> IMG_URL_LIST = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        initDatas();
        initView();
    }

    private void initView() {
        nineGridImageView = (NineGridImageView) findViewById(R.id.nineGridImageView);
        nineGridImageView.setAdapter(mAdapter);
        nineGridImageView.setImagesData(IMG_URL_LIST);
    }

    private void initDatas() {
        IMG_URL_LIST.clear();
        IMG_URL_LIST.add("http://www.5djiaren.com/uploads/2016-06/08-165944_676.jpg");
        IMG_URL_LIST.add("http://www.5djiaren.com/uploads/2016-06/08-165945_452.jpg");
        IMG_URL_LIST.add("http://p.ishowx.com/uploads/allimg/160819/486-160QZT454.jpg");
        IMG_URL_LIST.add("http://dmr.nosdn.127.net/1o51ADEcJbidc5Y2FFYnfA==/6896093022349149358.jpg");
        IMG_URL_LIST.add("http://p.ishowx.com/uploads/allimg/160907/486-160ZG40449.jpg");
        IMG_URL_LIST.add("http://tpic.home.news.cn/xhCloudNewsPic/xhpic1501/M0B/21/86/wKhTlFe7xeOEJDV4AAAAAPbonyI938.jpg");
        IMG_URL_LIST.add("http://p.ishowx.com/uploads/allimg/160902/415-160Z2093517.jpg");
        IMG_URL_LIST.add("http://img2.imgtn.bdimg.com/it/u=1770407502,1713614648&fm=11&gp=0.jpg");
        IMG_URL_LIST.add("http://img5.duitang.com/uploads/item/201407/27/20140727202737_sZLAX.jpeg");
    }

    @Override
    protected String getBackTransitionName(int index) {
        return IMG_URL_LIST.get(index);
    }

    @Override
    protected View getBackTransitionView(int index) {
        return nineGridImageView.getImageView(index);
    }


    private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String s) {
//            Glide.with(context)
//                    .load(s)
//                    .into(imageView);
            Picasso.with(context)
                    .load(s)
                    .into(imageView);
        }

        @Override
        protected ImageView generateImageView(Context context) {
            return super.generateImageView(context);
        }

        @Override
        protected void onItemImageClick(Context context, View v, int index, List<String> list) {
            startViewerActivity(TwoActivity.this, v, (ArrayList<String>) IMG_URL_LIST, index);
        }
    };

}
