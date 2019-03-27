package com.wangheart.androidopengl.ui;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.wangheart.androidopengl.common.BaseActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author arvin
 * @description: 你好，三角形
 * @date 2019/3/26
 */
public class HelloTriangleActivity extends BaseActivity {
    private MyGLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new MyGLSurfaceView(getThis());
        setContentView(mGLSurfaceView);
    }

    protected class MyGLSurfaceView extends GLSurfaceView {

        public MyGLSurfaceView(Context context) {
            super(context);
            setEGLContextClientVersion(3);
            setRenderer(new MyRender());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    protected class MyRender implements GLSurfaceView.Renderer {
        int[] VBO = new int[1];
        int[] VAO = new int[1];
        //顶点数据
        float vertices[] = {
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.0f, 0.5f, 0.0f};
        FloatBuffer vertexBuffer;
        //顶点着色器
        final String vertexShader = "#version 300 es\n" +
                "precision mediump float;\n" +
                "layout (location = 0) in vec3 vPosition;\n" +
                "void main() {\n" +
                "    gl_Position = vec4(vPosition,1.0);\n" +
                "}";
        //片元着色器
        final String fragmentShader = "#version 300 es\n" +
                "out vec4 FragColor;\n" +
                "void main()\n" +
                "{\n" +
                "    FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
                "} ";

        private int shaderProgram;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Logger.d("onSurfaceCreated");
            //glClearColor来设置清空屏幕所用的颜色。当调用glClear函数，清除颜色缓冲之后，
            // 整个颜色缓冲都会被填充为glClearColor里所设置的颜色
            GLES30.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            vertexBuffer.put(vertices);
            vertexBuffer.position(0);

            GLES30.glGenVertexArrays(1, VAO, 0);
            GLES30.glBindVertexArray(VAO[0]);
            GLES30.glGenBuffers(1, VBO, 0);
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0]);
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GLES30.GL_STATIC_DRAW);
            GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false,
                    3 * 4, 0);
            GLES30.glEnableVertexAttribArray(0);

            int vs = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
            //将代码加入到着色器中，并编译
            GLES20.glShaderSource(vs, vertexShader);
            //编译shader代码
            GLES20.glCompileShader(vs);

            int fs = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
            //将资源加入到着色器中，并编译
            GLES20.glShaderSource(fs, fragmentShader);
            GLES20.glCompileShader(fs);
            // 创建空的OpenGL ES Program
            shaderProgram = GLES20.glCreateProgram();

            // 将vertex shader(顶点着色器)添加到program
            GLES20.glAttachShader(shaderProgram, vs);

            // 将fragment shader（片元着色器）添加到program
            GLES20.glAttachShader(shaderProgram, fs);

            // 链接 OpenGL ES program
            GLES20.glLinkProgram(shaderProgram);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Logger.d("onSurfaceChanged");
            //glViewport中定义的位置和宽高进行2D坐标的转换，将OpenGL中的位置坐标转换为你的屏幕坐标
            GLES30.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            Logger.d("onDrawFrame");
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
            //使用着色器程序
            GLES20.glUseProgram(shaderProgram);
            //绑定顶点数组对象
            GLES30.glBindVertexArray(VAO[0]);
            // 绘制三角形
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
            GLES30.glBindVertexArray(0);
        }
    }
}
