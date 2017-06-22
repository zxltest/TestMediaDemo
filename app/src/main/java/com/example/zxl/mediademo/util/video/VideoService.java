package com.example.zxl.mediademo.util.video;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.zxl.mediademo.util.OnMediaStateChangeListener;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/23 16:44
 */

public class VideoService extends Service {

    private VideoUtils mVideoUtils;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mVideoUtils == null) {
            mVideoUtils = VideoUtils.getInstance();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new VedioPlayBind();
    }

    public boolean isEmptyUtils() {
        return mVideoUtils == null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isEmptyUtils()) {
            mVideoUtils.stop();
            mVideoUtils.release();
            mVideoUtils = null;
        }
    }

    public class VedioPlayBind extends Binder implements OnVideoOperateInter {

        @Override
        public void play(Uri uri, int position, VideoSurfaceView surfaceView, OnMediaStateChangeListener mediaStateChangeListener) {
            if (!isEmptyUtils()) {
                mVideoUtils.setContext(getBaseContext());
                mVideoUtils.play(uri, position, surfaceView, mediaStateChangeListener);
            }
        }

        @Override
        public void continuePlay(int position) {
            if (!isEmptyUtils()) {
                mVideoUtils.continuePlay(position);
            }
        }

        @Override
        public void seekTo(int position) {
            if (!isEmptyUtils()) {
                mVideoUtils.seekTo(position);
            }
        }

        @Override
        public int pause() {
            if (!isEmptyUtils()) {
                return mVideoUtils.pause();
            }
            return 0;
        }

        @Override
        public void stop() {
            if (!isEmptyUtils()) {
                mVideoUtils.stop();
            }
        }

        @Override
        public boolean isPlaying() {
            if (!isEmptyUtils()) {
                return mVideoUtils.isPlaying();
            }
            return false;
        }

        @Override
        public int getDuration() {
            if (!isEmptyUtils()) {
                return mVideoUtils.getDuration();
            }
            return 0;
        }

        @Override
        public int getCurrentPosition() {
            if (!isEmptyUtils()) {
                return mVideoUtils.getCurrentPosition();
            }
            return 0;
        }

        @Override
        public void release() {
            if (!isEmptyUtils()) {
                mVideoUtils.release();
            }
        }
    }
}
