package com.wangheart.androidopengl.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ColorRes;

import com.wangheart.androidopengl.OpenglApplication;
import com.wangheart.androidopengl.R;

/**
 * @author arvin
 * @description:
 * @date 2019/3/29
 */
public class UIUtils {
    public static Context getContext(){
        return OpenglApplication.getInstance();
    }

    public static Resources getResources(){
        return getContext().getResources();
    }

    public static int getColor(@ColorRes int colorId){
        return getResources().getColor(colorId);
    }
}
