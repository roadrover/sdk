package com.roadrover.sdk.radio;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import com.roadrover.sdk.BaseManager;
import com.roadrover.sdk.media.IVIMedia;
import com.roadrover.services.radio.IRadio;
import com.roadrover.services.radio.IRadioCallback;
import com.roadrover.sdk.utils.Logcat;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 收音机管理类，提供收音机接口
 */
public class RadioManager extends BaseManager {
	private IRadio mRadioInterface; // 收音机接口类
	private RadioListener mRadioListener;
	private boolean mIsOpen = false;
	private int mFreq = -1;
	private int mAudioZone = IVIMedia.Zone.UNKNOWN;
	private RadioScanAbortListener mRadioScanAbortListener ;   // 直接监听 收音机 搜索过程中停止

	public interface RadioListener {
		void onFreqChanged(int freq);
		void onScanResult(int freq, int signalStrength);
		void onScanStart(boolean isScanAll);
		void onScanEnd(boolean isScanAll);
		void onScanAbort(boolean isScanAll);
		void onSignalUpdate(int freq, int signalStrength);
		void suspend();
		void resume();
		void pause();
		void play();
		void playPause();
		void stop();
		void next();
		void prev();
		void quitApp();
		void select(int index);
		void setFavour(boolean isFavour);
		void onRdsPsChanged(int pi, int freq, String ps);
		void onRdsRtChanged(int pi, int freq, String rt);
		void onRdsMaskChanged(int pi, int freq, int pty, int tp, int ta);
	}

	public interface RadioScanAbortListener {
		void onScanAbort(boolean isScanAll);
	}

	public void setRadioScanAbortListener(RadioScanAbortListener radioScanAbortListener) {
		this.mRadioScanAbortListener = radioScanAbortListener ;
	}

	public RadioManager(Context context, ConnectListener connectListener, RadioListener radioListener) {
		super(context, connectListener, true);
		mRadioListener = radioListener;
	}

	@Override
	public void disconnect() {
		mRadioInterface = null;
		mRadioListener = null;
		mRadioCallback = null;
		mRadioScanAbortListener = null;
		super.disconnect();
	}

	@Override
	protected String getServiceActionName() {
		return ServiceAction.RADIO_ACTION;
	}

	@Override
	protected void onServiceConnected(IBinder service) {
		mRadioInterface = IRadio.Stub.asInterface(service);
		if (mIsOpen) {
			Logcat.d("Reopen radio: " + mFreq + " at service connected on zone: " + IVIMedia.Zone.getName(mAudioZone));
			if (mFreq != -1) {
				setFreq(mFreq);
                if (mAudioZone != IVIMedia.Zone.UNKNOWN) {
                    openInZone(mAudioZone);
                } else {
                    open();
                }
			}
		}
	}

