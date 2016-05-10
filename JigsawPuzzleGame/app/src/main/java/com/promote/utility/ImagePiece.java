package com.promote.utility;

import android.graphics.Bitmap;

/**
 *
 */
public class ImagePiece {
    public int index = 0;
    public Bitmap bitmap = null;

    /**
     * 设置图片的索引
     * @param index1
     */
    public void setIndex(int index1){
        index=index1;
    }

    /**
     * 设置图片
     * @param bitmap1
     */
    public void setBitmap(Bitmap bitmap1){
        bitmap=bitmap1;
    }

    /**
     * 获取图片索引
     * @return
     */
    public int getIndex(){
        return index;
    }

    /**
     * 获取图片
     * @return
     */
    public Bitmap getBitmap(){
        return bitmap;
    }

}
