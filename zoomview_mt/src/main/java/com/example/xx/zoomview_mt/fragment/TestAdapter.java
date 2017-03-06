package com.example.xx.zoomview_mt.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.xx.zoomview_mt.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by yangyu on 16/10/12.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private Context mContext;

    public LayoutInflater inflater;

    private List<String> IMG_URL_LIST;

    public TestAdapter(Context mContext, onImageViewClickListener listener, List<String> IMG_URL_LIST) {
        this.mContext = mContext;
        this.listener = listener;
        this.IMG_URL_LIST = IMG_URL_LIST;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_image, parent, false);
        ViewHolder v = new ViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(IMG_URL_LIST.get(holder.getAdapterPosition()))
                .into(holder.ivImg);
    }

    @Override
    public int getItemCount() {
        return IMG_URL_LIST.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivImg;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
            ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onImageClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    interface onImageViewClickListener {
        void onImageClick(View v, int position);
    }

    onImageViewClickListener listener;

    public void setListener(onImageViewClickListener listener) {
        this.listener = listener;
    }
}
