package com.roadrover.sdk.car;

import java.util.HashMap;
import java.util.Map;

/**
 * 空调信息组
 */
public class ClimateGroup {
    protected Map<Integer, Climate> mItems = new HashMap<>();
    public void set(int id, int rawValue) {
        Climate item = mItems.get(id);
        if (item != null) {
            item.mRawValue = rawValue;
        } else {
            mItems.put(id, new Climate(id, rawValue));
        }
    }

    public Climate get(int id) {
        return mItems.get(id);
    }

    public boolean contains(int id) {
        return mItems.containsKey(id);
    }
}
