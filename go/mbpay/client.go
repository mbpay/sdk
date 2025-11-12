package mbpay

import (
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"net/url"
	"sort"
	"strconv"
	"strings"
	"time"
)

// Client MBPay API 客户端
type Client struct {
	BaseURL    string       // API 基础地址，例如：https://www.mbpay.world
	AppID      string       // 商户的 App ID
	AppSecret  string       // 商户的 App Secret
	HTTPClient *http.Client // HTTP 客户端，可选
}

// NewClient 创建新的 MBPay 客户端
func NewClient(baseURL, appID, appSecret string) *Client {
	return &Client{
		BaseURL:   baseURL,
		AppID:     appID,
		AppSecret: appSecret,
		HTTPClient: &http.Client{
			Timeout: 30 * time.Second,
		},
	}
}

// generateSign 生成签名
// 规则：排除 sign 字段，按 ASCII 升序排序参数，拼接为 k=v&k2=v2...&key=app_secret，然后 SHA256 哈希
func (c *Client) generateSign(params map[string]string) string {
	// 排除 sign 字段，获取所有键并排序
	keys := make([]string, 0, len(params))
	for k := range params {
		if k != "sign" {
			keys = append(keys, k)
		}
	}
	sort.Strings(keys) // ASCII 升序

	// 拼接签名字符串
	var parts []string
	for _, k := range keys {
		parts = append(parts, fmt.Sprintf("%s=%s", k, params[k]))
	}
	signStr := strings.Join(parts, "&") + "&key=" + c.AppSecret

	// SHA256 哈希并转为小写 hex
	h := sha256.New()
	h.Write([]byte(signStr))
	return hex.EncodeToString(h.Sum(nil))
}

// doRequest 执行 HTTP 请求
func (c *Client) doRequest(method, path string, params map[string]string) (*Response, error) {
	// 添加必传参数
	if params == nil {
		params = make(map[string]string)
	}
	params["app_id"] = c.AppID
	params["timestamp"] = strconv.FormatInt(time.Now().Unix(), 10)

	// 生成签名
	params["sign"] = c.generateSign(params)

	// 构建 URL
	reqURL := strings.TrimSuffix(c.BaseURL, "/") + path

	// 构建请求
	var req *http.Request
	var err error

	if method == http.MethodGet {
		// GET 请求：参数放在 URL 中
		u, err := url.Parse(reqURL)
		if err != nil {
			return nil, fmt.Errorf("parse url failed: %w", err)
		}
		q := u.Query()
		for k, v := range params {
			q.Set(k, v)
		}
		u.RawQuery = q.Encode()
		req, err = http.NewRequest(method, u.String(), nil)
	} else {
		// POST 请求：参数放在 FormData 中
		values := url.Values{}
		for k, v := range params {
			values.Set(k, v)
		}
		req, err = http.NewRequest(method, reqURL, strings.NewReader(values.Encode()))
		if err == nil {
			req.Header.Set("Content-Type", "application/x-www-form-urlencoded")
		}
	}

	if err != nil {
		return nil, fmt.Errorf("create request failed: %w", err)
	}

	// 执行请求
	resp, err := c.HTTPClient.Do(req)
	if err != nil {
		return nil, fmt.Errorf("request failed: %w", err)
	}
	defer resp.Body.Close()

	// 读取响应
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, fmt.Errorf("read response failed: %w", err)
	}

	// 解析响应
	var apiResp Response
	if err := json.Unmarshal(body, &apiResp); err != nil {
		return nil, fmt.Errorf("parse response failed: %w, body: %s", err, string(body))
	}

	// 检查 HTTP 状态码
	if resp.StatusCode != http.StatusOK {
		return &apiResp, fmt.Errorf("http status %d: %s", resp.StatusCode, apiResp.Message)
	}

	return &apiResp, nil
}






