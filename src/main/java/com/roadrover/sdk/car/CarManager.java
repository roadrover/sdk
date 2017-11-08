package com.roadrover.sdk.car;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.roadrover.sdk.BaseManager;
import com.roadrover.services.car.ICar;
import com.roadrover.services.car.ICarCallback;
import com.roadrover.services.car.IMcuUpgradeCallback;
import com.roadrover.sdk.utils.Logcat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;
import java.util.Set;

/**
 * Car管理类，提供车辆接口
 */
public class CarManager extends BaseManager {
    private ICar mCarInterface;
    private CarListener mCarListener;
    private Set<Integer> mFilters = new HashSet<Integer>();
    private int mCarId;
    private int mDoorStatusMask, mDoorOldStatusMask;
    private int mLightStatusMask;
    private ClimateGroup mClimates = new ClimateGroup();
    private SparseArray<Float> mRealTimeInfoGroup = new SparseArray<>();
    private IVICar.Radar mRadar = null;
    public interface CarListener {
        void onMcuVersion(String version);

        // Normal car info
        void onAccChanged(boolean on);

        void onCcdChanged(int status);

        void onHandbrakeChanged(boolean hold);

        // 车门信息
        void onDoorChanged(int changeMask, int statusMask);

        // 车灯信息
        void onLightChanged(int changeMask, int statusMask);

        // 大灯状态
        void onHeadLightChanged(boolean on);

        // 空调信息
        void onClimateChanged(int id, int rawValue);

        // 车外温度信息
        void onOutsideTempChanged(float tempC);

        // 按键消息
        void onKeyPushed(int id, int type);

        // 车身警告信息、故障码
        void onAlertMessage(int messageCode);

        // 实时车辆消息
        void onRealTimeInfoChanged(int id, float value);

        // 雷达信息
        void onRadarChanged(IVICar.Radar radar);

        // 里程消息
        void onTripChanged(int id, int index, float value);

        // 其他车辆信息
        void onExtraStateChanged(int id, float value);

        // 车辆设置信息
        void onCarSettingChanged(int carId, byte[] data);

        // 外部设备数据
        void onExtraDeviceChanged(int carId, int deviceId, byte[] extraDeviceData);

        // 命令参数
        void onCmdParamChanged(int id, byte[] paramData);

        // 保养信息
        void onMaintenanceChanged(int id, int mileage, int days);

        // 汽车VIN
        void onCarVINChanged(String VIN, int keyNumber);

        // 故障报告
        void onCarReportChanged(int carid, int type, int[] list);

        // 自动泊车
        void onAutoParkChanged(IVICar.AutoPark status);
		
		// 能量流动
        void onEnergyFlowChanged(int battery, int engineToTyre, int engineToMotor, int motorToTyre, int motorToBattery);

        // 快速倒车
        void onFastReverseChanged(boolean on);
    }

    public CarManager(Context context, ConnectListener connectListener, CarListener carListener) {
        super(context, connectListener);
        mCarListener = carListener;
        EventBus.getDefault().register(this);
    }

    @Override
    public void disconnect() {
        EventBus.getDefault().unregister(this);
        super.disconnect();
    }

    @Override
    protected String getServiceActionName() {
        return ServiceAction.CAR_ACTION;
    }

