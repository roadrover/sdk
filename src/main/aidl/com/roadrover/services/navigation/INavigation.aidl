// INavigation.aidl
package com.roadrover.services.navigation;

import com.roadrover.services.navigation.INavigationCallback;

interface INavigation {
    /**
     * 注册监听导航回调
     * @param callback
     * @param packageName 当前应用的包名
     */
    void registerNavigationCallback(INavigationCallback callback, String packageName);

    /**
     * 注销监听导航回调
     */
    void unRegisterNavigationCallback(INavigationCallback callback);
}
