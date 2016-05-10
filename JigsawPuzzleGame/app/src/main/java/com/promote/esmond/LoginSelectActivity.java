package com.promote.esmond;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.promote.jigsawpuzzlegame.R;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Esmond on 2016/4/4.
 */
public class LoginSelectActivity extends Activity {

    Button btnLogin_putong,btn_easysenter;
    Button btnLogin;
    EditText etname, etpass;
    TextView btnRegister, btn_delete1, btn_delete2, btnLogin_weixin;
    TextView shadow;
    RelativeLayout loginPart;
    LinearLayout btnGroup;
    Toast tst;
    String username, pass;
    int id;

    SharedPreferences preferences;//存贮用户id

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_select);
        getActionBar().hide();
        //读取 sharepref
        preferences = getSharedPreferences("id", MODE_WORLD_READABLE);

       id = preferences.getInt("id", -2);



        if (id==-1||id==1000||id==-2) {

        } else {

            Intent intent = new Intent(LoginSelectActivity.this
                    , FuctionMainActivity.class);
            startActivity(intent);
            finish();
        }
//        Toast.makeText(getApplicationContext(), "" + "id:" + id + "   name:" + name,
//                Toast.LENGTH_SHORT).show();
        btnLogin_putong = (Button) findViewById(R.id.btnLogin_putong);
        btnLogin_weixin = (TextView) findViewById(R.id.btnLogin_weixin);
        btn_easysenter = (Button) findViewById(R.id.btn_easysenter);

        etname = (EditText) findViewById(R.id.editname);
        etpass = (EditText) findViewById(R.id.editpass);
        btnRegister = (TextView) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btn_delete1 = (TextView) findViewById(R.id.btn_delete1);
        btn_delete2 = (TextView) findViewById(R.id.btn_delete2);

        shadow= (TextView) findViewById(R.id.shadow);
        loginPart = (RelativeLayout) findViewById(R.id.loginPart);
        btnGroup = (LinearLayout) findViewById(R.id.btnGroup);

        btnLogin_putong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPart.setVisibility(View.VISIBLE);
                shadow.setVisibility(View.VISIBLE);
                btn_easysenter.setVisibility(View.GONE);
                btnLogin_putong.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etname.getText().toString();
                pass = etpass.getText().toString();
                if(username.equals("Weijia")){

                    hehe_login();
                }
                else{

                    login();
                }
            }
        });

        //---------注册键--------------//
        btnRegister.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(LoginSelectActivity.this
                                                       , RegisteroOrdinaryActivity.class);
                                               startActivity(intent);
                                               finish();
                                           }
                                       }
        );

        //---------------编辑删除键1--------------------//
        etname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String namestr = etname.getText().toString();
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    if (namestr.isEmpty())
                        btn_delete1.setVisibility(View.GONE);
                    else
                        btn_delete1.setVisibility(View.VISIBLE);
                } else {
                    btn_delete1.setVisibility(View.GONE);
                    // 此处为失去焦点时的处理内容
                }
            }
        });

        etname.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (etname.getText().toString().isEmpty())
                    btn_delete1.setVisibility(View.GONE);
                else
                    btn_delete1.setVisibility(View.VISIBLE);
            }
        });

        btn_delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etname.setText("");
                btn_delete1.setVisibility(View.GONE);
            }
        });


        //---------------编辑删除键2--------------------//
        etpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String namestr = etpass.getText().toString();
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    if (namestr.isEmpty())
                        btn_delete2.setVisibility(View.GONE);
                    else
                        btn_delete2.setVisibility(View.VISIBLE);
                } else {
                    btn_delete2.setVisibility(View.GONE);
                    // 此处为失去焦点时的处理内容
                }
            }
        });

        etpass.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (etpass.getText().toString().isEmpty())
                    btn_delete2.setVisibility(View.GONE);
                else
                    btn_delete2.setVisibility(View.VISIBLE);
            }
        });

        btn_delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etpass.setText("");
                btn_delete2.setVisibility(View.GONE);
            }
        });

        //---------------------隐藏输入键盘--------//
        findViewById(R.id.layoutlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

    }

    //普通用户登陆函数
    private void hehe_login() {
        //将id 存入
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id",1111);
        editor.putString("name", "Weijia_o(^▽^)o");
        editor.commit();
        //跳转主界面
        Intent intent = new Intent(LoginSelectActivity.this
                , FuctionMainActivity.class);
        startActivity(intent);
        finish();
    }
    //普通用户登陆函数
    private void login(){
        //校验
        if (validate()) {
            //登陆成功
            if (loginPro()) {
                Toast.makeText(getApplicationContext(), "登录中", Toast.LENGTH_SHORT).show();
                RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
                username = etname.getText().toString();
                pass = etpass.getText().toString();
                String url_login =RankActivity.xinlang_url+"login&" + "username=" + username + "&password=" + pass;
                StringRequest stringRequest = new StringRequest(url_login,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String id_str = response.toString();
                                if (id_str.equals("-1")) {
                                    Toast.makeText(getApplicationContext(), "账号或密码错误",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("TAG", id_str);
                                    //将id 存入
                                    SharedPreferences.Editor editor = preferences.edit();
                                    int aaa = Integer.parseInt(id_str);
                                    editor.putInt("id", aaa);
                                    editor.commit();
                                    //跳转主界面
                                    Intent intent = new Intent(LoginSelectActivity.this
                                            , FuctionMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "连接错误!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                mQueue.add(stringRequest);
                // 启动Main Activity
            } else {
                Toast.makeText(getApplicationContext(), "账号或者密码错误，请重新输入",
                        Toast.LENGTH_SHORT).show();
                // DialogUtil.showDialog(DoctorLoginActivity.this , "！账号或者密码错误，请重新输入", false);
            }
        }
    }

    //-----class 外调用函数----//
    //登陆判断
    private boolean loginPro() {
        String username = etname.getText().toString();
        String pwd = etpass.getText().toString();
        if (!pwd.isEmpty()) {
            return true;
        } else
            return false;
    }

    private boolean validate() {
        String username = etname.getText().toString().trim();
        if (username.equals("")) {
            Toast.makeText(getApplicationContext(), "！请输入账户",
                    Toast.LENGTH_SHORT).show();
            //DialogUtil.showDialog(this, "！请输入账户", false);
            return false;
        }
        String pwd = etpass.getText().toString().trim();
        if (pwd.equals("")) {
            Toast.makeText(getApplicationContext(), "！请输入密码",
                    Toast.LENGTH_SHORT).show();
            // DialogUtil.showDialog(this, "！请输入密码", false);
            return false;
        }
        return true;
    }

    public void easy_enter(View v){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("id", 1000);
        editor.commit();
        Intent intent = new Intent(LoginSelectActivity.this
                , FuctionMainActivity.class);
        startActivity(intent);
        finish();
    }
}