package com.roadrover.sdk.voice;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.roadrover.sdk.BaseManager;
import com.roadrover.services.voice.IVoice;
import com.roadrover.services.voice.IVoiceCallback;

import org.greenrobot.eventbus.Subscribe;

/**
 * 语音管理类 </br>
 * 语音录制流程： </br>
 *    1) 当你发出提示之前就应该调用 startVoice </br>
 *    2) 当你需要录音的时候，调用setMute(true) </br>
 *    3) 录制结束之后，执行语音动作。 </br>
 *    4) 调用setMute(false),endVoice(); </br>
 */

public class VoiceManager extends BaseManager {

    private IVoice mVoiceInterface = null;

    /**
     * 构造函数
     * @param context 上下文
     * @param listener 监听连接是否成功的回调
     */
    public VoiceManager(Context context, ConnectListener listener) {
        super(context, listener, true);
    }

    @Override
    public void disconnect() {
        mVoiceInterface = null;
        mIVoiceCallback = null;
        super.disconnect();
    }

    @Override
    protected String getServiceActionName() {
        return ServiceAction.VOICE_ACTION;
    }

    @Override
    protected void onServiceConnected(IBinder service) {
        mVoiceInterface = IVoice.Stub.asInterface(service);
        if (mVoiceInterface != null) {
            try {
                mVoiceInterface.registerVoiceCallback(mIVoiceCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onServiceDisconnected() {
        mVoiceInterface = null;
    }

    public void play() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.play();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.pause();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void prev() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.prev();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void next() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.next();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 声音控制，声音控制和方控声音控制不一样，方控一下调一格，语音一次可能调N格
    public void incVolume() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.incVolume();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void delVolume() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.decVolume();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setVolume(int volume) {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.setVolume(volume);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setMute(boolean isMute) {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.setMute(isMute);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 音乐接口
    public void selectMediaItem(int position) {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.selectMediaItem(position);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置播放模式
     * @param mode {@link IVIVoice.MediaPlayMode 定义}
     */
    public void setPlayMode(int mode) {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.setPlayMode(mode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void playRandom() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.playRandom();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放媒体
     * @param title 媒体名字
     * @param singer 歌手名字，可以为空
     */
    public void playMedia(String title, String singer) {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.playMedia(title, singer);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void favourMedia(boolean isFavour) {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.favourMedia(isFavour);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 收音机接口
    public void setFreq(int freq) {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.setFreq(freq);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void startScan() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.startScan();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void stopScan() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.stopScan();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置FM 还是 Am
     * @param band 对应 IVIRadio.Band
     */
    public void setBand(int band) {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.setBand(band);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 应用操作接口

    /**
     * 打开应用
     * @param packageName 包名
     * @param className 类名， 可以缺省，缺省打开默认Activity
     */
    public void openApp(String packageName, String className) {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.openApp(packageName, className);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void closeApp(String packageName) {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.closeApp(packageName);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 系统操作相关接口
    public void openScreen() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.openScreen();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void closeScreen() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.closeScreen();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开语音应用
     */
    public void openVoiceApp() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.openVoiceApp();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停其他媒体
     */
    public void startVoice() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.startVoice();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 恢复其他媒体的播放
     */
    public void endVoice() {
        try {
            if (mVoiceInterface != null) {
                mVoiceInterface.endVoice();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册语音回调
     * @param callback
     */
    public void registerVoiceCallback(IVoiceCallback.Stub callback) {
        if (callback != null) {
            mICallbackS.add(callback);
        }
    }

    public void unregisterVoiceCallback(IVoiceCallback.Stub callback) {
        if (callback != null) {
            mICallbackS.remove(callback);
        }
    }

    private IVoiceCallback.Stub mIVoiceCallback = new IVoiceCallback.Stub() {
        @Override
        public void openVoiceApp() throws RemoteException {
            post(new IVIVoice.EventVoiceCallback(IVIVoice.EventVoiceCallback.OPEN_VOICE_APP));
        }
    };

    @Subscribe
    public void onEventCallback(IVIVoice.EventVoiceCallback event) {
        if (event != null) {
            switch (event.mType) {
                case IVIVoice.EventVoiceCallback.OPEN_VOICE_APP:
                    for (IInterface iInterface : mICallbackS) {
                        if (iInterface instanceof IVoiceCallback.Stub) {
                            IVoiceCallback.Stub callback = (IVoiceCallback.Stub) iInterface;
                            if (callback != null) {
                                try {
                                    callback.openVoiceApp();
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }
}
