package com.promote.esmond;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import com.promote.jigsawpuzzlegame.R;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Esmond on 2016/4/17.
 */
public class RankActivity extends Activity {

    //定义输入框 按钮
    TextView btncli;
    Toast tst;
    private ListView listView;
    private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    public static String xinlang_url="http://1.paintbackstage.applinzi.com/user?action=";
//    private static final int REFRESH_COMPLETE = 0X110;
//    private SwipeRefreshLayout mSwipeLayout;
    SimpleAdapter adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranklist);
        getActionBar().hide();
        listView = (ListView) findViewById(R.id.rank_listview);
        listView.setLayoutAnimation(getListAnim());
        convertJsonString();
        btncli = (TextView) findViewById(R.id.cli);
        btncli.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                cleanlist();
                convertJsonString();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

//        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
//
//        mSwipeLayout.setOnRefreshListener(this);
//        mSwipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private LayoutAnimationController getListAnim() {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(300);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(
                set, 0.5f);
        return controller;
    }

    //连接服务器
//    protected void link() {
//        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
//        String url_login = xinlang_url+"rank";
//        StringRequest stringRequest = new StringRequest(url_login,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        String id_str = response.toString();
//                        if (id_str.equals("-1")) {
//                            Toast.makeText(getApplicationContext(), "账号或密码错误",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.d("TAG", id_str);
//                            Toast.makeText(getApplicationContext(), id_str,
//                                    Toast.LENGTH_SHORT).show();
//                            convertJsonString(id_str);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "！url连接错误",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//        mQueue.add(stringRequest);
//    }

    private void convertJsonString() {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(xinlang_url+"rank",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jb = response.getJSONObject(i);
                                String username = jb.getString("username");
                                int grade = jb.getInt("grade");

                                Map<String, String> map = new HashMap<String, String>();
                                map.put("rank", "" + (i + 1));
                                map.put("nick", username);
                                map.put("origtext", "" + grade);
                                data.add(map);
                                createlistviewv();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "！连接错误",
                        Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    private void createlistviewv() {
//        for (int i = 0; i < 8; i++) {
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("rank", "" + (i + 1));
//            map.put("nick", "huangyonghui"+i);
//            map.put("origtext", "" +(15000- i*1000));
//            data.add(map);
//        }

        adapter = new SimpleAdapter(
                this,
                data, //数据
                R.layout.ranklist_view,  //listview的布局文件
                new String[]{"rank", "nick", "origtext"},  //填充的数据的key
                new int[]{R.id.tv_rank, R.id.tv_nick, R.id.tv_origtext}  //填充对象的id
        );
        listView.setAdapter(adapter);

//        listView.getChildAt(0).setBackground(this.getResources().getDrawable(R.drawable.bg_border1));
//        listView.getChildAt(1).setBackground(this.getResources().getDrawable(R.drawable.bg_border2));
//        listView.getChildAt(3).setBackground(this.getResources().getDrawable(R.drawable.bg_border3));
    }

//    private Handler mHandler = new Handler()
//    {
//        public void handleMessage(android.os.Message msg)
//        {
//            switch (msg.what)
//            {
//                case REFRESH_COMPLETE:
//                    cleanlist();
//                    createlistviewv();
//                    mSwipeLayout.setRefreshing(false);
//                    break;
//
//            }
//        };
//    };
//
//
    //清除处理
    private void cleanlist(){
        int size=data.size();
        if(size>0){
            System.out.println(size);
            data.removeAll(data);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }
//
//    @Override
//    public void onRefresh() {
//        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
//    }
}
