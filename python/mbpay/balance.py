"""余额查询功能"""

from .client import Client
from .types import BalanceResponse, Response


def get_balance(self: Client) -> BalanceResponse:
    """
    获取商户余额
    
    Returns:
        BalanceResponse: 余额信息
        
    Raises:
        MBPayError: API 业务错误
        requests.RequestException: 网络请求错误
    """
    resp: Response = self._do_request("POST", "/merchant/balance")
    
    # 解析 data 字段
    data = resp.data
    balance = int(data.get("balance", 0))
    frozen = int(data.get("frozen", 0))
    
    return BalanceResponse(balance=balance, frozen=frozen)


# 将方法绑定到 Client 类
Client.get_balance = get_balance






