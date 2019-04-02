package com.example.libraryqa_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


public class ThreeActivity extends Activity {
    private TextView textView1;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //全屏显示
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.three_activity);
        //view.setKeepScreenOn(true);//设置常亮
        //设置返回键
        //以下代码写在onCreate（）方法当中
        textView1 = (TextView) findViewById(R.id.sound_help);
        textView1.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {


                Intent intent = new Intent(ThreeActivity.this, MainActivity.class);

                startActivity(intent);

            }

        });
    }
}
