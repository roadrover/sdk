package com.roadrover.sdk.utils;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 针对list的一些公共方法，列入剔除重复数据
 *
 * @author Administrator
 */
public class ListUtils<T> {

    /**
     * 剔除重复数据，该方法效率较低，如果可以，尽量直接用 Set
     *
     * @param sources
     * @return
     */
    public List<?> removeDuplicateWithOrder(List<T> sources) {
        List<T> result = new ArrayList<T>();

        for (T s : sources) {
            if (Collections.frequency(result, s) < 1)
                result.add(s);
        }
        return result;
    }

    /**
     * 合并两个数组
     *
     * @param a
     * @param b
     * @return
     */
    public static <T> T[] concat(T[] a, T[] b) {
        if (null == a) return b;
        if (null == b) return a;
        T[] c = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    /**
     * 是不是为空
     *
     * @param data
     * @return
     */
    public static boolean isEmpty(List data) {
        if (null == data || data.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断数组是否为空
     *
     * @param datas
     * @return
     */
    public static <T> boolean isEmpty(T[] datas) {
        if (null == datas || datas.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断数组是否为空
     * @param datas
     * @return
     */
    public static boolean isEmpty(byte[] datas) {
        if (null == datas || datas.length == 0) {
            return true;
        }
        return false;
    }

    /*
     * ArrayList转int[]
     */
    public static int[] listToIntArray(ArrayList<Integer> list) {

        if (list != null) {
            Integer[] arryInter = new Integer[list.size()];
            int[] arryint = new int[list.size()];
            list.toArray(arryInter);
            for (int i = 0; i < arryInter.length; i++) {
                arryint[i] = arryInter[i].intValue();
            }
            return arryint;
        }

        return null;
    }

    /**
     * 子项转换为字符串
     * @param item
     * @return
     */
    public String toString(T item) {
        if (null == item) {
            return "null";
        } else {
            return item.toString();
        }
    }

    /**
     * 列表转换为字符串
     * @param list
     * @param separator 分隔符，一般传入", "
     * @return
     */
    public String listToString(List<T> list, @NonNull String separator) {
        String ret = "";

        if (null != list) {
            final int size = list.size();
            if (size > 0) {
                ret = toString(list.get(0));
                for (int i = 1;i < size;i++) {
                    ret += separator + toString(list.get(i));
                }
            }
        }
        return ret;
    }

    /**
     * 比较两个列表是否完全相等
     * @param srcs 原始列表
     * @param dests 需要进行比较的列表
     * @return
     */
    public static <T> boolean equals(List<T> srcs, List<T> dests) {
        if (srcs == dests) {
            return true;
        }
        if (srcs != null && dests != null) {
            if (srcs.size() == dests.size()) {
                boolean isEqual = true;
                for (int i = 0; i < srcs.size(); ++i) {
                    if (srcs.get(i) != dests.get(i)) {
                        isEqual = false;
                        break;
                    }
                }
                return isEqual;
            }
        }
        return false;
    }
}
