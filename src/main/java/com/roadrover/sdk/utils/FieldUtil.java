package com.roadrover.sdk.utils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * 针对类，对象反射使用到的工具类
 */

public class FieldUtil {

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
}
