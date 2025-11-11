package com.mbpay;

import com.mbpay.exception.ErrorCode;
import com.mbpay.exception.MBPayException;
import com.mbpay.model.BalanceResponse;
import com.mbpay.model.PayRequest;
import com.mbpay.model.PayResponse;
import com.mbpay.model.PaymentLinkRequest;

/**
 * 使用示例
 */
public class Example {
    public static void main(String[] args) {
        // 创建客户端
        Client client = new Client(
                "https://www.mbpay.world",  // API 基础地址
                "your_api_key_here",         // App ID
                "your_api_key_here"          // App Secret
        );

        // 示例1：查询商户余额
        try {
            BalanceResponse balance = client.getBalance();
            System.out.println("可用余额: " + balance.getBalance() + " MB, 冻结余额: " + balance.getFrozen() + " MB");
        } catch (MBPayException e) {
            System.out.println("查询余额失败: " + e.getMessage());
            return;
        }

        // 示例2：商户付款给用户
        PayRequest payReq = new PayRequest(
                "Txxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",  // 收款地址
                "PAY202501011200001234567890",         // 商户订单号
                100,                                    // 付款金额（MB，100 = 1MB）
                "商户付款"                               // 备注（可选）
        );

        try {
            PayResponse payResp = client.pay(payReq);
            System.out.println("付款成功！");
            System.out.println("平台订单号: " + payResp.getPlatformOrderNo());
            System.out.println("实际支付金额: " + payResp.getActualAmount() + " MB");
            System.out.println("手续费: " + payResp.getFee() + " MB");
            System.out.println("商户剩余余额: " + payResp.getBalance() + " MB");
        } catch (MBPayException e) {
            if (e.getErrorCode() == ErrorCode.INSUFFICIENT_BALANCE) {
                System.out.println("余额不足");
            } else if (e.getErrorCode() == ErrorCode.ORDER_EXISTS_OR_ADDR_NOT_FOUND) {
                System.out.println("订单号已存在或收款地址不存在");
            } else {
                System.out.println("付款失败 [错误码: " + e.getErrorCode() + "]: " + e.getMessage());
            }
            return;
        }

        // 示例3：生成支付链接（用于生成支付二维码）
        PaymentLinkRequest linkReq = new PaymentLinkRequest(
                "ORD202501011200001234567890",        // 商户订单号
                "购买VIP，1个月",                       // 商品描述
                1000,                                  // 订单金额（分）
                15,                                    // 过期时间（分钟）
                null,                                  // 随机数（可选，不填会自动生成）
                "https://your-domain.com/notify"      // 回调通知地址（可选）
        );

        try {
            String paymentLink = client.generatePaymentLink(linkReq);
            System.out.println("支付链接: " + paymentLink);
            System.out.println("可以将此链接用于生成二维码");
        } catch (MBPayException e) {
            System.out.println("生成支付链接失败: " + e.getMessage());
        }
    }
}

