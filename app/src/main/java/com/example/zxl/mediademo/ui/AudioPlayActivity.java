package com.example.zxl.mediademo.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.zxl.mediademo.R;
import com.example.zxl.mediademo.util.audio.AudioHelper;

import java.io.File;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/23 15:50
 */

public class AudioPlayActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvStart, mTvPause, mTvContinue, mTvStop;
    private String path = "";
    private AudioHelper helper;
    private int pauseCurrentPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        initView();
        initData();
    }

    public void initView() {
        mTvStart = (TextView) findViewById(R.id.tv_start);
        mTvPause = (TextView) findViewById(R.id.tv_pause);
        mTvContinue = (TextView) findViewById(R.id.tv_continue);
        mTvStop = (TextView) findViewById(R.id.tv_stop);
        mTvStart.setOnClickListener(this);
        mTvPause.setOnClickListener(this);
        mTvContinue.setOnClickListener(this);
        mTvStop.setOnClickListener(this);
    }

    public void initData() {
        path = Environment.getExternalStorageDirectory().getPath() + File.separator + "test" + File.separator + "3826.mp3";
        helper = AudioHelper.getInstance();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start:
                pauseCurrentPosition = 0;
                File file = new File(path);
                if (file.exists()) {
                    helper.play(file, 0, null);
                }
                break;
            case R.id.tv_pause:
                pauseCurrentPosition = 0;
                pauseCurrentPosition = helper.pause();
                break;
            case R.id.tv_continue:
                File file1 = new File(path);
                if (file1.exists()) {
                    helper.continuePlay(pauseCurrentPosition);
                }
                break;
            case R.id.tv_stop:
                pauseCurrentPosition = 0;
                helper.stop();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (helper != null) {
            helper.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (helper != null) {
            helper.release();
        }
    }
}
