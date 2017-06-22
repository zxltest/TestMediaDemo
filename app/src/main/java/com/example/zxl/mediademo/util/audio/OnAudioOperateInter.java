package com.example.zxl.mediademo.util.audio;

import android.net.Uri;

import com.example.zxl.mediademo.util.OnMediaStateChangeListener;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/23 16:41
 */

public interface OnAudioOperateInter {
    void play(Uri uri, final int position, OnMediaStateChangeListener mediaStateChangeListener);

    void continuePlay(int position);

    void seekTo(int position);

    int pause();

    void stop();

    boolean isPlaying();

    int getDuration();

    int getCurrentPosition();

    void release();
}
