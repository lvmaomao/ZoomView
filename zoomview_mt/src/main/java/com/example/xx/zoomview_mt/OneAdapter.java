package com.example.xx.zoomview_mt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by xx on 2016/9/11.
 */
public class OneAdapter extends RecyclerView.Adapter<OneAdapter.ViewHolder> {


    private Context mContext;

    private LayoutInflater inflater;

    private List<String> img;

    public OneAdapter(OneActivity oneActivity, List<String> img) {
        this.mContext = oneActivity;
        this.img = img;
        this.inflater = LayoutInflater.from(mContext);
    }


    @Override
    public OneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.img_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OneAdapter.ViewHolder holder, int position) {
        Glide.with(mContext)
                .load(img.get(position))
                .into(holder.ivImg);
    }

    @Override
    public int getItemCount() {
        return img.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImg;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
            ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(ViewHolder.this);
                }
            });
        }
    }

    onItemClickListener listener;

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(OneAdapter.ViewHolder holder);
    }
}
