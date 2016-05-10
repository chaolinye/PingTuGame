package com.promote.ace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by ace on 2016/4/26.
 */
public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists tb_image" +
                " ( ID integer primary key autoincrement, " +
                "IMAGEPATH  varchar(99) ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void delete(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where ="ID" + " = ?";
        String[] whereValue ={ Integer.toString(id) };
        db.delete("tb_image", where, whereValue);
    }

}