	@Override
	protected void onServiceDisconnected() {
		mRadioInterface = null;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onFreqChanged(IVIRadio.EventFreqChanged event) {
		if (mRadioListener != null) {
			mRadioListener.onFreqChanged(event.mFreq);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onScanResult(IVIRadio.EventScanResult event) {
		if (mRadioListener != null) {
			mRadioListener.onScanResult(event.mFreq, event.mSignalStrength);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onScanStart(IVIRadio.EventScanStart event) {
		if (mRadioListener != null) {
			mRadioListener.onScanStart(event.mScanAll);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onScanEnd(IVIRadio.EventScanEnd event) {
		if (mRadioListener != null) {
			mRadioListener.onScanEnd(event.mScanAll);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onScanAbort(IVIRadio.EventScanAbort event) {
		if (mRadioListener != null) {
			mRadioListener.onScanAbort(event.mScanAll);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onSignalUpdate(IVIRadio.EventSignalUpdate event) {
		if (mRadioListener != null) {
			mRadioListener.onSignalUpdate(event.mFreq, event.mSignalStrength);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onControlEvent(IVIRadio.EventControl event) {
		if (mRadioListener != null) {
			switch (event.mAction) {
				case IVIRadio.EventControl.Action.QUIT_APP:
					mRadioListener.quitApp();
					break;

                case IVIRadio.EventControl.Action.SUSPEND:
                    mRadioListener.suspend();
                    break;

                case IVIRadio.EventControl.Action.RESUME:
                    mRadioListener.resume();
                    break;

                case IVIRadio.EventControl.Action.PAUSE:
                    mRadioListener.pause();
                    break;

                case IVIRadio.EventControl.Action.PLAY:
                    mRadioListener.play();
                    break;

				case IVIRadio.EventControl.Action.PLAY_PAUSE:
					mRadioListener.playPause();
					break;

				case IVIRadio.EventControl.Action.STOP:
					mRadioListener.stop();
					break;

				case IVIRadio.EventControl.Action.SELECT:
					mRadioListener.select(event.mValue);
					break;

				case IVIRadio.EventControl.Action.SET_FAVOUR:
					mRadioListener.setFavour(event.mValue == 1);
					break;

				case IVIRadio.EventControl.Action.NEXT:
					mRadioListener.next();
					break;

				case IVIRadio.EventControl.Action.PREV:
					mRadioListener.prev();
					break;

				default:
					Logcat.d("Unknown action " + event.mAction);
					break;
			}
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onRdsPsChanged(IVIRadio.EventRdsPs event) {
		if (mRadioListener != null) {
			mRadioListener.onRdsPsChanged(event.mPI, event.mFreq, event.mText);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onRdsRtChanged(IVIRadio.EventRdsRt event) {
		if (mRadioListener != null) {
			mRadioListener.onRdsRtChanged(event.mPI, event.mFreq, event.mText);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onRdsMaskChanged(IVIRadio.EventRdsMask event) {
		if (mRadioListener != null) {
			mRadioListener.onRdsMaskChanged(event.mPI, event.mFreq, event.mPTY, event.mTP, event.mTA);
		}
	}

	private IRadioCallback mRadioCallback = new IRadioCallback.Stub() {
		@Override
		public void onFreqChanged(int freq) {
			post(new IVIRadio.EventFreqChanged(freq));
		}

		@Override
		public void onScanResult(int freq, int signalStrength) {
			post(new IVIRadio.EventScanResult(freq, signalStrength));
		}

		@Override
		public void onScanStart(boolean isScanAll) {
			post(new IVIRadio.EventScanStart(isScanAll));
		}

		@Override
		public void onScanEnd(boolean isScanAll) {
			post(new IVIRadio.EventScanEnd(isScanAll));
		}

		@Override
		public void onScanAbort(boolean isScanAll) {
			//用来 在event 的收到 onScanAbort 时， 之间搜索结果可能没有及时收到 存在时序差  该接口只做逻辑不做UI操作
			if(mRadioScanAbortListener != null){
				mRadioScanAbortListener.onScanAbort(isScanAll);
			}
			post(new IVIRadio.EventScanAbort(isScanAll));
		}

		@Override
		public void onSignalUpdate(int freq, int signalStrength) {
			post(new IVIRadio.EventSignalUpdate(freq, signalStrength));
		}

        @Override
        public void suspend() throws RemoteException {
            post(new IVIRadio.EventControl(
                    IVIRadio.EventControl.Action.SUSPEND));
        }

        @Override
        public void resume() throws RemoteException {
            post(new IVIRadio.EventControl(
                    IVIRadio.EventControl.Action.RESUME));
        }

        @Override
        public void pause() throws RemoteException {
            post(new IVIRadio.EventControl(
                    IVIRadio.EventControl.Action.PAUSE));
        }

        @Override
        public void play() throws RemoteException {
            post(new IVIRadio.EventControl(
                    IVIRadio.EventControl.Action.PLAY));
        }

		@Override
		public void playPause() throws RemoteException {
			post(new IVIRadio.EventControl(
					IVIRadio.EventControl.Action.PLAY_PAUSE));
		}

        @Override
		public void stop() {
			mIsOpen = false;
			post(new IVIRadio.EventControl(
					IVIRadio.EventControl.Action.STOP));
		}

		@Override
		public void next() {
			post(new IVIRadio.EventControl(
					IVIRadio.EventControl.Action.NEXT));
		}

		@Override
		public void prev() {
			post(new IVIRadio.EventControl(
					IVIRadio.EventControl.Action.PREV));
		}

		@Override
		public void quitApp() {
			post(new IVIRadio.EventControl(
					IVIRadio.EventControl.Action.QUIT_APP));
		}

		@Override
		public void select(int index) {
			post(new IVIRadio.EventControl(
					IVIRadio.EventControl.Action.SELECT, index));
		}

		@Override
		public void setFavour(boolean isFavour) {
			post(new IVIRadio.EventControl(
					IVIRadio.EventControl.Action.SET_FAVOUR, isFavour ? 1 : 0));
		}

		@Override
		public void onRdsPsChanged(int pi, int freq, String ps) {
			post(new IVIRadio.EventRdsPs(pi, freq, ps));
		}

		@Override
		public void onRdsRtChanged(int pi, int freq, String rt) {
			post(new IVIRadio.EventRdsRt(pi, freq, rt));
		}

		@Override
		public void onRdsMaskChanged(int pi, int freq, int pty, int tp, int ta) {
			post(new IVIRadio.EventRdsMask(pi, freq, pty, tp, ta));
		}

		@Override
		public void onTuneRotate(boolean add) {
			post(new IVIRadio.EventControl(
					IVIRadio.EventControl.Action.TUNE_ROTATE, add ? 1 : 0));
		}
	};

	/**
	 * 打开设备，频率点和band以上次的为准
	 */
	public void open() {
		mIsOpen = true;
		if (mRadioInterface != null) {
			try {
				mRadioInterface.open(mRadioCallback, mContext.getPackageName());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}

		super.registerCallback(mRadioCallback);
	}

	/**
	 * 关闭设备
	 */
	public void close() {
		mIsOpen = false;
		if (mRadioInterface != null) {
			try {
				mRadioInterface.close();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
	}

	/**
	 * 设置频率
	 * @param freq 当前频率点 KHz
	 * 会自动终止上次未完成的搜索任务
	 */
	public void setFreq(int freq) {
		mFreq = freq;
		if (mRadioInterface != null) {
			try {
				mRadioInterface.setFreq(freq);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public int getFreq() {
		if (null != mRadioInterface) {
			try {
				return mRadioInterface.getFreq();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}

		return 0;
	}

	/**
	 * 设置 fm, am
	 * @param band {@link com.roadrover.sdk.radio.IVIRadio.Band}
	 * 会自动终止上次未完成的搜索任务
	 */
	public void setBand(int band) {
		if (null != mRadioInterface) {
			try {
				mRadioInterface.setBand(band);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
	}

	public int getBand() {
		if (null != mRadioInterface) {
			try {
				return mRadioInterface.getBand();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
		return IVIRadio.Band.FM;
	}

	/**
	 * 设置 收音机区域
	 * @param location 区域 {@link com.roadrover.sdk.radio.IVIRadio.Location}
	 * 会自动终止上次未完成的搜索任务
	 */
	public void setLocation(int location) {
		if (null != mRadioInterface) {
			try {
				mRadioInterface.setLocation(location);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
	}

	/**
	 * 上搜索
	 * @param freqStart 搜索的起始位置
	 * 会自动终止上次未完成的搜索任务
	 */
	public void scanUp(int freqStart) {
		if (null != mRadioInterface) {
			try {
				mRadioInterface.scanUp(freqStart);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
	}

	/**
	 * 下搜索
	 * @param freqStart 搜索的起始位置
	 * 会自动终止上次未完成的搜索任务
	 */
	public void scanDown(int freqStart) {
		if (null != mRadioInterface) {
			try {
				mRadioInterface.scanDown(freqStart);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
	}

	/**
	 * 停止搜索
	 */
	public boolean scanStop() {
		if (null != mRadioInterface) {
			try {
				return mRadioInterface.scanStop();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
		return false;
	}

	/**
	 * 全局搜索
	 * 会自动终止上次未完成的搜索任务
	 */
	public void scanAll() {
		if (null != mRadioInterface) {
			try {
				mRadioInterface.scanAll();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
	}

	/**
	 * 上下微调电台
	 */
	public void step(int direction) {
		if (null != mRadioInterface) {
			try {
				mRadioInterface.step(direction);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
	}

	/**
	 * 得到收音机芯片的ID
	 */
	public int getId() {
		if (null != mRadioInterface) {
			try {
				return mRadioInterface.getId();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}

		return -1;
	}

	/**
	 * RDS: 打开或者关闭TA
	 */
	public void selectRdsTa(boolean on) {
		if (mRadioInterface != null) {
			try {
				mRadioInterface.selectRdsTa(on);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
	}

	/**
	 * RDS: 打开或者关闭AF
	 */
	public void selectRdsAf(boolean on) {
		if (mRadioInterface != null) {
			try {
				mRadioInterface.selectRdsAf(on);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
	}

	/**
	 * RDS: 选择节目类型PTY， PTY_UNKNOWN : 取消选择
	 */
	public void selectRdsPty(int pty) {
		if (mRadioInterface != null) {
			try {
				mRadioInterface.selectRdsPty(pty);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
	}

	/**
	 * RDS 选择是否只收听包含TP的节目
	 */
	public void selectRdsTp(boolean on) {
		if (mRadioInterface != null) {
			try {
				mRadioInterface.selectRdsTp(on);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
	}

	/**
	 * 设置当前电台显示名称，一般为重命名的电台或者RDS PS值
	 * 如果有重命名就传入重命名的字符，没有的话就传入PS值，PS没有就传入""
	 * 调用时机：1.改变频率后，2.重命名完毕后，3.收到RDS的PS更新后
	 * @param popup 是否弹出媒体小窗口，上下曲的时候为true，其他情况下为false
	 */
	public void setStationDisplayName(String name, boolean popup) {
		if (mRadioInterface != null) {
			try {
				mRadioInterface.setStationDisplayName(name, popup);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
	}

	/**
	 * 获取收音机播放状态
	 * @return 打开返回true
	 */
	public boolean getIsOpen(){
		return mIsOpen ;
	}

	/**
	 * 选择收音机媒体声音输出区
	 * @param zone {@link IVIMedia.Zone}
	 */
	public void setZone(int zone) {
		mAudioZone = zone;
		if (mRadioInterface != null) {
			try {
				Logcat.d("to " + IVIMedia.Zone.getName(zone));
				mRadioInterface.setZone(zone);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
	}

	/**
	 * 获取收音机声音输出区
	 * @param zone {@link IVIMedia.Zone}
	 */
	public int getZone(int zone) {
		if (mRadioInterface != null) {
			try {
				return mRadioInterface.getZone();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}

		return IVIMedia.Zone.UNKNOWN;
	}

	/**
	 * 打开收音机，同时设置媒体区
	 * @param zone {@link IVIMedia.Zone}
	 */
	public void openInZone(int zone) {
		mIsOpen = true;
		mAudioZone = zone;
		if (mRadioInterface != null) {
			try {
				mRadioInterface.openInZone(mRadioCallback, mContext.getPackageName(), zone);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}

		super.registerCallback(mRadioCallback);
	}

	/**
	 * 获取PS文本
	 * @param freq 指定频率
	 */
	public String getPSText(int freq) {
		String ret = "";
		if (mRadioInterface != null) {
			try {
				ret = mRadioInterface.getPSText(freq);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			Logcat.d("Service not connected");
		}
		return ret;
	}

    /**
     * 获取搜台动作
     * @return {@link IVIRadio.ScanAction}
     */
    public int getScanAction() {
        int ret = IVIRadio.ScanAction.NONE;
        if (mRadioInterface != null) {
            try {
                ret = mRadioInterface.getScanAction();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Logcat.d("Service not connected");
        }
        return ret;
    }
}
