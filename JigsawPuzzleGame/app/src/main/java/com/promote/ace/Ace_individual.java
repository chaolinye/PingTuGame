package com.promote.ace;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import com.promote.jigsawpuzzlegame.R;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.promote.ace.ImageUnit.GetBitmapFromFilePath;
import static com.promote.ace.ImageUnit.getImageAbsolutePath;


/**
 * Created by ace on 2016/4/26.
 */
public class Ace_individual extends Activity{

    private ImageView tx;
    private RelativeLayout r_image;
    private RelativeLayout r_aboutus;
    private Button save_b;
    private Button back_b;
    private Bitmap bitmap;
    private TextView username;
    private Switch usersex;
    private TextView userscore;
    private String path;
    private int music_flag;
    private int sex_flag;
    private Switch swi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual);
        getActionBar().hide();

        tx=(ImageView)findViewById(R.id.ace_tx_image);
        r_aboutus=(RelativeLayout)findViewById(R.id.ace_aboutus_r);
        r_image=(RelativeLayout)findViewById(R.id.ace_imagepacket_r);
        save_b=(Button)findViewById(R.id.save_button);
        swi=(Switch)findViewById(R.id.music_buton);
        username=(TextView)findViewById(R.id.name_text);
        usersex=(Switch)findViewById(R.id.sex_text);
        userscore=(TextView)findViewById(R.id.score_text);


        swi.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    music_flag=1;
                }
                else
                {
                    music_flag=0;
                }
            }
        });

        usersex.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    sex_flag=1;
                }
                else
                {
                    sex_flag=0;
                }
            }
        });

        back_b=(Button)findViewById(R.id.ace_back_button);
        back_b.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        SharedPreferences sharedPreferences= getSharedPreferences("Test", MODE_PRIVATE);
        String opath=sharedPreferences.getString("imagepath", "null");
        String oname=sharedPreferences.getString("name", "普通用户");
        sex_flag=sharedPreferences.getInt("i_sex", 0);
        String oscore=sharedPreferences.getString("score","0");
        int omusic=sharedPreferences.getInt("music", 0);
        username.setText(oname);
        userscore.setText(oscore);

        if(omusic==0)
        {
            swi.setChecked(false);
        }
        else
        {
            swi.setChecked(true);
        }

        if(sex_flag==0)
        {
            usersex.setChecked(false);
        }
        else
        {
            usersex.setChecked(true);
        }

        if(opath.compareTo("null")==0)
        {
            tx.setImageResource(R.drawable.image_tx);
        }
        else
        {
            path=opath;
//            Uri ouri = Uri.parse(opath);
            Bitmap obitmap= null;
//            try {
//                obitmap = getBitmapFormUri(Ace_individual.this, ouri,100f,100f);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            obitmap=GetBitmapFromFilePath(opath,4);
            tx.setImageBitmap(obitmap);
        }


        tx.setOnClickListener(new ImageView.OnClickListener(){
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
        r_aboutus.setOnClickListener(new RelativeLayout.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent();
                intent2.setClass(Ace_individual.this, Ace_Aboutus.class);
                startActivity(intent2);
            }
        });
        r_image.setOnClickListener(new RelativeLayout.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent();
                intent2.setClass(Ace_individual.this,Ace_imagepacket.class);
                startActivity(intent2);
            }
        });
        save_b.setOnClickListener(new RelativeLayout.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String sname=username.getText().toString();
                String sscore=userscore.getText().toString();
                //获取SharedPreferences对象
                SharedPreferences sp = getSharedPreferences("Test", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("imagepath",path);
                editor.putString("name", sname);
                editor.putString("score",sscore);
                editor.putInt("i_sex", sex_flag);
                editor.putInt("music",music_flag);
                editor.commit();
                Toast.makeText(Ace_individual.this, "success", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            path=getImageAbsolutePath(Ace_individual.this,uri);

//            try {
//                bitmap = getBitmapFormUri(Ace_individual.this, uri,100f,100f);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
                /* 将Bitmap设定到ImageView */

            bitmap=GetBitmapFromFilePath(path,4);
                tx.setImageBitmap(bitmap);

            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
