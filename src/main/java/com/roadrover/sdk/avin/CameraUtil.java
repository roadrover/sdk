package com.roadrover.sdk.avin;

import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Camera工具类，用来适配不同平台的Camera
 */

public class CameraUtil {

    private ATCCamera mATCCamera; // ATC平台摄像头对象
    private Camera mCamera; // 标准的摄像头接口

    /**
     * 创建一个CameraUtil对象
     */
    public static CameraUtil open(int cameraId, Context context) {
        return new CameraUtil(cameraId, context);
    }

    /**
     * 判断是否是ATC平台
     * @return
     */
    public static boolean isATCPlatform() {
        return ATCCamera.detectionIsATCPlatform();
    }

    /**
     * 是否是ATC摄像头被打开
     * @return
     */
    public boolean isATCCamera() {
        return mATCCamera != null;
    }

    /**
     * 获取视频信号
     * @return
     */
    public int getVideoSignal() {
        if (mATCCamera != null) {
            return mATCCamera.getVideoSignal();
        }
        return IVIAVIn.Signal.UNSTABLE_SIGNAL;
    }

    private CameraUtil(int cameraId, Context context) {
        if (ATCCamera.detectionIsATCPlatform()) {
            mATCCamera = ATCCamera.open(cameraId, context);
        } else {
            mCamera = Camera.open(cameraId);
        }
    }

    /**
     * 设置视频监听回调
     * @param listener
     */
    public void setAVInCallback(IVIAVIn.AVInListener listener) {
        if (mATCCamera != null) { // ATC平台需要通过该方法返回当前信号状态
            mATCCamera.setAVInCallback(listener);
        }
    }

    /**
     * 初始化摄像头参数
     * @param avId       设置参数id
     * @param brightness 亮度
     * @param contrast   对比度
     * @param saturation 饱和度
     */
    public void initVideoParam(int avId, VideoParam brightness, VideoParam contrast, VideoParam saturation) {
        if (mATCCamera != null) { // atc 平台需要在UI起来之前，先刷一遍参数
            if (brightness != null) {
                mATCCamera.setParam(VideoParam.makeId(avId, VideoParam.SubId.BRIGHTNESS),
                        brightness.mValue);
            }

            if (contrast != null) {
                mATCCamera.setParam(VideoParam.makeId(avId, VideoParam.SubId.CONTRAST),
                        contrast.mValue);
            }

            if (saturation != null) {
                mATCCamera.setParam(VideoParam.makeId(avId, VideoParam.SubId.SATURATION),
                        saturation.mValue);
            }
        }
    }

    /**
     * Returns the current settings for this Camera service. </br>
     * ATC平台不需要该方法
     */
    public Camera.Parameters getParameters() {
        if (mCamera != null) {
            return mCamera.getParameters();
        }
        return null;
    }

    /**
     * Changes the settings for this Camera service. </br>
     * ATC平台不需要该方法
     */
    public void setParameters(Camera.Parameters params) {
        if (mCamera != null) {
            mCamera.setParameters(params);
        }
    }

    /**
     * 释放适配
     */
    public final void release() {
        if (mATCCamera != null) {
            mATCCamera.release();
        } else if (mCamera != null) {
            mCamera.release();
        }
    }

    /**
     * Starts capturing and drawing preview frames to the screen.
     * Preview will not actually start until a surface is supplied
     */
    public void startPreview() {
        if (mATCCamera != null) {
            mATCCamera.startPreview();
        } else if (mCamera != null) {
            mCamera.startPreview();
        }
    }

    /**
     * Stops capturing and drawing preview frames to the surface, and
     * resets the camera for a future call to {@link #startPreview()}.
     */
    public final void stopPreview() {
        if (mATCCamera != null) {
            mATCCamera.stopPreview();
        } else if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    /**
     * Sets the {@link Surface} to be used for live preview.
     * Either a surface or surface texture is necessary for preview, and
     * preview is necessary to take pictures.  The same surface can be re-set
     * without harm.  Setting a preview surface will un-set any preview surface
     * texture that was set via .
     */
    public final void setPreviewDisplay(SurfaceHolder holder) throws IOException {
        if (mATCCamera != null) {
            mATCCamera.setPreviewDisplay(holder);
        } else if (mCamera != null) {
            mCamera.setPreviewDisplay(holder);
        }
    }

    /**
     * 设置ATC摄像头视频参数
     * @param id
     * @param value
     */
    public void setATCCameraParam(int id, int value) {
        if (mATCCamera != null) {
            mATCCamera.setParam(id, value);
        }
    }

    /**
     * 获取ATC摄像头的视频参数
     * @param id
     * @return
     */
    public int getATCCameraParam(int id) {
        if (mATCCamera != null) {
            return mATCCamera.getParamValue(id);
        }
        return 0;
    }
}
