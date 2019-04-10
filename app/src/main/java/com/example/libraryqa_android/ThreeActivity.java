package com.example.libraryqa_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.skyfishjy.library.RippleBackground;

import pl.droidsonroids.gif.GifImageView;


public class ThreeActivity extends Activity {
    private GifImageView textView1;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //全屏显示
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        getWindow().setAttributes(params);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(NavUtils.getParentActivityName(ThreeActivity.this)!=null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.three_activity);
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        rippleBackground.startRippleAnimation();
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThreeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
