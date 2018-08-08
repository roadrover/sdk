package com.roadrover.sdk.car;

/**
 * 设置硬件版本号类
 * 1. 启动后，应用自动在U盘中检索pcb_version.txt文件，并校验项目编码是否跟当前系统版本匹配，如匹配则通过CMD_HW_VER下发给MCU，并开启一个2秒的定时器
 * 2. MCU把版本信息写入EEPROM，并立即从EEPROM回读
 * 3. 若2秒内收到MCU反馈，上位机弹出提示框，显示“硬件版本信息写入成功，当前版本x.x”或“硬件版本信息写入失败，MCU存储异常”
 * 4．若2秒定时器超时仍未收到MCU反馈，则弹出提示框显示“硬件版本信息写入失败，MCU无响应”
 * 5．每次开机时，MCU从EEPROM读出版本号发送给上位机（间隔3秒发送10次），如读取不到记录则不发送
 * 6. 上位机根据MCU发来的版本号显示，如未收到则不显示
 */

import com.roadrover.sdk.utils.ByteUtil;
import com.roadrover.sdk.utils.Logcat;

public class HardwareVersion {
    private String TAG = "HardwareVersion";

    public byte[] mData;
    public int status;                // 写入硬件版本号返回状态  0x04表示MCU写入失败（可能EEPROM故障等） 0x05表示MCU写入成功
    public String mHardwareVersion;   // 硬件版本号
    public String mSupplier;          // PCB版本
    public String mEcnCode;           // ECN/DCN编码
    public String mDate;              // SMT日期
    public String mManufactureDate;   //机器生产日期
    public boolean mIsHardwareWriteFeedback; // 是否收到MCU反馈

    public HardwareVersion(int status, String hardware, String supplier, String ecn, String date, String manufactureDate) {
        this.status = status;
        this.mHardwareVersion = hardware;
        this.mSupplier = supplier;
        this.mEcnCode = ecn;
        this.mDate = date;
        this.mManufactureDate = manufactureDate;
        this.mIsHardwareWriteFeedback = false;
    }

    public HardwareVersion(byte[] data) {
        this.mIsHardwareWriteFeedback = true;
        this.mData = data;
        parseData(mData);
    }

    /**
     * 根据下位机返回的数据进行解析
     * 1. data[0] 表示车机主控板
     * 2. data[1] 操作或状态 0x05表示成功  0x04表示失败
     * 3. data[2] PCB版本号 例如返回 3，需显示0.3
     * 4. data[3] PCB供应商
     * 5. data[4] - data[6] ECN/DCN编号  将data[4]+ data[5] +data[6]拼接一起
     * 6. data[7] - data[9] SMT日期  将data[7] 表示年的后两位 data[8]表示月 data[9]表示日，通过.拼接成年.月.日
     * 7. data[10] - data[12] 机器生产日期  将data[10] 表示年的后两位 data[11]表示月 data[12]表示日，通过.拼接成年.月.日
     * 8. 例如 Data = 00 05 07 0c 01 48 17 12 01 17 12 01 0d
     *    a. 則data[1] = 0x05，表示成功
     *    b. data[2] = 0x07,  PCB版本号为7，显示0.7
     *    c. data[3] = 0x0c,  PCB供应商为12
     *    d. 将data[4] + data[5] + data[6]拼接为: 17223
     *    e. 将data[7] + data[8] + data[9]拼接为: 2018.1.13
     *    f. 将data[10] + data[11] + data[12]拼接为: 2018.1.13
     */
    private void parseData(byte[] data) {
        Logcat.d("data = " + ByteUtil.bytesToString(ByteUtil.subBytes(data, 0, data.length)));
        if (data != null && data.length > 0) {
            status = (int) data[1];
            if (data.length > 2) {
                mHardwareVersion = String.format("%.1f", (data[2] / 10f));// PCB版本号（一位小数）
            }

            if (data.length > 3) {
                mSupplier = String.format("%02d", data[3]);
            }

            if (data.length > 6) {
                mEcnCode = String.format("%02d", data[4]) + String.format("%02d", data[5]) + String.format("%02d", data[6]);
            }

            if (data.length > 9) {
                mDate = getYear(data[7]) + getMonth(data[8]) + getDay(data[9]);
            }

            if (data.length > 12) {
                mManufactureDate = getYear(data[10]) + getMonth(data[11]) + getDay(data[12]);
            }
            Logcat.d(TAG, "status = " + status + "  mHardwareVersion = " + mHardwareVersion
                    + " mSupplier = " + mSupplier + " mEcnCode = " + mEcnCode + "  mDate = " + mDate + " mManufactureDate = " +mManufactureDate);
        }
    }

    /**
     * 获取年
     * @param year
     * @return
     */
    public String getYear(byte year) {
        return "20" + String.format("%02d", year);
    }

    /**
     * 获取月
     * @param month
     * @return
     */
    public String getMonth(byte month) {
        return String.format("%02d", month);
    }

    /**
     * 获取天
     * @param day
     * @return
     */
    public String getDay(byte day) {
        return String.format("%02d", day);
    }

}
