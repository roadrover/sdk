package com.roadrover.sdk.dab;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import com.roadrover.sdk.BaseManager;
import com.roadrover.services.dab.IDAB;
import com.roadrover.services.dab.IDABCallback;
import com.roadrover.sdk.utils.Logcat;

/**
 * 数字音频广播管理类，提供数字音频广播接口
 */

public class DABManager extends BaseManager {

    /**
     * DAB接口类对象
     */
    private IDAB mDABInterface = null;

    /**
     * DAB服务回调对象
     */
    private IDABCallback mDABCallback = new IDABCallback.Stub() {

        @Override
        public void onSystemNotification(int systemState) throws RemoteException {
            post(new IVIDAB.EventSystemStateChanged(systemState));
        }
    };

    public DABManager(Context context, ConnectListener connectListener) {
        super(context, connectListener, true);
    }

    @Override
    public void disconnect() {
        mDABCallback = null;

        super.disconnect();
    }

    @Override
    protected String getServiceActionName() {
        return ServiceAction.DAB_ACTION;
    }

    @Override
    protected void onServiceConnected(IBinder service) {
        mDABInterface = IDAB.Stub.asInterface(service);
    }

    @Override
    protected void onServiceDisconnected() {
        mDABInterface = null;
    }

    /**
     * 打开设备，电台以上次的为准
     */
    public void open() {
        if (mDABInterface != null) {
            try {
                mDABInterface.open(mDABCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }

        super.registerCallback(mDABCallback);
    }

    /**
     * 关闭设备
     */
    public void close() {
        if (mDABInterface != null) {
            try {
                mDABInterface.close();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }

    /**
     * 获取中间件版本
     */
    public String getMiddlewareVersion() {
        if (mDABInterface != null) {
            try {
                return mDABInterface.getMiddlewareVersion();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
        return null;
    }

    /**
     * 获取固件版本
     */
    public String getSWVersion() {
        if (mDABInterface != null) {
            try {
                return mDABInterface.getMiddlewareVersion();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
        return null;
    }

    /**
     * 获取硬件版本
     */
    public String getHWVersion() {
        if (mDABInterface != null) {
            try {
                return mDABInterface.getMiddlewareVersion();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
        return null;
    }

    /**
     * 单个调谐器系统以触发全频段扫描
     */
    public void triggerBandScan() {
        if (mDABInterface != null) {
            try {
                mDABInterface.triggerBandScan();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
    }
}
