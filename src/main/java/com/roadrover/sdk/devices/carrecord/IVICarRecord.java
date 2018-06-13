package com.roadrover.sdk.devices.carrecord;

/**
 * 功能：行车记录仪常用静态常量和接口
 * 
 */

public class IVICarRecord {

    /***行车记录仪用户按键操作标记***/
    public static class interType{

        /** 菜单 */
        public static final int TYPE_MENU = 1;

        /** 向上 */
        public static final int TYPE_UP = 2;

        /** 向下 */
        public static final int TYPE_DOWN = 3;

        /** 确认 */
        public static final int TYPE_ENSURE = 4;

        /** 模式 */
        public static final int TYPE_MODEL = 5;

        /** 紧急录制 */
        public static final int TYPE_EMERGENCY = 6;
    }

    /***行车记录仪用户按键协议***/
    public static class UserKeyOperation{

        /** 菜单 */
        public static final byte MENU = 0x01;

        /** 向上 */
        public static final byte UP = 0x02;

        /** 向下 */
        public static final byte DOWN = 0x03;

        /** 确认 */
        public static final byte ENSURE = 0x04;

        /** 模式 */
        public static final byte MODEL = 0x05;

        /** 紧急录制 */
        public static final byte EMERGENCY = 0x06;
    }

    /***用户操作状态***/
    public static class OperationType{
        public static final byte KeyOperation = 0x01; //按键
    }


    public interface CarRecordListener{

        /** 点击菜单键 */
        void onClickMenu();

        /** 点击模式键 */
        void onClickModel();

        /** 点击向上键 */
        void onClickUp();

        /** 点击向下键 */
        void onClickDown();

        /** 点击确认键 */
        void onClickEnsure();

        /** 点击视频坐标 */
        void onCLickCoordinate(int x, int y);

        /** 销毁内部注册**/
        void onDestroy();
    }
}
