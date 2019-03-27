package com.wangheart.androidopengl.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.wangheart.androidopengl.R;
import com.wangheart.androidopengl.common.BaseActivity;
import com.wangheart.androidopengl.es.IShader;
import com.wangheart.androidopengl.es.ShaderES30;
import com.wangheart.androidopengl.utils.LogUtils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author arvinwli
 * @description: 纹理
 * @date 2019/3/26
 */
public class TexturesActivity extends BaseActivity {
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
        int[] EBO = new int[1];
        final int[] textureIds = new int[1];
        //顶点数据
        float vertices[] = {
//     ---- 位置 ----       ---- 颜色 ----     - 纹理坐标 -
                0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f,   // 右上
                0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f,   // 右下
                -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f,   // 左下
                -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f    // 左上
        };

        short indices[] = { // 注意索引从0开始!
                0, 1, 3, // 第一个三角形
                1, 2, 3  // 第二个三角形
        };

        float vColor[]={1.0f,0.0f,0.0f};
        FloatBuffer vertexBuffer;
        ShortBuffer indicesBuffer;

        private IShader shader;

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

            indicesBuffer = ByteBuffer.allocateDirect(indices.length * 2)
                    .order(ByteOrder.nativeOrder())
                    .asShortBuffer();
            indicesBuffer.put(indices);
            indicesBuffer.position(0);

            GLES30.glGenVertexArrays(1, VAO, 0);
            GLES30.glBindVertexArray(VAO[0]);
            GLES30.glGenBuffers(1, VBO, 0);
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0]);
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GLES30.GL_STATIC_DRAW);
            GLES30.glGenBuffers(1,EBO,0);
            GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,EBO[0]);
            GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER,indicesBuffer.capacity()*2,indicesBuffer,GLES30.GL_STATIC_DRAW);

            GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false,
                    8 * 4, 0);
            GLES30.glEnableVertexAttribArray(0);

            GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false,
                    8 * 4, 3*4);
            GLES30.glEnableVertexAttribArray(1);

            GLES30.glVertexAttribPointer(2, 2, GLES30.GL_FLOAT, false,
                    8 * 4, 6*4);
            GLES30.glEnableVertexAttribArray(2);

            GLES30.glGenTextures(1, textureIds, 0);
            if (textureIds[0] == 0) {
                LogUtils.e("Could not generate a new OpenGL textureId object.");
                return;
            }
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.box, options);
            if (bitmap == null) {
                LogUtils.e( "Resource could not be decoded.");
                GLES30.glDeleteTextures(1, textureIds, 0);
                return;
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

            try {
                shader=new ShaderES30(IOUtils.toString(getThis().getAssets().open("textures/box.vert")),
                        IOUtils.toString(getThis().getAssets().open("textures/box.frag")));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Logger.d("onSurfaceChanged %d * %d",width,height);
            //glViewport中定义的位置和宽高进行2D坐标的转换，将OpenGL中的位置坐标转换为你的屏幕坐标
            GLES30.glViewport(0, 0, width, width);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
//            Logger.d("onDrawFrame");
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
            //使用着色器程序
            shader.use();
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,textureIds[0]);
            //绑定顶点数组对象
            GLES30.glBindVertexArray(VAO[0]);
            // 绘制三角形
            GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6,  GLES30.GL_UNSIGNED_SHORT,0);
            GLES30.glBindVertexArray(0);
        }
    }
}