    @Override
    protected void onServiceConnected(IBinder service) {
        Logcat.d();
        mCarInterface = ICar.Stub.asInterface(service);
        registerCallback(mCarCallback);
        for (Integer id : mFilters) {
            try {
                mCarInterface.registerRealTimeInfo(id, mCarCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        String protocolMcuVersion = null;
        try {
            mCarId = mCarInterface.getCarId();
            protocolMcuVersion = mCarInterface.getProtocolMcuVersion();
            mDoorStatusMask = mCarInterface.getDoorStatusMask();
            mDoorOldStatusMask = mCarInterface.getDoorStatusMask();
            mLightStatusMask = mCarInterface.getLightStatusMask();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // 主动发送一次版本号给需要者
        if (!TextUtils.isEmpty(protocolMcuVersion)) {
            EventBus.getDefault().post(new IVICar.EventMcuVersion(protocolMcuVersion));
        }

        // 主动发送一次车灯消息，360全景需要用，左右转向灯会触发360全景
        EventBus.getDefault().post(new IVICar.Light(0, mLightStatusMask));

        // 主动发送一次手刹消息
        requestHandbrakeEvent();
    }

    /**
     * 注册高频实时车辆信息ID，只有注册后才能获取该ID对应的实时回调
     * 该函数不需要等待服务连接上才能调用，回将请求缓存，服务连接上后自动同步
     *
     * @param id IVICar.RealTimeInfoId
     */
    public void registerRealTimeInfoId(int id) {
        mFilters.add(id);
        if (mCarInterface != null) {
            try {
                mCarInterface.registerRealTimeInfo(id, mCarCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注销高频实时车辆信息ID，不再接收该ID对应的回调
     *
     * @param id IVICar.RealTimeInfoId
     */
    public void unRegisterRealTimeInfoId(int id) {
        mFilters.remove(id);
        if (mCarInterface != null) {
            try {
                mCarInterface.unRegisterRealTimeInfo(id, mCarCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMcuVersion(IVICar.EventMcuVersion version) {
        if (mCarListener != null) {
            mCarListener.onMcuVersion(version.mVersion);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccChanged(IVICar.Acc acc) {
        if (mCarListener != null) {
            mCarListener.onAccChanged(acc.mOn);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCcdChanged(IVICar.Ccd ccd) {
        if (mCarListener != null) {
            mCarListener.onCcdChanged(ccd.mStatus);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandbrakeChanged(IVICar.Handbrake handbrake) {
        if (mCarListener != null) {
            mCarListener.onHandbrakeChanged(handbrake.mStatus == IVICar.Handbrake.Status.HOLD);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDoorChanged(IVICar.Door door) {
        if (mCarListener != null) {
            mCarListener.onDoorChanged(door.mChangeMask, door.mStatusMask);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLightChanged(IVICar.Light light) {
        if (mCarListener != null) {
            mCarListener.onLightChanged(light.mChangeMask, light.mStatusMask);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHeadLightChanged(IVICar.HeadLight light) {
        if (mCarListener != null) {
            mCarListener.onHeadLightChanged(light.isOn());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClimateChanged(Climate climate) {
        if (mCarListener != null) {
            mCarListener.onClimateChanged(climate.mId, climate.mRawValue);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOutsideTempChanged(IVICar.OutsideTemp outsideTemp) {
        if (mCarListener != null) {
            mCarListener.onOutsideTempChanged(outsideTemp.getTemp(IVICar.TemperatureUnit.C));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onKeyPushed(IVICar.Key key) {
        if (mCarListener != null) {
            mCarListener.onKeyPushed(key.mId, key.mType);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlertMessage(IVICar.AlertMessage message) {
        if (mCarListener != null) {
            mCarListener.onAlertMessage(message.mMessageCode);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRealTimeInfoChanged(IVICar.RealTimeInfo info) {
        if (mCarListener != null) {
            mCarListener.onRealTimeInfoChanged(info.mId, info.mValue);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRadarChanged(IVICar.Radar radar) {
        mRadar = radar;        if (mCarListener != null) {
            mCarListener.onRadarChanged(radar);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTripChanged(Trip trip) {
        if (mCarListener != null) {
            mCarListener.onTripChanged(trip.mId, trip.mIndex, trip.mValue);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExtraStateChanged(IVICar.ExtraState state) {
        if (mCarListener != null) {
            mCarListener.onExtraStateChanged(state.mId, state.mValue);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCarSettingChanged(IVICar.Setting setting) {
        if (mCarListener != null) {
            mCarListener.onCarSettingChanged(setting.mCarId, setting.mData);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExtraDeviceChanged(IVICar.ExtraDevice device) {
        if (mCarListener != null) {
            mCarListener.onExtraDeviceChanged(device.mCarId, device.mDeviceId, device.mData);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCmdParamChanged(IVICar.CmdParam param) {
        if (mCarListener != null) {
            mCarListener.onCmdParamChanged(param.mId, param.mData);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMaintenanceChanged(IVICar.Maintenance maintenance) {
        if (mCarListener != null) {
            mCarListener.onMaintenanceChanged(maintenance.mId, maintenance.mMileage, maintenance.mDays);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCarVINChanged(IVICar.CarVIN carVIN) {
        if (mCarListener != null) {
            mCarListener.onCarVINChanged(carVIN.mVIN, carVIN.mKeyNumber);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAutoParkChanged(IVICar.AutoPark status) {
        if (mCarListener != null) {
            mCarListener.onAutoParkChanged(status);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFastReverseChanged(IVICar.FastReverse reverse) {
        if (mCarListener != null) {
            mCarListener.onFastReverseChanged(reverse.isOn());
        }
    }

    @Override
    protected void onServiceDisconnected() {
        mCarInterface = null;
    }

    @Override
    protected void registerCallback(IInterface callback) {
        if (null != mCarInterface) {
            try {
                mCarInterface.registerCallback((ICarCallback) callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        super.registerCallback(callback);
    }

    @Override
    protected void unRegisterCallback(IInterface callback) {
        if (null != mCarInterface) {
            try {
                mCarInterface.unRegisterCallback((ICarCallback) callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        super.unRegisterCallback(callback);
    }

    private ICarCallback mCarCallback = new ICarCallback.Stub() {
        @Override
        public void onMcuVersion(String version) {
            EventBus.getDefault().post(new IVICar.EventMcuVersion(version));
        }

        @Override
        public void onAccChanged(boolean on) {
            EventBus.getDefault().postSticky(new IVICar.Acc(on));
        }

        @Override
        public void onCcdChanged(int status) {
            EventBus.getDefault().postSticky(new IVICar.Ccd(status));
        }

        @Override
        public void onHandbrakeChanged(boolean hold) {
            EventBus.getDefault().post(new IVICar.Handbrake(hold));
        }

        @Override
        public void onDoorChanged(int changeMask, int statusMask) {
            mDoorOldStatusMask = mDoorStatusMask;
            mDoorStatusMask = statusMask;
            EventBus.getDefault().post(new IVICar.Door(changeMask, statusMask));
        }

        @Override
        public void onLightChanged(int changeMask, int statusMask) {
            mLightStatusMask = statusMask;
            EventBus.getDefault().post(new IVICar.Light(changeMask, statusMask));
        }

        @Override
        public void onHeadLightChanged(boolean on) {
            EventBus.getDefault().post(new IVICar.HeadLight(on));
        }

        @Override
        public void onClimateChanged(int id, int rawValue) {
            mClimates.set(id, rawValue);
            if (id == Climate.Id.INSIDE_TEMP) {
                EventBus.getDefault().post(new IVICar.InsideTemp(rawValue));
            } else {
                EventBus.getDefault().post(new Climate(id, rawValue));
            }
        }

        @Override
        public void onOutsideTempChanged(int rawValue) {
            EventBus.getDefault().post(new IVICar.OutsideTemp(rawValue));
        }

        @Override
        public void onKeyPushed(int id, int type) {
            EventBus.getDefault().post(new IVICar.Key(id, type));
        }

        @Override
        public void onAlertMessage(int messageCode) {
            EventBus.getDefault().post(new IVICar.AlertMessage(messageCode));
        }

        @Override
        public void onRealTimeInfoChanged(int id, float value) {
            EventBus.getDefault().post(new IVICar.RealTimeInfo(id, value));
        }

        @Override
        public void onTripChanged(int id, int index, float value) {
            EventBus.getDefault().post(new Trip(id, index, value));
        }

        @Override
        public void onExtraStateChanged(int id, float value) {
            EventBus.getDefault().post(new IVICar.ExtraState(id, value));
        }

        @Override
        public void onRadarChanged(int radarType, byte[] radarData) {
            EventBus.getDefault().post(new IVICar.Radar(radarType, radarData));
        }

        @Override
        public void onCarSettingChanged(int carId, byte[] data) {
            EventBus.getDefault().post(new IVICar.Setting(carId, data));
        }

        @Override
        public void onExtraDeviceChanged(int carId, int deviceId, byte[] extraDeviceData) {
            EventBus.getDefault().post(new IVICar.ExtraDevice(carId, deviceId, extraDeviceData));
        }

        @Override
        public void onCmdParamChanged(int id, byte[] paramData) {
            EventBus.getDefault().post(new IVICar.CmdParam(id, paramData));
        }

        @Override
        public void onMaintenanceChanged(int id, int mileage, int days) {
            EventBus.getDefault().post(new IVICar.Maintenance(id, mileage, days));
        }

        @Override
        public void onCarVINChanged(String VIN, int keyNumber) {
            EventBus.getDefault().post(new IVICar.CarVIN(VIN, keyNumber));
        }

        @Override
        public void onCarReportChanged(int carid, int type, int[] list) {
            EventBus.getDefault().post(new IVICar.CarReport(carid, type, list));
        }

        @Override
        public void onAutoParkChanged(int status) {
            EventBus.getDefault().post(new IVICar.AutoPark(status));
        }
		
		@Override
        public void onEnergyFlowChanged(int battery, int engineToTyre, int engineToMotor, int motorToTyre, int motorToBattery){
            EventBus.getDefault().post(new IVICar.EnergyFlow(battery, engineToTyre, engineToMotor, motorToTyre, motorToBattery));
        }

        @Override
        public void onFastReverseChanged(boolean on) {
            EventBus.getDefault().post(new IVICar.FastReverse(on));
        }

        @Override
        public void onADKeyChanged(int channel, int value) throws RemoteException {
            EventBus.getDefault().post(new IVICar.StudyKeyItem(channel, value));
        }

        @Override
        public void onClusterMessage(byte[] datas) throws RemoteException {
            EventBus.getDefault().post(new IVICar.EventClusterMessage(datas));
        }
    };

    /**
     * 得到CCD状态
     * @return {@link com.roadrover.sdk.car.IVICar.Ccd.Status}
     */
    public int getCcdStatus() {
        if (null != mCarInterface) {
            try {
                return mCarInterface.getCcdStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return IVICar.Ccd.Status.OFF;
    }

    /**
     * 得到大灯状态
     */
    public boolean getHeadLightStatus() {
        if (null != mCarInterface) {
            try {
                return mCarInterface.getHeadLightStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        return false;
    }

    /**
     * 获取协议卡MCU的软件版本
     */
    public String getProtocolMcuVersion() {
        try {
            if (mCarInterface != null) {
                return mCarInterface.getProtocolMcuVersion();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取车型ID
     * @return
     */
    public int getCarId() {
        return mCarId;
    }

    /**
     * 获取车门状态
     * @param doorId {@link com.roadrover.sdk.car.IVICar.Door.Id}
     */
    public boolean isDoorOpen(int doorId) {
        return (mDoorStatusMask & (1 << doorId)) != 0;
    }

    /**
     * 是否所有车门已关闭
     */
    public boolean isAllDoorClosed() {
        return mDoorStatusMask == 0;
    }

    /**
     * 获取车门是否变化
     */
    public boolean isDoorChange() {
        return (mDoorStatusMask != mDoorOldStatusMask);
    }

    /**
     * 获取之前的车门状态
     * @param doorId {@link com.roadrover.sdk.car.IVICar.Door.Id}
     */
    public boolean isOldDoorOpen(int doorId) {
        return (mDoorOldStatusMask & (1 << doorId)) != 0;
    }

    /**
     * 是否之前所有车门已关闭
     */
    public boolean isOldAllDoorClosed() {
        return mDoorOldStatusMask == 0;
    }

    /**
     * 获取车灯状态
     * @param lightId {@link com.roadrover.sdk.car.IVICar.Light.Id}
     */
    public boolean isLightOn(int lightId) {
        return (mLightStatusMask & (1 << lightId)) != 0;
    }

    /**
     * 车灯是否在闪烁状态
     */
    public boolean isLightBlink() {
        return (isLightOn(IVICar.Light.Id.EMERGENCY) ||
                isLightOn(IVICar.Light.Id.TURN_LEFT) ||
                isLightOn(IVICar.Light.Id.TURN_RIGHT));
    }

    /**
     * 获取空调信息
     */
    public Climate getClimate(int id) {
        if (!updateClimateCache(id)) {
            return Climate.getUnknown();
        }

        return mClimates.get(id);
    }

    /**
     * 设置空调值
     *
     * @param id    Climate.Id
     * @param value 参数值
     */
    public void setClimate(int id, int value) {
        mClimates.set(id, value);
        if (mCarInterface != null) {
            try {
                mCarInterface.setClimate(id, value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置原车参数
     *
     * @param id    参数ID
     * @param value 参数值
     */
    public void setCarSetting(int id, int value) {
        if (mCarInterface != null) {
            try {
                mCarInterface.setCarSetting(id, value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 填充原车设置的原始值
     *
     * @param carSetting 原车设置结构
     */
    public void getCarSetting(CarSettingsGroup carSetting) {
        if (carSetting == null) {
            return;
        }

        if (mCarInterface != null) {
            try {
                byte[] bytes = mCarInterface.getCarSettingBytes();
                carSetting.loadFromBytes(bytes);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

	 /**
     * 获取原车设置Bytes
     */
    public byte[] getCarSettingBytes() {
        if (mCarInterface != null) {
            try {
                return mCarInterface.getCarSettingBytes();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
	
    /**
     * 得到里程数
     *
     * @param id    参见 Trip.Id
     * @param index 参见 Trip.Index
     */
    public float getTrip(int id, int index) {
        if (mCarInterface != null) {
            try {
                return mCarInterface.getTrip(id, index);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return IVICar.DISTANCE_UNKNOWN;
    }

    /**
     * 得到剩余油量的续航里程，单位km
     */
    public float getRemainFuelDistance() {
        if (mCarInterface != null) {
            try {
                return mCarInterface.getExtraState(IVICar.ExtraState.Id.REMAIN_FUEL_DISTANCE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return IVICar.DISTANCE_UNKNOWN;
    }

    public float getExtraState(int id) {
        if (mCarInterface != null) {
            try {
                return mCarInterface.getExtraState(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return -1.0f;
    }

    public int getHandBrakeStatus() {
        if (mCarInterface != null) {
            try {
                return mCarInterface.getHandbrakeStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.e("service not connected");
        }

        return IVICar.Handbrake.Status.UNKNOWN;
    }

    /**
     * 得到车外温度类
     *
     * @return IVICar.OutsideTemp
     */
    public IVICar.OutsideTemp getOutsideTemp() {
        return new IVICar.OutsideTemp(getOutsideTempRawValue());
    }

    public int getOutsideTempRawValue() {
        if (mCarInterface != null) {
            try {
                return mCarInterface.getOutsideTempRawValue();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return IVICar.OUTSIDE_TEMP_UNKNOWN;
    }

    /**
     * 得到车内温度类
     *
     * @return IVICar.InsideTemp
     */
    public IVICar.InsideTemp getInsideTemp() {
        return new IVICar.InsideTemp(getClimateRawValue(Climate.Id.INSIDE_TEMP));
    }

    /**
     * 设置外设参数
     */
    public void setExtraDevice(int carId, int deviceId, byte[] extraDeviceData) {
        if (mCarInterface != null) {
            try {
                mCarInterface.setExtraDevice(carId, deviceId, extraDeviceData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置氛围灯音频参数
     */
    public void setExtraAudioParameters(byte[] extraAudioData) {
        if (mCarInterface != null) {
            try {
                mCarInterface.setExtraAudioParameters(extraAudioData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求发送所有service端缓存的外设参数，通过CarListener或者EventBus获取
     */
    public void requestExtraDeviceEvent() {
        if (mCarInterface != null) {
            try {
                mCarInterface.requestExtraDeviceEvent();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置CMD_PARAM参数
     */
    public void setCmdParam(int id, byte[] paramData) {
        if (mCarInterface != null) {
            try {
                mCarInterface.setCmdParam(id, paramData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求发送所有service端缓存的CMD_PARAM参数，通过CarListener回调或者EventBus获取
     */
    public void requestCmdParamEvent() {
        if (mCarInterface != null) {
            try {
                mCarInterface.requestCmdParamEvent();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求发送手刹消息，通过CarListener回调或者EventBus获取，
     * 在Service连接后，会自动调用该函数
     */
    public void requestHandbrakeEvent() {
        if (mCarInterface != null) {
            try {
                int status = mCarInterface.getHandbrakeStatus();
                EventBus.getDefault().post(new IVICar.Handbrake(status));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送触摸消息给单片机
     * X: 0 -> Left, 255 -> Right
     * Y: 0 -> Top, 255 -> Bottom
     */
    public void sendTouchClick(int x, int y) {
        if (mCarInterface != null) {
            try {
                mCarInterface.sendTouchClick(x, y);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取保养里程（保养周期或保养检查）
     *
     * @param id
     * @return 获取保养里程，单位 KM
     */
    public int getMaintenanceMileage(int id) {
        int ret = 0;
        if (mCarInterface != null) {
            try {
                ret = mCarInterface.getMaintenanceMileage(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 获取保养天数（保养周期或保养检查）
     *
     * @param id
     * @return 保养天数，单位 天
     */
    public int getMaintenanceDays(int id) {
        int ret = 0;
        if (mCarInterface != null) {
            try {
                ret = mCarInterface.getMaintenanceDays(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 获取车辆识别码VIN
     *
     * @return 返回车辆识别码
     */
    public String getCarVIN() {
        String ret = "";
        if (mCarInterface != null) {
            try {
                ret = mCarInterface.getCarVIN();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 获取匹配钥匙数目
     *
     * @return 返回钥匙数
     */
    public int getPairKeyNumber() {
        int ret = 0;
        if (mCarInterface != null) {
            try {
                ret = mCarInterface.getPairKeyNumber();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 获取故障报告数组
     *
     * @param carid
     * @param reportType
     * @return
     */
    public int[] getReportArray(int carid, int reportType) {
        int[] ret = null;
        if (mCarInterface != null) {
            try {
                ret = mCarInterface.getReportArray(carid, reportType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 获取自动泊车状态
     *
     * @return
     */
    public IVICar.AutoPark getAutoPark() {
        IVICar.AutoPark ret = null;
        if (mCarInterface != null) {
            try {
                ret = new IVICar.AutoPark(mCarInterface.getAutoPark());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 获取能量流动数据
     *
     * @return
     */
    public byte[] getEnergyFlowData(){
        if (mCarInterface != null) {
            try {
                return mCarInterface.getEnergyFlowData();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 设置原车LedKey键值
     * @param key
     * @param pushType
     */
    public void setCarLedKey(int key, int pushType){
        if (mCarInterface != null) {
            try {
                mCarInterface.setCarLedKey(key, pushType);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据配置，更新空调的控件
     *
     * @param item 配置：空调ID和图片ID（支持特殊的图片ID来完成显示和隐藏）
     */
    public void updateClimateView(ClimateView item) {
        View view = item.mView;
        if (view == null) {
            return;
        }

        Climate climate = getClimate(item.mClimateId);
        if (climate.isValid()) {
            boolean valueHit = false;
            for (ClimateView.Value2Image value : item.mValue2Images) {
                if (climate.mRawValue == value.mValue) {
                    if (value.mDrawableId == ClimateView.DRAWABLE_ID_HIDE) {
                        view.setVisibility(View.INVISIBLE);
                    } else if (value.mDrawableId == ClimateView.DRAWABLE_ID_SHOW) {
                        view.setVisibility(View.VISIBLE);
                    } else {
                        ImageView imageView = (ImageView) view;
                        imageView.setImageResource(value.mDrawableId);
                        imageView.setVisibility(View.VISIBLE);
                    }
                    valueHit = true;
                }
            }

            if (!valueHit) {
                Logcat.e("climate id " + item.mClimateId +
                        " value " + climate.mRawValue + " unexpected");
                view.setVisibility(View.INVISIBLE);
            }
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public void updateClimateView(int climateId, View view) {
        ClimateView climateView = new ClimateView(climateId, view).addDefaultValue();
        updateClimateView(climateView);
    }

    public void updateClimateTempView(int climateId, TextView textView) {
        if (textView == null)
            return;

        Climate temp = getClimate(climateId);
        if (temp.isValid()) {
            textView.setText(temp.getTemp(IVICar.TemperatureUnit.C));
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * mcu升级的 event类
     */
    public static class EventUpgradeMcu {
        public static final int SUCCESS_TYPE    = 0; // 升级成功
        public static final int FAILURE_TYPE    = 1; // 升级失败
        public static final int PROGRESS_TYPE   = 2; // 进度
        public static final int WAIT_MCU_REBOOT = 3; // 等待mcu重启
        public int status;
        public int errorCode;
        public int progress;
        public String mcuVersion; // mcu版本

        public EventUpgradeMcu(int status, int errorCode, int progress, String mcuVersion) {
            this.status     = status;
            this.errorCode  = errorCode;
            this.progress   = progress;
            this.mcuVersion = mcuVersion;
        }

        public static EventUpgradeMcu progress(int progress) {
            return new EventUpgradeMcu(EventUpgradeMcu.PROGRESS_TYPE, 0, progress, "");
        }

        public static EventUpgradeMcu failure(int errorCode) {
            return new EventUpgradeMcu(EventUpgradeMcu.FAILURE_TYPE, errorCode, 0, "");
        }

        public static EventUpgradeMcu success(String mcuVersion) {
            return new EventUpgradeMcu(EventUpgradeMcu.SUCCESS_TYPE, 0, 0, mcuVersion);
        }

        public static EventUpgradeMcu waitMcuReboot() {
            return new EventUpgradeMcu(EventUpgradeMcu.WAIT_MCU_REBOOT, 0, 0, "");
        }
    }

    private IMcuUpgradeCallback mIMcuUpgradeCallback = null;

    /**
     * mcu 升级
     *
     * @param filePath
     * @param callback
     */
    public void upgradeMcu(String filePath, IMcuUpgradeCallback callback) {
        mIMcuUpgradeCallback = callback;
        if (mCarInterface != null) {
            try {
                mCarInterface.upgradeMcu(filePath, new IMcuUpgradeCallback.Stub() {

                    @Override
                    public void onSuccess(String mcuVersion) throws RemoteException {
                        EventBus.getDefault().post(EventUpgradeMcu.success(mcuVersion));
                    }

                    @Override
                    public void onFailure(int errorCode) throws RemoteException {
                        EventBus.getDefault().post(EventUpgradeMcu.failure(errorCode));
                    }

                    @Override
                    public void onProgress(int progress) throws RemoteException {
                        EventBus.getDefault().post(EventUpgradeMcu.progress(progress));
                    }

                    @Override
                    public void onWaitMcuReboot() throws RemoteException {
                        EventBus.getDefault().post(EventUpgradeMcu.waitMcuReboot());
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 升级mcu消息
     * 来源：upgradeMcu (onSuccess or onFailure or onProgress)
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpgradeMcu(EventUpgradeMcu event) {
        if (event != null) {
            try {
                switch (event.status) {
                    case EventUpgradeMcu.SUCCESS_TYPE:
                        if (mIMcuUpgradeCallback != null) {
                            mIMcuUpgradeCallback.onSuccess(event.mcuVersion);
                        }
                        break;
                    case EventUpgradeMcu.FAILURE_TYPE:
                        if (mIMcuUpgradeCallback != null) {
                            mIMcuUpgradeCallback.onFailure(event.errorCode);
                        }
                        break;
                    case EventUpgradeMcu.PROGRESS_TYPE:
                        if (mIMcuUpgradeCallback != null) {
                            mIMcuUpgradeCallback.onProgress(event.progress);
                        }
                        break;
                    case EventUpgradeMcu.WAIT_MCU_REBOOT:
                        if (mIMcuUpgradeCallback != null) {
                            mIMcuUpgradeCallback.onWaitMcuReboot();
                        }
                        break;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置倒车摄像头的电源
     */
    public void setCcdPower(boolean on) {
        Logcat.d("to " + on);
        if (mCarInterface != null) {
            try {
                mCarInterface.setCcdPower(on);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 退出操作系统启动过程中提供的快速倒车功能，由应用来接管
     */
    public void disableFastReverse() {
        Logcat.d();
        if (mCarInterface != null) {
            try {
                mCarInterface.disableFastReverse();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 系统是否处于快速倒车状态
     */
    public boolean isInFastReverse() {
        if (mCarInterface != null) {
            try {
                return mCarInterface.isInFastReverse();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 下发原车ADKey数据
     * @param channel
     * @param keyValue
     */
    public void sendCarADkey(int channel, int keyValue){
        if (mCarInterface != null) {
            try {
                mCarInterface.setADKey(channel, keyValue);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 主动请求发送一次Ccd状态Event
     */
    public void requestCcdEvent() {
        if (mCarInterface == null) {
            Logcat.e("service not connected");
            return;
        }

        int status = getCcdStatus();
        EventBus.getDefault().post(new IVICar.Ccd(status));
    }

    /**
     * 获取雷达数据
     * @return 返回雷达数据
     */
    public IVICar.Radar getRadar() {
        return mRadar;
    }

    /**
     * 从服务刷新空调信息
     * @param id 空调信息ID
     * @return 服务没有收到该空调ID
     */
    private boolean updateClimateCache(int id) {
        if (!mClimates.contains(id)) {
            int rawValue = getClimateRawValue(id);
            if (rawValue != Climate.CLIMATE_VALUE_UNKNOWN) {
                mClimates.set(id, rawValue);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private int getClimateRawValue(int id) {
        if (mCarInterface == null) {
            return Climate.CLIMATE_VALUE_UNKNOWN;
        }

        try {
            return mCarInterface.getClimate(id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return Climate.CLIMATE_VALUE_UNKNOWN;
    }

    /**
     * 发送数据给仪表
     * @param params 参数
     */
    public void setClusterParam(byte[] params) {
        if (mCarInterface != null) {
            try {
                mCarInterface.setClusterParam(params);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
