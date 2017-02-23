package com.example.xx.zoomview_mt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yy.www.libs.TransitionConstant;
import com.yy.www.libs.TransitionManager;
import com.yy.www.libs.bean.ZoomBean;
import com.yy.www.libs.helper.TransitionMultiHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.xx.zoomview_mt.ImageZoomActivity.IMAGE_POSITION;
import static com.example.xx.zoomview_mt.ImageZoomActivity.IMAGE_THUMB_URL;
import static com.example.xx.zoomview_mt.ImageZoomActivity.IMAGE_URL;
import static com.example.xx.zoomview_mt.ImageZoomActivity.IMAGE_VIEWS;

/**
 * Created by yangyu on 16/9/13.
 */
public class TwoActivity extends AppCompatActivity {
    private TransitionMultiHelper helper;

    private RecyclerView recyclerView;
    private GridLayoutManager manager;
    private ArrayList<String> thumbUrl = new ArrayList<>();
    private ArrayList<String> url = new ArrayList<>();
    ImageAdapter adapter;


    private List<ZoomBean<String>> IMG_URL_LIST = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        initDatas();
        initView();
        initHelper();
    }

    private void initHelper() {
        helper = new TransitionManager(TwoActivity.this).getMulti();
        helper.setAnim_type(TransitionConstant.Type.TYPE_HAVE_ANIM);
        setExitSharedElementCallback(helper.sharedElementCallback);

    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.nineGridImageView);
        manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        adapter = new ImageAdapter();
        recyclerView.setAdapter(adapter);


    }

    private void initDatas() {
        IMG_URL_LIST.clear();
        IMG_URL_LIST.add(new ZoomBean<>("http://img.my.csdn.net/uploads/201701/06/1483664940_9893.jpg", "http://img.my.csdn.net/uploads/201701/17/1484647899_2806.jpg"));
        IMG_URL_LIST.add(new ZoomBean<>("http://img.my.csdn.net/uploads/201701/06/1483664940_3308.jpg", "http://img.my.csdn.net/uploads/201701/17/1484647798_4500.jpg"));
        IMG_URL_LIST.add(new ZoomBean<>("http://img.my.csdn.net/uploads/201701/06/1483664927_3920.png", "http://img.my.csdn.net/uploads/201701/17/1484647897_1367.png"));
        IMG_URL_LIST.add(new ZoomBean<>("http://img.my.csdn.net/uploads/201701/06/1483664926_8360.png", "http://img.my.csdn.net/uploads/201701/17/1484650736_2101.png"));
        IMG_URL_LIST.add(new ZoomBean<>("http://img.my.csdn.net/uploads/201701/06/1483664926_6184.png", "http://img.my.csdn.net/uploads/201701/17/1484647701_9893.png"));
        IMG_URL_LIST.add(new ZoomBean<>("http://img.my.csdn.net/uploads/201701/06/1483664925_8382.png", "http://img.my.csdn.net/uploads/201701/17/1484650700_2514.png"));
        IMG_URL_LIST.add(new ZoomBean<>("http://img.my.csdn.net/uploads/201701/06/1483664925_2087.jpg", "http://img.my.csdn.net/uploads/201701/17/1484647930_5139.jpg"));
        IMG_URL_LIST.add(new ZoomBean<>("http://img.my.csdn.net/uploads/201701/06/1483664777_5730.png", "http://img.my.csdn.net/uploads/201701/17/1484647929_8108.png"));
        IMG_URL_LIST.add(new ZoomBean<>("http://img.my.csdn.net/uploads/201701/06/1483664741_1378.jpg", "http://img.my.csdn.net/uploads/201701/17/1484647897_1978.jpg"));

        thumbUrl.add("http://img.my.csdn.net/uploads/201701/06/1483664940_9893.jpg");
        thumbUrl.add("http://img.my.csdn.net/uploads/201701/06/1483664940_3308.jpg");
        thumbUrl.add("http://img.my.csdn.net/uploads/201701/06/1483664927_3920.png");
        thumbUrl.add("http://img.my.csdn.net/uploads/201701/06/1483664926_8360.png");
        thumbUrl.add("http://img.my.csdn.net/uploads/201701/06/1483664926_6184.png");
        thumbUrl.add("http://img.my.csdn.net/uploads/201701/06/1483664925_8382.png");
        thumbUrl.add("http://img.my.csdn.net/uploads/201701/06/1483664925_2087.jpg");
        thumbUrl.add("http://img.my.csdn.net/uploads/201701/06/1483664777_5730.png");
        thumbUrl.add("http://img.my.csdn.net/uploads/201701/06/1483664741_1378.jpg");

        url.add("http://img.my.csdn.net/uploads/201701/17/1484647899_2806.jpg");
        url.add("http://img.my.csdn.net/uploads/201701/17/1484647798_4500.jpg");
        url.add("http://img.my.csdn.net/uploads/201701/17/1484647897_1367.png");
        url.add("http://img.my.csdn.net/uploads/201701/17/1484650736_2101.png");
        url.add("http://img.my.csdn.net/uploads/201701/17/1484647701_9893.png");
        url.add("http://img.my.csdn.net/uploads/201701/17/1484650700_2514.png");
        url.add("http://img.my.csdn.net/uploads/201701/17/1484647930_5139.jpg");
        url.add("http://img.my.csdn.net/uploads/201701/17/1484647929_8108.png");
        url.add("http://img.my.csdn.net/uploads/201701/17/1484647897_1978.jpg");
    }


    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        helper.update(data, new TransitionMultiHelper.UpdateTransitionListener() {
            @Override
            public View updateView(int position) {
//                recyclerView.scrollToPosition(position);
                ImageAdapter.ViewHolder vh = (ImageAdapter.ViewHolder) recyclerView.findViewHolderForLayoutPosition(amendPosition(position));
                if (vh != null) {
                    return vh.itemView.findViewById(R.id.ivImg);
                } else {
                    return null;
                }
            }

            @Override
            public String updateName(int position) {
                return null;

            }
        });
    }


    //取图片 1
    private ArrayList<ImageBean> getIvs(int max) {
        ArrayList<ImageBean> imageBeen = new ArrayList<>();
        for (int i = 0; i < amendPosition(max); i++) {
            ImageAdapter.ViewHolder vh = (ImageAdapter.ViewHolder) recyclerView.findViewHolderForLayoutPosition(i);
            if (vh != null) {
                imageBeen.add(new ImageBean((ImageView) vh.itemView.findViewById(R.id.ivImg)));
            }
        }
        return imageBeen;
    }

    private ArrayList<ImageBean> getIvs2() {
        ArrayList<ImageBean> imageBeen = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            View itemView = manager.findViewByPosition(i);
            if (itemView != null) {
                ImageView iv = (ImageView) itemView.findViewById(R.id.ivImg);
                imageBeen.add(new ImageBean(iv));
                Log.e("getIvs2", "getIvs2 not null : " + i);

            } else {
                Log.e("getIvs2", "getIvs2 null : " + i);
            }
        }

        return imageBeen;
    }

    int amendPosition(int previewPosition) {
        return Math.min(previewPosition, manager.findLastVisibleItemPosition());
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(TwoActivity.this).inflate(R.layout.item_image, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Picasso.with(TwoActivity.this)
                    .load(IMG_URL_LIST.get(position).getThumb())
                    .into(holder.ivImg);
        }

        @Override
        public int getItemCount() {
            return IMG_URL_LIST.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView ivImg;

            public ViewHolder(View itemView) {
                super(itemView);
                ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
                ivImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TwoActivity.this, ImageZoomActivity.class);
                        intent.putExtra(IMAGE_VIEWS, getIvs2());
                        intent.putStringArrayListExtra(IMAGE_THUMB_URL, thumbUrl);
                        intent.putStringArrayListExtra(IMAGE_URL, url);
                        intent.putExtra(IMAGE_POSITION, getAdapterPosition());
                        //默认位置为0 所以不传递 startPosition
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                });
            }
        }
    }

}
