package com.example.zxl.mediademo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.zxl.mediademo.ui.AudioPlayActivity;
import com.example.zxl.mediademo.ui.VideoPlay2Activity;
import com.example.zxl.mediademo.ui.VideoPlayActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AudioPlayActivity.class));
            }
        });
        findViewById(R.id.tv_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VideoPlayActivity.class));
            }
        });
        findViewById(R.id.tv_test3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VideoPlay2Activity.class));
            }
        });
    }

}
