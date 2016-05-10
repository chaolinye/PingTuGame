package com.promote.esmond;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.promote.jigsawpuzzlegame.R;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RegisteroOrdinaryActivity extends Activity {

    //定义输入框 按钮
    EditText etname, etpass,etre_pass;
    Button btnRegister;
    TextView bar_back_register;
    Toast tst;
    String url_name;
    SharedPreferences preferences;//存贮用户id

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        getActionBar().hide();
        preferences=getSharedPreferences("id",MODE_WORLD_READABLE);

        etname=(EditText)findViewById(R.id.editname);
        etpass=(EditText)findViewById(R.id.editpass);
        etre_pass=(EditText)findViewById(R.id.editrepass);

        btnRegister=(Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //校验
                if(register_able()){
                    RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

                    try {
                        url_name= URLEncoder.encode(etname.getText().toString(),"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String url_pass=etpass.getText().toString();
                    String url_register=RankActivity.xinlang_url+"register&"+"username="+url_name+"&password="+url_pass;
                    StringRequest stringRequest = new StringRequest(url_register,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    int id_str=Integer.parseInt(response.toString());
                                    if(id_str==-1){
                                        Toast.makeText(getApplicationContext(), "!注册失败",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        //将id 存入
                                        SharedPreferences.Editor editor=preferences.edit();
                                        editor.putInt("id", id_str);
//                                        editor.putString("name",url_name);
                                        editor.commit();
                                        //跳转主界面
                                        Intent intent = new Intent(RegisteroOrdinaryActivity.this
                                                , FuctionMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "！连接错误",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    mQueue.add(stringRequest);
                }
            }
        });
    }

    public void back(View v){
        Intent intent = new Intent(RegisteroOrdinaryActivity.this
                , LoginSelectActivity.class);
        startActivity(intent);
        finish();
    }

    protected boolean register_able(){
        String name=etname.getText().toString();
        String pass=etpass.getText().toString();
        String repass=etre_pass.getText().toString();
        if (name.equals("")) {
            Toast.makeText(getApplicationContext(), "！请输入昵称",
                    Toast.LENGTH_SHORT).show();
            //DialogUtil.showDialog(this, "！请输入账户", false);
            return false;
        }
        if (pass.equals("")) {
            Toast.makeText(getApplicationContext(), "！请输入密码",
                    Toast.LENGTH_SHORT).show();
            // DialogUtil.showDialog(this, "！请输入密码", false);
            return false;
        }
        if(etpass.getText().toString().equals(etre_pass.getText().toString())){
          return true;
        }
        else{
            Toast.makeText(getApplicationContext(), "！两次密码输入不正确",
                    Toast.LENGTH_SHORT).show();
          return false;
        }
    }
}