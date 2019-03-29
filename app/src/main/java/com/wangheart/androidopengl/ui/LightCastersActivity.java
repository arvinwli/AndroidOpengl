package com.wangheart.androidopengl.ui;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import com.orhanobut.logger.Logger;
import com.wangheart.androidopengl.R;
import com.wangheart.androidopengl.common.BaseMovementActivity;
import com.wangheart.androidopengl.es.IShader;
import com.wangheart.androidopengl.es.ShaderES30;
import com.wangheart.androidopengl.es.TexturesES;
import com.wangheart.androidopengl.utils.MatUtils;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author arvin
 * @description: 光照-投光物
 * @date 2019/3/26
 */
public class LightCastersActivity extends BaseMovementActivity {
    private MyGLSurfaceView mGLSurfaceView;
    private final int LIGHT_DIRECTIONAL=0;
    private final int LIGHT_POINT=1;
    private final int LIGHT_SPOT=2;
    private int lightType=LIGHT_POINT;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView=new MyGLSurfaceView(getThis());
        setSurfaceView(mGLSurfaceView);
    }

    protected class MyGLSurfaceView extends GLSurfaceView {
        public MyGLSurfaceView(Context context) {
            super(context);
            setEGLContextClientVersion(3);
            setRenderer(new MyRender());
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return onSurfaceViewTouchEvent(event);
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
        int[] VBO_LIGHT = new int[1];
        int[] VAO_LIGHT = new int[1];
        //顶点数据
        float vertices[] = {
                // positions          // normals           // texture coords
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f, 0.0f,

                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   0.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,   0.0f, 0.0f,

                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f, 0.0f,
                -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f, 0.0f,

                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  1.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  0.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  1.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f, 1.0f,

                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f, 1.0f
        };
        float cubePositions[] = {
                0.0f, 0.0f, 0.0f,
                2.0f, 5.0f, -15.0f,
                -1.5f, -2.2f, -2.5f,
                -3.8f, -2.0f, -12.3f,
                2.4f, -0.4f, -3.5f,
                -1.7f, 3.0f, -7.5f,
                1.3f, -2.0f, -2.5f,
                1.5f, 2.0f, -2.5f,
                1.5f, 0.2f, -1.5f,
                -1.3f, 1.0f, -1.5f};
        FloatBuffer vertexBuffer;

        private IShader shader;
        private IShader shaderLight;
        private float[] model;
        private float[] projection;
        private int width;
        private int height;
        private float[] lightPos = {0.0f, 1.0f, -2.0f};
        private float[] lightColor = {1.0f, 1.0f, 1.0f};
        private float[] objectColor = {1.0f, 0.5f, 0.31f};
        private int texture0;
        private int texture1;

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
                    8 * 4, 0);
            GLES30.glEnableVertexAttribArray(0);

            GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false,
                    8* 4, 3 * 4);
            GLES30.glEnableVertexAttribArray(1);

            GLES30.glVertexAttribPointer(2, 2, GLES30.GL_FLOAT, false,
                    8 * 4, 6 * 4);
            GLES30.glEnableVertexAttribArray(2);

            vertexBuffer.position(0);
            GLES30.glGenVertexArrays(1, VAO_LIGHT, 0);
            GLES30.glBindVertexArray(VAO_LIGHT[0]);
            GLES30.glGenBuffers(1, VBO_LIGHT, 0);
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO_LIGHT[0]);
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GLES30.GL_STATIC_DRAW);
            GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false,
                    8 * 4, 0);
            GLES30.glEnableVertexAttribArray(0);
            texture0= TexturesES.loadTexture(R.mipmap.box);
            texture1= TexturesES.loadTexture(R.mipmap.box_specular);

            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            try {
                String shaderPath="light_casters/box_directional.frag";
                switch (lightType){
                    case LIGHT_DIRECTIONAL:
                        shaderPath="light_casters/box_directional.frag";
                        break;
                    case LIGHT_POINT:
                        shaderPath="light_casters/box_point.frag";
                        break;
                    case LIGHT_SPOT:
                        shaderPath="light_casters/box_spot.frag";
                        break;
                }
                shader = new ShaderES30(IOUtils.toString(getThis().getAssets().open("light_casters/box.vert")),
                        IOUtils.toString(getThis().getAssets().open(shaderPath)));
                shaderLight = new ShaderES30(IOUtils.toString(getThis().getAssets().open("light_casters/light.vert")),
                        IOUtils.toString(getThis().getAssets().open("light_casters/light.frag")));
                //设置shader对应的纹理单元
                shader.setInt("material.diffuse",  0);
                shader.setInt("material.specular",  1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Logger.d("onSurfaceChanged");
            //glViewport中定义的位置和宽高进行2D坐标的转换，将OpenGL中的位置坐标转换为你的屏幕坐标
            GLES30.glViewport(0, 0, width, height);
            this.width = width;
            this.height = height;
        }

        @Override
        public void onDrawFrame(GL10 gl) {
//            Logger.d("onDrawFrame");
            onRenderDraw();
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
            shader.use();
            //激活对应的纹理单元
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,texture0);
            GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,texture1);
            projection = MatUtils.genMat4();
            Matrix.perspectiveM(projection, 0, getGlCamera().getZoom(), width / (float) height, 0.1f, 100.0f);
            shader.setMat4("view",  getGlCamera().getViewMatrix());
            shader.setMat4("projection", projection);
            //绑定顶点数组对象
            GLES30.glBindVertexArray(VAO[0]);
            //shader.setVec3("objectColor", objectColor);
            //shader.setVec3("material.ambient",  1.0f, 0.5f, 0.31f);
            //shader.setVec3("material.diffuse",  1.0f, 0.5f, 0.31f);
            shader.setVec3("material.specular", 0.5f, 0.5f, 0.5f);
            shader.setFloat("material.shininess", 64.0f);
            shader.setVec3("viewPos", getGlCamera().getPosition());
            shader.setVec3("light.ambient",  0.2f, 0.2f, 0.2f);
            shader.setVec3("light.diffuse",  0.5f, 0.5f, 0.5f); // 将光照调暗了一些以搭配场景
            shader.setVec3("light.specular", 1.0f, 1.0f, 1.0f);
            switch (lightType){
                case LIGHT_DIRECTIONAL:
                    shader.setVec3("light.direction", new float[]{0.0f, 0.0f, -1});
                    break;
                case LIGHT_POINT:
                    shader.setVec3("light.position", lightPos);
                    shader.setFloat("light.constant",  1.0f);
                    shader.setFloat("light.linear",    0.09f);
                    shader.setFloat("light.quadratic", 0.032f);
                    break;
                case LIGHT_SPOT:
                    break;
            }

            for (int i = 0; i < cubePositions.length; i += 3) {
                model = MatUtils.genMat4();
                Matrix.translateM(model, 0, cubePositions[i], cubePositions[i + 1], cubePositions[i + 2]);
                Matrix.scaleM(model, 0, 0.5f, 0.5f, 0.5f);
                float angle = 20.0f * i;
                Matrix.rotateM(model, 0, angle, 1.0f, 0.3f, 0.5f);
                shader.setMat4("model", model);
                // 绘制三角形
                GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
            }

            GLES30.glBindVertexArray(VAO_LIGHT[0]);
            shaderLight.use();
            shaderLight.setMat4("view",  getGlCamera().getViewMatrix());
            shaderLight.setMat4("projection", projection);
            float[] modelLight = MatUtils.genMat4();
            Matrix.translateM(modelLight, 0, lightPos[0], lightPos[1], lightPos[2]);
            Matrix.scaleM(modelLight, 0, 0.1f, 0.1f, 0.1f);
            shaderLight.setMat4("model", modelLight);
            shaderLight.setVec3("lightColor", lightColor);
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
            GLES30.glBindVertexArray(0);
        }
    }
}
