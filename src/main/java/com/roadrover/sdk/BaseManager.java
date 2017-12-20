package com.roadrover.sdk;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.text.TextUtils;

import com.roadrover.sdk.system.IVISystem;
import com.roadrover.sdk.utils.EventBusUtil;
import com.roadrover.sdk.utils.Logcat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 所有aidl服务连接管理类的基类
 */
public abstract class BaseManager {
	/**
	 * ROADROVER核心服务都起来之后会发送该广播，广播 action
	 */
	public static String ROADROVER_IVI_SERVICE_READY = "com.roadrover.services.ready";
	/**
	 * 蓝牙服务起来之后会发送该广播，广播 action
	 */
    public static String ROADROVER_BT_SERVICE_READY = "com.roadrover.btservice.ready";

	/**
	 * 所有服务的 action 定义类，需要和服务的 xml 里面对应
	 */
	public static class ServiceAction {
		/**
		 * 主服务 action
		 */
		public static final String MAIN_ACTION     	= "com.roadrover.services.action.main";

		/**
		 * 收音机服务的action
		 */
		public static final String RADIO_ACTION     = "com.roadrover.services.action.radio";

		/**
		 * avin服务的action
		 */
		public static final String AVIN_ACTION      = "com.roadrover.services.action.avin";

		/**
		 * 音频服务的action
		 */
		public static final String AUDIO_ACTION     = "com.roadrover.services.action.audio";

		/**
		 * 蓝牙服务的action
		 */
		public static final String BLUETOOTH_ACTION = "com.roadrover.btservice.action.bluetooth";

		/**
		 * 原车服务的action
		 */
		public static final String CAR_ACTION       = "com.roadrover.services.action.car";

		/**
		 * 媒体服务的action
		 */
		public static final String MEDIA_ACTION     = "com.roadrover.services.action.media";

		/**
		 * 系统服务的action
		 */
		public static final String SYSTEM_ACTION    = "com.roadrover.services.action.system";

		/**
		 * 语音服务的action
		 */
		public static final String VOICE_ACTION     = "com.roadrover.services.action.voice";

		/**
		 * 汽车仪表通信的服务的action
		 */
		public static final String CLUSTER_ACTION   = "com.roadrover.services.action.cluster";

		/**
		 * 导航服务的action
		 */
		public static final String NAVIGATION_ACTION = "com.roadrover.services.action.navigation";
	}

	/**
	 * 服务连接的接口类
	 */
	public interface ConnectListener {
		/**
		 * 服务连接上
		 */
		void onServiceConnected();

		/**
		 * 和服务断开连接
		 */
		void onServiceDisconnected();
	}

	protected Context mContext = null;
	private boolean mIsConnected = false; // 是否已经绑定服务
	private ConnectListener mConnectListener;
    private EventBusUtil mEventBus;

	protected Set<IInterface> mICallbackS = new HashSet<IInterface>(); // 采用Set缓存回调集合
	private boolean mIsRegisterReceiver = false; // 判断是否已经注册广播接收者

	public boolean isConnected() {
		return mIsConnected;
	}

	/**
	 * 构造函数
	 * @param context 上下文
	 * @param listener 服务监听
     * @param useDefaultEventBus 是否使用默认的EventBus，如果需要不使用默认的EventBus，可以使用false
     */
	protected BaseManager(Context context, ConnectListener listener, boolean useDefaultEventBus) {
		if (context != null) { // 获取应用 application的上下文，免得在 manager 里面持有外部Activity的指针
			mContext = context.getApplicationContext();
		}
		mConnectListener = listener;
		connect();

        mEventBus = new EventBusUtil(this, useDefaultEventBus);
	}

	/**
	 * 启动主服务
	 */
	private void startMainService() {
		if (mContext != null) {
			Intent mainService = new Intent();
			mainService.setAction(ServiceAction.MAIN_ACTION);
			mainService.setPackage(IVISystem.PACKAGE_IVI_SERVICES);
			mContext.startService(mainService);
		}
	}

	/**
	 * 启动蓝牙服务
	 */
	private void startBluetoothService() {
		if (mContext != null) {
			Intent service = new Intent();
			service.setAction(ServiceAction.BLUETOOTH_ACTION);
			service.setPackage(IVISystem.PACKAGE_BT_SERVICE);
			mContext.startService(service);
		}
	}

