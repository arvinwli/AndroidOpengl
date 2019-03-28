package com.wangheart.androidopengl.es;

import android.opengl.Matrix;
import android.renderscript.Matrix4f;

/**
 * @author arvin
 * @description:
 * @date 2019/3/27
 */
public class GlCamera {
    enum Camera_Movement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    };

    // Default camera values
    private float YAW         = -90.0f;
    private float PITCH       =  0.0f;
    private float SPEED       =  2.5f;
    private float SENSITIVITY =  0.05f;
    private float ZOOM        =  45.0f;

    public float[] position={0.0f, 0.0f, 0.0f};
    public float[] front={0.0f, 0.0f, -1.0f};
    public float[] up={0.0f, 1.0f, 0.0f};
    public float[] right;
    public float[] worldUp;

    private float[] view;

    public GlCamera(float[] position,float[] front,float[] up) {
        if(position!=null&&position.length>=3){
            this.position=position;
        }
        if(front!=null&&front.length>=3){
            this.front=front;
        }
        if(up!=null&&up.length>=3){
            this.up=up;
        }
        this.worldUp=up;
    }

    public float[] getViewMatrix(){
        view= new Matrix4f().getArray();
        Matrix.setLookAtM(view,0,position[0],position[1],position[2],
                front[0],front[1],front[2],
                front[0],front[1],front[2]);
        return view;
    }

    // Calculates the front vector from the Camera's (updated) Euler Angles
    private void updateCameraVectors()
    {
        // Calculate the new Front vector
        //float[] frontCurrent;
        //frontCurrent[0] = cos(glm::radians(Yaw)) * cos(glm::radians(Pitch));
        //frontCurrent[1] = sin(glm::radians(Pitch));
        //frontCurrent[2] = sin(glm::radians(Yaw)) * cos(glm::radians(Pitch));
        //Front = glm::normalize(front);
        //// Also re-calculate the Right and Up vector
        //Right = glm::normalize(glm::cross(Front, WorldUp));  // Normalize the vectors, because their length gets closer to 0 the more you look up or down which results in slower movement.
        //Up    = glm::normalize(glm::cross(Right, Front));
    }
}
