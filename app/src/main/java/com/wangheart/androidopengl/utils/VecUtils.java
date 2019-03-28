package com.wangheart.androidopengl.utils;

/**
 * @author arvinwli
 * @description: 向量计算
 * @date 2019/3/28
 */
public class VecUtils {
    /**
     * 标准化
     * @param vec
     */
    public static float[] normalize(float[] vec){
        if(vec==null||vec.length<3){
            return null;
        }
        float value= (float) Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]+vec[2]*vec[2]);
        if(value==0){
            return vec;
        }
        float[] res=new float[3];
        res[0]=vec[0]/value;
        res[1]=vec[1]/value;
        res[2]=vec[2]/value;
        return res;
    }

    public static void normalize(float[] vec,float[] target){
        if(vec==null||vec.length<3||target==null||target.length<3){
            return ;
        }
        float value= (float) Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]+vec[2]*vec[2]);
        if(value==0){
            return;
        }
        target[0]=vec[0]/value;
        target[1]=vec[1]/value;
        target[2]=vec[2]/value;
    }
    public static void normalize(float x,float y,float z,float[] target){
        if(target==null||target.length<3){
            return ;
        }
        float value= (float) Math.sqrt(x*x+y*y+z*z);
        if(value==0){
            return;
        }
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

    public static void multiply2Left(float[] vec,float x){
        if(isValid(vec)){
            vec[0]=vec[0]*x;
            vec[1]=vec[1]*x;
            vec[2]=vec[2]*x;
        }
    }
    public static float[] multiply(float[] vec,float x){
        if(isValid(vec)){
            float[] res=new float[3];
            res[0]=vec[0]*x;
            res[1]=vec[1]*x;
            res[2]=vec[2]*x;
            return res;
        }else{
            return null;
        }
    }

    public static void add2Left(float[] a,float[] b){
        if(!isValid(a)||!isValid(b)){
            return;
        }
        a[0]=a[0]+b[0];
        a[1]=a[1]+b[1];
        a[2]=a[2]+b[2];
    }

    public static void subtract2Left(float[] a,float[] b){
        if(!isValid(a)||!isValid(b)){
            return;
        }
        a[0]=a[0]-b[0];
        a[1]=a[1]-b[1];
        a[2]=a[2]-b[2];
    }

    public static boolean isValid(float[] vec){
        return vec!=null&&vec.length>=3;
    }
}
