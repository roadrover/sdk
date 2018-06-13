package com.roadrover.sdk.car;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ClimateView {
    public static final int DRAWABLE_ID_HIDE = -1;
    public static final int DRAWABLE_ID_SHOW = -2;

    /**
     * mValue空调对应的值
     * mDrawableId该值对应的图片在R中的ID
     */
    public static class Value2Image{
        public int mValue;
        public int mDrawableId;
        public Value2Image(int value, int drawableId) {
            mValue = value;
            mDrawableId = drawableId;
        }
    }

    public int mClimateId;
    public View mView;
    public List<ClimateView.Value2Image> mValue2Images = new ArrayList<>();

    public ClimateView addValue(int value, int drawableId) {
        mValue2Images.add(new Value2Image(value, drawableId));
        return this;
    }

    public ClimateView addDefaultValue() {
        mValue2Images.add(new Value2Image(0, DRAWABLE_ID_HIDE));
        mValue2Images.add(new Value2Image(1, DRAWABLE_ID_SHOW));
        return this;
    }

    public ClimateView(int climateId, View view) {
        mClimateId = climateId;
        mView = view;
    }
}