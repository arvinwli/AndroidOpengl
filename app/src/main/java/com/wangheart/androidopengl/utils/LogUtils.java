package com.wangheart.androidopengl.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

/**
 * @author arvinwli
 * @description:
 * @date 2019/3/26
 */
public class LogUtils {
    public static void d(Object object){
        Logger.d(object);
    }
    public static void d(@NonNull String message, @Nullable Object... args){
        Logger.d(message,args);
    }

    public static void w(@NonNull String message, @Nullable Object... args){
        Logger.w(message,args);
    }

    public static void e(Throwable throwable,String message,Object... args){
        Logger.e(throwable,message,args);
    }
    public static void e(@NonNull String message, @Nullable Object... args){
        Logger.e(message,args);
    }
}
