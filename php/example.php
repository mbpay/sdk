<?php

require_once __DIR__ . '/vendor/autoload.php';

use MBPay\Client;
use MBPay\Types\PayRequest;
use MBPay\Types\PaymentLinkRequest;
use MBPay\Exception\MBPayException;
use MBPay\Exception\ErrorCode;

// 创建客户端
$client = new Client(
    'https://www.mbpay.world',  // API 基础地址
    'your_api_key_here',        // App ID
    'your_api_key_here'         // App Secret
);

// 示例1：查询商户余额
try {
    $balance = $client->getBalance();
    echo "可用余额: {$balance->getBalance()} MB, 冻结余额: {$balance->getFrozen()} MB\n";
} catch (MBPayException $e) {
    echo "查询余额失败: {$e->getMessage()}\n";
    exit;
}

// 示例2：商户付款给用户
$payReq = new PayRequest(
    'Txxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx',  // 收款地址
    'PAY202501011200001234567890',         // 商户订单号
    100,                                    // 付款金额（MB，100 = 1MB）
    '商户付款'                               // 备注（可选）
);

try {
    $payResp = $client->pay($payReq);
    echo "付款成功！\n";
    echo "平台订单号: {$payResp->getPlatformOrderNo()}\n";
    echo "实际支付金额: {$payResp->getActualAmount()} MB\n";
    echo "手续费: {$payResp->getFee()} MB\n";
    echo "商户剩余余额: {$payResp->getBalance()} MB\n";
} catch (MBPayException $e) {
    if ($e->getErrorCode() === ErrorCode::INSUFFICIENT_BALANCE) {
        echo "余额不足\n";
    } elseif ($e->getErrorCode() === ErrorCode::ORDER_EXISTS_OR_ADDR_NOT_FOUND) {
        echo "订单号已存在或收款地址不存在\n";
    } else {
        echo "付款失败 [错误码: {$e->getErrorCode()}]: {$e->getMessage()}\n";
    }
    exit;
}

// 示例3：生成支付链接（用于生成支付二维码）
$linkReq = new PaymentLinkRequest(
    'ORD202501011200001234567890',        // 商户订单号
    '购买VIP，1个月',                       // 商品描述
    1000,                                  // 订单金额（分）
    15,                                    // 过期时间（分钟）
    null,                                  // 随机数（可选，不填会自动生成）
    'https://your-domain.com/notify'      // 回调通知地址（可选）
);

try {
    $paymentLink = $client->generatePaymentLink($linkReq);
    echo "支付链接: {$paymentLink}\n";
    echo "可以将此链接用于生成二维码\n";
} catch (MBPayException $e) {
    echo "生成支付链接失败: {$e->getMessage()}\n";
}

