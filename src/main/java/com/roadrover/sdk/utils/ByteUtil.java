package com.roadrover.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * 处理byte的工具类
 */

public class ByteUtil {

    /**
     * 转换命令为int
     * @param cmd
     * @return
     */
    public static int getCmd(byte cmd) {
        return cmd & 0xff;
    }

    /**
     * 在原来 srcs 数组上增加一个 dest byte
     * @param srcs
     * @param dest
     * @return
     */
    public static byte[] addByte(byte[] srcs, byte dest) {
        if (null == srcs) return new byte[]{dest};
        byte[] results = new byte[srcs.length + 1];
        for (int i = 0; i < srcs.length; ++i) {
            results[i] = srcs[i];
        }
        results[srcs.length] = dest;
        return results;
    }

    /**
     * 在 srcs 数组上增加一个 dests
     * @param srcs
     * @param dests
     * @return
     */
    public static byte[] addBytes(byte[] srcs, byte[] dests) {
        return concat(srcs, dests);
    }

    /**
     * 从 srcs 列表中找到 dest 字节的位置
     * @param srcs
     * @param dest
     * @return
     */
    public static int indexOf(byte[] srcs, byte dest) {
        if (null == srcs) return -1;
        for (int i = 0; i < srcs.length; ++i) {
            if (srcs[i] == dest) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 将dests 合并到 srcs
     * @param srcs
     * @param dests
     * @return
     */
    public static byte[] concat(byte[] srcs, byte[] dests) {
        if (null == srcs) return dests == null ? new byte[0] : dests;
        if (null == dests) return srcs;
        byte[] result = Arrays.copyOf(srcs, srcs.length + dests.length);
        System.arraycopy(dests, 0, result, srcs.length, dests.length);
        return result;
    }

    /**
     * 从byte[] srcs 里面截取 start 开头， 长度为len的数组
     * @param srcs
     * @param start
     * @param len
     * @return
     */
    public static byte[] subBytes(byte[] srcs, int start, int len) {
        if (null == srcs) return new byte[0];
        if (start >= srcs.length || len < 0) return new byte[0];
        if (start < 0) start = 0;
        if (start + len > srcs.length) len = srcs.length - start;
        byte[] result = new byte[len];
        System.arraycopy(srcs, start, result, 0, len);
        return result;
    }

    /**
     * 转换成string
     * @param buff
     * @return
     */
    public static String intsToString(int[] buff) {
        StringBuilder sb = new StringBuilder();
        if (buff != null) {
            for (int i = 0; i < buff.length; ++i) {
                String string = " " + buff[i];
                sb.append(string);
            }
        }
        return sb.toString();
    }

    /**
     * 转换成string，16进制输出
     * @param buff
     * @return
     */
    public static String bytesToString(byte[] buff) {
        StringBuilder sb = new StringBuilder();
        if (buff != null) {
            for (int i = 0; i < buff.length; ++i)
                sb.append(String.format("%02x ", buff[i]));
        }
        return sb.toString();
    }

    /**
     * 转换成 Ascii 码输出
     * @param buff
     * @return
     */
    public static String bytesToStringAscii(byte[] buff) {
        try {
            return new String(buff, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * tag 在 src 的位置，没找到返回 -1
     * @param tag
     * @param src
     * @return 找到tag在src的位置，找到返回位置，否则返回-1
     */
    public static int indexOf(byte[] tag, byte[] src) {
        return indexOf(tag, src, 1);
    }

    public static int indexOf(byte[] tag, byte[] src, int index) {
        return indexOf(tag, src, index, src.length);
    }

    public static int indexOf(byte[] tag, byte[] src, int index, int len) {
        if (len <= src.length) {
            int tagLen = tag.length;
            byte[] tmp = new byte[tagLen];
            for (int j = 0; j < len - tagLen + 1; j++) {
                for (int i = 0; i < tagLen; i++) {
                    tmp[i] = src[j + i];
                }
                for (int i = 0; i < tagLen; i++) {
                    if (tmp[i] != tag[i])
                        break;
                    if (i == tagLen - 1) {
                        return j;
                    }
                }

            }
        }
        return -1;
    }

    /**
     * 找到 tag 在 src 内的最后一个位置
     * @param tag
     * @param src
     * @return
     */
    public static int lastIndexOf(byte[] tag, byte[] src) {
        return lastIndexOf(tag, src, 1);
    }

    public static int lastIndexOf(byte[] tag, byte[] src, int index) {
        return lastIndexOf(tag, src, src.length);
    }

    public static int lastIndexOf(byte[] tag, byte[] src, int index, int len) {
        if (len <= src.length) {
            int tagLen = tag.length;
            byte[] tmp = new byte[tagLen];
            for (int j = len - tagLen; j >= 0; j--) {
                for (int i = 0; i < tagLen; i++) {
                    tmp[i] = src[j + i];

                }
                for (int i = 0; i < tagLen; i++) {
                    if (tmp[i] != tag[i])
                        break;
                    if (i == tagLen - 1) {
                        return j;
                    }
                }

            }
        }
        return -1;
    }

    /**
     * 获取tag和src两个相等的长度
     * @param tag
     * @param src
     * @return
     */
    public static int size(byte[] tag, byte[] src) {
        int size = 0;
        int tagLen = tag.length;
        int srcLen = src.length;
        byte[] tmp = new byte[tagLen];
        for (int j = 0; j < srcLen - tagLen + 1; j++) {
            for (int i = 0; i < tagLen; i++) {
                tmp[i] = src[j + i];
            }
            for (int i = 0; i < tagLen; i++) {
                if (tmp[i] != tag[i])
                    break;
                if (i == tagLen - 1) {
                    size++;
                }
            }
        }
        return size;
    }

    /**
     * 从byte[] src 里面截取 start 开头， 长度为len的数组 </br>
     * 方法同 subBytes，建议使用subBytes，该方法后续会删掉
     * @param start 截取的起始位置
     * @param end 结束位置
     * @param src 需要截取的数据
     * @return
     */
    @Deprecated
    public static byte[] cutBytes(int start, int end, byte[] src) {
        return subBytes(src, start, end);
    }

    /**
     * 检测buf是否是utf8编码
     *
     * @param buf the data buffer.
     * @return true is UTF8, false is not UTF8
     */
    static boolean checkUTF8(byte[] buf) {
        int i = 0;
        int bytes = 0;
        int chr;
        int bAllAscii = 1;
        int len = buf.length;

        if (len <= 0) {
            return false;
        }

        for (i = 0; i < len; i++) {
            chr = buf[i] & 0xff;
            if ((chr & 0x0080) != 0) {
                bAllAscii = -1;
            }
            if (bytes == 0) {
                if (chr >= 0x0080) {
                    if (chr >= 0x00fc && chr <= 0x00fd) {
                        bytes = 6;
                    }else if (chr >= 0x00f8) {
                        bytes = 5;
                    }else if (chr >= 0x00f0) {
                        bytes = 4;
                    }else if (chr >= 0x00e0) {
                        bytes = 3;
                    }else if (chr >= 0x00c0) {
                        bytes = 2;
                    }else {
                        return false;
                    }
                    bytes --;
                }
            } else {
                if ((chr&0xc0) != 0x0080) {
                    return false;
                }
                bytes --;
            }
        }

        if (bytes > 0) {
            return false;
        }

        if (bAllAscii > 0) {
            return false;
        }

        return true;
    }

    /**
     * byte array to string.
     *
     * @param buf the byte buffer
     * @return the byte buffer string.
     * @throws UnsupportedEncodingException unsupport encoding exception.
     */
    public static String byte2Str(byte[] buf) throws UnsupportedEncodingException {
        String str=null;

        if (buf[0] == -1 && buf[1] == -2) {
            str = new String(buf, 2, buf.length-2, "UTF-16LE");
        } else if (buf[0] == -2 && buf[1] == -1) {
            str = new String(buf, 2, buf.length-2, "UTF-16BE");
        } else {
            if (checkUTF8(buf)) {
                str = new String(buf, 0, buf.length, "UTF-8");
            }else {
                str = new String(buf, 0, buf.length, "GBK");
            }
        }

        return str;
    }

    /**
     * 判断两个byte数组相等
     * @param srcs 原始数据
     * @param dests 比较数据
     * @return 相等返回true
     */
    public static boolean equals(byte[] srcs, byte[] dests) {
        if (srcs == dests) {
            return true;
        }
        boolean ret = false;
        if (srcs != null && dests != null) {
            if (srcs.length == dests.length) {
                boolean isEqual = true;
                for (int i = 0; i < srcs.length; ++i) {
                    if (srcs[i] != dests[i]) {
                        isEqual = false;
                        break;
                    }
                }
                ret = isEqual;
            }
        }
        return ret;
    }
}
