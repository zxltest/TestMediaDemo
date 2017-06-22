package com.example.zxl.mediademo.util.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.example.zxl.mediademo.util.LLL;
import com.example.zxl.mediademo.util.OnMediaStateChangeListener;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/23 16:40
 */

public class AudioUtils implements OnAudioOperateInter {

    private static volatile AudioUtils mInstance = null;
    private MediaPlayer mMediaPlayer;
    private Context mContext;
    private Uri mPlayUri;
    private OnMediaStateChangeListener mMediaStateChangeListener;
    private int mCurrStatu;
    private static final int PLAY_IDEL = 0x01;  // 空闲
    private static final int PLAY_STOP = 0x02;  // 停止
    private static final int PLAY_PAUSE = 0x03; // 暂停
    private static final int PLAY_PLAY = 0x04;  // 正在播放
    private static final int PLAY_COMMPLETE = 0x05; // 播放完成
    private static final int PLAY_PREPARE = 0x06; // 播放准备
    private static final int PLAY_RELEASE = 0x06; // 释放资源

    private AudioUtils() {
        initMedia();
    }

    public static AudioUtils getInstance() {
        AudioUtils instance = mInstance;
        if (instance == null) {
            synchronized (AudioUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new AudioUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    public void initMedia() {
        mCurrStatu = PLAY_IDEL;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mCurrStatu = PLAY_COMMPLETE;
                if (mMediaStateChangeListener != null) {
                    mMediaStateChangeListener.onCompleteListener();
                }
            }
        });

    }

    public boolean isEmptyMedia() {
        return mMediaPlayer == null;
    }

    public void setmMediaStateChangeListener(OnMediaStateChangeListener mediaStateChangeListener) {
        this.mMediaStateChangeListener = mediaStateChangeListener;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    @Override
    public void play(Uri uri, final int position, OnMediaStateChangeListener mediaStateChangeListener) {
        LLL.eee("[play](" + position + ")");
        try {
            this.mPlayUri = uri;
            this.mMediaStateChangeListener = mediaStateChangeListener;
            stop();
            initMedia();
            mMediaPlayer.setDataSource(mContext, mPlayUri);
            mMediaPlayer.prepareAsync();
            mCurrStatu = PLAY_PREPARE;
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    LLL.eee("[play](" + position + "," + mCurrStatu + ")");
                    if (mCurrStatu != PLAY_PREPARE) {
                        return;
                    }
                    mCurrStatu = PLAY_PLAY;
                    try {
                        if (position == 0) {
                            mp.start();
                        } else {
                            int pos = Math.min(position, getDuration());
                            mp.seekTo(pos);
                            mp.start();
                        }
                        if (mMediaStateChangeListener != null) {
                            mMediaStateChangeListener.onMediaPlayStart();
                        }
                    } catch (Exception e) {
                        LLL.eee("[play][onPrepared](" + position + ")" + e.toString());
                    }
                }
            });
        } catch (Exception e) {
            LLL.eee("[play](" + position + ")" + e.toString());
        }
    }

    @Override
    public void continuePlay(int position) {
        LLL.eee("[continuePlay](" + position + ")");
        if (!isEmptyMedia()) {
            try {
                mMediaPlayer.seekTo(position);
                mMediaPlayer.start();
                if (mMediaStateChangeListener != null) {
                    mMediaStateChangeListener.onMediaPlayContinueStart();
                }
            } catch (Exception e) {
                LLL.eee("[continuePlay](" + position + ")" + e.toString());
                play(mPlayUri, position, mMediaStateChangeListener);
            }
        } else {
            play(mPlayUri, position, mMediaStateChangeListener);
        }
    }

    @Override
    public void seekTo(int position) {
        try {
            if (!isEmptyMedia()) {
                if (position < 0) {
                    position = 0;
                } else {
                    position = Math.min(position, getDuration());
                }
                mMediaPlayer.seekTo(position);
            }
        } catch (Exception e) {
            LLL.eee("[seekTo](" + position + ")" + e.toString());
        }
    }

    @Override
    public int pause() {
        mCurrStatu = PLAY_PAUSE;
        try {
            if (isPlaying()) {
                mMediaPlayer.pause();
                if (mMediaStateChangeListener != null) {
                    mMediaStateChangeListener.onMediaPlayPause();
                }
                return getCurrentPosition();
            }
        } catch (Exception e) {
            LLL.eee("[pause]" + e.toString());
        }
        return 0;
    }

    @Override
    public void stop() {
        mCurrStatu = PLAY_STOP;
        if (isEmptyMedia()) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (Exception e1) {
                LLL.eee("[stop1]" + e1.toString());
                try {
                    if (!isEmptyMedia()) {
                        mMediaPlayer.release();
                    }
                } catch (Exception e2) {
                    LLL.eee("[stop2]" + e2.toString());
                }
                mMediaPlayer = null;
            }
        }
        if (mMediaStateChangeListener != null) {
            mMediaStateChangeListener.onMediaPlayStop();
        }
    }

    @Override
    public boolean isPlaying() {
        try {
            if (!isEmptyMedia()) {
                return mMediaPlayer.isPlaying();
            }
        } catch (Exception e) {
            LLL.eee("[isPlaying]" + e.toString());
        }
        return false;
    }

    @Override
    public int getDuration() {
        try {
            if (!isEmptyMedia()) {
                return mMediaPlayer.getDuration();
            }
        } catch (Exception e) {
            LLL.eee("[getDuration]" + e.toString());
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        try {
            if (!isEmptyMedia()) {
                return mMediaPlayer.getCurrentPosition();
            }
        } catch (Exception e) {
            LLL.eee("[getCurrentPosition]" + e.toString());
        }
        return 0;
    }


    @Override
    public void release() {
        mCurrStatu = PLAY_RELEASE;
        if (!isEmptyMedia()) {
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
        mMediaStateChangeListener = null;
        mContext = null;
        mPlayUri = null;
    }
}
