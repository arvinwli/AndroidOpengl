package com.wangheart.androidopengl.ui;

import android.os.Bundle;
import android.renderscript.Matrix4f;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.wangheart.androidopengl.common.BaseActivity;
import com.wangheart.androidopengl.R;

import java.util.Arrays;

/**
 * @author arvinwli
 * @description:
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


    private void initData() {
//        float[] pos=new float[]{
//            1.0f,0.0f,0.0f,0.0f,
//            0.0f,1.0f,0.0f,0.0f,
//            0.0f,0.0f,1.0f,0.0f,
//            0.0f,0.0f,0.0f,1.0f
//        };
        Matrix4f matrix4f=new Matrix4f();
        println(Arrays.toString(matrix4f.getArray()));
        matrix4f.translate(1.0f,2.0f,3.0f);
       // matrix4f.rotate(1.0f,2.0f,3.0f);
        println(Arrays.toString(matrix4f.getArray()));
    }

    private void println(String content){
        tvInfo.setText(tvInfo.getText()+content+"\n");
    }

}
