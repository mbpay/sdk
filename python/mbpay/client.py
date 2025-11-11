"""MBPay API 客户端"""

import hashlib
import json
import time
from typing import Dict, Optional
from urllib.parse import urlencode

import requests

from .types import Response, MBPayError


class Client:
    """MBPay API 客户端"""
    
    def __init__(self, base_url: str, app_id: str, app_secret: str, timeout: int = 30):
        """
        创建新的 MBPay 客户端
        
        Args:
            base_url: API 基础地址，例如：https://www.mbpay.world
            app_id: 商户的 App ID
            app_secret: 商户的 App Secret
            timeout: HTTP 请求超时时间（秒），默认 30 秒
        """
        self.base_url = base_url.rstrip('/')
        self.app_id = app_id
        self.app_secret = app_secret
        self.timeout = timeout
        self.session = requests.Session()
    
    def _generate_sign(self, params: Dict[str, str]) -> str:
        """
        生成签名
        规则：排除 sign 字段，按 ASCII 升序排序参数，拼接为 k=v&k2=v2...&key=app_secret，然后 SHA256 哈希
        
        Args:
            params: 参数字典
            
        Returns:
            签名字符串（小写 hex）
        """
        # 排除 sign 字段，获取所有键并排序
        keys = sorted([k for k in params.keys() if k != "sign"])
        
        # 拼接签名字符串
        parts = [f"{k}={params[k]}" for k in keys]
        sign_str = "&".join(parts) + f"&key={self.app_secret}"
        
        # SHA256 哈希并转为小写 hex
        return hashlib.sha256(sign_str.encode('utf-8')).hexdigest()
    
    def _do_request(
        self,
        method: str,
        path: str,
        params: Optional[Dict[str, str]] = None
    ) -> Response:
        """
        执行 HTTP 请求
        
        Args:
            method: HTTP 方法（GET 或 POST）
            path: API 路径
            params: 请求参数
            
        Returns:
            Response 对象
            
        Raises:
            MBPayError: API 业务错误
            requests.RequestException: 网络请求错误
        """
        if params is None:
            params = {}
        
        # 添加必传参数
        params["app_id"] = self.app_id
        params["timestamp"] = str(int(time.time()))
        
        # 生成签名
        params["sign"] = self._generate_sign(params)
        
        # 构建 URL
        url = f"{self.base_url}{path}"
        
        try:
            if method.upper() == "GET":
                # GET 请求：参数放在 URL 中
                response = self.session.get(
                    url,
                    params=params,
                    timeout=self.timeout
                )
            else:
                # POST 请求：参数放在 FormData 中
                response = self.session.post(
                    url,
                    data=params,
                    timeout=self.timeout,
                    headers={"Content-Type": "application/x-www-form-urlencoded"}
                )
            
            # 检查 HTTP 状态码
            response.raise_for_status()
            
            # 解析响应
            result = response.json()
            api_resp = Response(
                code=result.get("code", 0),
                message=result.get("message", ""),
                data=result.get("data", {})
            )
            
            # 检查业务错误
            if not api_resp.is_success():
                error = api_resp.to_error()
                if error:
                    raise error
            
            return api_resp
            
        except requests.RequestException as e:
            raise MBPayError(0, f"Request failed: {str(e)}")
        except json.JSONDecodeError as e:
            raise MBPayError(0, f"Parse response failed: {str(e)}")





