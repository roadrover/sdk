// INavigationCallback.aidl
package com.roadrover.services.navigation;


interface INavigationCallback {
    /**
     * 导航过程中发送引导类型，和引导属性
     * @param twelveClock {@link com.roadrover.sdk.navigation.IVINavigation.TwelveClockType}
     * @param turnID twelveClock参数为TwelveClockType.NORMAL时 </br>
     *                    {@link com.roadrover.sdk.navigation.IVINavigation.NormalTurnId} </br>
     *               twelveClock参数为TwelveClockType.TWELVE_CLOCK时 </br>
     *                     {@link com.roadrover.sdk.navigation.IVINavigation.TwelveClockTurnId} </br>
     * @param arrayTurn 方向背景，twelveClock参数为TwelveClockType.TWELVE_CLOCK时，有背景 </br>
     *                    {@link com.roadrover.sdk.navigation.IVINavigation.TwelveClockTurnId} </br>
     * @param guideType 引导点属性 {@link com.roadrover.sdk.navigation.IVINavigation.GuideType}
     * @param distance 到下一个引导点的距离，单位 m
     * @param destDistance 到目的地的距离，单位 m
     * @param destTime 到目的地所需要的时间，单位 s
     * @param roadName 当前道路的名称，utf-8编码
     * @param nextRoadName 下一个道路的名称，utf-8编码
     * @param destName 目的地的名字
     */
    void onNavigationType(int twelveClock, int turnID, in int []arrayTurn,int guideType, int distance,
                            int destDistance,int destTime, String roadName, String nextRoadName,String destName);

    /**
     * 发自车行政区变更时，发送该消息，例：广东省深圳市南山区
     * @param province 省
     * @param city 市
     * @param county 区域
     */
    void onNavigationAddress(String province, String city, String county);

    /**
     * 设置指南针的指向
     * @param direction 指向
     */
    void onNavigationGuide(int direction);

    /**
     * 设置电子眼的信息
     * @param type 电子眼的类型，{@link com.roadrover.sdk.navigation.IVINavigation.ElectronicEyeType}
     * @param distance 到下一个电子眼的距离，单位 m
     * @param speedLimit 限速多少
     */
    void onNavigationEyeInfo(int type, int distance, int speedLimit);
}
