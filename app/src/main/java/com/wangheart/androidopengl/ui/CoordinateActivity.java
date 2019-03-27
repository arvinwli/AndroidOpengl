package com.wangheart.androidopengl.ui;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.renderscript.Matrix4f;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.wangheart.androidopengl.common.BaseActivity;
import com.wangheart.androidopengl.es.IShader;
import com.wangheart.androidopengl.es.ShaderES30;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author arvinwli
 * @description: 坐标系统
 * @date 2019/3/26
 */
public class CoordinateActivity extends BaseActivity {
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

        float vColor[]={0.0f,1.0f,0.0f};
        FloatBuffer vertexBuffer;

        private IShader shader;
        private Matrix4f view;
        private Matrix4f model;
        private Matrix4f projection;
        private int width;
        private int height;

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
            try {
                shader=new ShaderES30(IOUtils.toString(getThis().getAssets().open("coordinate/triangle.vs")),
                        IOUtils.toString(getThis().getAssets().open("coordinate/triangle.fs")));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Logger.d("onSurfaceChanged");
            //glViewport中定义的位置和宽高进行2D坐标的转换，将OpenGL中的位置坐标转换为你的屏幕坐标
            GLES30.glViewport(0, 0, width, height);
            model=new Matrix4f();
            view=new Matrix4f();
            projection=new Matrix4f();
            model.translate(0.5f,0.0f,0.0f);
            model.scale(0.5f,0.5f,0.5f);
            model.rotate(-55.0f,1.0f,0.0f,0.0f);
            view.translate(0.0f,0.0f,-3.0f);
            projection.loadPerspective(45.0f,width/(float)height,0.1f,100.0f);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
//            Logger.d("onDrawFrame");
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
            //使用着色器程序
            shader.use();
            shader.setVec3("vColor",vColor);
            shader.setMat4("view",view.getArray());
            shader.setMat4("model",model.getArray());
            shader.setMat4("projection",projection.getArray());
            //绑定顶点数组对象
            GLES30.glBindVertexArray(VAO[0]);
            // 绘制三角形
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
            GLES30.glBindVertexArray(0);
        }
    }
}
