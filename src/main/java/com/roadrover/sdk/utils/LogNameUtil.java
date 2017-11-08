package com.roadrover.sdk.utils;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 用来协助打印每个常量类的 getName 方法
 */

public class LogNameUtil {

    /**
     * 打印出 class 里面定义的常量名
     * 例： class 里面定义了 public static final int ID_MUSIC = 0;
     *      如果 id 传 0， 则返回 "ID_MUSIC"
     * @param id 需要打印的id
     * @param c  类对象，一般传 类.class
     * @return
     */
    public static String getName(int id, Class c) {
        return getName(id, c, "unknown:" + id);
    }

    /**
     * 打印出 class 里面定义的常量名
     * @param id
     * @param c
     * @param unknownString 未定义的提示
     * @return
     */
    public static String getName(int id, Class c, String unknownString, String... exceptArray) {
        if (c != null) {
            Field[] fields = c.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    if (null != field) {
                        field.setAccessible(true);
                        if (field.getType() == int.class) {
                            try {
                                int value = field.getInt(null);
                                if (value == id) {
                                    final String string = field.getName();
                                    boolean find = true;
                                    if (null != exceptArray) {
                                        for (int i = 0;i < exceptArray.length;i++) {
                                            if (TextUtils.equals(string, exceptArray[i])) {
                                                find = false;
                                                break;
                                            }
                                        }
                                    }
                                    if (find) {
                                        return string;
                                    }
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            }
        }
        return unknownString;
    }

    /**
     * 获取指定类名的所有值域
     * @param c
     * @return
     */
    public static ArrayList<Integer> getFields(Class c) {
        ArrayList<Integer> ret = new ArrayList<>();
        if (c != null) {
            Field[] fields = c.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    if (null != field) {
                        field.setAccessible(true);
                        if (field.getType() == int.class) {
                            try {
                                int value = field.getInt(null);
                                ret.add(value);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    /**
     * 获取filed名称对应的值
     * @param c 类名
     * @param name filed名称
     * @param def 默认值
     * @return
     */
    public static int getValue(Class c, String name, int def) {
        if (!TextUtils.isEmpty(name)) {
            ArrayList<Integer> list = getFields(c);
            if (null != list) {
                for (Integer id : list) {
                    if (name.equals(getName(id, c))) {
                        return id;
                    }
                }
            }
        }
        return def;
    }
}
