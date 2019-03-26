package com.wangheart.androidopengl.common;

/**
 * @author arvinwli
 * @description:
 * @date 2019/3/26
 */
abstract public class BaseShader implements IShader {
    private int id;

    public int getId() {
        return id;
    }

    protected void setId(int id){
        this.id=id;
    }
}
