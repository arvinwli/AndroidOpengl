package com.wangheart.androidopengl.ui;

import android.opengl.Matrix;
import android.os.Bundle;
import android.renderscript.Matrix4f;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.wangheart.androidopengl.common.BaseActivity;
import com.wangheart.androidopengl.R;
import com.wangheart.androidopengl.utils.MatrixUtils;

import java.util.Arrays;

/**
 * @author arvin
 * @description:  矩阵变换操作
 * @date 2019/3/26
 */
public class TransformationsActivity extends BaseActivity {
    private TextView tvInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transformations);
        initView();
        initData();
    }

    private void initView() {
        tvInfo=findViewById(R.id.tv_info);
    }

    private float[] getMat4(){
        return new float[]{
            1.0f,0.0f,0.0f,0.0f,
            0.0f,1.0f,0.0f,0.0f,
            0.0f,0.0f,1.0f,0.0f,
            0.0f,0.0f,0.0f,1.0f
        };
    }

    private void initData() {
        //opengl转换的矩阵形式如下
        //   m0  m4   m8    m12
        //   m1  m5   m9    m13
        //   m2  m6   m10   m14
        //   m3  m7   m11   m15
        println("OpenGL转换的矩阵形式：\n");
        println(String.format("%-8s%-8s%-8s%-8s\n%-8s%-8s%-8s%-8s\n%-8s%-8s%-8s%-8s\n%-8s%-8s%-8s%-8s\n"
                ,"m0","m4","m8","m12"
                ,"m1","m5","m9","m13"
                ,"m2","m6","m10","m14"
                ,"m3","m7","m11","m15"));

        float[] trans=getMat4();
        println("单位矩阵：\n");
        println(MatrixUtils.formatToString(trans));

        Matrix.translateM(trans,0,1.0f,2.0f,3.0f);
        println("根据单位矩阵 translate (1.0,2.0,3.0)\n");
        println(MatrixUtils.formatToString(trans)+"\n");

        float[] scaleM=getMat4();
        Matrix.scaleM(scaleM,0,1.0f,2.0f,3.0f);
        println("根据单位矩阵 scale (1.0,2.0,3.0)\n");
        println(MatrixUtils.formatToString(scaleM)+"\n");

        float[] rotateM=getMat4();
        Matrix.rotateM(rotateM,0,90.0f,1.0f,2.0f,3.0f);
        println("根据单位矩阵 rotate (1.0,2.0,3.0)\n");
        println(MatrixUtils.formatToString(rotateM)+"\n");
    }

    private void println(String content){
        tvInfo.setText(tvInfo.getText()+content+"\n");
    }

}
