package mbpay

import (
	"fmt"
	"net/http"
)

// PayRequest 付款请求参数
type PayRequest struct {
	Address string // 收款地址（必填）
	OrderNo string // 商户订单号（必填）
	Amount  int64  // 付款金额（MB，必填，最小单位：100 = 1MB）
	Remark  string // 备注（可选）
}

// Pay 商户向用户地址付款
func (c *Client) Pay(req *PayRequest) (*PayResponse, error) {
	// 参数验证
	if req.Address == "" {
		return nil, fmt.Errorf("address is required")
	}
	if req.OrderNo == "" {
		return nil, fmt.Errorf("order_no is required")
	}
	if req.Amount <= 0 {
		return nil, fmt.Errorf("amount must be greater than 0")
	}

	// 构建请求参数
	params := map[string]string{
		"address":  req.Address,
		"order_no": req.OrderNo,
		"amount":   fmt.Sprintf("%d", req.Amount),
	}
	if req.Remark != "" {
		params["remark"] = req.Remark
	}

	// 执行请求
	resp, err := c.doRequest(http.MethodPost, "/merchant/pay", params)
	if err != nil {
		return nil, err
	}

	// 检查业务错误
	if !resp.IsSuccess() {
		return nil, resp.ToError()
	}

	// 解析 data 字段
	payResp := &PayResponse{}

	if platformOrderNo, ok := resp.Data["platform_order_no"].(string); ok {
		payResp.PlatformOrderNo = platformOrderNo
	} else {
		return nil, fmt.Errorf("invalid platform_order_no format in response")
	}

	if actualAmount, ok := resp.Data["actual_amount"].(float64); ok {
		payResp.ActualAmount = int64(actualAmount)
	} else if actualAmount, ok := resp.Data["actual_amount"].(int64); ok {
		payResp.ActualAmount = actualAmount
	} else if actualAmount, ok := resp.Data["actual_amount"].(int); ok {
		payResp.ActualAmount = int64(actualAmount)
	} else {
		return nil, fmt.Errorf("invalid actual_amount format in response")
	}

	if fee, ok := resp.Data["fee"].(float64); ok {
		payResp.Fee = int64(fee)
	} else if fee, ok := resp.Data["fee"].(int64); ok {
		payResp.Fee = fee
	} else if fee, ok := resp.Data["fee"].(int); ok {
		payResp.Fee = int64(fee)
	} else {
		payResp.Fee = 0 // 默认为 0
	}

	if balance, ok := resp.Data["balance"].(float64); ok {
		payResp.Balance = int64(balance)
	} else if balance, ok := resp.Data["balance"].(int64); ok {
		payResp.Balance = balance
	} else if balance, ok := resp.Data["balance"].(int); ok {
		payResp.Balance = int64(balance)
	} else {
		payResp.Balance = 0 // 默认为 0
	}

	return payResp, nil
}





