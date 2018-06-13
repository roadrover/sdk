package com.roadrover.sdk.devices.carrecord;

import android.content.Context;

import com.roadrover.sdk.car.CarManager;
import com.roadrover.sdk.car.IVICar;

/**
 * 功能：行车记录仪管理类
 * 
 */
public class CarRecordManager implements IVICarRecord.CarRecordListener{

    private CarManager mCarManager;
    private int mCarId;

    /**
     * 构造函数
     * @param context  上下文
     */
    public CarRecordManager(Context context, int carId) {
        mCarManager = new CarManager(context, null, null, false);
        this.mCarId = carId;
    }

    @Override
    public void onClickMenu() {
        setExtraDeviceData(IVICarRecord.UserKeyOperation.MENU);
    }

    @Override
    public void onClickModel() {
        setExtraDeviceData(IVICarRecord.UserKeyOperation.MODEL);
    }

    @Override
    public void onClickUp() {
        setExtraDeviceData(IVICarRecord.UserKeyOperation.UP);
    }

    @Override
    public void onClickDown() {
        setExtraDeviceData(IVICarRecord.UserKeyOperation.DOWN);
    }

    @Override
    public void onClickEnsure() {
        setExtraDeviceData(IVICarRecord.UserKeyOperation.ENSURE);
    }

    /**
     * 向MCU发送触摸坐标值
     * @param x
     * @param y
     */
    @Override
    public void onCLickCoordinate(int x, int y) {
        if (mCarManager != null) {
            mCarManager.setTouch(x, y, IVICar.TouchClickEvent.DOWN);
        }
    }

    @Override
    public void onDestroy() {
        mCarManager.disconnect();
    }

    /**
     * 向MCU发送按键状态码
     *
     * @param extraDeviceData 行车记录仪功能码组
     */
    public void setExtraDeviceData(byte extraDeviceData) {
        if (mCarManager != null) {
            byte[] bytes = new byte[]{IVICarRecord.OperationType.KeyOperation, extraDeviceData};
            mCarManager.setExtraDevice(mCarId, IVICar.ExtraDevice.DeviceId.DRIVING_RECORDER, bytes);
        }
    }
}
