package com.wangheart.androidopengl.es;

import android.opengl.GLES30;

/**
 * @author arvinwli
 * @description:
 * @date 2019/3/26
 */
public interface IShader {
    void use();

    void setBool(String name, boolean value);
    // ------------------------------------------------------------------------
    void setInt(String name, int value);
    // ------------------------------------------------------------------------
    void setFloat(String name, float value);
    // ------------------------------------------------------------------------
    void setVec2(String name, float x, float y);

    void setVec2(String name, float[] value);
    // ------------------------------------------------------------------------
    void setVec3(String name, float x, float y, float z);

    void setVec3(String name, float[] value);
    // ------------------------------------------------------------------------

    void setVec4(String name, float x, float y, float z, float w);

    void setVec4(String name, float[] value);
    // ------------------------------------------------------------------------
    void setMat2(String name, float[] value);
    // ------------------------------------------------------------------------
    void setMat3(String name, float[] value);
    // ------------------------------------------------------------------------
    void setMat4(String name, float[] value);

}
