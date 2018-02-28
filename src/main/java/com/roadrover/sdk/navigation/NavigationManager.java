package com.roadrover.sdk.navigation;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.roadrover.sdk.BaseManager;
import com.roadrover.sdk.utils.Logcat;
import com.roadrover.services.navigation.INavigation;
import com.roadrover.services.navigation.INavigationCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航的管理类
 */
public class NavigationManager extends BaseManager {

    private INavigation mNavigationInterface = null; // 导航接口类

    private NavigationListener mNavigationListener;

    public interface NavigationListener {

        /**
         * 导航过程中发送引导类型，和引导属性
         */
        void onNavigationType(int twelveClock, int turnID, int []arrayTurn,int guideType, int distance,
                              int destDistance,int destTime, String roadName, String nextRoadName,String destName);

        /**
         * 发自车行政区变更时，发送该消息，例：广东省深圳市南山区
         */
        void onNavigationAddress(String province, String city, String county);

        /**
         * 设置指南针的指向
         */
        void onNavigationGuide(int direction);

        /**
         * 设置电子眼的信息
         */
        void onNavigationEyeInfo(int type, int distance, int speedLimit);
    }


    public NavigationManager(Context context, ConnectListener listener, NavigationListener navigationListener) {
        super(context, listener, true);
        mNavigationListener = navigationListener;
    }

    @Override
    public void disconnect() {
        mINavigationCallback = null;
        mNavigationInterface = null;
        mNavigationListener = null;
        super.disconnect();
    }

    @Override
    protected String getServiceActionName() {
        return ServiceAction.NAVIGATION_ACTION;
    }

    @Override
    protected void onServiceConnected(IBinder service) {
        mNavigationInterface = INavigation.Stub.asInterface(service);

        // 如果服务挂了，重启，必须在该位置重新注册回调，否则会没反应
        registerNavigationCallback();
    }

    @Override
    protected void onServiceDisconnected() {
        mNavigationInterface = null;
    }

    /**
     * 注册Navigation回调
     */
    private void registerNavigationCallback() {
        if (mNavigationInterface != null && mContext != null) {
            try {
                mNavigationInterface.registerNavigationCallback(mINavigationCallback, mContext.getPackageName());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.w("mNavigationInterface = " + mNavigationInterface + " mContext = " + mContext);
        }
    }

    /**
     * 注销Navigation回调
     */
    private void unRegisterSystemCallback() {
        if (mNavigationInterface != null) {
            try {
                mNavigationInterface.unRegisterNavigationCallback(mINavigationCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNavigationEyeInfo(IVINavigation.EventNavigationEyeInfo event) {
        if (mNavigationListener != null) {
            mNavigationListener.onNavigationEyeInfo(event.mType, event.mDistance, event.mSpeedLimit);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNavigationGuide(IVINavigation.EventNavigationGuide event) {
        if (mNavigationListener != null) {
            mNavigationListener.onNavigationGuide(event.mDirection);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNavigationAddress(IVINavigation.EventNavigationAddress event) {
        if (mNavigationListener != null) {
            mNavigationListener.onNavigationAddress(event.mProvince, event.mCity, event.mCounty);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNavigationType(IVINavigation.EventNavigationType event) {
        if (mNavigationListener != null) {
            mNavigationListener.onNavigationType(event.mTwelveClock,
                    event.mTurnID,
                    event.mArrayTurn,
                    event.mGuideType,
                    event.mDistance,
                    event.mDestDistance,
                    event.mDestTime,
                    event.mRoadName,
                    event.mNextRoadName,
                    event.mDestName);
        }
    }

    private INavigationCallback.Stub mINavigationCallback = new INavigationCallback.Stub() {

        @Override
        public void onNavigationType(int twelveClock, int turnID, int[] arrayTurn, int guideType, int distance, int destDistance, int destTime, String roadName, String nextRoadName, String destName) throws RemoteException {
            post(new IVINavigation.EventNavigationType(twelveClock, turnID, arrayTurn, guideType, distance, destDistance, destTime, roadName, nextRoadName, destName));
        }

        @Override
        public void onNavigationAddress(String province, String city, String county) throws RemoteException {
            post(new IVINavigation.EventNavigationAddress(province, city, county));
        }

        @Override
        public void onNavigationGuide(int direction) throws RemoteException {
            post(new IVINavigation.EventNavigationGuide(direction));
        }

        @Override
        public void onNavigationEyeInfo(int type, int distance, int speedLimit) throws RemoteException {
            post(new IVINavigation.EventNavigationEyeInfo(type, distance, speedLimit));
        }
    };


}
