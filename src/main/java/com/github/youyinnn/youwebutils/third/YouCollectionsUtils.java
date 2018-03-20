package com.github.youyinnn.youwebutils.third;

import com.github.youyinnn.youwebutils.exceptions.YouMapException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 提供扫描包下类的方法;
 *
 * @author youyinnn
 */
public class YouCollectionsUtils {

    /**
     * 快速构建一个用于查询的条件和条件之组成的map
     *
     * 使用限制在方法中的错误提示已经写得很清楚了
     *
     * @param objects the objects
     * @return the you hash map
     */
    public static HashMap<String, Object> getYouHashMap(Object ... objects) {
        HashMap<String, Object> youMap = new HashMap<>(10);
        int length = objects.length;
        if (length % 2 != 0) {
            try {
                throw new YouMapException("传入参数数目不是偶数，无法创建完整的键值对。");
            } catch (YouMapException e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0 ; i < length ; i += 2) {
                if (objects[i] instanceof String){
                    youMap.put(String.valueOf(objects[i]),objects[i+1]);
                } else {
                    try {
                        throw new YouMapException("传入的第["+(i+1)+"]个参数不是String类型，不能作为键。");
                    } catch (YouMapException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return youMap;
    }

    /**
     * 快速构建一个用于选择查询列的list或者用于填充占位符的List
     *
     * @param field the query field list
     * @return the array list
     */
    public static ArrayList<String> getYouArrayList(String ... field){

        return new ArrayList<>(Arrays.asList(field));
    }

}
