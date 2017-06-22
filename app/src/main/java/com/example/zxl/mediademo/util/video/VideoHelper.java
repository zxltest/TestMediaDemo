package com.example.zxl.mediademo.util.video;

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
 * @Date: 2017/1/23 16:43
 */

public class VideoHelper implements OnVideoOperateInter {

    private static volatile VideoHelper mInstance = null;
    private VideoService.VedioPlayBind mVideoPlayBind;
    private ServiceConnection mVideoServiceConn;
    public static final String TAG = "VideoHelper";
    public static VideoHelper getInstance() {
        VideoHelper instance = mInstance;
        if (instance == null) {
            synchronized (VideoHelper.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new VideoHelper();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    private VideoHelper() {
        startService(getContext());
    }

    public Context getContext() {
        return MediaApplication.getContext();
    }

    public void startService(Context context) {
        Intent intent = new Intent(context, VideoService.class);
        mVideoServiceConn = new VideoServiceConn();
        context.bindService(intent, mVideoServiceConn, Context.BIND_AUTO_CREATE);
        context.startService(intent);
    }
    public void play(File srcFile, int position,VideoSurfaceView surfaceView, OnMediaStateChangeListener onMediaStateChangeListener) {
        try {
            Uri uri = Uri.fromFile(srcFile);
            play(uri, position, surfaceView,onMediaStateChangeListener);
        } catch (Exception e) {
            LLL.eee("[play]" + e.toString());
        }
    }
    @Override
    public void play(Uri uri, int position, VideoSurfaceView surfaceView, OnMediaStateChangeListener mediaStateChangeListener) {
        if (!isEmptyBind()) {
            mVideoPlayBind.play(uri, position, surfaceView, mediaStateChangeListener);
        }
    }

    @Override
    public void continuePlay(int position) {
        if (!isEmptyBind()) {
            mVideoPlayBind.continuePlay(position);
        }
    }

    @Override
    public void seekTo(int position) {
        if (!isEmptyBind()) {
            mVideoPlayBind.seekTo(position);
        }
    }

    @Override
    public int pause() {
        if (!isEmptyBind()) {
            return mVideoPlayBind.pause();
        }
        return 0;
    }

    @Override
    public void stop() {
        if (!isEmptyBind()) {
            mVideoPlayBind.stop();
        }
    }

    @Override
    public boolean isPlaying() {
        if (!isEmptyBind()) {
            return mVideoPlayBind.isPlaying();
        }
        return false;
    }

    @Override
    public int getDuration() {
        if (!isEmptyBind()) {
            return mVideoPlayBind.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (!isEmptyBind()) {
            return mVideoPlayBind.getCurrentPosition();
        }
        return 0;
    }

    public void release() {
        Context context = getContext();
        if (mVideoServiceConn != null) {
            context.unbindService(mVideoServiceConn);
        }
        context.stopService(new Intent(context, VideoService.class));
        mVideoServiceConn = null;
        mInstance = null;
    }

    public boolean isEmptyBind() {
        return mVideoPlayBind == null;
    }

    private class VideoServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mVideoPlayBind = (VideoService.VedioPlayBind) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mVideoPlayBind = null;
        }
    }
}
