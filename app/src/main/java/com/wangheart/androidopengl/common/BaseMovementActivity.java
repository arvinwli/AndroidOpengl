package com.wangheart.androidopengl.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;

import com.wangheart.androidopengl.R;
import com.wangheart.androidopengl.es.GlCamera;
import com.wangheart.androidopengl.utils.LogUtils;

/**
 * @author arvinwli
 * @description:
 * @date 2019/3/29
 */
abstract public class BaseMovementActivity extends BaseActivity {
    //onDrawFrame刷新时间
    private long lastTime = 0;
    private FrameLayout llCameraRoot;
    //摄像机
    private GlCamera glCamera;
    //当前平移类型
    private GlCamera.Camera_Movement currentMovement = GlCamera.Camera_Movement.NONE;
    //手势处理
    private ScaleGestureDetector mScaleGestureDetector = null;

    private float lastX;
    private float lastY;
    //单点滑动是否有效
    private boolean isScrollValid = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_movement);
        mScaleGestureDetector = new ScaleGestureDetector(getThis(), new ScaleGestureListener());
        llCameraRoot = findViewById(R.id.ll_camera_root);
        TransTouchListener transTouchListener = new TransTouchListener();
        findViewById(R.id.btn_left).setOnTouchListener(transTouchListener);
        findViewById(R.id.btn_right).setOnTouchListener(transTouchListener);
        findViewById(R.id.btn_top).setOnTouchListener(transTouchListener);
        findViewById(R.id.btn_bottom).setOnTouchListener(transTouchListener);
        glCamera = new GlCamera(new float[]{0.0f, 0.0f, 3.0f},
                new float[]{0.0f, 0.0f, 0.0f},
                new float[]{0.0f, 1.0f, 0.0f});
    }

    public boolean onSurfaceViewTouchEvent(MotionEvent event) {
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
                    getGlCamera().processDirection(-xOffset, -yOffset);
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


    protected void onRenderDraw() {
        if (lastTime == 0) {
            lastTime = System.currentTimeMillis();
        } else {
            long currentTime = System.currentTimeMillis();
            getGlCamera().processKeyboard(currentMovement, currentTime - lastTime);
            lastTime = currentTime;
        }
    }

    public GlCamera getGlCamera() {
        return glCamera;
    }

    protected void setSurfaceView(View view) {
        llCameraRoot.addView(view);
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
}
