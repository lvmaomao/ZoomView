package com.example.xx.zoomview_mt;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by yangyu on 2017/2/15.
 * 封装imageView进行传递
 */

public class ImageViewBean implements Serializable {

    ImageView iv;

    public ImageViewBean(ImageView iv) {
        this.iv = iv;
    }

}