	/**
	 * 连接指定服务
	 */
	public void connect() {
		if (!mIsConnected) {
			startMainService();

			if (mContext != null) {
				boolean bluetoothService = TextUtils.equals(getServiceActionName(), ServiceAction.BLUETOOTH_ACTION);
				if (bluetoothService) { // 如果是连接蓝牙服务，则启动蓝牙服务
					startBluetoothService();
				}

				Intent service = new Intent();
				service.setAction(getServiceActionName());
				service.setPackage(bluetoothService ? IVISystem.PACKAGE_BT_SERVICE : IVISystem.PACKAGE_IVI_SERVICES);

				boolean isSuccess = mContext.bindService(service, mServiceConnection, Context.BIND_AUTO_CREATE);
				Logcat.d(getServiceActionName() + ", result: " + isSuccess);

				/*
				 * 	注册广播监听MainService是否启动完成，完成后自动连接，
				 * 	防止Roadrover IVI Service崩溃重新启动后，连接没有继续
				 */
				if (!mIsRegisterReceiver) {
					mIsRegisterReceiver = true;
					IntentFilter f = new IntentFilter();
					f.addAction(bluetoothService ? ROADROVER_BT_SERVICE_READY : ROADROVER_IVI_SERVICE_READY);
					mContext.registerReceiver(mListener, f);
				}
			}
		}
 	}

	private BroadcastReceiver mListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Logcat.d("action = " + action);
            boolean bluetoothService = TextUtils.equals(getServiceActionName(), ServiceAction.BLUETOOTH_ACTION);
            if (bluetoothService) {
                if (action.equals(ROADROVER_BT_SERVICE_READY)) {
                    connect();
                }
            } else {
                if (action.equals(ROADROVER_IVI_SERVICE_READY)) {
                    connect();
                }
            }
		}
	};

	/**
	 * 和服务断开连接
	 */
	public void disconnect() {
		// 用户在销毁这个类的时候，将销毁所有的回调
		List<IInterface> callbacks = new ArrayList<>(mICallbackS); // 转换成一个列表进行删除，防止在unRegisterCallback里面直接操作遍历列表
		if (callbacks != null) {
			for (int i = 0; i < callbacks.size(); ++i) {
				if (callbacks.get(i) != null) {
					unRegisterCallback(callbacks.get(i));
				}
			}
		}
		mICallbackS.clear();

		if (mIsConnected) {
			if (null != mContext) {
				mContext.unbindService(mServiceConnection);
			}
			mIsConnected = false;
		}

		if (mIsRegisterReceiver) { // 注销广播接收者
			mIsRegisterReceiver = false;
			if (null != mContext) {
				mContext.unregisterReceiver(mListener);
			}
		}

		if (null != mConnectListener){
			mConnectListener = null;
		}

		if (null != mContext){
			mContext = null;
		}

        if (mEventBus != null) {
            mEventBus.destroy(this);
            mEventBus = null;
        }
	}

	/**
	 * 获取服务名，子类需要重载该方法，返回服务名
	 * @return 不同manager需要返回对应服务的action名
	 */
	protected abstract String getServiceActionName();
	
	/**
	 * 服务连接上
	 * @param service
	 */
	protected abstract void onServiceConnected(IBinder service);
	
	/**
	 * 服务断开连接
	 */
	protected abstract void onServiceDisconnected();

	/**
	 * 注册回调，将回调添加到一个列表中
	 * 所有设置aidl回调，都需要 super.registerCallback(callback);  加入到列表中，统一管理
	 * @param callback aidl回调
     */
	protected void registerCallback(IInterface callback) {
		if (callback != null) {
			mICallbackS.add(callback);
		}
	}

	/**
	 * 注销回调
	 * 所有注销aidl回调，都需要 super.unRegisterCallback(callback); 从列表中移除
	 * @param callback aidl 回调
     */
	protected void unRegisterCallback(IInterface callback) {
		if (callback != null) {
			mICallbackS.remove(callback);
		}
	}

	/**
	 * 服务连接
	 */
	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Logcat.d("service: " + name);
			BaseManager.this.onServiceConnected(service);
			mIsConnected = true;
			if (mConnectListener != null) {
				mConnectListener.onServiceConnected();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mIsConnected = false;
			BaseManager.this.onServiceDisconnected();
			Logcat.d("service: " + name);
			if (mConnectListener != null) {
				mConnectListener.onServiceDisconnected();
			}
		}
	};

    /**
     * 注册EventBus
     * @param object 宿主对象
     */
    public void registerEventBus(Object object) {
        if (mEventBus != null) {
            mEventBus.register(object);
        }
    }

    /**
     * 反注册EventBus
     * @param object 宿主对象
     */
    public void unregisterEventBus(Object object) {
        if (mEventBus != null) {
            mEventBus.unregister(object);
        }
    }

    /**
     * 发送一个消息
     * @param object 事件对象
     */
    protected void post(Object object) {
        if (mEventBus != null) {
            mEventBus.post(object);
        }
    }

    /**
     * 发送一个粘性消息
     * @param object 时间对象
     */
    protected void postSticky(Object object) {
        if (mEventBus != null) {
            mEventBus.postSticky(object);
        }
    }
}
