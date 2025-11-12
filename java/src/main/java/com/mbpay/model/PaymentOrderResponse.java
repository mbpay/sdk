package com.mbpay.model;

/**
 * 生成支付订单响应
 */
public class PaymentOrderResponse {
    private String paymentLink; // 收银台页面链接

    /**
     * @param paymentLink 收银台页面链接
     */
    public PaymentOrderResponse(String paymentLink) {
        this.paymentLink = paymentLink;
    }

    public String getPaymentLink() {
        return paymentLink;
    }

    @Override
    public String toString() {
        return "PaymentOrderResponse{" +
                "paymentLink='" + paymentLink + '\'' +
                '}';
    }
}

