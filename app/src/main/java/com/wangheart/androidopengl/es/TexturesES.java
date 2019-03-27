package com.wangheart.androidopengl.es;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.support.annotation.DrawableRes;

import com.wangheart.androidopengl.OpenglApplication;
import com.wangheart.androidopengl.utils.LogUtils;

/**
 * @author arvin
 * @description: 纹理相关操作
 * @date 2019/3/27
 */
public class TexturesES {
    public static int loadTexture(@DrawableRes int resId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Matrix flip = new Matrix();
        flip.postScale(1f, -1f);
        final Bitmap bitmap = BitmapFactory.decodeResource(OpenglApplication.getInstance().getResources(),
                resId, options);
        if(bitmap==null){
            LogUtils.e("decodeResource bitmap failed");
            return -1;
        }
        //y轴翻转
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), flip, true);
        bitmap.recycle();
        return loadTexture(bmp);
    }

    public static int loadTexture(Bitmap bitmap) {
        if(bitmap==null){
            LogUtils.e("bitmap is null");
            return -1;
        }
        int[] textureIds = new int[1];
        GLES30.glGenTextures(1, textureIds, 0);
        if (textureIds[0] == 0) {
            LogUtils.e("Could not generate a new OpenGL textureId object.");
            bitmap.recycle();
            return -1;
        }
        // 绑定纹理到OpenGL
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureIds[0]);

        //设置默认的纹理过滤参数
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR_MIPMAP_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);

        // 加载bitmap到纹理中
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);

        // 生成MIP贴图
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);

        // 数据如果已经被加载进OpenGL,则可以回收该bitmap
        bitmap.recycle();
        // 取消绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        return textureIds[0];
    }
}
