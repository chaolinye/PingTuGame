package com.promote.ace;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;

import com.promote.jigsawpuzzlegame.R;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import static com.promote.ace.ImageUnit.GetBitmapFromFilePath;


/**
 * Created by ace on 2016/4/26.
 */
public class Ace_imagepacketSelect extends Activity {
    private Button b_add;
    private GridView grid_image;
    private DbHelper dbhelper;
    private SQLiteDatabase db;
    private String path[];
    private Button back_b;
    private int bid[];
    private Bitmap bmpout[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagepacket);
        getActionBar().hide();


        b_add = (Button) findViewById(R.id.add_button);
        grid_image = (GridView) findViewById(R.id.grid_iamge);
        back_b = (Button) findViewById(R.id.ace_back_button);

        back_b.setVisibility(View.GONE);
        b_add.setVisibility(View.GONE);

        back_b.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        b_add.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(Ace_imagepacketSelect.this, Ace_add.class);
                startActivity(it);
            }
        });
        grid_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                intent.putExtra("imagePath", path[position]);
                Ace_imagepacketSelect.this.setResult(0, intent);
                Ace_imagepacketSelect.this.finish();
            }
        });

        dbhelper = new DbHelper(this, "Db_image", null, 1);
        db = dbhelper.getReadableDatabase();

        Cursor cursor = db.query("tb_image", new String[]{"ID", "IMAGEPATH"}, null, null, null, null, null);
        int sum = cursor.getCount();

        System.out.println(sum + "______________________________________");

        if (sum != 0) {
            int t = 0;
            bmpout = new Bitmap[sum];
            path = new String[sum];
            bid = new int[sum];
            cursor.moveToFirst();
            do {
                System.out.println(cursor.getCount());
                String path2 = cursor.getString(cursor.getColumnIndex("IMAGEPATH"));
                bid[t] = cursor.getInt(cursor.getColumnIndex("ID"));
                path[t] = path2;
                Uri uri2 = Uri.parse(path2);

//                try {
//                    bmpout[t] = getBitmapFormUri(Ace_imagepacket.this, uri2,100f,100f);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                bmpout[t] = GetBitmapFromFilePath(path[t], 4);
                t++;
            } while (cursor.moveToNext());

            BaseAdapter adapter2 = new BaseAdapter() {
                @Override
                public int getCount() {
                    return bmpout.length;
                }

                @Override
                public Object getItem(int position) {
                    return position;
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view;
                    if (convertView == null) {
                        view = LayoutInflater.from(Ace_imagepacketSelect.this).inflate(R.layout.items, null);
                    } else {
                        view = convertView;
                    }
                    ImageView photo = (ImageView) view.findViewById(R.id.photo);
                    photo.setImageBitmap(bmpout[position]);
                    return view;
                }
            };

            grid_image.setAdapter(adapter2);
        }
    }


}
