package com.promote.esmond;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.promote.ace.Ace_imagepacket;
import com.promote.ace.Ace_imagepacketSelect;
import com.promote.ace.Ace_individual;
import com.promote.jigsawpuzzlegame.MainActivity;
import com.promote.jigsawpuzzlegame.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Esmond on 2016/4/25.
 */
public class FuctionMainActivity extends Activity implements View.OnClickListener {

    SharedPreferences preferences;//存贮用户id
    private int a = 0, b = 0, m;//a 为选择的拼图大小  b为模式,m 为音乐
    TextView tv_name;
    TextView tv_stage1, tv_stage2, tv_stage3;
    TextView tv_score, tv_gamedetail;
    TextView tv_photo_pic, tv_sound;
    TableLayout bd_stage1, bd_stage2, bd_stage3;

    boolean hasPic = false;
    Button btn_start1;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    String path;
    String name;
    int grade;
    int id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function_main);
        getActionBar().hide();

        //读取 sharepref
        preferences = getSharedPreferences("id", MODE_WORLD_READABLE);
        id = preferences.getInt("id", -2);

        SharedPreferences sharedPreferences= getSharedPreferences("Test", MODE_PRIVATE);
        m=sharedPreferences.getInt("music", 0);
        //初始化界面 名字 分数
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_score = (TextView) findViewById(R.id.tv_score);

        //得到分数并 赋值显示
        if (id == 1000) {
            tv_name.setText("普通用户");
            tv_score.setText("");
        } else {
            getScore();
        }

        tv_gamedetail = (TextView) findViewById(R.id.tv_gamedetail);
        tv_gamedetail.setText("游戏规则:" + '\n' + "规定时间内拼完不规则图形");

        //初始 选择 模式及住界面
        tv_stage1 = (TextView) findViewById(R.id.tv_stage1);
        tv_stage2 = (TextView) findViewById(R.id.tv_stage2);
        tv_stage3 = (TextView) findViewById(R.id.tv_stage3);
        tv_stage1.setOnClickListener(this);
        tv_stage2.setOnClickListener(this);
        tv_stage3.setOnClickListener(this);
        bd_stage1 = (TableLayout) findViewById(R.id.body_stage1);
        bd_stage2 = (TableLayout) findViewById(R.id.body_stage2);
        bd_stage3 = (TableLayout) findViewById(R.id.body_stage3);

        //btn赋id值
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        tv_photo_pic = (TextView) findViewById(R.id.tv_photo_pic);
        btn6 = (Button) findViewById(R.id.button6);
        btn7 = (Button) findViewById(R.id.button7);
        btn8 = (Button) findViewById(R.id.button8);
        btn9 = (Button) findViewById(R.id.button9);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);

        btn_start1 = (Button) findViewById(R.id.btn_start1);

        tv_sound = (TextView) findViewById(R.id.tv_sound);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_stage1:
                a = 0;
                b = 0;
                tv_stage1.setTextColor(this.getResources().getColor((R.color.main_txt)));
                tv_stage2.setTextColor(this.getResources().getColor((R.color.blue_hard)));
                tv_stage3.setTextColor(this.getResources().getColor((R.color.blue_hard)));
                bd_stage1.setVisibility(View.VISIBLE);
                bd_stage2.setVisibility(View.GONE);
                bd_stage3.setVisibility(View.GONE);
                nineButton();
                break;
            case R.id.tv_stage2:
                a = 0;
                b = 1;
                tv_stage1.setTextColor(this.getResources().getColor((R.color.blue_hard)));
                tv_stage2.setTextColor(this.getResources().getColor((R.color.main_txt)));
                tv_stage3.setTextColor(this.getResources().getColor((R.color.blue_hard)));
                bd_stage1.setVisibility(View.VISIBLE);
                bd_stage2.setVisibility(View.GONE);
                bd_stage3.setVisibility(View.GONE);
                nineButton();
                break;
            case R.id.tv_stage3:
                a = 0;
                b = 2;
                tv_stage1.setTextColor(this.getResources().getColor(R.color.blue_hard));
                tv_stage2.setTextColor(this.getResources().getColor(R.color.blue_hard));
                tv_stage3.setTextColor(this.getResources().getColor(R.color.main_txt));
                bd_stage1.setVisibility(View.GONE);
                bd_stage2.setVisibility(View.GONE);
                bd_stage3.setVisibility(View.VISIBLE);
                nineButton();
                break;
            case R.id.button1:
                a = 3;
                nineButton();
                break;
            case R.id.button2:
                a = 4;
                nineButton();
                break;
            case R.id.button3:
                a = 5;
                nineButton();
                break;
            case R.id.button4:
                a = 6;
                nineButton();
                break;
            case R.id.button6:
                a = 7;
                nineButton();
                break;
            case R.id.button7:
                a = 8;
                nineButton();
                break;
            case R.id.button8:
                a = 9;
                nineButton();
                break;
            case R.id.button9:
                a = 10;
                nineButton();
                break;
        }
    }

    //音乐
    public void sound_onoff(View v) {
        if (m == 1) {
            tv_sound.setBackgroundResource(R.drawable.af2);
            SharedPreferences sp = getSharedPreferences("Test", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("music",0);
            editor.commit();

            m = 0;
        } else {
            tv_sound.setBackgroundResource(R.drawable.af1);
            SharedPreferences sp = getSharedPreferences("Test", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("music", 1);
            editor.commit();
            m = 1;
        }

    }

    //拍照 图片选择
    public void photo_pic(View v) {
        Intent intent = new Intent(FuctionMainActivity.this, Ace_imagepacketSelect.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            path=data.getExtras().getString("imagePath");
            tv_photo_pic.setBackgroundResource(R.drawable.pic);
            hasPic = true;
        }
    }

    //开始游戏
    public void start_game_stage3(View v) {
        Intent intent = new Intent(FuctionMainActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("mode", 3);
        bundle.putInt("kind", a);
        bundle.putInt("grade", grade);
        intent.putExtras(bundle);

        startActivity(intent);
        finish();
    }

    public void start_game(View v) {

        Intent intent = new Intent(FuctionMainActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("mode", b);
        bundle.putInt("kind", a);
        bundle.putString("path",path);
        bundle.putInt("grade", grade);
        intent.putExtras(bundle);

        startActivity(intent);
        finish();
    }

    public void me(View v) {
        Intent intent = new Intent(FuctionMainActivity.this, Ace_individual.class);
        Bundle b = new Bundle();
        b.putString("name", name);
        b.putInt("grade", grade);
        startActivity(intent);
    }

    //跟新 btn 界面
    public void nineButton() {
        int gain = 0;
        btn1.setTextColor(this.getResources().getColor(R.color.main_txt));
        btn2.setTextColor(this.getResources().getColor(R.color.main_txt));
        btn3.setTextColor(this.getResources().getColor(R.color.main_txt));
        btn4.setTextColor(this.getResources().getColor(R.color.main_txt));

        btn6.setTextColor(this.getResources().getColor(R.color.main_txt));
        btn7.setTextColor(this.getResources().getColor(R.color.main_txt));
        btn8.setTextColor(this.getResources().getColor(R.color.main_txt));
        btn9.setTextColor(this.getResources().getColor(R.color.main_txt));
        btn_start1.setVisibility(View.VISIBLE);
        switch (a) {
            case 0:
                btn_start1.setVisibility(View.GONE);
                gain = 0;
                break;
            case 3:
                btn1.setTextColor(this.getResources().getColor(R.color.pink_soft));
                gain = 100;
                break;
            case 4:
                btn2.setTextColor(this.getResources().getColor(R.color.pink_soft));
                gain = 200;
                break;
            case 5:
                btn3.setTextColor(this.getResources().getColor(R.color.pink_soft));
                gain = 300;
                break;
            case 6:
                btn4.setTextColor(this.getResources().getColor(R.color.pink_soft));
                gain = 400;
                break;
            case 7:
                btn6.setTextColor(this.getResources().getColor(R.color.pink_soft));
                gain = 500;
                break;
            case 8:
                btn7.setTextColor(this.getResources().getColor(R.color.pink_soft));
                gain = 600;
                break;
            case 9:
                btn8.setTextColor(this.getResources().getColor(R.color.pink_soft));
                gain = 700;
                break;
            case 10:
                btn_start1.setVisibility(View.GONE);
                gain = 0;
                break;
        }
        btn_start1.setText("开始" + '\n' + "分值:" + gain);
    }

    //进入排行榜
    public void toRanklist(View v) {
        Intent intent = new Intent(FuctionMainActivity.this
                , RankActivity.class);
        startActivity(intent);
    }


//    //进入登录界面 重新登陆 退出登录
//    public void toRelogin(View v) {
//
//        Intent intent = new Intent(FuctionMainActivity.this
//                , LoginSelectActivity.class);
//        startActivity(intent);
//        finish();
//    }

    //获取对应 id 的 score
    public void getScore() {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(RankActivity.xinlang_url + "rank",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jb = response.getJSONObject(i);
                                int id_fromrank = Integer.parseInt(jb.getString("id"));

                                //判定id 是否相同
                                if (id == id_fromrank) {
                                    grade = jb.getInt("grade");
                                    name = URLDecoder.decode(jb.getString("username"), "utf-8");
                                    getname_grade(grade, name);

                                    break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "!连接错误",
                        Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    public void getname_grade(int g, String n) {
        tv_name.setText(name);
        tv_score.setText("总分:" + grade);
    }
}
