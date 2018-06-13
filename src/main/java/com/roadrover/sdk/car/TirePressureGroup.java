package com.roadrover.sdk.car;

import java.util.HashMap;
import java.util.Map;

/**
 * 胎压信息组
 */

public class TirePressureGroup {

    private Map<Integer, TirePressure> mItems = new HashMap<>();

    /**
     * @param id          ID 定义 见{@link com.roadrover.sdk.car.TirePressure}
     * @param rawValue    数据 见{@link com.roadrover.sdk.car.TirePressure}
     * @param extraValue  附加状态数据 见{@link com.roadrover.sdk.car.TirePressure}
     * @param dotType     温度小数点标志 见{@link com.roadrover.sdk.car.TirePressure}
     */
    public void set(int id, int rawValue, int extraValue, int dotType) {
        TirePressure item = mItems.get(id);
        if (item != null) {
            item.rawValue = rawValue;
            item.extraValue = extraValue;
            item.dotType = dotType;
        } else {
            mItems.put(id, new TirePressure(id, rawValue, extraValue, dotType));
        }
    }

    public TirePressure get(int id) {
        return mItems.get(id);
    }

    public boolean contains(int id) {
        return mItems.containsKey(id);
    }

    public Map<Integer, TirePressure> getTtems() {
        return mItems;
    }

    public int size() {
        return mItems.size();
    }
}
