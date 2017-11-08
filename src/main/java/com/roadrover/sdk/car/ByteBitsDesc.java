package com.roadrover.sdk.car;

import android.util.Log;

/**
 * 处理一些字节相关的流程，字符串描述格式为以下几种
 * BYTE[X]:BIT[Y-Z]，第X个字节，BIT位从Y到Z的值，Y可以比Z大也可以比Z小
 * BYTE[X]，第X个字节整个值
 * BYTE[X]:BIT[Y], 第X个字节，BIT Y单个Bit
 * 大小写不敏感，BYTE和BIT中间的‘:’可以不要或者换别的字符，
 * Y和Z中间的-可以换成别的字符，但不能没有分隔符
 */
public class ByteBitsDesc {
    public static final String TAG = "ByteBitsDesc";
    public static final String BYTE_NAME = "BYTE";
    public static final String BIT_NAME = "BIT";

    private String mDesc;
    private boolean mFormatIsValid = false;
    private int mByteIndex = -1;
    private int mBitMask = 0;
    private int mBitHighIndex;
    private int mBitLowIndex;

    public ByteBitsDesc() {
    }

    public ByteBitsDesc(String desc) {
        setDesc(desc);
    }

    public void setDesc(String desc) {
        mDesc = desc;
        if (!getByteIndex()) {
            mFormatIsValid = false;
            Log.e(TAG, "error BYTE format " + desc);
        } else {
            if (!getBitMask()) {
                mFormatIsValid = false;
                Log.e(TAG, "error BIT format " + desc);
            } else {
                mFormatIsValid = true;
            }
        }
    }

    public int get(byte[] buff) {
        if (!mFormatIsValid || mByteIndex >= buff.length) {
            return 0;
        }

        return (buff[mByteIndex] & mBitMask) >> mBitLowIndex;
    }

    public void set(byte[] buff, int value) {
        if (!mFormatIsValid || mByteIndex >= buff.length) {
            return;
        }

        if (value > (mBitMask >> mBitLowIndex)) {
            Log.e(TAG, "Warning value is overflow at " + mDesc + " value is " + value);
        }

        buff[mByteIndex] &= ~mBitMask;
        buff[mByteIndex] |= (value << mBitLowIndex);
    }

    private boolean getByteIndex() {
        int start = mDesc.toUpperCase().indexOf(BYTE_NAME);
        if (start < 0) {
            return false;
        }

        start = mDesc.indexOf('[', start);
        if (start < 0) {
            return false;
        }

        int end = mDesc.indexOf(']', start);
        if (end < 0) {
            return false;
        }

        mByteIndex = Integer.parseInt(mDesc.substring(start+1, end), 10);
        return true;
    }

    private boolean getBitMask() {
        int start = mDesc.toUpperCase().indexOf(BIT_NAME);
        if (start < 0) {
            Log.e(TAG, "Can not find " + BIT_NAME + " from " + mDesc);
            return false;
        }

        start = mDesc.indexOf('[', start);
        if (start < 0) {
            Log.e(TAG, "Can not find [ after BIT from " + mDesc);
            return false;
        }

        int end = mDesc.indexOf(']', start);
        if (end < 0) {
            Log.e(TAG, "Can not find ] after BIT from " + mDesc);
            return false;
        }

        String bitMaskDesc = mDesc.substring(start+1, end);
        if (bitMaskDesc.length() != 1 && bitMaskDesc.length() != 3) {
            return false;
        }

        if (bitMaskDesc.length() == 1) {
            mBitLowIndex = Integer.parseInt(bitMaskDesc, 10);
            mBitMask = (1 << mBitLowIndex);
        } else if (bitMaskDesc.length() == 3) {
            int bitsIndex0 = Integer.parseInt(String.valueOf(bitMaskDesc.charAt(0)), 10);
            int bitsIndex1 = Integer.parseInt(String.valueOf(bitMaskDesc.charAt(2)), 10);
            if (bitsIndex0 >= bitsIndex1) {
                mBitLowIndex = bitsIndex1;
                mBitHighIndex = bitsIndex0;
            } else {
                mBitLowIndex = bitsIndex0;
                mBitHighIndex = bitsIndex1;
            }

            mBitMask = 0;
            for (int i = mBitLowIndex; i <= mBitHighIndex; i++) {
                mBitMask |= (1 << i);
            }
        }

        return true;
    }

    /**
     * 测试流程
     */
    public static void test() {
        int TEST_COUNT = 10;
        byte[] buff = new byte[TEST_COUNT];

        /**
         * 测试例子 "Byte[x]
         */
        for (int i=0; i<TEST_COUNT; i++) {
            ByteBitsDesc desc = new ByteBitsDesc("Byte["+i+"]");
            desc.set(buff, i);
            if (buff[i] != i) {
                Log.e(TAG, "TEST set error at " + desc.mDesc);
            }
        }

        for (int i=0; i<TEST_COUNT; i++) {
            ByteBitsDesc desc = new ByteBitsDesc("Byte["+i+"]");
            if (desc.get(buff) != i) {
                Log.e(TAG, "TEST get error at " + desc.mDesc);
            }
        }

        /**
         * 测试例子 "Byte[x]:Bit[4:7]
         */
        for (int i=0; i<TEST_COUNT; i++) {
            ByteBitsDesc desc = new ByteBitsDesc("Byte["+i+"]:Bit[4:7]");
            desc.set(buff, i);
            if ((buff[i] & 0xF0)!= i) {
                Log.e(TAG, "TEST set error at " + desc.mDesc);
            }
        }

        for (int i=0; i<TEST_COUNT; i++) {
            ByteBitsDesc desc = new ByteBitsDesc("Byte["+i+"]:Bit[4:7]");
            int value = desc.get(buff);
            if (value != i) {
                Log.e(TAG, "TEST set error at " + desc.mDesc);
            }
        }

        /**
         * 测试例子 "Byte[x]:Bit[0]
         */
        for (int i=0; i<TEST_COUNT; i++) {
            ByteBitsDesc desc = new ByteBitsDesc("Byte["+i+"]:Bit[0]");
            desc.set(buff, i % 2);
            int value = (buff[i] & 0x1);
            if (value != (i % 2)) {
                Log.e(TAG, "TEST set error at " + desc.mDesc + ", got " + value);
            }
        }

        for (int i=0; i<TEST_COUNT; i++) {
            ByteBitsDesc desc = new ByteBitsDesc("BYTE["+i+"] BIT[0]");
            int value = desc.get(buff);
            if (value != (i % 2)) {
                Log.e(TAG, "TEST set error at " + desc.mDesc + ", got " + value);
            }
        }
    }
}
