package com.wangheart.androidopengl.utils;

/**
 * @author arvinwli
 * @description:
 * @date 2019/3/28
 */
public class VecUtils {
    public static float[] normalize(float[] vec){
        if(vec==null||vec.length<3){
            return null;
        }
        float value= (float) Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]+vec[2]*vec[2]);
        float[] res=new float[3];
        res[0]=vec[0]/value;
        res[1]=vec[1]/value;
        res[2]=vec[2]/value;
        return res;
    }

    /**
     * 标准化
     * @param vec
     * @param target
     */
    public static void normalize(float[] vec,float[] target){
        if(vec==null||vec.length<3||target==null||target.length<3){
            return ;
        }
        float value= (float) Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]+vec[2]*vec[2]);
        target[0]=vec[0]/value;
        target[1]=vec[1]/value;
        target[2]=vec[2]/value;
    }
    public static void normalize(float x,float y,float z,float[] target){
        if(target==null||target.length<3){
            return ;
        }
        float value= (float) Math.sqrt(x*x+y*y+z*z);
        target[0]=x/value;
        target[1]=y/value;
        target[2]=z/value;
    }

    /**
     * 叉乘
     * @param a
     * @param b
     * @return
     */
    public static float[] cross(float[] a,float[] b){
        if(a==null||a.length<3||b==null||b.length<3){
            return null;
        }
        float[] res=new float[3];
        res[0]=a[1]*b[2]-a[2]*b[1];
        res[1]=a[2]*b[0]-a[0]*b[2];
        res[2]=a[0]*b[1]-a[1]*b[0];
        return res;
    }
}
