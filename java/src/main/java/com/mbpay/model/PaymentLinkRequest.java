package com.mbpay.model;

/**
 * 支付链接生成请求参数
 */
public class PaymentLinkRequest {
    private String orderNo;   // 商户订单号（必填）
    private String subject;   // 商品描述（必填）
    private long amount;      // 订单金额（分，必填）
    private int expire;       // 过期时间（分钟，必填）
    private String nonce;     // 随机数（可选，不填会自动生成）
    private String notifyUrl; // 回调通知地址（可选）

    /**
     * @param orderNo 商户订单号（必填）
     * @param subject 商品描述（必填）
     * @param amount 订单金额（分，必填）
     * @param expire 过期时间（分钟，必填）
     * @param nonce 随机数（可选，不填会自动生成）
     * @param notifyUrl 回调通知地址（可选）
     */
    public PaymentLinkRequest(String orderNo, String subject, long amount, int expire, String nonce, String notifyUrl) {
        this.orderNo = orderNo;
        this.subject = subject;
        this.amount = amount;
        this.expire = expire;
        this.nonce = nonce;
        this.notifyUrl = notifyUrl;
    }

    public PaymentLinkRequest(String orderNo, String subject, long amount, int expire) {
        this(orderNo, subject, amount, expire, null, null);
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getSubject() {
        return subject;
    }

    public long getAmount() {
        return amount;
    }

    public int getExpire() {
        return expire;
    }

    public String getNonce() {
        return nonce;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }
}






