import {
    NativeModules
} from 'react-native';
const MobileAliPayClient = NativeModules.AliPayNativeModule;
/**
 * 移动rn支付入口
 */
export default class MobileAliPay {
    /**
     * 支付宝
     * @param orderInfo 订单信息
     */
    static startAlipay(orderInfo) {
        MobileAliPayClient.startAlipay(orderInfo);
    }

}