package com.draga;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 19/09/2015.
 */
public abstract class TestHelper
{
    public static void invokeMethod(
        Class targetClass,
        String methodName,
        Object obj, Class[] argClasses,
        Object[] argObjects) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException
    {
        Method method = targetClass.getDeclaredMethod(
            methodName,
            argClasses);
        method.setAccessible(true);
        method.invoke(obj, argObjects);
    }
}
