package com.yy.www.libs.bean;

import java.io.Serializable;

/**
 * Created by yangyu on 16/11/1.
 * 图片展示 , 增加缩略图
 */

public class ZoomBean<T> implements Serializable {

    private T image;
    private T thumb;

    public ZoomBean(T image, T thumb) {
        this.image = image;
        this.thumb = thumb;
    }

    public T getImage() {
        return image;
    }

    public void setImage(T image) {
        this.image = image;
    }

    public T getThumb() {
        return thumb;
    }

    public void setThumb(T thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "ZoomBean{" +
                "image=" + image +
                ", thumb=" + thumb +
                '}';
    }
}
