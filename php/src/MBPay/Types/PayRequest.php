<?php

namespace MBPay\Types;

/**
 * 付款请求参数
 */
class PayRequest
{
    /**
     * @var string 收款地址（必填）
     */
    private $address;

    /**
     * @var string 商户订单号（必填）
     */
    private $orderNo;

    /**
     * @var int 付款金额（MB，必填，最小单位：100 = 1MB）
     */
    private $amount;

    /**
     * @var string 备注（可选）
     */
    private $remark;

    /**
     * @param string $address 收款地址（必填）
     * @param string $orderNo 商户订单号（必填）
     * @param int $amount 付款金额（MB，必填，最小单位：100 = 1MB）
     * @param string $remark 备注（可选）
     */
    public function __construct(string $address, string $orderNo, int $amount, string $remark = '')
    {
        $this->address = $address;
        $this->orderNo = $orderNo;
        $this->amount = $amount;
        $this->remark = $remark;
    }

    /**
     * 获取收款地址
     *
     * @return string
     */
    public function getAddress(): string
    {
        return $this->address;
    }

    /**
     * 获取商户订单号
     *
     * @return string
     */
    public function getOrderNo(): string
    {
        return $this->orderNo;
    }

    /**
     * 获取付款金额
     *
     * @return int
     */
    public function getAmount(): int
    {
        return $this->amount;
    }

    /**
     * 获取备注
     *
     * @return string
     */
    public function getRemark(): string
    {
        return $this->remark;
    }
}





