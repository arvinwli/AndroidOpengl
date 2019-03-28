package com.wangheart.androidopengl.es;

import android.opengl.Matrix;

import com.wangheart.androidopengl.utils.LogUtils;
import com.wangheart.androidopengl.utils.MatUtils;
import com.wangheart.androidopengl.utils.VecUtils;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author arvin
 * @description:
 * @date 2019/3/27
 */
public class GlCamera {
    public enum Camera_Movement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }

    // Default camera values
    private float YAW = -90.0f;
    private float PITCH = 0.0f;
    private float SPEED = 2.5f;
    private float SENSITIVITY = 0.05f;
    private float ZOOM = 45.0f;

    private float[] position = {0.0f, 0.0f, 0.0f};
    private float[] front = {0.0f, 0.0f, -1.0f};
    private float[] up = {0.0f, 1.0f, 0.0f};
    private float[] right=new float[3];
    private float[] worldUp=new float[3];

    private float[] view;
    private float yaw = YAW;
    private float pitch = PITCH;
    // Camera options
    private float movementSpeed = SPEED;
    private float sensitivity = SENSITIVITY;
    private float zoom = ZOOM;

    public GlCamera(float[] position, float[] front, float[] up) {
        if (position != null && position.length >= 3) {
            System.arraycopy(position,0,this.position,0,3);
        }
        if (front != null && front.length >= 3) {
            System.arraycopy(front,0,this.front,0,3);
        }
        if (up != null && up.length >= 3) {
            System.arraycopy(up,0,this.up,0,3);
            System.arraycopy(up,0,this.worldUp,0,3);
        }
        updateCameraVectors();
    }

    public float[] getViewMatrix() {
        view = MatUtils.genMat4();
        Matrix.setLookAtM(view, 0,
                position[0], position[1], position[2],
                position[0]+front[0],position[1]+ front[1], position[2]+front[2],
                up[0], up[1], up[2]);
        return view;
    }

    // Calculates the front vector from the Camera's (updated) Euler Angles
    private void updateCameraVectors() {
        float yawRadians= (float) Math.toRadians(yaw);
        float pitchRadians= (float) Math.toRadians(pitch);
        float x = (float) Math.cos(yawRadians) * (float) Math.cos((pitchRadians));
        float y = (float) Math.sin(pitchRadians);
        float z = (float) Math.sin(yawRadians) * (float) Math.cos((pitchRadians));
//        LogUtils.d("frontCurrent:"+Arrays.toString(frontCurrent));
        VecUtils.normalize(x,y,z, front);
        VecUtils.normalize(VecUtils.cross(front, worldUp), right);
        VecUtils.normalize(VecUtils.cross(right, front), up);
    }

    // Processes input received from any keyboard-like input system. Accepts input parameter in the form of camera defined ENUM (to abstract it from windowing systems)
    public void processKeyboard(Camera_Movement direction)
    {
        float velocity = movementSpeed * 0.1f;
        if (direction == Camera_Movement.FORWARD)
            VecUtils.add2Left(position,VecUtils.multiply(front,velocity));
        if (direction == Camera_Movement.BACKWARD)
            VecUtils.subtract2Left(position,VecUtils.multiply(front,velocity));
        if (direction == Camera_Movement.LEFT)
            VecUtils.subtract2Left(position,VecUtils.multiply(right,velocity));
        if (direction == Camera_Movement.RIGHT)
            VecUtils.add2Left(position,VecUtils.multiply(right,velocity));
        updateCameraVectors();
    }

    public void processDirection(float xOffset,float yOffset){
        xOffset *= sensitivity;
        yOffset *= sensitivity;


        yaw   += xOffset;
        pitch += yOffset;
        LogUtils.d("xOffset %f,yOffset %f,yaw %f,pitch %f",xOffset,yOffset,yaw,pitch);

        if(pitch>89f){
            pitch=89f;
        }
        if(pitch<-89f){
            pitch=-89f;
        }
        if(yaw<-179){
            yaw=-179;
        }
        if(yaw>1){
            yaw=1;
        }
        updateCameraVectors();
    }

    public float[] getPosition() {
        return Arrays.copyOf(this.position,3);
    }

    public float[] getFront() {
        return Arrays.copyOf(this.front,3);
    }

    public float[] getUp() {
        return Arrays.copyOf(this.up,3);
    }

    public float[] getRight() {
        return Arrays.copyOf(this.right,3);
    }

    public float getZoom() {
        return zoom;
    }
}
