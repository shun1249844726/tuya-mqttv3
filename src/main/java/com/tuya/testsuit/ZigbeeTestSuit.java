package com.tuya.testsuit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ZigbeeTestSuit {

    static {
        System.loadLibrary("c++_shared");
        System.loadLibrary("tuya_test_suit");
    }

    public final static class Config {
        //存储路径，注意长度，底层为64个char字符长度的数组，还追加了一个文件名，也就是说路径要比64位还要短。
        public String mPath;
        //产品key
        public String mProductKey;

        //uuid和authkey成对出现，而且设备唯一
        public String mUUID;
        public String mAuthKey;

        //固件版本号 & 包名
        public String mVersion;

        //zigbee config
        public String mSerialPort;
        //临时文件目录，注意长度，底层为64个char字符长度的数组，还追加了一个文件名，也就是说路径要比64位还要短。
        public String mTempDir;
        //bin文件目录，勿存放文件，其他平台可能为只读文件系统
        //注意长度，底层为64个char字符长度的数组，还追加了一个文件名，也就是说路径要比64位还要短。
        public String mBinDir;
        //是否带流控
        public boolean mIsCTS;
        public boolean mIsOEM;

        //zigbee dongle的信道
        public int mZigbeeChannel;
        //测试总共发送多少个包。
        public int mPackageCount;
        //测试时长，单位为秒，建议10秒以上。
        public int mTestTime;
    }

    private static final String TAG  = "ZigbeTestSuit";


    public interface OnTestCompletion {
        void onTestResult(final int result);
    }

    public static final int TEST_OK = 0;
    public static final int TEST_VERSION_FAILED = 1;
    public static final int TEST_SEND_FAILED = 2;
    public static final int TEST_RECV_FAILED = 3;
    public static final int TEST_START_FAILED = 4;
    public static final int TEST_PARAM_ERROR = 5;

    private OnTestCompletion mListener = null;
    public void setOnTestCompletion(OnTestCompletion listener) {
        mListener = listener;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(mListener == null) {
                return ;
            }
            mListener.onTestResult(msg.what);
        }
    };

    public void tuyaZigbeeTest(final Config config) {
        Log.i(TAG, "zigbeeTest_start");
        new Thread(new Runnable() {
            @Override
            public void run() {
                int err = native_iotZigbeeTest(config);
                Log.i(TAG, "native start returned:" + err);
                mHandler.sendEmptyMessage(err);
            }
        }).start();
    }


    private native final int native_iotZigbeeTest(Config config);
}
