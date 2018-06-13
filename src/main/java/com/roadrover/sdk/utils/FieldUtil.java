package com.roadrover.sdk.utils;

import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 针对类，对象反射使用到的工具类
 */

public class FieldUtil {

    /**
     * 获取变量属性 field
     * @param className 类名
     * @param fieldName 变量名
     * @return
     */
    public static Field getField(String className, String fieldName) {
        if (TextUtils.isEmpty(className) || TextUtils.isEmpty(fieldName)) {
            return null;
        }

        Class c = null;
        try {
            c = Class.forName(className);
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (field != null && TextUtils.equals(field.getName(), fieldName)) {
                    return field;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定类，指定对象的指定变量值，可以获取静态的成员变量，object传null
     * @param className 类名
     * @param object    对象
     * @param fieldName 变量名
     * @return
     */
    public static Object getFieldValue(String className, Object object, String fieldName) {
        Field field = getField(className, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                return field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 设置变量值，不能设置静态变量
     * @param object    对象
     * @param fieldName 变量名
     * @param value     值
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        if (object == null) {
            Logcat.w("object is null!");
            return;
        }
        setFieldValue(object.getClass().getName(), object, fieldName, value);
    }

    /**
     * 设置变量值
     * @param className  类名
     * @param object     对象，可以为null，如果为null，则认为是设置静态变量
     * @param fieldName  变量名
     * @param value      值
     */
    public static void setFieldValue(String className, Object object, String fieldName, Object value) {
        Field field = getField(className, fieldName);
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(object, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取指定类，指定对象指定int成员的变量值，可以获取静态的成员变量，object传null
     * @param className 类名
     * @param object    对象
     * @param fieldName 变量名
     * @return
     */
    public static int getFieldIntValue(String className, Object object, String fieldName) {
        Object ret = getFieldValue(className, object, fieldName);
        if (ret != null) {
            try {
                return (int) ret;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 获取该类所有的属性，包括父对象的属性
     * @param c 类
     * @return
     */
    public static Field[] getAllDeclaredFields(Class<?> c) {
        if (null == c) {
            return null;
        }
        Field[] fields = c.getDeclaredFields();

        // 过滤"$change"之类的未知属性
        Set<Field> ret = new HashSet<Field>();
        for (int i = 0; i < fields.length; ++i) {
            if (fields[i].isSynthetic() || fields[i].getName().contains("$") || "serialVersionUID".equals(fields[i].getName()) ) {
                continue;
            }
            fields[i].setAccessible(true);
            try {
                fields[i].get(null); // 常量不做处理
                continue;
            } catch (Exception e) {

            }
            ret.add(fields[i]);
        }

        final int size = ret.size();
        fields = new Field[size];
        ret.toArray(fields);

        if (c.getSuperclass() != null) {
            Field[] superFileds = getAllDeclaredFields(c.getSuperclass());
            fields = ListUtils.concat(fields, superFileds);
        }
        return fields;
    }

    /**
     * 获取类的名字
     * @param c
     * @return
     */
    public static String getClassName(Class<?> c) {
        if (null == c) {
            return "";
        }
        String className = c.getName();

        if (className.contains(".")) {
            String[] names = className.split("\\.");
            if (!ListUtils.isEmpty(names)) {
                className = names[names.length - 1];
            }
        }
        return className;
    }

    /**
     * 通过类名，创建一个对象
     * @param className 类名
     * @return
     */
    public static Object createObject(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建内部类对象
     * @param className       公布出来的类
     * @param innerClassName  内部类
     * @return
     */
    public static Object createObject(String className, String innerClassName) {
        try {
            Class externalClazz = Class.forName(className);
            Class innerClazzs[] = externalClazz.getDeclaredClasses();

            if (innerClazzs != null) {
                for (Class clazz : innerClazzs) {
//                    Logcat.d("clazz.getName:" + clazz.getName() + " " + innerClassName);
                    if (TextUtils.equals(innerClassName, clazz.getName())) {
                        return clazz.getDeclaredConstructor(externalClazz).newInstance(externalClazz.newInstance());
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行静态方法，通过类名执行
     * @param className   类名
     * @param methodName  方法名
     * @param params      参数列表
     * @return
     */
    public static Object invoke(String className, String methodName, Object ...params) {
        if (TextUtils.isEmpty(className) || TextUtils.isEmpty(methodName)) {
            Logcat.w("className:" + className + " methodName" + methodName);
            return null;
        }

        Class<?>[] parameterTypes = getParameterTypes(params);
        try {
            Method method = getMethod(Class.forName(className), methodName, parameterTypes);
            if (method != null) {
                return method.invoke(null, params);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行一次反射操作，该方法主要用于当 params对象和parameterTypes不是一一对应情况，例如存在一些继承关系时
     * @param object          调用对象
     * @param methodName      方法名
     * @param parameterTypes  参数类型列表
     * @param params          参数列表
     * @return
     */
    public static Object invoke(Object object, String methodName, Class<?>[] parameterTypes, Object[] params) {
        if (object == null || TextUtils.isEmpty(methodName)) {
            Logcat.w("object is null, methodName:" + methodName);
            return null;
        }
        Method method = getMethod(object.getClass(), methodName, parameterTypes);
        if (method != null) {
            try {
                return method.invoke(object, params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 执行一次反射操作
     * @param methodName 方法名
     * @param params     参数列表
     * @return
     */
    public static Object invoke(Object object, String methodName, Object ...params) {
        if (object == null || TextUtils.isEmpty(methodName)) {
            Logcat.w("object is null, methodName:" + methodName);
            return null;
        }
        try {
            Class<?>[] parameterTypes = getParameterTypes(params);
            Method method = getMethod(object.getClass(), methodName, parameterTypes);
            if (method != null) {
                return method.invoke(object, params);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定类的指定方法，包含父类的方法
     * @param clasz          类
     * @param name           方法名
     * @param parameterTypes 参数列表
     * @return
     */
    private static Method getMethod(Class<?> clasz, String name, Class<?>... parameterTypes) {
//        Logcat.d("clasz:" + clasz.getName() + " name:" + name + " parameterTypes.length:" + parameterTypes);
        try {
            return clasz.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            if (clasz.getSuperclass() != null) { // 如果存在父类，到父类获取方法
                return getMethod(clasz.getSuperclass(), name, parameterTypes);
            } else {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 通过参数列表获取类列表
     * @param params
     * @return
     */
    private static Class<?>[] getParameterTypes(Object ...params) {
        Class<?>[] parameterTypes = null;
        if (params != null && params.length != 0) {
            parameterTypes = new Class<?>[params.length];
            for (int i = 0; i < params.length; ++i) {
                if (params[i] != null) {
                    parameterTypes[i] = params[i].getClass();
                    if (parameterTypes[i] == Integer.class) {
                        parameterTypes[i] = int.class;
                    }
                }
            }
        } else {
            parameterTypes = new Class<?>[0];
        }
        return parameterTypes;
    }
}
