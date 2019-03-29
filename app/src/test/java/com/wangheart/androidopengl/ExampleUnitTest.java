package com.wangheart.androidopengl;

import com.wangheart.androidopengl.utils.VecUtils;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        double[] v=new double[]{2.0f,2.0f,2.0f};
        float[] v1=new float[]{2.0f,2.0f,2.0f};
        RealVector rv=new ArrayRealVector(v);
        System.out.println(rv.getNorm());
        System.out.println(Arrays.toString(VecUtils.normalize(v1)));

        float[] v11=new float[]{1.0f,0.0f,0.0f};
        float[] v12=new float[]{0.0f,1.0f,0.0f};
        System.out.println(Arrays.toString(VecUtils.normalize(VecUtils.cross(v11,v12))));


        float[] v21=new float[]{0.6f,-0.8f,0.0f};
        float[] v22=new float[]{0.0f,1.0f,0.0f};
        System.out.println(VecUtils.dotMultiply(v21,v22));

        float[] v31=new float[]{1.0f,0.0f,0.0f};
        float[] v32=new float[]{1.0f,1.0f,0.0f};
        System.out.println(VecUtils.getVecAngleRadians(v31,v32));
        System.out.println(VecUtils.length(v32));

        assertEquals(4, 2 + 2);
    }
}