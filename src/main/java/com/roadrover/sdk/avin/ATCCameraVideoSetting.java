package com.roadrover.sdk.avin;

import com.roadrover.sdk.utils.FieldUtil;
import com.roadrover.sdk.utils.Logcat;

/**
 * atc 摄像头视频参数调节
 */

public class ATCCameraVideoSetting {

    /** AtcVcpSettings 类名 */
    private static final String ATC_VCP_SETTINGS_CLASS_NAME = "com.autochips.settings.AtcVcpSettings";
    /** 亮度，对比度，饱和度调节的对象类名 */
    private static final String CONTR_BRIT_SATR_CLASS_NAME  = ATC_VCP_SETTINGS_CLASS_NAME + "$ContrBritSatr";
    /** SrcType类名 */
    private static final String SRC_TYPE_CLASS_NAME = ATC_VCP_SETTINGS_CLASS_NAME + "$SrcType";

    /** 亮度，对比度，饱和度，调节对象 */
    private Object mContrastBrightnessSaturation;

    public ATCCameraVideoSetting() {
        mContrastBrightnessSaturation = FieldUtil.createObject(ATC_VCP_SETTINGS_CLASS_NAME, CONTR_BRIT_SATR_CLASS_NAME);
    }

    /**
     * 设置视频参数
     * @param cameraIndex 摄像头index
     * @param id {@link VideoParam#makeId(int, int)} 获得
     * @param value 参数值
     */
    public void setParam(int cameraIndex, int id, int value) {
        int srcType = videoIdToSrcType(cameraIndex);
        Logcat.d("srcType:" + srcType);

        int subId = VideoParam.getSubId(id);
        switch (subId) {
            case VideoParam.SubId.BRIGHTNESS:
                setBrightnessLevel(srcType, value);
                break;

            case VideoParam.SubId.CONTRAST:
                setContrastLevel(srcType, value);
                break;

            case VideoParam.SubId.SATURATION:
                setSaturationLevel(srcType, value);
                break;

            default:
                Logcat.d("subId:" + VideoParam.SubId.getName(subId) + " is invalid!");
                break;
        }
    }

    /**
     * 获取视频参数的值
     * @param cameraIndex 摄像头index
     * @param id {@link VideoParam#makeId(int, int)} 获得
     * @return
     */
    public int getParamValue(int cameraIndex, int id) {
        int srcType = videoIdToSrcType(cameraIndex);
        Logcat.d("srcType:" + srcType);

        int subId = VideoParam.getSubId(id);
        switch (subId) {
            case VideoParam.SubId.BRIGHTNESS:
                return getBrightnessLevel(srcType);

            case VideoParam.SubId.CONTRAST:
                return getContrastLevel(srcType);

            case VideoParam.SubId.SATURATION:
                return getSaturationLevel(srcType);

            default:
                Logcat.d("subId:" + VideoParam.SubId.getName(subId) + " is invalid!");
                break;
        }
        return -1;
    }

    /**
     * 设置亮度
     * @param brightness
     */
    private void setBrightnessLevel(int srcType, int brightness) {
        Object contrBritSatr = getContrBritSatr(srcType);
        FieldUtil.setFieldValue(contrBritSatr, "i4Brit", brightness);
        FieldUtil.invoke(ATC_VCP_SETTINGS_CLASS_NAME, "SetVcpContrBritSatrLevel", contrBritSatr);

        Logcat.d("brightness:" + brightness + " " + getBrightnessLevel(srcType));
    }

    /**
     * 获取当前实际亮度
     * @param srcType 源类型
     * @return
     */
    private int getBrightnessLevel(int srcType) {
        return FieldUtil.getFieldIntValue(CONTR_BRIT_SATR_CLASS_NAME, getContrBritSatr(srcType), "i4Brit");
    }

    /**
     * 设置对比度
     * @param srcType 设置的源类型
     * @param contrast
     */
    private void setContrastLevel(int srcType, int contrast) {
        Object contrBritSatr = getContrBritSatr(srcType);
        FieldUtil.setFieldValue(contrBritSatr, "i4Contr", contrast);
        FieldUtil.invoke(ATC_VCP_SETTINGS_CLASS_NAME, "SetVcpContrBritSatrLevel", contrBritSatr);
        Logcat.d("contrast:" + contrast + " " + getContrastLevel(srcType));
    }

    /**
     * 获取当前的实际对比度
     * @return
     */
    private int getContrastLevel(int srcType) {
        return FieldUtil.getFieldIntValue(CONTR_BRIT_SATR_CLASS_NAME, getContrBritSatr(srcType), "i4Contr");
    }

    /**
     * 设置饱和度
     * @param srcType    设置的视频源
     * @param saturation 饱和度
     */
    private void setSaturationLevel(int srcType, int saturation) {
        Object contrBritSatr = getContrBritSatr(srcType);
        FieldUtil.setFieldValue(contrBritSatr, "i4Satr", saturation);
        FieldUtil.invoke(ATC_VCP_SETTINGS_CLASS_NAME, "SetVcpContrBritSatrLevel", contrBritSatr);
        Logcat.d("saturation:" + saturation + " " + getSaturationLevel(srcType));
    }

    /**
     * 获取当前的实际饱和度
     * @param srcType 源类型
     * @return
     */
    private int getSaturationLevel(int srcType) {
        return FieldUtil.getFieldIntValue(CONTR_BRIT_SATR_CLASS_NAME, getContrBritSatr(srcType), "i4Satr");
    }

    /**
     * 获取当前的对比度，亮度
     * @return
     */
    private Object getContrBritSatr(int srcType) {
        FieldUtil.setFieldValue(CONTR_BRIT_SATR_CLASS_NAME, mContrastBrightnessSaturation, "srctype", srcType);
        FieldUtil.invoke(ATC_VCP_SETTINGS_CLASS_NAME, "GetVcpContrBritSatrLevel", mContrastBrightnessSaturation);
        return mContrastBrightnessSaturation;
    }

    /**
     * 将视频id转换成 srcType
     * @param cameraIndex 摄像头index
     * @return
     */
    private int videoIdToSrcType(int cameraIndex) {
        return FieldUtil.getFieldIntValue(SRC_TYPE_CLASS_NAME, null, "AVIN_" + cameraIndex);
    }
}
