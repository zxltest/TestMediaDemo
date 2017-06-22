package com.example.zxl.mediademo.util.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/23 16:58
 */

public class VideoSurfaceView extends SurfaceView {
    private boolean isCreateHolder = false;
    private OnSurfaceStateChangeListener mSurfaceStateChangeListener;

    public VideoSurfaceView(Context context) {
        this(context, null);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                isCreateHolder = true;
                if (mSurfaceStateChangeListener != null) {
                    mSurfaceStateChangeListener.surfaceCreated(holder);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mSurfaceStateChangeListener != null) {
                    mSurfaceStateChangeListener.surfaceChanged(holder, format, width, height);
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                isCreateHolder = false;
                if (mSurfaceStateChangeListener != null) {
                    mSurfaceStateChangeListener.surfaceDestroyed(holder);
                }
            }
        });
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public boolean isCreatedHolder() {
        return isCreateHolder;
    }

    public void setOnSurfaceStateChangeListener(OnSurfaceStateChangeListener onSurfaceStateChangeListener) {
        mSurfaceStateChangeListener = onSurfaceStateChangeListener;
    }

    public interface OnSurfaceStateChangeListener {
        void surfaceCreated(SurfaceHolder holder);

        void surfaceChanged(SurfaceHolder holder, int format, int width, int height);

        void surfaceDestroyed(SurfaceHolder holder);
    }
}
