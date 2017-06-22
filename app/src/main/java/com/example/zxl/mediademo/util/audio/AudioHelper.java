package com.example.zxl.mediademo.util.audio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;

import com.example.zxl.mediademo.MediaApplication;
import com.example.zxl.mediademo.util.LLL;
import com.example.zxl.mediademo.util.OnMediaStateChangeListener;

import java.io.File;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/23 15:33
 */

public class AudioHelper implements OnAudioOperateInter {

    private static volatile AudioHelper mInstance = null;
    private AudioServiceConn mAudioServiceConn;
    private AudioService.AudioPlayBind mAudioPlayBind;
    public static final String TAG = "AudioHelper";

    public static AudioHelper getInstance() {
        AudioHelper instance = mInstance;
        if (instance == null) {
            synchronized (AudioHelper.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new AudioHelper();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    private AudioHelper() {
        startService(getContext());
    }

    public void startService(Context context) {
        Intent intent = new Intent(context, AudioService.class);
        mAudioServiceConn = new AudioServiceConn();
        context.bindService(intent, mAudioServiceConn, Context.BIND_AUTO_CREATE);
        context.startService(intent);
    }

    public Context getContext() {
        return MediaApplication.getContext();
    }

    public void play(File srcFile, int position, OnMediaStateChangeListener onMediaStateChangeListener) {
        try {
            Uri uri = Uri.fromFile(srcFile);
            play(uri, position, onMediaStateChangeListener);
        } catch (Exception e) {
            LLL.eee("[play]" + e.toString());
        }
    }

    @Override
    public void play(Uri uri, int position, OnMediaStateChangeListener onMediaStateChangeListener) {
        if (!isEmptyBind()) {
            mAudioPlayBind.play(uri, position, onMediaStateChangeListener);
        }

    }

    @Override
    public void continuePlay(int position) {
        if (!isEmptyBind()) {
            mAudioPlayBind.continuePlay(position);
        }
    }

    @Override
    public void seekTo(int position) {
        if (!isEmptyBind()) {
            mAudioPlayBind.seekTo(position);
        }
    }

    @Override
    public int pause() {
        if (!isEmptyBind())
            return mAudioPlayBind.pause();
        return 0;
    }

    @Override
    public void stop() {
        if (!isEmptyBind()) {
            mAudioPlayBind.stop();
        }
    }


    @Override
    public boolean isPlaying() {
        if (!isEmptyBind()) {
            return mAudioPlayBind.isPlaying();
        }
        return false;
    }

    @Override
    public int getDuration() {
        if (!isEmptyBind()) {
            return mAudioPlayBind.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (!isEmptyBind()) {
            return mAudioPlayBind.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void release() {
        Context context = getContext();
        if (mAudioServiceConn != null) {
            context.unbindService(mAudioServiceConn);
        }
        context.stopService(new Intent(context, AudioService.class));
        mAudioServiceConn = null;
        mInstance = null;
    }

    public boolean isEmptyBind() {
        return mAudioPlayBind == null;
    }

    private class AudioServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAudioPlayBind = (AudioService.AudioPlayBind) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAudioPlayBind = null;
        }
    }
}
