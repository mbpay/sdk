package mbpay

import (
	"fmt"
	"log"
)

// Example 使用示例
func Example() {
	// 创建客户端
	client := NewClient(
		"https://www.mbpay.xxx", // API 基础地址
		"your_api_key_here",     // App ID
		"your_api_key_here",     // App Secret
	)

	// 示例1：查询商户余额
	balance, err := client.GetBalance()
	if err != nil {
		log.Printf("查询余额失败: %v", err)
		return
	}
	fmt.Printf("可用余额: %d MB, 冻结余额: %d MB\n", balance.Balance, balance.Frozen)

	// 示例2：商户付款给用户
	payReq := &PayRequest{
		Address: "Txxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", // 收款地址
		OrderNo: "PAY202501011200001234567890",        // 商户订单号
		Amount:  100,                                  // 付款金额（MB，100 = 1MB）
		Remark:  "商户付款",                               // 备注（可选）
	}

	payResp, err := client.Pay(payReq)
	if err != nil {
		// 检查是否是业务错误
		if mbpayErr, ok := err.(*Error); ok {
			log.Printf("付款失败 [错误码: %d]: %s", mbpayErr.Code, mbpayErr.Message)
		} else {
			log.Printf("付款失败: %v", err)
		}
		return
	}

	fmt.Printf("付款成功！\n")
	fmt.Printf("平台订单号: %s\n", payResp.PlatformOrderNo)
	fmt.Printf("实际支付金额: %d MB\n", payResp.ActualAmount)
	fmt.Printf("手续费: %d MB\n", payResp.Fee)
	fmt.Printf("商户剩余余额: %d MB\n", payResp.Balance)

	// 示例3：生成支付链接（用于生成支付二维码）
	linkReq := &PaymentLinkRequest{
		OrderNo:   "ORD202501011200001234567890",    // 商户订单号
		Subject:   "购买VIP，1个月",                      // 商品描述
		Amount:    1000,                             // 订单金额（分）
		Expire:    15,                               // 过期时间（分钟）
		NotifyURL: "https://your-domain.com/notify", // 回调通知地址（可选）
	}

	paymentLink, err := client.GeneratePaymentLink(linkReq)
	if err != nil {
		log.Printf("生成支付链接失败: %v", err)
		return
	}

	fmt.Printf("支付链接: %s\n", paymentLink)
	fmt.Printf("可以将此链接用于生成二维码\n")
}
