// ICarCallback.aidl
package com.roadrover.services.car;

interface ICarCallback {
    /**
     * 下位机版本号
     * @param version 版本号
     */
    void onMcuVersion(String version);
    /**
     * ACC状态发生变化
     * @param on true表示上了ACC，false表示无ACC
     */
    void onAccChanged(boolean on);
    /**
     * 倒车状态发生变化
     * @param on true表示处于倒车状态，false表示不在倒车状态
     */
    void onCcdChanged(int status);
    /**
     * 刹车状态发生变化
     * @param hold IVICar.Handbrake
     */
    void onHandbrakeChanged(boolean hold);
    /**
     * 车门状态发生变化
     * @param changeMask 变化掩码
     * @param statusMask 状态掩码
     */
    void onDoorChanged(int changeMask, int statusMask);
    /**
     * 车灯状态发生变化
     * @param changeMask 变化掩码
     * @param statusMask 状态掩码
     */
    void onLightChanged(int changeMask, int statusMask);
    /**
     * 大灯状态发生变化
     * @param on true表示大灯打开，false表示大灯关闭
     */
    void onHeadLightChanged(boolean on);
    /**
     * 空调状态发生变化
     * @param id       Climate.java
     * @param rawValue 空调的数值
     */
    void onClimateChanged(int id, int rawValue);
    /**
     * 外部温度发生变化
     * @param rawValue 外部温度
     */
    void onOutsideTempChanged(int rawValue);
    /**
     * 按键被按下
     * @param id   IVICar.Key.Id
     * @param type IVICar.Key.Type
     */
    void onKeyPushed(int id, int type);
    /**
     * 来了车辆故障和警告信息
     * @param messageCode 消息值
     */
    void onAlertMessage(int messageCode);
    /**
     * 油耗发生变化
     * @param id    Trip.Id
     * @param index Trip.Index
     * @param value 油耗值
     */
    void onTripChanged(int id, int index, float value);
    /**
     * 瞬时信息发生变化
     * @param id    IVICar.RealTimeInfo
     * @param value 瞬时信息的值
     */
    void onRealTimeInfoChanged(int id, float value);
    /**
     * 原车其它参数发生变化
     * @param id    IVICar.ExtraState
     * @param value 其它参数的数值
     */
    void onExtraStateChanged(int id, float value);
    /**
     * 原车雷达数据发生变化
     * @param radarType IVICar.Radar.Type
     * @param radarData 雷达数据的值
     */
    void onRadarChanged(int radarType, in byte[] radarData);
    /**
     * 原车设置信息发生变化
     * @param carId       汽车Id
     * @param settingData 设置数据的值
     */
    void onCarSettingChanged(int carId, in byte[] settingData);
    /**
     * 原车外部设备数据发生变化
     * @param carId           IVICar.ExtraDevice.CarId
     * @param deviceId        IVICar.ExtraDevice.DeviceId
     * @param extraDeviceData 原车外部设备数据
     */
    void onExtraDeviceChanged(int carId, int deviceId, in byte[] extraDeviceData);
    /**
     * 原车配置参数发生变化
     * @param id        IVICar.CmdParam.Id
     * @param paramData 配置参数的值
     */
    void onCmdParamChanged(int id, in byte[] paramData);
    /**
     * 原车保养信息发生变化
     * @param id      IVICar.Maintenance.Id
     * @param mileage 里程数值
     * @param days    天数
     */
    void onMaintenanceChanged(int id, int mileage, int days);
    /**
     * 车辆识别码发生变化
     * @param VIN       车辆识别码VIN（VIN码由17位字符组成）
     * @param keyNumber 匹配钥匙数目
     */
    void onCarVINChanged(String VIN, int keyNumber);
    /**
     * 原车故障码发生变化
     * @param carid IVICar.CarReport.Car
     * @param type  IVICar.CarReport.Type
     * @param list  故障码数据
     */
    void onCarReportChanged(int carid, int type, in int[] list);
    /**
     * 自动泊车发生变化
     * @param status 自动泊车系统状态
     */
    void onAutoParkChanged(int status);
    /**
     * 原车故障码发生变化
     * @param battery        电池电量,最大值由具体的车型确定
     * @param engineToTyre   发动机(A)和驱动轮(B)流向
     * @param engineToMotor  发动机(A)和马达(B)流向
     * @param motorToTyre    马达(A)和驱动轮(B)流向
     * @param motorToBattery 马达(A)和电池(B)的流向
     */
    void onEnergyFlowChanged(int battery, int engineToTyre, int engineToMotor, int motorToTyre, int motorToBattery);
    /**
     * 快速倒车状态发生变化
     * @param on true为快速倒车状态，false为正常倒车状态
     */
    void onFastReverseChanged(boolean on);
    /**
     * AD按键发生变化
     * @param channel AD通道
     * @param value   按键AD值
     */
    void onADKeyChanged(int channel, int value);
    /**
     * 从仪表发送数据到APP
     * @param datas 仪表发送过来的数据
     */
    void onClusterMessage(in byte[] datas);

    /**
     * 胎压状态发生变化
     * @param id          ID 定义 见{@link com.roadrover.sdk.car.TirePressure}
     * @param rawValue    数据 见{@link com.roadrover.sdk.car.TirePressure}
     * @param extraValue  附加状态数据 见{@link com.roadrover.sdk.car.TirePressure}
     * @param dotType     温度小数点标志 见{@link com.roadrover.sdk.car.TirePressure}
     */
    void onTirePressureChanged(int id, int rawValue, int extraValue, int dotType);
}
