package com.lskj.gx.lib_common.error

/**
 *   创建时间:  2021/1/28
 *   错误处理 应该和网页端保持一致
 */
class ErrorCode() {
    companion object {
        val instance: ErrorCode by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ErrorCode()
        }
        var sourceMap: HashMap<Int, String> = HashMap()

        init {
            sourceMap[200] = "成功"
            sourceMap[0] = "失败"
            /* 参数错误：10001-19999 */
            sourceMap[10001] = "参数无效"
            sourceMap[10002] = "参数为空"
            sourceMap[10003] = "参数类型错误"
            sourceMap[10004] = "参数缺失"

            /* 用户错误：20001-29999*/
            sourceMap[20001] = "用户未登录"
            sourceMap[20002] = "账号不存在或密码错误"
            sourceMap[20003] = "账号已被禁用"
            sourceMap[20004] = "用户不存在"
            sourceMap[20005] = "用户已存在"
            sourceMap[20007] = "手机号已注册"
            sourceMap[20008] = "邮箱已注册"
            sourceMap[20006] = "凭证已存在"

            /* spring security start */
            sourceMap[20009] = "密码错误"
            sourceMap[20010] = "该账户过期"
            sourceMap[20011] = "该账户登录状态异常"
            sourceMap[20012] = "该账户禁用"
            sourceMap[20013] = "登录凭证异常"
            sourceMap[20014] = "该账户已锁定"
            sourceMap[20015] = "refresh token 过期"

            /*spring security end */

            /* 业务错误：30001-39999 */
            sourceMap[30001] = "业务错误"
            /* 系统错误：40001-49999 */
            sourceMap[500] = "服务端异常"
            /* 数据错误：50001-599999 */
            sourceMap[50001] = "数据未找到"
            sourceMap[50002] = "数据有误"
            sourceMap[50003] = "数据已存在"


            /* 接口错误：60001-69999 */
            sourceMap[60001] = "内部系统接口调用异常"
            sourceMap[60002] = "外部系统接口调用异常"
            sourceMap[60003] = "该接口禁止访问"
            sourceMap[60004] = "接口地址无效"
            sourceMap[60005] = "接口请求超时"
            sourceMap[60006] = "接口负载过高"

            /* 权限错误：70001-79999 */
            sourceMap[70001] = "无访问权限"
            sourceMap[70002] = "资源已存在"
            sourceMap[70003] = "资源不存在"
            sourceMap[70004] = "运算异常"

            /* shiro */
            sourceMap[8000] = "shiro 错误"
            sourceMap[8001] = "shiro 认证失败"
            sourceMap[8002] = "shiro 账户错误"
            sourceMap[8003] = "账户不存在"
            sourceMap[8004] = "用户密码错误"
            sourceMap[8005] = "用户密码错误"
            sourceMap[8006] = "shiro session 禁用错误"
            sourceMap[8007] = "禁用账户"

            /* mybatis*/
            sourceMap[70005] = "mybatis 绑定异常"
            sourceMap[9000] = "jwt token 错误"
            sourceMap[9002] = "jwt token 过期"
            sourceMap[9003] = "jwt token 格式不支持"

            /* 用户角色 */
            sourceMap[10000] = "角色缺失"


            /* sftp 用到很多 非法的状态*/
            sourceMap[11000] = "sftp 非法状态 IllegalStateException"
            sourceMap[12000] = "sftp IO 异常"
            sourceMap[13000] = "Jsch 异常"
            sourceMap[14000] = "sftp 异常"
            sourceMap[15000] = "业务出错"
            sourceMap[16000] = "servert exception"

            /* redis */
            sourceMap[17000] = "redis 系统异常"
            sourceMap[1710] = "redis error"
            sourceMap[1720] = "文件上传异常"
            sourceMap[1730] = "nested_runtime_exception"
            sourceMap[1740] = "data access exception"
            sourceMap[1750] = "mybatis system exception"
            sourceMap[1760] = "mail send exception"
            sourceMap[1770] = "arithmetic Exception"
            sourceMap[1780] = "connect exception"
            sourceMap[1790] = "file not found"
            sourceMap[1800] = "Interrupted Exception"
            sourceMap[1810] = "Execution error"
            sourceMap[1820] = "runtime exception"
            sourceMap[1830] = "空指针异常"

        }

        fun getByCode(code: Int): String? {
            if (sourceMap.size > 0 && sourceMap.containsKey(code)) {
                return sourceMap.get(code)
            }
            return null
        }
    }
}