package com.promote.ace;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import com.promote.jigsawpuzzlegame.R;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.promote.ace.ImageUnit.GetBitmapFromFilePath;
import static com.promote.ace.ImageUnit.getImageAbsolutePath;


/**
 * Created by ace on 2016/4/26.
 */
public class Ace_add extends Activity {

    private Button b1;
    private Button b2;
    private Button back_b;
    private Button b3;
    private ImageView v1;
    private Bitmap bitmap_ace;
    private String path = "none";
    private Bitmap cameraBitmap;
    private  String photoFileName;
    private String s= Environment.getExternalStorageDirectory().toString()+"/pt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        getActionBar().hide();
        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        v1 = (ImageView) findViewById(R.id.iamge_show);
        back_b=(Button)findViewById(R.id.ace_back_button);
        back_b.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {finish();
            }
        });
        b1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
            }
        });
        b2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
               // startActivityForResult(cameraIntent, 2);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				因为通过系统照相机intent返回的照片会被压缩，所以事先将拍得的照片先保存在本地（未缩小）。
//				指定照片保存路径（SD卡），拍照的照片为当前时间戳，onActivityResult那边直接获取全局变量photoFileName即可得到未压缩的照片
                File photoDir = new File(s);
                if (!photoDir.exists()) {
                    photoDir.mkdirs();
                }
                photoFileName = System.currentTimeMillis() + ".jpg";
                File photoFile = new File(s, photoFileName);
                Uri imageUri = Uri.fromFile(photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 2);
            }
        });
        b3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (path.compareTo("none") == 0) {
                    Toast.makeText(Ace_add.this, "添加失败，请选择图片", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues value = new ContentValues();
                    value.put("IMAGEPATH", path);
                    DbHelper dbhelper = new DbHelper(Ace_add.this, "Db_image", null, 1);
                    SQLiteDatabase db = dbhelper.getWritableDatabase();
                    long status;
                    status = db.insert("tb_image", null, value);
                    if (status != -1) {
                        Toast.makeText(Ace_add.this, "添加成功", Toast.LENGTH_LONG).show();
                    }
                    Intent it4 = new Intent();
                    it4.setClass(Ace_add.this, Ace_imagepacket.class);
                    startActivity(it4);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
if(requestCode==1) {
    if (resultCode == RESULT_OK) {

        Uri uri = data.getData();

        path =getImageAbsolutePath(Ace_add.this,uri);

        Log.d("地址", path + "");
        Log.d("地址", uri + "");
        Log.d("地址", s + "");
        bitmap_ace=GetBitmapFromFilePath(path,2);
        v1.setImageBitmap(bitmap_ace);

//        try {
//
//            if (uri2 != null) {
//
//                bitmap_ace = getBitmapFormUri(Ace_add.this, uri2, 100f, 100f);
//
//            }
//            // bitmap_ace = BitmapFactory.decodeStream(cr.openInputStream(uri2));
//                /* 将Bitmap设定到ImageView */
//            v1.setImageBitmap(bitmap_ace);
//        } catch (FileNotFoundException e) {
//            Log.e("Exception", e.getMessage(), e);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
        if(requestCode==2) {
            if (resultCode == RESULT_OK) {


                if (resultCode == Activity.RESULT_OK) {
                    if (cameraBitmap != null) {
//					清除原来图片的内存
                        cameraBitmap.recycle();
                    }
                    Log.d("系统相机返回数据", resultCode + "");
//				Log.d("资源路径", PHOTORESOURSEPATH);
//				压缩图片
//                    BitmapFactory.Options opts = new BitmapFactory.Options();
//                    opts.inPreferredConfig = Bitmap.Config.RGB_565;
//                    opts.inPurgeable = true;
////				设置图片压缩为原来尺寸的1/8
//                    opts.inSampleSize = 8;
//                    //设置解码位图的尺寸信息
//                    opts.inInputShareable = true;
////				因为拍照的照片已经提前输出到sd卡中，所以返回的照片信息为null，直接通过photoFileName获取图片显示出来即可
//                    cameraBitmap = BitmapFactory.decodeFile(s + "/" + photoFileName, opts);
//
                    path=s+"/"+photoFileName;
                    cameraBitmap=GetBitmapFromFilePath(path,2);
                    //System.out.println("cameraBitmap=="+cameraBitmap.getByteCount());
//				显示图片在手机上

                    v1.setImageBitmap(cameraBitmap);

                    //  Bitmap bt=(Bitmap) data.getExtras().get("data");
                    //  v1.setImageBitmap(bt);
                }
            }
        }

    }
}

