package com.lskj.gx.busi_account.error

/**
 *   创建时间:  2021/1/28
 *   错误处理 应该和网页端保持一致
 */
enum class ErrorCode(code: Int, msg: String) {
    /* 成功状态码 */
    SUCCESS(1, "成功"),
    FAILURE(0, "失败"),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),

    /* 用户错误：20001-29999*/
    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    USER_LOGIN_ERROR(20002, "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用"),
    USER_NOT_EXIST(20004, "用户不存在"),
    USER_HAS_EXISTED(20005, "用户已存在"),
    PHONE_HAS_REGISTER(20007, "手机号已注册"),
    EMAIL_HAS_REGISTER(20008, "邮箱已注册"),
    LOGIN_CREDENTIAL_EXISTED(20006, "凭证已存在"),

    /**
     * spring security start
     */
    PWD_ERROR(20009, "密码错误"),
    ACCOUNT_EXPIRED(20010, "该账户过期"),
    ACCOUNT_STATUS(20011, "该账户登录状态异常"),
    ACCOUNT_DISABLED(20012, "该账户禁用"),
    BAD_CREDENTIALS(20013, "登录凭证异常"),
    ACCOUNT_LOCKED(20014, "该账户已锁定"),
    TOKEN_REFRESH_EXPIRE(20015, "refresh token 过期"),

    /**
     * spring security end
     */

    /* 业务错误：30001-39999 */
    SPECIFIED_QUESTIONED_USER_NOT_EXIST(30001, "业务错误"),

    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(500, "服务端异常"),

    /* 数据错误：50001-599999 */
    RESULE_DATA_NONE(50001, "数据未找到"),
    DATA_IS_WRONG(50002, "数据有误"),
    DATA_ALREADY_EXISTED(50003, "数据已存在"),

    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),

    /* 权限错误：70001-79999 */
    PERMISSION_NO_ACCESS(70001, "无访问权限"),
    RESOURCE_EXISTED(70002, "资源已存在"),
    RESOURCE_NOT_EXISTED(70003, "资源不存在"),
    SYSTEM_ARITHMETIC_ERROR(70004, "运算异常"),

    // shiro
    SHIRO_EXCEPTION(8000, "shiro 错误"),
    SHIRO_AUTHENTICATION_EXCEPTION(8001, "shiro 认证失败"),
    SHIRO_ACCOUNT_EXCEPTION(8002, "shiro 账户错误"),
    SHIRO_UNKNOWN_ACCOUNT_EXCEPTION(8003, "账户不存在"),
    SHIRO_CREDENTIALS_EXCEPTION(8004, "用户密码错误"),
    SHIRO_INCORRECT_CREDENTIALS_EXCEPTION(8005, "用户密码错误"),
    SHIRO_DISABLED_SESSION_EXCEPTION(8006, "shiro session 禁用错误"),
    SHIRO_DISABLED_ACCOUNT_EXCEPTION(8007, "禁用账户"),
    // 此处可以扩张用户 状态 禁用等异常 参考 org.apache.shiro.authc 包下的异常

    /* mybatis*/
    MYBATIS_BINDING_ERROR(70005, "mybatis 绑定异常"),

    // token error
    JWT_EXCEPTION(9000, "jwt token 错误"),
    JWT_SIGNATURE_EXCEPTION(9001, "jwt token 签名错误"),
    JWT_EXPIRED_EXCEPTION(9002, "jwt token 过期"),
    JWT_UNSUPPORTED_EXCEPTION(9003, "jwt token 格式不支持"),
    JWT_MALFORMED_EXCEPTION(9004, "jwt token Malformed"),

    // 用户角色
    ROLE_NOT_FOUND(10000, "角色缺失"),

    // sftp 用到很多 非法的状态
    ILLEGAL_STATE_ERROR(11000, "非法状态  IllegalStateException"),
    IO_EXCEPTION(12000, "IO 异常"),
    JSCH_EXCEPTION(13000, "Jsch 异常"),
    SFTP_EXCEPTION(14000, "sftp 异常"),
    BUSINESS_EXCEPTION(15000, "业务出错"),
    SERVLET_EXCEPTION(16000, "servert exception"),

    // redis
    REDIS_SYSTEM_EXCEPTION(17000, "redis 系统异常"),
    REDIS_EXCEPTION(1710, "redis error"),
    FILE_UPLOAD_EXCEPTION(1720, "文件上传异常"),

    NESTED_RUNTIME_EXCEPTION(1730, "nested_runtime_exception"),
    DATE_ACCESS_EXCEPTION(1740, "data access exception"),
    MYBATIS_SYSTEM_EXCEPTION(1750, "mybatis system exception"),
    MAIL_EXCEPTION(1760, "mail exception"),
    MAIL_PARSE_EXCEPTION(1750, "mail parse exception"),
    MAIL_SEND_EXCEPTION(1760, "mail send exception"),
    ARITHMETIC_EXCEPTION(1770, "arithmetic Exception"),
    JSON_PROCESSING_EXCEPTION(1780, "json processing exception"),
    NOT_FOUND_EXCEPTION(1790, "not found"),
    CONNECT_EXCEPTION(1780, "connect exception"),
    FILE_NOT_FOUND(1790, "file not found"),
    INTERRUPTED_EXCEPTION(1800, "Interrupted Exception"),
    EXECUTION_EXCEPTION(1810, "Execution error"),
    RUN_TIME_EXCEPTION(1820, "runtime exception"),
    MYBATIS_NULLPOINT_ERROR(1830, "空指针异常"),

    // openfire
    ILLEGAL_ARGUMENT_EgXCEPTION(1840, "非法参数"),

    // smack
    SMACK_NOTCONNECTED_EXCEPTION(1841, "smack disconnect"),
    SMACK_EXCEPTION(1842, "smack exception"),
    SMACK_SECURITY_NOT_POSSIBLE_EXCEPTION(1843, "smack security not possible"),
    SMACK_ALREADY_CONNECTED_EXCEPTION(1844, "smack already connected"),
    SMACK_ILLEGAL_STATE_CHANGE_EXCEPTION(1845, "illegalStateChangeException"),
    SMACK_ALREADY_LOGGEDIN_EXCEPTION(1846, "AlreadyLoggedInException"),
    SMACK_NORESPONSE_EXCEPTION(1847, "NoResponseException"),
    SMACK_SECURITY_REQUIREDBY_CLIENT_EXCEPTION(
        1848,
        "SecurityRequiredByClientException"
    ),
    SMACK_SECURITY_REQUIREDBY_SERVER_EXCEPTION(1849, "SecurityRequiredByServerException"),
    SMACK_NOTLOGGEDIN_EXCEPTION(1850, "NotLoggedInException"),
    SMACK_RESOURCE_BINDING_NOTOFFERED_EXCEPTION(1851, "ResourceBindingNotOfferedException"),

    // xmpp
    XMPP_EXCEPTION(1901, "xmpp"),
    XMPP_ERROR_EXCEPTION(1902, "XMPP_ERROR_EXCEPTION"),
    PARSE_EXCEPTION(2001, "parse exception"),

    // 定时任务
    SCHEDULER_EXCEPTION(2002, "scheduler exception"),

    // Encryption 加密错误
    ENCRYPTION_EXCEPTION(2100, "EncryptionUtil 加密错误"),
    DECRYPTION_EXCEPTION(2101, "EncryptionUtil 解密错误"),

    // vo_to_bo model 和 实体类 转化
    VO_TO_BO_EXCEPTION(2200, "vo to bo exception"),
    BO_TO_VO_EXCEPTION(2201, "bo to vo exception");

    fun getCode(): Int {
        return this.ordinal
    }

    fun getMsg(): String {
        return this.name
    }
}