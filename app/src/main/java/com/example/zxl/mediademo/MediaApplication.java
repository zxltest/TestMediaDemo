package com.example.zxl.mediademo;

import android.app.Application;
import android.content.Context;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/23 15:30
 */

public class MediaApplication extends Application {
    private static Context _context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        _context = this;
    }

    public static Context getContext() {
        return _context;
    }
}
