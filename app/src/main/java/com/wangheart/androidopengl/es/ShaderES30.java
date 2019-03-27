package com.wangheart.androidopengl.es;

import android.opengl.GLES20;
import android.opengl.GLES30;

import com.wangheart.androidopengl.utils.LogUtils;

/**
 * @author arvin
 * @description: Shader
 * @date 2019/3/26
 */
public class ShaderES30 extends BaseShader {

    public ShaderES30(String vsCode, String fsCode) {
        LogUtils.d("vsCode:\n"+vsCode);
        LogUtils.d("fsCode:\n"+fsCode);
        int vs = GLES30.glCreateShader(GLES20.GL_VERTEX_SHADER);
        //将代码加入到着色器中，并编译
        GLES30.glShaderSource(vs, vsCode);
        //编译shader代码
        GLES30.glCompileShader(vs);

        int fs = GLES30.glCreateShader(GLES30.GL_FRAGMENT_SHADER);
        //将资源加入到着色器中，并编译
        GLES30.glShaderSource(fs, fsCode);
        GLES30.glCompileShader(fs);
        // 创建空的OpenGL ES Program
        int shaderProgram = GLES30.glCreateProgram();

        // 将vertex shader(顶点着色器)添加到program
        GLES30.glAttachShader(shaderProgram, vs);

        // 将fragment shader（片元着色器）添加到program
        GLES30.glAttachShader(shaderProgram, fs);

        // 链接 OpenGL ES program
        GLES30.glLinkProgram(shaderProgram);

        setId(shaderProgram);
    }

    @Override
    public void use() {
        GLES30.glUseProgram(getId());
    }

    @Override
    public void setBool(String name, boolean value) {
        GLES30.glUniform1i(GLES20.glGetUniformLocation(getId(),name),value?1:0);
    }

    @Override
    public void setInt(String name, int value) {
        GLES30.glUniform1i(GLES20.glGetUniformLocation(getId(),name),value);
    }

    @Override
    public void setFloat(String name, float value) {
        GLES30.glUniform1f(GLES20.glGetUniformLocation(getId(),name),value);
    }

    @Override
    public void setVec2(String name, float x, float y) {
        GLES30.glUniform2f(GLES20.glGetUniformLocation(getId(),name),x,y);
    }

    @Override
    public void setVec2(String name, float[] value) {
        GLES30.glUniform2fv(GLES20.glGetUniformLocation(getId(),name),1,value,0);
    }

    @Override
    public void setVec3(String name, float x, float y, float z) {
        GLES30.glUniform3f(GLES20.glGetUniformLocation(getId(),name),x,y,z);
    }

    @Override
    public void setVec3(String name, float[] value) {
        GLES30.glUniform3fv(GLES20.glGetUniformLocation(getId(),name),1,value,0);
    }

    @Override
    public void setVec4(String name, float x, float y, float z, float w) {
        GLES30.glUniform4f(GLES20.glGetUniformLocation(getId(),name),x,y,z,w);
    }

    @Override
    public void setVec4(String name, float[] value) {
        GLES30.glUniform4fv(GLES20.glGetUniformLocation(getId(),name),1,value,0);
    }

    @Override
    public void setMat2(String name, float[] value) {
        GLES30.glUniformMatrix2fv(GLES20.glGetUniformLocation(getId(),name),1,false,value,0);

    }

    @Override
    public void setMat3(String name, float[] value) {
        GLES30.glUniformMatrix3fv(GLES20.glGetUniformLocation(getId(),name),1,false,value,0);
    }

    @Override
    public void setMat4(String name, float[] value) {
        GLES30.glUniformMatrix4fv(GLES20.glGetUniformLocation(getId(),name),1,false,value,0);
    }
}
