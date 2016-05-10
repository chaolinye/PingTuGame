package com.promote.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.promote.jigsawpuzzlegame.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class ImageUtil {


    public static int itemShape;
    /**
     * 将图片切成 , piece *piece
     *
     * @param bitmap
     * @param piece
     * @return
     */
    public static List<ImagePiece> split(Bitmap bitmap, int piece) {

        List<ImagePiece> pieces = new ArrayList<ImagePiece>(piece * piece);

        int width = bitmap.getWidth() / piece;
        int height = bitmap.getHeight() / piece;

        if(itemShape == 1){
            width = height = Math.min(bitmap.getWidth(), bitmap.getHeight()) / piece;
        }

        for (int i = 0; i < piece; i++) {
            for (int j = 0; j < piece; j++) {
                ImagePiece imagePiece = new ImagePiece();
                imagePiece.index = j + i * piece;

                // Log.e("TAG", "imagePiece.index" + (j + i * piece));
                int xValue = j * width;
                int yValue = i * height;

                imagePiece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue,
                        width, height);
                pieces.add(imagePiece);
            }
        }
        return pieces;
    }

    //public View createIrgularView(List<>)

    /**
     * 获取资源中所有默认图片的Id
     *
     * @return 图片Id的链表
     */
    public static List<Integer> getImageValues() {
        try {
            // 得到R.drawable所有的属性, 即获取drawable目录下的所有图片
            Field[] drawableFields = R.drawable.class.getFields();
            List<Integer> resourceValues = new ArrayList<Integer>();
            for (Field field : drawableFields) {
                // 如果该Field的名称以p_开头
                if (field.getName().substring(0, 2).equals("p_")) {
                    resourceValues.add(field.getInt(R.drawable.class));
                    // Log.e("IMAGE_ID_TAG",field.getName() + " = "
                    //        + field.getInt(R.drawable.class));
                }
            }
            return resourceValues;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 随机获取一个默认的图片资源
     *
     * @param context
     * @return 一个Bitmap资源
     */
    public static Bitmap getRandomBitmap(Context context) {
        // 创建结果集合
        List<Integer> imageValues = getImageValues();
        Integer image;
        try {
            // 随机获取一个数字，大小小于imageValues.sixe()的数值
            Random random = new Random();
            int index = random.nextInt(imageValues.size());
            // 从图片Id集合中获取该图片对象
            image = imageValues.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        // 加载图片
        Bitmap bitmap = BitmapFactory.decodeResource(
                context.getResources(), image);
        return bitmap;
    }


}
