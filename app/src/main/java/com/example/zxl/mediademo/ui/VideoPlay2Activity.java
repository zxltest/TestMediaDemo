package com.example.zxl.mediademo.ui;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.zxl.mediademo.R;
import com.example.zxl.mediademo.util.LLL;
import com.example.zxl.mediademo.util.OnMediaStateChangeListener;
import com.example.zxl.mediademo.util.video.VideoHelper;
import com.example.zxl.mediademo.util.video.VideoSurfaceView;

import java.io.File;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/4/21 17:57
 */

public class VideoPlay2Activity extends AppCompatActivity {
    private VideoSurfaceView surfaceView;
    private ImageView ivPaly;
    private SeekBar mProgress;
    private TextView mTvCurrent, mTvTotal;
    private String path = "";
    private VideoHelper helper;
    private int pauseCurrentPosition = 0;

    public boolean isRestart = false;
    private int total = 0;
    private int current = 0;

    public static final int PLAY_TIME_WHAT = 0x001;

    private boolean wasStop = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            diapatchMsg(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video2);
        initView();
        initData();
    }


    public void initView() {
        surfaceView = (VideoSurfaceView) findViewById(R.id.surface);
        ivPaly = (ImageView) findViewById(R.id.iv_play);
        mProgress = (SeekBar) findViewById(R.id.progress);
        mTvCurrent = (TextView) findViewById(R.id.tv_current);
        mTvTotal = (TextView) findViewById(R.id.tv_total);
        ivPaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClick();
            }
        });
        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (helper != null && total != 0) {
                    LLL.eee("progress---" + seekBar.getProgress());
                    helper.seekTo(seekBar.getProgress());
                }
            }
        });
    }

    public void initData() {
        pauseCurrentPosition = 0;
        path = Environment.getExternalStorageDirectory().getPath() + File.separator + "test" + File.separator + "2676.mp4";
        helper = VideoHelper.getInstance();
        surfaceView.postDelayed(new Runnable() {
            @Override
            public void run() {
                pauseCurrentPosition = 0;
                play();
            }
        }, 1000);
    }

    public void playClick() {
        if (isRestart) {
            play();
        } else {
            if (helper.isPlaying()) {
                pauseCurrentPosition = helper.pause();
            } else {
                helper.continuePlay(pauseCurrentPosition);
            }
        }
    }

    public void play() {
        if (helper != null) {
            File file = new File(path);
            if (file.exists()) {
                helper.play(file, pauseCurrentPosition, surfaceView, listener);
            }
        }
    }

    public void diapatchMsg(Message msg) {
        int what = msg.what;
        switch (what) {
            case PLAY_TIME_WHAT:
                current = helper.getCurrentPosition();
                mProgress.setProgress(current);
                mTvCurrent.setText(current / 1000 + "");
                if (helper.isPlaying()) {
                    mHandler.sendEmptyMessageDelayed(PLAY_TIME_WHAT, 100);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (helper != null && wasStop) {
            wasStop=false;
            LLL.eee("onResume"+pauseCurrentPosition);
            play();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        wasStop = true;
        if (helper != null && helper.isPlaying()) {
            pauseCurrentPosition = helper.pause();
            LLL.eee("onStop"+pauseCurrentPosition);
            ivPaly.setImageResource(R.mipmap.stop_step);
            mHandler.removeMessages(PLAY_TIME_WHAT);
            helper.stop();
        }
    }

    public OnMediaStateChangeListener listener = new OnMediaStateChangeListener() {
        @Override
        public void onMediaPlayStart() {
            super.onMediaPlayStart();
            isRestart = false;
            ivPaly.setImageResource(R.mipmap.stop_icon);
            total = helper.getDuration();
            current = helper.getDuration();
            mProgress.setMax(total);
            mProgress.setProgress(current);
            mTvTotal.setText(total / 1000 + "");
            mTvCurrent.setText(current / 1000 + "");
            mHandler.sendEmptyMessage(PLAY_TIME_WHAT);
        }

        @Override
        public void onMediaPlayContinueStart() {
            super.onMediaPlayContinueStart();
            isRestart = false;
            ivPaly.setImageResource(R.mipmap.stop_icon);
            mHandler.sendEmptyMessage(PLAY_TIME_WHAT);
        }

        @Override
        public void onMediaPlayPause() {
            super.onMediaPlayPause();
            isRestart = false;
            ivPaly.setImageResource(R.mipmap.stop_step);
            mHandler.removeMessages(PLAY_TIME_WHAT);
        }

        @Override
        public void onCompleteListener() {
            super.onCompleteListener();
            isRestart = true;
            pauseCurrentPosition = 0;
            ivPaly.setImageResource(R.mipmap.stop_step);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeMessages(PLAY_TIME_WHAT);
        }
        if (helper != null) {
            helper.release();
        }
    }
}
