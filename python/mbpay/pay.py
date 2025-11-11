"""付款功能"""

from .client import Client
from .types import PayResponse, Response, MBPayError, PayRequest


def pay(self: Client, req: PayRequest) -> PayResponse:
    """
    商户向用户地址付款
    
    Args:
        req: 付款请求
        
    Returns:
        PayResponse: 付款响应
        
    Raises:
        MBPayError: API 业务错误
        requests.RequestException: 网络请求错误
    """
    # 参数验证
    if not req.address:
        raise MBPayError(0, "address is required")
    if not req.order_no:
        raise MBPayError(0, "order_no is required")
    if req.amount <= 0:
        raise MBPayError(0, "amount must be greater than 0")
    
    # 构建请求参数
    params = {
        "address": req.address,
        "order_no": req.order_no,
        "amount": str(req.amount),
    }
    if req.remark:
        params["remark"] = req.remark
    
    # 执行请求
    resp: Response = self._do_request("POST", "/merchant/pay", params)
    
    # 解析 data 字段
    data = resp.data
    platform_order_no = data.get("platform_order_no", "")
    actual_amount = int(data.get("actual_amount", 0))
    fee = int(data.get("fee", 0))
    balance = int(data.get("balance", 0))
    
    if not platform_order_no:
        raise MBPayError(0, "invalid platform_order_no format in response")
    
    return PayResponse(
        platform_order_no=platform_order_no,
        actual_amount=actual_amount,
        fee=fee,
        balance=balance
    )


# 将方法绑定到 Client 类
Client.pay = pay

