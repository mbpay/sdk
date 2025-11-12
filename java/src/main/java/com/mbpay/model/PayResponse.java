package com.mbpay.model;

/**
 * 付款响应
 */
public class PayResponse {
    private String platformOrderNo; // 平台订单号
    private long actualAmount;      // 实际支付金额（MB）
    private long fee;               // 手续费（MB）
    private long balance;           // 商户剩余余额（MB）

    public PayResponse(String platformOrderNo, long actualAmount, long fee, long balance) {
        this.platformOrderNo = platformOrderNo;
        this.actualAmount = actualAmount;
        this.fee = fee;
        this.balance = balance;
    }

    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public long getActualAmount() {
        return actualAmount;
    }

    public long getFee() {
        return fee;
    }

    public long getBalance() {
        return balance;
    }
}






