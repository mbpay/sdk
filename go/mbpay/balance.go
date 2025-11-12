package mbpay

import (
	"fmt"
	"net/http"
)

// GetBalance 获取商户余额
func (c *Client) GetBalance() (*BalanceResponse, error) {
	params := make(map[string]string)

	resp, err := c.doRequest(http.MethodPost, "/merchant/balance", params)
	if err != nil {
		return nil, err
	}

	// 检查业务错误
	if !resp.IsSuccess() {
		return nil, resp.ToError()
	}

	// 解析 data 字段
	balanceResp := &BalanceResponse{}

	if balance, ok := resp.Data["balance"].(float64); ok {
		balanceResp.Balance = int64(balance)
	} else if balance, ok := resp.Data["balance"].(int64); ok {
		balanceResp.Balance = balance
	} else if balance, ok := resp.Data["balance"].(int); ok {
		balanceResp.Balance = int64(balance)
	} else {
		return nil, fmt.Errorf("invalid balance format in response")
	}

	if frozen, ok := resp.Data["frozen"].(float64); ok {
		balanceResp.Frozen = int64(frozen)
	} else if frozen, ok := resp.Data["frozen"].(int64); ok {
		balanceResp.Frozen = frozen
	} else if frozen, ok := resp.Data["frozen"].(int); ok {
		balanceResp.Frozen = int64(frozen)
	} else {
		balanceResp.Frozen = 0 // 默认为 0
	}

	return balanceResp, nil
}






