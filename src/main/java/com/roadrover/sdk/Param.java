package com.roadrover.sdk;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class Param {
    public int mId;
    public int mValue;
    public int mMin;
    public int mMax;
    public int mDefault;

    public Param() {
        mId = 0;
    }

    /**
     * 普通参数
     */
    public Param(int id, int min, int max, int def, int value) {
        mId = id;
        mMin = min;
        mMax = max;
        mDefault = def;
        mValue = value;
    }

    /**
     * 普通参数
     */
    public Param(int id, int min, int max, int defaultValue) {
        mId = id;
        mMin = min;
        mMax = max;
        mDefault = defaultValue;
        mValue = defaultValue;
    }

    /**
     * 布尔型参数
     */
    public Param(int id, boolean defaultValue) {
        mId = id;
        mMin = 0;
        mMax = 1;
        mDefault = defaultValue ? 1 : 0;
        mValue = mDefault;
    }

    /**
     * 只读的参数，是一个属性值
     */
    public Param(int id, int value) {
        mId = id;
        mMin = value;
        mMax = value;
        mDefault = value;
        mValue = value;
    }

    public boolean set(int value) {
        int nextValue;
        if (value < mMin) {
            nextValue = mMin;
        } else if(value > mMax) {
            nextValue = mMax;
        } else {
            nextValue = value;
        }

        if (mValue != nextValue) {
            mValue = nextValue;
            return true;
        } else {
            return false;
        }
    }

    public boolean isReadOnly() {
        return (mMax == mMin);
    }
    public boolean isOn() {
        return mValue == 1;
    }

    public String getName() {
        return "";
    }

    public String getValueText() {
        return String.valueOf(mValue);
    }

    public static void updateSeekBarAndTextView(Param param, SeekBar seekBar,
                                                TextView textView, String unit) {
        if (seekBar != null) {
            if (param == null) {
                seekBar.setVisibility(View.INVISIBLE);
            } else {
                seekBar.setVisibility(View.VISIBLE);
                seekBar.setMax(param.mMax - param.mMin);
                seekBar.setProgress(param.mValue - param.mMin);
            }
        }

        if (textView != null) {
            if (param == null) {
                textView.setVisibility(View.INVISIBLE);
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(String.valueOf(param.mValue) + unit);
            }
        }
    }
}
