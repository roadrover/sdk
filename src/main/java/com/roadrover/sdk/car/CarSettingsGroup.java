package com.roadrover.sdk.car;

import java.util.HashMap;
import java.util.Map;

/**
 * 原车设置类
 */
public abstract class CarSettingsGroup {
    public int mCarId;
    public static class Item {
        public int mValue;
        public ByteBitsDesc mBits;

        public void load(byte[] buff) {
            mValue = mBits.get(buff);
        }

        public Item(String desc) {
            mBits = new ByteBitsDesc(desc);
        }
    }

    public Map<Integer, Item> mItems = new HashMap<>();
    public CarSettingsGroup() {
    }

    public void insertItem(int id, String desc) {
        Item item = mItems.get(id);
        if (item != null) {
            item.mBits.setDesc(desc);
        } else {
            mItems.put(id, new Item(desc));
        }
    }

    public void loadFromBytes(byte[] buff) {
        if (buff == null) {
            return;
        }

        mCarId = buff[0];
        for (Map.Entry<Integer, Item> entry : mItems.entrySet()) {
            entry.getValue().load(buff);
        }
    }

    public int get(int id) {
        return mItems.get(id).mValue;
    }

    public boolean set(int id, int value) {
        Item item = mItems.get(id);
        if (item == null)
            return false;

        item.mValue = value;
        return true;
    }
}
