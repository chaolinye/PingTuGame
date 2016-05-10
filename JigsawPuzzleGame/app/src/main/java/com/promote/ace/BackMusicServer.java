package com.promote.ace;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import com.promote.jigsawpuzzlegame.R;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.lang.annotation.Target;

/**
 * Created by ace on 2016/4/20.
 */
public class BackMusicServer extends Service {


    MediaPlayer mediaPlayer;
    private MyBinder mybinder=new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mybinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {
        Toast.makeText(this,"create success", Toast.LENGTH_LONG).show();
        mediaPlayer= MediaPlayer.create(this, R.raw.music1);
        mediaPlayer.setLooping(true);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }


    public class MyBinder extends Binder {
        BackMusicServer getService()
        {
            return BackMusicServer.this;
        }
    }

}
