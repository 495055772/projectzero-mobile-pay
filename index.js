import {
    NativeModules
} from 'react-native';
const MobilePay = NativeModules.MobilePayNatvieModule;
/**
 * 移动rn支付入口
 */
export default class MobilePay {
    /**
     * 支付宝
     * @param orderInfo 订单信息
     */
    static startAlipay(orderInfo) {
        MobilePay.startAlipay(orderInfo);
    }

}