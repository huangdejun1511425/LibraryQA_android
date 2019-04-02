package com.example.libraryqa_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private Button btn7;
    private Button btn1;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //全屏显示
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //view.setKeepScreenOn(true);//设置常亮
        //设置返回键
        btn7 = (Button) findViewById(R.id.button7);
        btn1 = (Button) findViewById(R.id.button1);
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        //    通过setImageResource()来设置引用的图片
        imageView.setVisibility(View.VISIBLE);
        imageView.requestLayout();
        btn7.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, TwoActivity.class);
                startActivity(intent);
            }
        });
        btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, ThreeActivity.class);
                startActivity(intent);
            }
        });
    }
}

