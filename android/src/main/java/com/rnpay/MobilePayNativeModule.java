package com.rnpay;

import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import android.util.Log;
import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;
import android.text.TextUtils;
import android.annotation.SuppressLint;
import android.widget.Toast;

import java.util.Map;

import android.content.Context;
import com.facebook.react.bridge.Arguments;
import android.app.Activity;
import com.facebook.react.bridge.ReactMethod;

public class MobilePayNativeModule extends ReactContextBaseJavaModule {
    public static final String NAME = "MobilePayNativeModule";
    private ReactApplicationContext reactContext;

    public MobilePayNativeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @ReactMethod
    @SuppressWarnings("unused")
    public void startAlipay(final String orderInfo) {
        Activity currentActivity = getCurrentActivity();
        PayTask alipay = new PayTask(currentActivity);
        Map<String, String> payResult = alipay.payV2(orderInfo, true);
        Log.i("dale", payResult.toString());
        String version = alipay.getVersion();
        Log.i("dale", "支付task version-->" + version);
        PayResult backResult = new PayResult(payResult);
        /**
         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
         */
        String resultInfo = backResult.getResult();// 同步返回需要验证的信息
        String resultStatus = backResult.getResultStatus();
        // 判断resultStatus 为9000则代表支付成功
        WritableMap params = Arguments.createMap();
        params.putString("resultStatus", resultStatus);
        params.putString("resultInfo", resultInfo);
        params.putString("payType", "alipay");//支付类型
        sendEvent(params);
    }

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
        reactContext.getJSModule(RCTNativeAppEventEmitter.class)
                .emit("payResult", params);
    }

}