/**
 * 使用示例
 */

import { Client, PayRequest, PaymentLinkRequest, MBPayError, ErrorCode } from './src';

async function example() {
    // 创建客户端
    const client = new Client(
        'https://www.mbpay.world',  // API 基础地址
        'your_api_key_here',        // App ID
        'your_api_key_here'         // App Secret
    );

    // 示例1：查询商户余额
    try {
        const balance = await client.getBalance();
        console.log(`可用余额: ${balance.getBalance()} MB, 冻结余额: ${balance.getFrozen()} MB`);
    } catch (error) {
        if (error instanceof MBPayError) {
            console.log(`查询余额失败: ${error.message}`);
        } else {
            console.log(`查询余额失败: ${error}`);
        }
        return;
    }

    // 示例2：商户付款给用户
    const payReq = new PayRequest(
        'Txxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx',  // 收款地址
        'PAY202501011200001234567890',         // 商户订单号
        100,                                    // 付款金额（MB，100 = 1MB）
        '商户付款'                               // 备注（可选）
    );

    try {
        const payResp = await client.pay(payReq);
        console.log('付款成功！');
        console.log(`平台订单号: ${payResp.getPlatformOrderNo()}`);
        console.log(`实际支付金额: ${payResp.getActualAmount()} MB`);
        console.log(`手续费: ${payResp.getFee()} MB`);
        console.log(`商户剩余余额: ${payResp.getBalance()} MB`);
    } catch (error) {
        if (error instanceof MBPayError) {
            if (error.getErrorCode() === ErrorCode.INSUFFICIENT_BALANCE) {
                console.log('余额不足');
            } else if (error.getErrorCode() === ErrorCode.ORDER_EXISTS_OR_ADDR_NOT_FOUND) {
                console.log('订单号已存在或收款地址不存在');
            } else {
                console.log(`付款失败 [错误码: ${error.getErrorCode()}]: ${error.message}`);
            }
        } else {
            console.log(`付款失败: ${error}`);
        }
        return;
    }

    // 示例3：生成支付链接（用于生成支付二维码）
    const linkReq = new PaymentLinkRequest(
        'ORD202501011200001234567890',        // 商户订单号
        '购买VIP，1个月',                       // 商品描述
        1000,                                  // 订单金额（分）
        15,                                    // 过期时间（分钟）
        undefined,                             // 随机数（可选，不填会自动生成）
        'https://your-domain.com/notify'      // 回调通知地址（可选）
    );

    try {
        const paymentLink = await client.generatePaymentLink(linkReq);
        console.log(`支付链接: ${paymentLink}`);
        console.log('可以将此链接用于生成二维码');
    } catch (error) {
        if (error instanceof MBPayError) {
            console.log(`生成支付链接失败: ${error.message}`);
        } else {
            console.log(`生成支付链接失败: ${error}`);
        }
    }
}

// 运行示例
example().catch(console.error);

