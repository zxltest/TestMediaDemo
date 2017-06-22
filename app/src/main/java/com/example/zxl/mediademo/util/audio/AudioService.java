package com.example.zxl.mediademo.util.audio;

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
 * @Date: 2017/1/23 15:38
 */

public class AudioService extends Service {
    private AudioUtils mAudioUtils;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mAudioUtils == null) {
            mAudioUtils = AudioUtils.getInstance();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new AudioPlayBind();
    }

    public boolean isEmptyUtils() {
        return mAudioUtils == null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isEmptyUtils()) {
            mAudioUtils.stop();
            mAudioUtils.release();
            mAudioUtils = null;
        }
    }

    public class AudioPlayBind extends Binder implements OnAudioOperateInter {

        @Override
        public void play(Uri uri, int position, OnMediaStateChangeListener onMediaStateChangeListener) {
            if (!isEmptyUtils()) {
                mAudioUtils.setContext(getBaseContext());
                mAudioUtils.play(uri, position, onMediaStateChangeListener);
            }
        }

        @Override
        public void continuePlay(int position) {
            if (!isEmptyUtils()) {
                mAudioUtils.continuePlay(position);
            }
        }

        @Override
        public void seekTo(int position) {
            if (!isEmptyUtils()) {
                mAudioUtils.seekTo(position);
            }
        }

        @Override
        public int pause() {
            if (!isEmptyUtils()) {
                return mAudioUtils.pause();
            }
            return 0;
        }

        @Override
        public void stop() {
            if (!isEmptyUtils()) {
                mAudioUtils.stop();
            }
        }


        @Override
        public boolean isPlaying() {
            if (!isEmptyUtils()) {
                return mAudioUtils.isPlaying();
            }
            return false;
        }

        @Override
        public int getDuration() {
            if (!isEmptyUtils()) {
                return mAudioUtils.getDuration();
            }
            return 0;
        }

        @Override
        public int getCurrentPosition() {
            if (!isEmptyUtils()) {
                return mAudioUtils.getCurrentPosition();
            }
            return 0;
        }

        @Override
        public void release() {
            if (!isEmptyUtils()) {
                mAudioUtils.release();
            }
        }
    }


}
