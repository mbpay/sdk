<?php

namespace MBPay\Types;

/**
 * 余额查询响应
 */
class BalanceResponse
{
    /**
     * @var int 可用余额（MB）
     */
    private $balance;

    /**
     * @var int 冻结余额（MB）
     */
    private $frozen;

    /**
     * @param int $balance
     * @param int $frozen
     */
    public function __construct(int $balance, int $frozen)
    {
        $this->balance = $balance;
        $this->frozen = $frozen;
    }

    /**
     * 获取可用余额
     *
     * @return int
     */
    public function getBalance(): int
    {
        return $this->balance;
    }

    /**
     * 获取冻结余额
     *
     * @return int
     */
    public function getFrozen(): int
    {
        return $this->frozen;
    }
}





