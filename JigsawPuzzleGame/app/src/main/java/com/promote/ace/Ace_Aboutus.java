package com.promote.ace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.promote.jigsawpuzzlegame.R;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by ace on 2016/4/26.
 */
public class Ace_Aboutus extends Activity{

    private Button back_b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);
        getActionBar().hide();
        back_b=(Button)findViewById(R.id.ace_back_button);
        back_b.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
