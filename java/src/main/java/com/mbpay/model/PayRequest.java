package com.mbpay.model;

/**
 * 付款请求参数
 */
public class PayRequest {
    private String address; // 收款地址（必填）
    private String orderNo; // 商户订单号（必填）
    private long amount;   // 付款金额（MB，必填，最小单位：100 = 1MB）
    private String remark; // 备注（可选）

    /**
     * @param address 收款地址（必填）
     * @param orderNo 商户订单号（必填）
     * @param amount 付款金额（MB，必填，最小单位：100 = 1MB）
     * @param remark 备注（可选）
     */
    public PayRequest(String address, String orderNo, long amount, String remark) {
        this.address = address;
        this.orderNo = orderNo;
        this.amount = amount;
        this.remark = remark;
    }

    public PayRequest(String address, String orderNo, long amount) {
        this(address, orderNo, amount, "");
    }

    public String getAddress() {
        return address;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public long getAmount() {
        return amount;
    }

    public String getRemark() {
        return remark;
    }
}





