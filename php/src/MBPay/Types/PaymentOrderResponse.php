<?php

namespace MBPay\Types;

/**
 * 生成支付订单响应
 */
class PaymentOrderResponse
{
    private $paymentLink; // 收银台页面链接

    /**
     * @param string $paymentLink 收银台页面链接
     */
    public function __construct($paymentLink)
    {
        $this->paymentLink = $paymentLink;
    }

    public function getPaymentLink()
    {
        return $this->paymentLink;
    }
}

