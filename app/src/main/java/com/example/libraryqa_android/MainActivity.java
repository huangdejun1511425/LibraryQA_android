package com.example.libraryqa_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.skyfishjy.library.RippleBackground;

public class MainActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        getWindow().setAttributes(params);
//        //横屏显示
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        if(NavUtils.getParentActivityName(MainActivity.this)!=null) {
//            getActionBar().setDisplayHomeAsUpEnabled(true);
//        }
        setContentView(R.layout.activity_main);
        //设置波纹
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        rippleBackground.startRippleAnimation();
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到会话界面
                Intent intent = new Intent(MainActivity.this,TwoActivity.class);
                startActivity(intent);
            }
        });
    }
}
