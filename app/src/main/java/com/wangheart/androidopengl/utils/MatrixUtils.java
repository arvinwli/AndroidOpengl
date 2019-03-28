package com.wangheart.androidopengl.utils;

import java.util.Arrays;

/**
 * @author arvin
 * @description:  矩阵相关操作
 * @date 2019/3/27
 */
public class MatrixUtils {
    public static String formatToString(float[] mat){
        String res="";
        if(!isValid(mat,4)){
            res=Arrays.toString(mat);
            LogUtils.w("printMat4 failed,mat4 invalid %s",res );
            return res;
        }
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<16;i+=4){
            sb.append(String.format("%-8.2f\t\t%-8.2f\t\t%-8.2f\t\t%-8.2f\n",mat[i],mat[i+1],mat[i+2],mat[i+3]));
        }
        res=sb.toString();
        LogUtils.d(res);
        return res;
    }

    public static boolean isValid(float[] mat,int dimension){
        return mat!=null&&mat.length>=dimension*dimension;
    }

    public static float[] genMat4(){
        return new float[]{
                1.0f,0.0f,0.0f,0.0f,
                0.0f,1.0f,0.0f,0.0f,
                0.0f,0.0f,1.0f,0.0f,
                0.0f,0.0f,0.0f,1.0f
        };
    }
}
