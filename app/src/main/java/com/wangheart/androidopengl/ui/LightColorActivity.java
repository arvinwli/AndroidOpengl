package com.wangheart.androidopengl.ui;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;

import com.orhanobut.logger.Logger;
import com.wangheart.androidopengl.R;
import com.wangheart.androidopengl.common.BaseActivity;
import com.wangheart.androidopengl.es.GlCamera;
import com.wangheart.androidopengl.es.IShader;
import com.wangheart.androidopengl.es.ShaderES30;
import com.wangheart.androidopengl.es.TexturesES;
import com.wangheart.androidopengl.utils.LogUtils;
import com.wangheart.androidopengl.utils.MatUtils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author arvin
 * @description: 光照-颜色
 * @date 2019/3/26
 */
public class LightColorActivity extends BaseActivity {
    private MyGLSurfaceView mGLSurfaceView;
    private FrameLayout llCameraRoot;
    //摄像机
    private GlCamera glCamera;
    //onDrawFrame刷新时间
    private long lastTime = 0;
    //当前平移类型
    private GlCamera.Camera_Movement currentMovement = GlCamera.Camera_Movement.NONE;
    //手势处理
    private ScaleGestureDetector mScaleGestureDetector = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mScaleGestureDetector = new ScaleGestureDetector(getThis(), new ScaleGestureListener());
        mGLSurfaceView = new MyGLSurfaceView(getThis());
        llCameraRoot = findViewById(R.id.ll_camera_root);
        llCameraRoot.addView(mGLSurfaceView);
        TransTouchListener transTouchListener = new TransTouchListener();
        findViewById(R.id.btn_left).setOnTouchListener(transTouchListener);
        findViewById(R.id.btn_right).setOnTouchListener(transTouchListener);
        findViewById(R.id.btn_top).setOnTouchListener(transTouchListener);
        findViewById(R.id.btn_bottom).setOnTouchListener(transTouchListener);
    }

    protected class TransTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setMovement(v.getId());
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    currentMovement = GlCamera.Camera_Movement.NONE;
                    break;
            }
            return true;
        }
    }

    private void setMovement(int btnId) {
        switch (btnId) {
            case R.id.btn_left:
                currentMovement = GlCamera.Camera_Movement.LEFT;
                break;
            case R.id.btn_right:
                currentMovement = GlCamera.Camera_Movement.RIGHT;
                break;
            case R.id.btn_top:
                currentMovement = GlCamera.Camera_Movement.FORWARD;
                break;
            case R.id.btn_bottom:
                currentMovement = GlCamera.Camera_Movement.BACKWARD;
                break;
        }
    }

    protected class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        private float lastSpan = 0;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
//            float previousSpan = detector.getPreviousSpan();// 前一次双指间距
            float currentSpan = detector.getCurrentSpan();// 本次双指间距
            if (lastSpan == 0) {
                lastSpan = currentSpan;
                return false;
            }
            float scale = (currentSpan - lastSpan) / 10;
//            LogUtils.d("onScale currentSpan:%f,previousSpan:%f,scale:%f",currentSpan,lastSpan,scale);
            // 缩放view
            if (Math.abs(scale) > 0.1) {
                glCamera.processScale(scale);
            }
            lastSpan = currentSpan;
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            lastSpan = 0;
            LogUtils.d("onScaleBegin " + detector.getCurrentSpan());
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
//            preScale = scale;// 记录本次缩放比例
            LogUtils.d("onScaleEnd");
        }
    }

    protected class MyGLSurfaceView extends GLSurfaceView {
        private float lastX;
        private float lastY;
        //单点滑动是否有效
        private boolean isScrollValid = true;

        public MyGLSurfaceView(Context context) {
            super(context);
            setEGLContextClientVersion(3);
            setRenderer(new MyRender());
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
//            LogUtils.d("onTouchEvent "+event.getX()+" "+event.getY());
            int pointerCount = event.getPointerCount(); // 获得多少点
//            LogUtils.d("PointerCount:%d",pointerCount);
            //缩放处理
            if (pointerCount == 2) {
                mScaleGestureDetector.onTouchEvent(event);
            }
            //如果有多点触摸，就不做camera方向的改变
            if (pointerCount > 1) {
                isScrollValid = false;
            }
            LogUtils.d("MotionEvent %d ,index:%d", event.getAction(), event.getActionIndex());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isScrollValid = true;
                    lastX = event.getX();
                    lastY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isScrollValid) {
                        float xOffset = event.getX() - lastX;
                        float yOffset = lastY - event.getY();
                        lastX = event.getX();
                        lastY = event.getY();
                        glCamera.processDirection(-xOffset, -yOffset);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;

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
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
        };
        FloatBuffer vertexBuffer;

        private IShader shader;
        private IShader shaderLight;
        private float[] model;
        private float[] projection;
        private int width;
        private int height;
        private float[] lightPos = {0.6f, 0.5f, 1.0f};
        private float[] lightColor = {1.0f, 1.0f, 1.0f};
        private float[] objectColor = {1.0f, 0.5f, 0.31f};

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Logger.d("onSurfaceCreated");
            glCamera = new GlCamera(new float[]{0.0f, 0.0f, 3.0f},
                    new float[]{0.0f, 0.0f, 0.0f},
                    new float[]{0.0f, 1.0f, 0.0f});
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
                    5 * 4, 0);
            GLES30.glEnableVertexAttribArray(0);


//            FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
//                    .order(ByteOrder.nativeOrder())
//                    .asFloatBuffer();
//            vertexBuffer.put(vertices);
//            vertexBuffer.position(0);
            vertexBuffer.position(0);
            GLES30.glGenVertexArrays(1, VAO_LIGHT, 0);
            GLES30.glBindVertexArray(VAO_LIGHT[0]);
            GLES30.glGenBuffers(1, VBO_LIGHT, 0);
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO_LIGHT[0]);
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GLES30.GL_STATIC_DRAW);
            GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false,
                    5 * 4, 0);
            GLES30.glEnableVertexAttribArray(0);


            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            try {
                shader = new ShaderES30(IOUtils.toString(getThis().getAssets().open("light_color/box.vert")),
                        IOUtils.toString(getThis().getAssets().open("light_color/box.frag")));
                shaderLight = new ShaderES30(IOUtils.toString(getThis().getAssets().open("light_color/light.vert")),
                        IOUtils.toString(getThis().getAssets().open("light_color/light.frag")));
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
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
            if (lastTime == 0) {
                lastTime = System.currentTimeMillis();
            } else {
                long currentTime = System.currentTimeMillis();
                glCamera.processKeyboard(currentMovement, currentTime - lastTime);
                lastTime = currentTime;
            }
            shader.use();
            projection = MatUtils.genMat4();
            Matrix.perspectiveM(projection, 0, glCamera.getZoom(), width / (float) height, 0.1f, 100.0f);
            shader.setMat4("view", glCamera.getViewMatrix());
            shader.setMat4("projection", projection);
            //绑定顶点数组对象
            GLES30.glBindVertexArray(VAO[0]);
            model = MatUtils.genMat4();
            Matrix.translateM(model, 0, -0.5f, -0.5f, 0.0f);
            Matrix.scaleM(model, 0, 0.5f, 0.5f, 0.5f);
            shader.setMat4("model", model);
            shader.setVec3("objectColor", objectColor);
            shader.setVec3("lightColor", lightColor);
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);

            GLES30.glBindVertexArray(VAO_LIGHT[0]);
            shaderLight.use();
            shaderLight.setMat4("view", glCamera.getViewMatrix());
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
