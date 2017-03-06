package com.yy.www.libs.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

/**
 * Created by yangyu on 2017/2/16.
 * 传递图片基本信息
 */

public class ImageBean implements Parcelable, Cloneable {

    /**
     * 正常的图片状态是normal
     */
    public static final int STATE_NORMAL = 100;
    /**
     * 站位的图片信息
     */
    public static final int STATE_PLACEHOLDER = 200;


    public float width;
    public float height;
    public float translationX;
    public float translationY;
    public float scaleX = 1.0f;
    public float scaleY = 1.0f;
    public float alpha = 1.0f;
    public int state;

    public ImageBean() {
        this.state = STATE_PLACEHOLDER;
    }

    public ImageBean(ImageView imageView) {
        this.state = STATE_NORMAL;
        int[] location = new int[2];
        imageView.getLocationOnScreen(location);
        this.height = imageView.getHeight();
        this.width = imageView.getWidth();
        this.translationX = location[0];
        this.translationY = location[1]; //全屏状态下可能需要减掉一个状态栏高度;暂时未处理
        this.scaleX = imageView.getScaleX();
        this.scaleY = imageView.getScaleY();

    }

    public ImageBean clone() {
        Object o = null;
        try {
            o = super.clone();//Object 中的clone()识别出你要复制的是哪一个对象。
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return (ImageBean) o;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.width);
        dest.writeFloat(this.height);
        dest.writeFloat(this.translationX);
        dest.writeFloat(this.translationY);
        dest.writeFloat(this.scaleX);
        dest.writeFloat(this.scaleY);
        dest.writeFloat(this.alpha);
        dest.writeInt(this.state);

    }


    protected ImageBean(Parcel in) {
        this.width = in.readFloat();
        this.height = in.readFloat();
        this.translationX = in.readFloat();
        this.translationY = in.readFloat();
        this.scaleX = in.readFloat();
        this.scaleY = in.readFloat();
        this.alpha = in.readFloat();
        this.state = in.readInt();
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
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
