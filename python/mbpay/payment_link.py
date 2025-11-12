"""支付链接生成功能"""

import base64
import hashlib
import json
import secrets
import time
from typing import Dict, Any
from urllib.parse import quote

from .client import Client
from .types import PaymentLinkRequest, MBPayError


def generate_payment_link(self: Client, req: PaymentLinkRequest) -> str:
    """
    生成支付链接（用于生成支付二维码）
    返回支付链接字符串，格式：mbpay://payorder?data={base64_encoded_json}
    
    Args:
        req: 支付链接生成请求
        
    Returns:
        支付链接字符串
        
    Raises:
        MBPayError: 参数错误
    """
    # 参数验证
    if not req.order_no:
        raise MBPayError(0, "order_no is required")
    if not req.subject:
        raise MBPayError(0, "subject is required")
    if req.amount <= 0:
        raise MBPayError(0, "amount must be greater than 0")
    if req.expire <= 0:
        raise MBPayError(0, "expire must be greater than 0")
    
    # 生成 nonce（如果未提供）
    nonce = req.nonce
    if not nonce:
        nonce = _generate_nonce(16)
    
    # 计算过期时间戳（当前时间 + 过期分钟数）
    expire_ts = int(time.time()) + req.expire * 60
    
    # 构造签名用对象（不含 sign）
    data_to_sign: Dict[str, Any] = {
        "app_id": self.app_id,
        "expire": expire_ts,
        "nonce": nonce,
        "order_no": req.order_no,
        "amount": req.amount,
        "subject": req.subject,
    }
    
    # 如果 notify_url 有值，添加到签名对象中
    if req.notify_url:
        data_to_sign["notify_url"] = req.notify_url
    
    # 生成签名
    sign = _generate_sign_for_payment_link(data_to_sign, self.app_secret)
    
    # 把 sign 写回数据
    final_data = data_to_sign.copy()
    final_data["sign"] = sign
    
    # 将数据转为 JSON 字符串
    json_str = json.dumps(final_data, separators=(',', ':'))
    
    # Base64 编码（标准编码）
    base64_str = base64.b64encode(json_str.encode('utf-8')).decode('utf-8')
    
    # URL 编码（类似 JavaScript 的 encodeURIComponent）
    encoded_base64 = quote(base64_str, safe='')
    
    # 生成支付链接
    payment_link = f"mbpay://payorder?data={encoded_base64}"
    
    return payment_link


def _generate_sign_for_payment_link(data: Dict[str, Any], app_secret: str) -> str:
    """
    为支付链接生成签名
    规则：排除 sign 字段，按 ASCII 升序排序参数，拼接为 k=v&k2=v2...&key=app_secret，然后 SHA256 哈希
    
    Args:
        data: 数据字典
        app_secret: App Secret
        
    Returns:
        签名字符串（小写 hex）
    """
    # 将值转为字符串
    params: Dict[str, str] = {}
    for k, v in data.items():
        if k != "sign":
            if isinstance(v, (int, float)):
                # 如果是整数，格式化为整数字符串
                if isinstance(v, float) and v == int(v):
                    params[k] = str(int(v))
                else:
                    params[k] = str(v)
            else:
                params[k] = str(v)
    
    # 排除 sign 字段，获取所有键并排序
    keys = sorted([k for k in params.keys() if k != "sign"])
    
    # 拼接签名字符串
    parts = [f"{k}={params[k]}" for k in keys]
    sign_str = "&".join(parts) + f"&key={app_secret}"
    
    # SHA256 哈希并转为小写 hex
    return hashlib.sha256(sign_str.encode('utf-8')).hexdigest()


def _generate_nonce(length: int = 16) -> str:
    """
    生成随机字符串
    
    Args:
        length: 字符串长度
        
    Returns:
        随机字符串
    """
    chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return ''.join(secrets.choice(chars) for _ in range(length))


# 将方法绑定到 Client 类
Client.generate_payment_link = generate_payment_link






