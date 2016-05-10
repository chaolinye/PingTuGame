package com.promote.ace;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import com.promote.jigsawpuzzlegame.R;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.promote.ace.ImageUnit.GetBitmapFromFilePath;


/**
 * Created by ace on 2016/4/26.
 */
public class Ace_Show extends Activity{
    private Button delete;
    private Button select;
    private ImageView im;
    private DbHelper dbhelper;
    private Button back_b;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);
        getActionBar().hide();
        Bundle bundle=this.getIntent().getExtras();
        String path=bundle.getString("PATH");
        final int bid=bundle.getInt("ID");

        delete=(Button)findViewById(R.id.show_delete);
        select=(Button)findViewById(R.id.show_select);
        im=(ImageView)findViewById(R.id.show_image);
        back_b=(Button)findViewById(R.id.ace_back_button);
        back_b.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

//        Uri uri = Uri.parse(path);
        Bitmap bitmap= null;
//        try {
//            bitmap = getBitmapFormUri(Ace_Show.this, uri,100f,100f);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        bitmap=GetBitmapFromFilePath(path,2);
        im.setImageBitmap(bitmap);


        dbhelper = new DbHelper(this, "Db_image", null, 1);
        db = dbhelper.getReadableDatabase();


        delete.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                db.delete("tb_image", "ID=?",new String[]{Integer.toString(bid)});
                Intent it2=new Intent();
                it2.setClass(Ace_Show.this,Ace_imagepacket.class);
                startActivity(it2);
            }
        });
    }
}
