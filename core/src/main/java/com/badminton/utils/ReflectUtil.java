package com.badminton.utils;

import org.apache.commons.lang.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * Created by Ethan.Yuan on 2017/1/9.
 * 反射工具类，随便乱写了几个功能，兄弟们继续添加
 */

public class ReflectUtil {

    public static void setFieldValue(Object obj, String fieldName, Object value){
        if(null == obj){
            return;
        }

        Field targetField = getTargetField(obj.getClass(), fieldName);

        try{
            FieldUtils.writeField(targetField, obj, value);
        }catch (Exception e){

        }
    }

    public static Field getTargetField(Class<?> targetClass, String fieldName){
        Field field = null;
        try{
            if(targetClass == null){
                return field;
            }

            if(Object.class.equals(targetClass)){
                return field;
            }

            field = FieldUtils.getDeclaredField(targetClass, fieldName, true);
            if (field == null) {
                field = getTargetField(targetClass.getSuperclass(), fieldName);
            }
        }catch (Exception e){
            // do nothing
        }
        return field;
    }

    public static Object getFieldValue(Object obj , String fieldName ){

        if(obj == null){
            return null ;
        }

        Field targetField = getTargetField(obj.getClass(), fieldName);

        try {
            return FieldUtils.readField(targetField, obj, true ) ;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
