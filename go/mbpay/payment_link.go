package mbpay

import (
	"crypto/rand"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"math/big"
	"net/url"
	"time"
)

// GeneratePaymentLink 生成支付链接（用于生成支付二维码）
// 返回支付链接字符串，格式：mbpay://payorder?data={base64_encoded_json}
func (c *Client) GeneratePaymentLink(req *PaymentLinkRequest) (string, error) {
	// 参数验证
	if req.OrderNo == "" {
		return "", fmt.Errorf("order_no is required")
	}
	if req.Subject == "" {
		return "", fmt.Errorf("subject is required")
	}
	if req.Amount <= 0 {
		return "", fmt.Errorf("amount must be greater than 0")
	}
	if req.Expire <= 0 {
		return "", fmt.Errorf("expire must be greater than 0")
	}

	// 生成 nonce（如果未提供）
	nonce := req.Nonce
	if nonce == "" {
		nonce = generateNonce(16)
	}

	// 计算过期时间戳（当前时间 + 过期分钟数）
	expireTs := time.Now().Unix() + int64(req.Expire)*60

	// 构造签名用对象（不含 sign）
	dataToSign := map[string]interface{}{
		"app_id":   c.AppID,
		"expire":   expireTs,
		"nonce":    nonce,
		"order_no": req.OrderNo,
		"amount":   req.Amount,
		"subject":  req.Subject,
	}

	// 如果 notify_url 有值，添加到签名对象中
	if req.NotifyURL != "" {
		dataToSign["notify_url"] = req.NotifyURL
	}

	// 生成签名
	sign := c.generateSignForPaymentLink(dataToSign)

	// 把 sign 写回数据
	finalData := make(map[string]interface{})
	for k, v := range dataToSign {
		finalData[k] = v
	}
	finalData["sign"] = sign

	// 将数据转为 JSON 字符串
	jsonBytes, err := json.Marshal(finalData)
	if err != nil {
		return "", fmt.Errorf("marshal json failed: %w", err)
	}

	// Base64 编码（标准编码）
	base64Str := base64.StdEncoding.EncodeToString(jsonBytes)

	// URL 编码（类似 JavaScript 的 encodeURIComponent）
	encodedBase64 := url.QueryEscape(base64Str)

	// 生成支付链接
	paymentLink := fmt.Sprintf("mbpay://payorder?data=%s", encodedBase64)

	return paymentLink, nil
}

// generateSignForPaymentLink 为支付链接生成签名
// 规则：排除 sign 字段，按 ASCII 升序排序参数，拼接为 k=v&k2=v2...&key=app_secret，然后 SHA256 哈希
func (c *Client) generateSignForPaymentLink(data map[string]interface{}) string {
	// 将 map[string]interface{} 转为 map[string]string 用于签名
	params := make(map[string]string)
	for k, v := range data {
		if k != "sign" {
			// 将值转为字符串
			switch val := v.(type) {
			case string:
				params[k] = val
			case int64:
				params[k] = fmt.Sprintf("%d", val)
			case int:
				params[k] = fmt.Sprintf("%d", val)
			case float64:
				// 如果是整数，格式化为整数字符串
				if val == float64(int64(val)) {
					params[k] = fmt.Sprintf("%.0f", val)
				} else {
					params[k] = fmt.Sprintf("%v", val)
				}
			default:
				params[k] = fmt.Sprintf("%v", val)
			}
		}
	}

	// 使用已有的签名生成方法
	return c.generateSign(params)
}

// generateNonce 生成随机字符串
func generateNonce(length int) string {
	const chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
	result := make([]byte, length)
	for i := range result {
		num, _ := rand.Int(rand.Reader, big.NewInt(int64(len(chars))))
		result[i] = chars[num.Int64()]
	}
	return string(result)
}
