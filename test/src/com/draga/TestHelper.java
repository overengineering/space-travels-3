package com.draga;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class TestHelper
{
    public static void invokeMethod(
        Class targetClass, String methodName, Object obj, Class[] argClasses, Object[] argObjects)
        throws InvocationTargetException, NoSuchMethodException, IllegalAccessException
    {
        Method method = targetClass.getDeclaredMethod(
            methodName, argClasses);
        method.setAccessible(true);
        method.invoke(obj, argObjects);
    }
}
