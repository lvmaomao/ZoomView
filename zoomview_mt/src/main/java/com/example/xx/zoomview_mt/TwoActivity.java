package com.example.xx.zoomview_mt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yy.www.libs.TransitionManager;
import com.yy.www.libs.helper.TransitionMultiHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyu on 16/9/13.
 */
public class TwoActivity extends AppCompatActivity {
    private TransitionMultiHelper helper;

    private RecyclerView recyclerView;
    private GridLayoutManager manager;


    private List<String> IMG_URL_LIST = new ArrayList<>();

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
        setExitSharedElementCallback(helper.sharedElementCallback);

    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.nineGridImageView);
        manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new ImageAdapter());


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
                return IMG_URL_LIST.get(position);

            }
        });
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
                    .load(IMG_URL_LIST.get(position))
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
                        helper.startViewerActivity(v, (ArrayList<String>) IMG_URL_LIST, getAdapterPosition());
                    }
                });
            }
        }
    }

}
