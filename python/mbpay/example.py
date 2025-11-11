"""使用示例"""

from mbpay import Client, PaymentLinkRequest, PayRequest


def example():
    """使用示例"""
    # 创建客户端
    client = Client(
        base_url="https://www.mbpay.world",  # API 基础地址
        app_id="your_api_key_here",          # App ID
        app_secret="your_api_key_here"       # App Secret
    )
    
    # 示例1：查询商户余额
    try:
        balance = client.get_balance()
        print(f"可用余额: {balance.balance} MB, 冻结余额: {balance.frozen} MB")
    except Exception as e:
        print(f"查询余额失败: {e}")
        return
    
    # 示例2：商户付款给用户
    pay_req = PayRequest(
        address="Txxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",  # 收款地址
        order_no="PAY202501011200001234567890",        # 商户订单号
        amount=100,                                     # 付款金额（MB，100 = 1MB）
        remark="商户付款"                                # 备注（可选）
    )
    
    try:
        pay_resp = client.pay(pay_req)
        print("付款成功！")
        print(f"平台订单号: {pay_resp.platform_order_no}")
        print(f"实际支付金额: {pay_resp.actual_amount} MB")
        print(f"手续费: {pay_resp.fee} MB")
        print(f"商户剩余余额: {pay_resp.balance} MB")
    except Exception as e:
        print(f"付款失败: {e}")
        return
    
    # 示例3：生成支付链接（用于生成支付二维码）
    link_req = PaymentLinkRequest(
        order_no="ORD202501011200001234567890",        # 商户订单号
        subject="购买VIP，1个月",                       # 商品描述
        amount=1000,                                    # 订单金额（分）
        expire=15,                                      # 过期时间（分钟）
        notify_url="https://your-domain.com/notify"    # 回调通知地址（可选）
    )
    
    try:
        payment_link = client.generate_payment_link(link_req)
        print(f"支付链接: {payment_link}")
        print("可以将此链接用于生成二维码")
    except Exception as e:
        print(f"生成支付链接失败: {e}")


if __name__ == "__main__":
    example()

