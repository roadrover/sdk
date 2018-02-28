package com.roadrover.sdk.system;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.RecoverySystem;
import android.text.TextUtils;

import com.roadrover.sdk.BaseManager;
import com.roadrover.sdk.car.CarManager;
import com.roadrover.sdk.system.IVISystem.UpgradeStatus;
import com.roadrover.sdk.system.IVISystem.ProgressListener;
import com.roadrover.sdk.utils.Logcat;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.GeneralSecurityException;

/**
 * 系统升级类.
 */

public class SystemUpgrade implements BaseManager.ConnectListener {

    public static final String CHCHE_PARTITION = "/cache/";

    public static final String DEFAULT_PACKAGE_NAME = "update.zip";

    private static final int STATUS = 0; // handler status
    private static final int PROGRESS = 1; // handler progress

    // handler
    private static class SafeHandler extends Handler {
        WeakReference<SystemUpgrade> mReference;

        SafeHandler(SystemUpgrade upgrade) {
            mReference = new WeakReference<SystemUpgrade>(upgrade);
        }

        @Override
        public void handleMessage(Message msg) {
            final SystemUpgrade upgrade = mReference.get();
            if (upgrade != null) {
                switch (msg.what) {
                    case STATUS:
                        if (null != upgrade.mProgressListener) {
                            upgrade.mProgressListener.onUpgradeStatus(msg.arg1, (String) msg.obj);
                        }
                        break;
                    case PROGRESS:
                        if (null != upgrade.mProgressListener) {
                            upgrade.mProgressListener.onProgress(msg.arg1);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    // handler对象
    private SafeHandler mHandler = null;

    // system manager对象
    private SystemManager mSystemManager = null;

    //car manager 对象
    private CarManager mCarManager = null;

    /**
     * 文件升级线程
     */
    private class UpgradeTask extends AsyncTask<File, Integer, Integer> {
        @Override
        protected Integer doInBackground(File... params) {
            int ret = UpgradeStatus.SUCCESS_REBOOT;
            if (null != params) {
                if (params.length > 0) {

                    // 进行重启mute
                    if (null != mSystemManager) {
                        mSystemManager.resetMute();
                    }

                    if (null != mCarManager) {
                        mCarManager.pauseHeartbeat();
                    }

                    // 进行系统升级文件校验
                    try {
                        RecoverySystem.verifyPackage(params[0], mListener, null);
                    } catch (IOException e) {
                        ret = UpgradeStatus.ERROR_FILE_IO_ERROR;
                        e.printStackTrace();
                    } catch (GeneralSecurityException e) {
                        ret = UpgradeStatus.ERROR_INVALID_UPGRADE_PACKAGE;
                        e.printStackTrace();
                    }
                } else {
                    post(UpgradeStatus.ERROR_INPUT_PARAMS);
                }
            } else {
                post(UpgradeStatus.ERROR_INPUT_PARAMS);
            }
            return ret;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);

            // 通知用户升级状态
            if (null != mProgressListener) {
                mProgressListener.onUpgradeStatus(s, mUpgradeFilePath);
            }

            // 校验成功，则进行安装
            if (UpgradeStatus.SUCCESS_REBOOT == s) {
                installPackage(mUpgradeFilePath);
            }

            // GC回收
            mUpgradeTask = null;
        }
    }

    private Context mContext = null;// 上下文对象

    private String mUpgradeFilePath = null;// 升级文件路径

    private RecoverySystem.ProgressListener mListener = new RecoverySystem.ProgressListener() {
        @Override
        public void onProgress(int progress) {
            if (null != mHandler) {
                Message message = Message.obtain();
                message.what = PROGRESS;
                message.arg1 = progress;
                mHandler.sendMessage(message);
            }
        }
    };

    private ProgressListener mProgressListener = null;//升级信息监听器

    private UpgradeTask mUpgradeTask = null;//升级线程

    protected SystemUpgrade() {

    }

    public SystemUpgrade(Context context) {
        mContext = context;

        mHandler = new SafeHandler(this);

        mCarManager = new CarManager(mContext, null, null, false);
    }

    public void release() {
        if (mCarManager != null) {
            mCarManager.disconnect();
            mCarManager = null;
        }
    }

    @Override
    public void onServiceConnected() {
        executeUpgradeTaskASync();
    }

    @Override
    public void onServiceDisconnected() {

    }

    /**
     * 系统升级
     *
     * @param filePath 升级文件
     * @param listener 升级状态，见{@link UpgradeStatus}
     * @return true表示执行升级动作成功，false表示升级校验动作失败
     */
    public boolean upgradePackage(String filePath, ProgressListener listener) {
        return verifyPackage(filePath, listener);
    }

    /**
     * 取消系统升级
     *
     * @return true成功打断升级，false打断失败或升级动作未执行
     */
    public boolean cancelUpgradePackage() {
        boolean ret = false;
        if (null != mHandler) {
            mHandler.removeMessages(STATUS);
            mHandler.removeMessages(PROGRESS);
        }
        if (null != mUpgradeTask) {
            ret = mUpgradeTask.cancel(true);
            mUpgradeTask = null;
        }
        return ret;
    }

    /**
     * 校验MD5
     *
     * @param filepath 传入路径
     * @return true表示执行校验动作成功，false表示执行校验动作失败
     */
    protected boolean verifyPackage(String filepath, ProgressListener listener) {
        boolean ret = false;
        mProgressListener = listener;
        if (null != mUpgradeTask) {
            post(UpgradeStatus.WAIT_UPGRADE);
        } else {
            if (null != mContext) {
                if (!TextUtils.isEmpty(filepath)) {
                    File file = new File(filepath);
                    if (file.exists()) {
                        ret = true;
                        mUpgradeFilePath = filepath;

                        if (null != mSystemManager) {
                            if (mSystemManager.isConnected()) {
                                executeUpgradeTaskASync();
                            } else {
                                Logcat.d("Wait connect to SystemManager connect.");
                            }
                        } else {
                            mSystemManager = new SystemManager(mContext, this);
                        }

                        post(UpgradeStatus.UPGRADING);
                    } else {
                        post(UpgradeStatus.ERROR_FILE_DOES_NOT_EXIST);
                    }
                } else {
                    post(UpgradeStatus.ERROR_FILE_DOES_NOT_EXIST);
                }
            } else {
                post(UpgradeStatus.ERROR_INPUT_PARAMS);
            }
        }
        return ret;
    }

    /**
     * 异步执行升级任务
     */
    protected void executeUpgradeTaskASync() {
        mUpgradeTask = new UpgradeTask();
        mUpgradeTask.execute(new File(mUpgradeFilePath));
    }

    /**
     * 安装文件
     *
     * @param filepath 升级文件路径
     * @return true表示安装校验动作成功，false表示安装校验动作失败
     */
    protected boolean installPackage(String filepath) {
        boolean ret = false;
        if (null != mContext) {
            if (!TextUtils.isEmpty(filepath)) {
                File file = new File(filepath);
                if (file.exists()) {
                    ret = true;

                    try {
                        RecoverySystem.installPackage(mContext, file);
                    } catch (IOException e) {
                        post(UpgradeStatus.ERROR_FILE_IO_ERROR);
                        e.printStackTrace();
                    }
                } else {
                    post(UpgradeStatus.ERROR_FILE_DOES_NOT_EXIST);
                }
            } else {
                post(UpgradeStatus.ERROR_FILE_DOES_NOT_EXIST);
            }
        } else {
            post(UpgradeStatus.ERROR_INPUT_PARAMS);
        }
        return ret;
    }

    /**
     * 发送升级状态
     *
     * @param status 升级状态，见{@link UpgradeStatus}
     */
    protected void post(int status) {
        if (null != mHandler) {
            Message message = Message.obtain();
            message.what = STATUS;
            message.arg1 = status;
            message.obj = mUpgradeFilePath;
            mHandler.sendMessage(message);
        }
    }
}
