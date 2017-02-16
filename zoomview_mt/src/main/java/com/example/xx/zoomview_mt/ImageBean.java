package com.example.xx.zoomview_mt;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by yangyu on 2017/2/16.
 * 传递图片基本信息
 */

public class ImageBean implements Parcelable {
    int width;
    int height;
    float translationX;
    float translationY;
    float scaleX;
    float scaleY;

    private ImageBean(){}

    public ImageBean(ImageView imageView) {
        int[] location = new int[2];
        imageView.getLocationOnScreen(location);
        this.height = imageView.getHeight();
        this.width = imageView.getWidth();
        this.translationX = location[0];
        this.translationY = location[1]; //全屏状态下可能需要减掉一个状态栏高度;暂时未处理

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeFloat(this.translationX);
        dest.writeFloat(this.translationY);
        dest.writeFloat(this.scaleX);
        dest.writeFloat(this.scaleY);
    }



    protected ImageBean(Parcel in) {
        this.width = in.readInt();
        this.height = in.readInt();
        this.translationX = in.readFloat();
        this.translationY = in.readFloat();
        this.scaleX = in.readFloat();
        this.scaleY = in.readFloat();
    }

    public static final Parcelable.Creator<ImageBean> CREATOR = new Parcelable.Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel source) {
            return new ImageBean(source);
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };
}
