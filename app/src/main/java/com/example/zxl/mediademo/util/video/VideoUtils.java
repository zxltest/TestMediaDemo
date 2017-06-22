package com.example.zxl.mediademo.util.video;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.widget.FrameLayout;

import com.example.zxl.mediademo.util.LLL;
import com.example.zxl.mediademo.util.OnMediaStateChangeListener;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/23 16:48
 */

public class VideoUtils implements OnVideoOperateInter {
    private static volatile VideoUtils mInstance = null;
    public static final String TAG = "VideoUtils";
    private MediaPlayer mMediaPlayer;
    private VideoSurfaceView mSurfaceView;
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

    public static VideoUtils getInstance() {
        VideoUtils instance = mInstance;
        if (instance == null) {
            synchronized (VideoUtils.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new VideoUtils();
                    mInstance = instance;
                }
            }
        }
        return instance;
    }

    private VideoUtils() {
        initMedia();
    }

    public void initMedia() {
        mCurrStatu = PLAY_IDEL;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    mp.reset();
                } catch (Exception e) {
                    LLL.eee("[onCompletion]" + e.toString());
                }
                if (mMediaStateChangeListener != null) {
                    mMediaStateChangeListener.onCompleteListener();
                }
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LLL.eee("onError(" + what + "," + extra + ")");
                return false;
            }
        });

    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public boolean isEmptyMedia() {
        return mMediaPlayer == null;
    }

    public void setmMediaStateChangeListener(OnMediaStateChangeListener mediaStateChangeListener) {
        this.mMediaStateChangeListener = mediaStateChangeListener;
    }

    public void startPlay(final int position) {
        try {
            mMediaPlayer.setDataSource(mContext, mPlayUri);
            mMediaPlayer.prepareAsync();
            mCurrStatu = PLAY_PREPARE;
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    LLL.eee("[play]  (" + position + "," + mCurrStatu + ")");
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
                        if (mSurfaceView != null) {
                            mSurfaceView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setSurfaceViewSize();
                                }
                            }, 10);
                        }
                    } catch (Exception e) {
                        LLL.eee("[play][onPrepared][posotion==" + position + "]" + e.toString());
                    }
                }
            });
        } catch (Exception e) {
            LLL.eee("[play][posotion==" + position + "]" + e.toString());
        }
    }

    @Override
    public void play(Uri uri, final int position, VideoSurfaceView surfaceView, OnMediaStateChangeListener mediaStateChangeListener) {
        this.mMediaStateChangeListener = mediaStateChangeListener;
        this.mPlayUri = uri;
        this.mSurfaceView = surfaceView;
        try {
            stop();
            initMedia();
            if (mSurfaceView == null) {
                mMediaPlayer.setDisplay(null);
                startPlay(position);//play  surface==null
            } else {
                boolean isCreated = mSurfaceView.isCreatedHolder();
                if (!isCreated) {
                    mSurfaceView.setOnSurfaceStateChangeListener(new VideoSurfaceView.OnSurfaceStateChangeListener() {
                        @Override
                        public void surfaceCreated(SurfaceHolder holder) {
                            try {
                                mMediaPlayer.setDisplay(holder);
                                startPlay(position);//play  surface!=null isCreted=false
                            } catch (Exception e) {
                                LLL.eee("[play][surfaceCreated][posotion==" + position + "]" + e.toString());
                            }
                        }

                        @Override
                        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                            try {
                                if (mMediaPlayer != null) {
                                    mMediaPlayer.setDisplay(holder);
                                }
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void surfaceDestroyed(SurfaceHolder holder) {
                            try {
                                mMediaPlayer.setDisplay(null);
                            } catch (Exception e) {

                            }
                        }
                    });
                } else {
                    try {
                        mSurfaceView.requestLayout();
                        mMediaPlayer.setDisplay(mSurfaceView.getHolder());
                        startPlay(position);//play  surface!=null isCreted=true
                    } catch (Exception e) {
                        LLL.eee("[play][isCreated==true](" + position + ")" + e.toString());
                    }

                }
            }
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
                play(mPlayUri, position, mSurfaceView, mMediaStateChangeListener);
            }
        } else {
            play(mPlayUri, position, mSurfaceView, mMediaStateChangeListener);
        }
    }

    @Override
    public void seekTo(int position) {
        LLL.eee("[seekTo](" + position + ")");
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
            LLL.eee("[seekTo](" + position + ")   " + e.toString());
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
        if (!isEmptyMedia()) {
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

    public void setSurfaceViewSize() {
        try {
            if (mMediaPlayer != null && mSurfaceView != null) {
                int mVideoWidth = mMediaPlayer.getVideoWidth();
                int mVideoHeight = mMediaPlayer.getVideoHeight();
                int width = mSurfaceView.getWidth();
                int height = mSurfaceView.getHeight();
                LLL.eee("(" + mVideoWidth + "," + mVideoHeight + ")---(" + width + "," + height + ")");
                FrameLayout.LayoutParams sufaceviewParams = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
                if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    sufaceviewParams.height = width * mVideoHeight / mVideoWidth;
                } else if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    sufaceviewParams.width = height * mVideoWidth / mVideoHeight;
                } else {
                    sufaceviewParams.height = height;
                    sufaceviewParams.width = width;
                }
                sufaceviewParams.gravity = Gravity.CENTER;
                mSurfaceView.setLayoutParams(sufaceviewParams);
            }
        } catch (Exception e) {

        }

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
