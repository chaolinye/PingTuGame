package com.promote.jigsawpuzzlegame;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.promote.ace.BackMusicServer;
import com.promote.esmond.FuctionMainActivity;
import com.promote.jigsawpuzzleview.JigsawPuzzleView;
import com.promote.utility.ImageUtil;

import static com.promote.ace.ImageUnit.GetBitmapFromFilePath;

/**
 * @version 3.0
 */
public class MainActivity extends Activity {
    /**
     * 模式
     */
    public static final int FLAG_NORMAL = 0;
    public static final int FLAG_BLUR = 1;

    //模式设置
    public static int flag ;
    public static int  grade;
    public String path;
    int omusic;

    /**
     * 拼图界面
     */
    private JigsawPuzzleView mPuzzleView;
    /**
     * 当前拼图的图片
     */
    private Bitmap mBitmap;
    /**
     * 当前拼图的列数
     */
    public int mColumn = 3;
    /**
     * 活动条
     */
    private ActionBar actionBar;
    /**
     * 计时器显示
     */
    public static Chronometer mTextView;

    private Button mBack, mTip;
    /**
     * 判断是否使用默认图片
     */
    public boolean isDefaultBitmap = true;

    //tip次数
    boolean haveTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPuzzleView=(JigsawPuzzleView)findViewById(R.id.id_puzzle);

        SharedPreferences sharedPreferences= getSharedPreferences("Test", MODE_PRIVATE);
        omusic=sharedPreferences.getInt("music", 0);

        if (omusic==1)
        {
            startService(new Intent(MainActivity.this,BackMusicServer.class));
        }


        // ActionBar的设置
        actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        // 设置ActionBar背景半透明
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));
        // actionbar是分为上下两栏显示的，上面的代码只能设置顶部actionbar的背景色，
        // 为了让下面的背景色一致，还需要添加一行代码：
        actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));

        mTextView = (Chronometer) getActionBar().getCustomView().findViewById(R.id.time);
        mBack = (Button) getActionBar().getCustomView().findViewById(R.id.back);

        Log.e("TAG","123");
        mColumn = this.getIntent().getExtras().getInt("kind");
        flag = this.getIntent().getExtras().getInt("mode");
        path = this.getIntent().getExtras().getString("path");
        grade = this.getIntent().getExtras().getInt("grade");
        if(path!=null)
            isDefaultBitmap = false;

        if(flag == 0||flag == 1){
            JigsawPuzzleView.itemShape=0;
        }else {
            JigsawPuzzleView.itemShape = 1;
            mColumn = 8;
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FuctionMainActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        if(isDefaultBitmap){
            mBitmap = ImageUtil.getRandomBitmap(this);  // 默认图片
        } else{
            // select other user`s picture
            mBitmap = GetBitmapFromFilePath(path,1);
        }
        if(mBitmap == null){
            // should return previous activity to restart
            Log.e("BITMAP_TAG","Bitmap is null");
            return;
        }

        // 初始化游戏数据
        initData(mColumn,mBitmap);

        /* 下面的注释暂时没有用，没有实现成功 */
        // mGameView = (JigsawGridLayout) findViewById(R.id.id_gameview);
        // mGameGridView = (PuzzleGridView)findViewById(R.id.id_gameview);
        // mPuzzleAdapter=new PuzzleGridViewAdapter(this,mColumn);
        // mGameGridView.setAdapter(mPuzzleAdapter);
    }

    public void initData(int column,Bitmap bitmap){

        mPuzzleView.setColumn(column);
        mPuzzleView.setBitmap(bitmap);

        actionBar.show();
        setGameStartTime();
        haveTip = true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isCheckable())
            item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.tip:
                if (haveTip) {
                    haveTip = false;
                    mPuzzleView.tip();
                } else
                    Toast.makeText(MainActivity.this, "No more tips!", Toast.LENGTH_SHORT).show();
                //intent
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置游戏开始时间
     */
    public void setGameStartTime(){
        // 设置开始讲时时间，并开始计时
        mTextView.setBase(SystemClock.elapsedRealtime());
        mTextView.start();
        mTextView.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                // 如果开始计时到现在超过了startime秒
                if (SystemClock.elapsedRealtime()
                        - chronometer.getBase() > 5 * 60 * 1000) {
                    // 给用户提示
                    chronometer.setTextColor(Color.RED);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (omusic==1) {
            stopService(new Intent(MainActivity.this, BackMusicServer.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (omusic==1) {
            stopService(new Intent(MainActivity.this, BackMusicServer.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (omusic==1)
        {
            startService(new Intent(MainActivity.this,BackMusicServer.class));
        }
    }
}
