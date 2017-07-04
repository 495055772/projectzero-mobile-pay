import {
    NativeModules
} from 'react-native';
const MobilePayClient = NativeModules.MobilePayNativeModule;
/**
 * 移动rn支付入口
 */
export default class MobilePay {
    /**
     * 支付宝
     * @param orderInfo 订单信息
     */
    static startAlipay(orderInfo) {
        MobilePayClient.startAlipay(orderInfo);
    }

}