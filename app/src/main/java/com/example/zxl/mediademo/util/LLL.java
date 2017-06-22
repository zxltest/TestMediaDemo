package com.example.zxl.mediademo.util;

import android.util.Log;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2017/1/23 16:39
 */

public class LLL {
    public static void eee(String msg) {
        Log.e("zxl", "[" + msg + "]");
    }

    public static void eet(String msg, Throwable th) {
        Log.e("zxl", "[" + msg + "]", th);
    }
}
