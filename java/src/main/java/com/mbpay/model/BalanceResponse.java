package com.mbpay.model;

/**
 * 余额查询响应
 */
public class BalanceResponse {
    private long balance; // 可用余额（MB）
    private long frozen;  // 冻结余额（MB）

    public BalanceResponse(long balance, long frozen) {
        this.balance = balance;
        this.frozen = frozen;
    }

    public long getBalance() {
        return balance;
    }

    public long getFrozen() {
        return frozen;
    }
}






