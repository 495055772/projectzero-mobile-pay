package com.rnpay;

import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import android.util.Log;
import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.annotation.SuppressLint;
import android.widget.Toast;
import java.util.Map;
import com.facebook.react.bridge.Arguments;
public class MobilePayNativeModule extends ReactContextBaseJavaModule {
    private static final int    ALI_PAY_FLAG = 0x10;
    public static final  String NAME         = "MobilePayNativeModule";
    private ReactApplicationContext mContext;

    public MobilePayNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.mContext = reactContext;
    }

    @ReactMethod
    public void startAlipay(String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(mContext);
                Map<String, String> payResult = alipay.payV2(orderInfo, true);
                Log.i("dale", payResult.toString());
                String version = payTask.getVersion();
                Log.i("dale", "支付task version-->" + version);
                Message msg = new Message();
                msg.what = ALI_PAY_FLAG;
                msg.obj = payResult;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case ALI_PAY_FLAG:
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                WritableMap params = Arguments.createMap();
                params.put("resultStatus", resultStatus);
                params.put("resultInfo", resultInfo);
                params.put("payType", "alipay");//支付类型
                sendEvent(params);
                break;
            default:
                break;
            }
        }
    };

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * 支付结果返回rn层
     *
     * @param params
     */
    public void sendEvent(WritableMap params) {
        mContext.getJSModule(RCTNativeAppEventEmitter.class)
                .emit("payResult", params);
    }

}