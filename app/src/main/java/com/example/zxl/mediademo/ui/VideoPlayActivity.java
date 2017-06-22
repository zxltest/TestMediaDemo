package com.example.zxl.mediademo.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.zxl.mediademo.R;
import com.example.zxl.mediademo.util.video.VideoHelper;
import com.example.zxl.mediademo.util.video.VideoSurfaceView;

import java.io.File;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/23 15:50
 */

public class VideoPlayActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvStart, mTvPause, mTvContinue, mTvStop, mTvChangRes, mTvSeekTo;
    private VideoSurfaceView surfaceView;
    private String path = "";
    private String path1 = "";
    private String path2 = "";
    private VideoHelper helper;
    private int pauseCurrentPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
        initData();
    }

    public void initView() {
        surfaceView = (VideoSurfaceView) findViewById(R.id.surface);
        mTvStart = (TextView) findViewById(R.id.tv_start);
        mTvPause = (TextView) findViewById(R.id.tv_pause);
        mTvContinue = (TextView) findViewById(R.id.tv_continue);
        mTvStop = (TextView) findViewById(R.id.tv_stop);
        mTvChangRes = (TextView) findViewById(R.id.tv_changRes);
        mTvSeekTo = (TextView) findViewById(R.id.tv_seekto);
        mTvStart.setOnClickListener(this);
        mTvPause.setOnClickListener(this);
        mTvContinue.setOnClickListener(this);
        mTvStop.setOnClickListener(this);
        mTvChangRes.setOnClickListener(this);
        mTvSeekTo.setOnClickListener(this);
    }

    public void initData() {
        path1 = Environment.getExternalStorageDirectory().getPath() + File.separator + "test" + File.separator + "2674.mp4";
        path2 = Environment.getExternalStorageDirectory().getPath() + File.separator + "test" + File.separator + "2676.mp4";
        path = path1;
        helper = VideoHelper.getInstance();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start:
                pauseCurrentPosition = 0;
                File file = new File(path);
                if (file.exists()) {
                    helper.play(file, 0, surfaceView, null);
                }
                break;
            case R.id.tv_changRes:
                pauseCurrentPosition = 0;
                if (TextUtils.equals(path, path1)) {
                    path = path2;
                } else {
                    path = path1;
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
            case R.id.tv_seekto:
                pauseCurrentPosition += 1000;
                helper.seekTo(pauseCurrentPosition);
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
