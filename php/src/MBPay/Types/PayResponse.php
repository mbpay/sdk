<?php

namespace MBPay\Types;

/**
 * 付款响应
 */
class PayResponse
{
    /**
     * @var string 平台订单号
     */
    private $platformOrderNo;

    /**
     * @var int 实际支付金额（MB）
     */
    private $actualAmount;

    /**
     * @var int 手续费（MB）
     */
    private $fee;

    /**
     * @var int 商户剩余余额（MB）
     */
    private $balance;

    /**
     * @param string $platformOrderNo
     * @param int $actualAmount
     * @param int $fee
     * @param int $balance
     */
    public function __construct(string $platformOrderNo, int $actualAmount, int $fee, int $balance)
    {
        $this->platformOrderNo = $platformOrderNo;
        $this->actualAmount = $actualAmount;
        $this->fee = $fee;
        $this->balance = $balance;
    }

    /**
     * 获取平台订单号
     *
     * @return string
     */
    public function getPlatformOrderNo(): string
    {
        return $this->platformOrderNo;
    }

    /**
     * 获取实际支付金额
     *
     * @return int
     */
    public function getActualAmount(): int
    {
        return $this->actualAmount;
    }

    /**
     * 获取手续费
     *
     * @return int
     */
    public function getFee(): int
    {
        return $this->fee;
    }

    /**
     * 获取商户剩余余额
     *
     * @return int
     */
    public function getBalance(): int
    {
        return $this->balance;
    }
}






