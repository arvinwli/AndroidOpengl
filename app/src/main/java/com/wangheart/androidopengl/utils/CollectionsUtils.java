package com.wangheart.androidopengl.utils;

import java.util.Collection;
import java.util.List;

/**
 * @author arvin
 * @description:
 * @date 2019/3/29
 */
public class CollectionsUtils {
    public  static <T> boolean isEmpty(Collection<T> collection){
        return collection==null||collection.isEmpty();
    }
}
