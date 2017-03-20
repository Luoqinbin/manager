package com.badminton.result;

/**
 * Created by Administrator on 2016/8/17.
 */
public class BaseResult {
    /**
     * 成功
     */
    public static final int CODE_OK = 200;
    /**
     * 业务成功默认消息
     */
    public static final String MSG_OK = "success";
    /**
     * 业务失败默认消息
     */
    public static final String MSG_FAIL = "fail";
    /**
     * 业务失败默认代码
     */
    public static final int CODE_FAIL = 999;
    /**
     * 无效用户
     */
    public static final int CODE_USER_INVALID = 201;
    /**
     * Steam用户校验失败
     */
    public static final int CODE_USER_VERIFICATION_FAILURE = 202;
    
    /**用户已存在**/
    public static final int CODE_USER_EXISTED = 203;
    
    /**邮箱格式不正确**/
    public static final int CODE_USER_EMAIL_INVALID = 204;
    
    /**密码不正确**/
    public static final int CODE_USER_PASSWORD_ERROR = 205;
    
    /**
     * 程序内部错误
     */
    public static final int CODE_INTERNAL_ERROR = 500;
    /**
     * 参数错误
     */
    public static final int CODE_PARAM_ERROR = 501;

    /**
     * The constant MSG_PARAM_ERROR.
     */
    public static final String MSG_PARAM_ERROR = "params error";

    /**
     * 参数重复
     */
    public static final int CODE_PARAM_ALREADY = 555;
    
    /**
     * 购物车中没有商品
     */
    public static final int CODE_SHOPPINGCART_EXECUTE_FAILED = 600;
    
    /**
     * 购物车中没有商品
     */
    public static final int CODE_SHOPPINGCART_EMPTY = 601;
    
    /**
     * 没能找到对应的商品可加入购物车
     */
    public static final int CODE_SHOPPINGCART_NONE_PRODUCT = 602;
    
    /**
     * 商品已失效
     */
    public static final int CODE_SHOPPINGCART_INVALID_PRODUCT = 603;
    
    /**
     * 没能找到对应规格的商品可加入购物车
     */
    public static final int CODE_SHOPPINGCART_NONE_PRODUCT_SPEC = 604;
    
    /**
     * 商品规格已失效
     */
    public static final int CODE_SHOPPINGCART_INVALID_PRODUCT_SPEC = 605;
    
    /**
     * 没能找到对应价格的商品可加入购物车
     */
    public static final int CODE_SHOPPINGCART_NONE_PRODUCT_PRICE = 606;
    
    /**
     * 商品价格已失效
     */
    public static final int CODE_SHOPPINGCART_INVALID_PRODUCT_PRICE = 607;
    
    /**
     * 本商品未设置成本商品价格已失效
     */
    public static final int CODE_SHOPPINGCART_INVALID_PRODUCT_COST_PRICE = 608;
    
    /**
     * 购物车中的商品类型不一致
     */
    public static final int CODE_SHOPPINGCART_INVALID_PRODUCT_TYPE = 609;
    
    /**
     * 未找到对应的支付方式
     */
    public static final int CODE_SHOPPINGCART_NONE_PAYMENT = 610;
    
    /**
     * 支付方式已失效
     */
    public static final int CODE_SHOPPINGCART_INVALID_PAYMENT = 611;
    
    /**
     * 未设置支付密码
     */
    public static final int CODE_SHOPPINGCART_NONE_PASSWORD = 612;
    
    /**
     * 支付密码错误
     */
    public static final int CODE_SHOPPINGCART_NONE_INVALID = 613;
    
    /**
     * 账户余额不足
     */
    public static final int CODE_SHOPPINGCART_BALANCE_INVALID = 614;
    
    /**
     * 订单创建失败
     */
    public static final int CODE_SHOPPINGCART_CREATE_ORDER_FAILED = 615;
    
    /**
     * 金额错误
     */
    public static final int CODE_SHOPPINGCART_AMOUNT_INVALID = 616;
    
    /**
     * 无效的币种
     */
    public static final int CODE_SHOPPINGCART_CURRENCY_INVALID = 617;

    /**
     * The constant MSG_PARAM_ALREADY.
     */
    public static final String MSG_PARAM_ALREADY = "params already error";
    public static final String MSG_USER_VERIFICATION_FAILURE="user_verification_failure";


    private String message;
    private Object data;
    private int code;

    /**
     * Instantiates a new Base result.
     */
    public BaseResult() {
        this.message = MSG_OK;
        this.code = CODE_OK;
    }


    /**
     * Instantiates a new Base result.
     *
     * @param message the message
     */
    public BaseResult(String message) {

        this.message = message;
        this.code = CODE_OK;
    }

    /**
     * Instantiates a new Base result.
     *
     * @param message the message
     * @param data    the data
     */
    public BaseResult(String message, Object data) {
        this.message = message;
        this.data = data;
        this.code = CODE_OK;
    }

    /**
     * Instantiates a new Base result.
     *
     * @param message the message
     * @param code    the code
     */
    public BaseResult(String message, int code) {
        this.message = message;
        this.code = code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

}
