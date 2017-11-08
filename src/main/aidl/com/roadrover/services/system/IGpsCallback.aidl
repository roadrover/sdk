// IGpsCallback.aidl
package com.roadrover.services.system;

// Declare any non-default types here with import statements

interface IGpsCallback {
    /**
     * gps 定位回调
     * @param longitude 经度
     * @param latitude 维度
     * @param accuracy 精度
     * @param altitude 海拔
     * @param fSpeed 速度，km/h
     */
    void onGpsLocationInfoChanged(String longitude, String latitude, float accuracy, double altitude, float fSpeed);

    /**
     * gps 可以使用的数量
     * @param iGpsInView
     * @param iGpsInUse
     * @param iGlonassInView
     * @param iGLonassInUse
     */
    void onGpsCountChanged(int iGpsInView, int iGpsInUse, int iGlonassInView, int iGLonassInUse);
}
