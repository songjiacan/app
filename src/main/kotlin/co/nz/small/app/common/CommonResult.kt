package co.nz.small.app.common

import cn.hutool.json.JSON
import cn.hutool.json.JSONUtil

class CommonResult<T: Any?>(var code: Long = 0, var message: String = "", var data: T){


    companion object {
        /**
         * return success result
         *
         * @param data data
         */
        fun <T : Any> success(data: T): CommonResult<T> {
            return CommonResult(ResultCode.SUCCESS.code, ResultCode.SUCCESS.message, data)
        }

        /**
         * return success result
         *
         * @param data    return success data
         * @param message return message
         */
        fun <T : Any> success(data: T, message: String): CommonResult<T> {
            return CommonResult(ResultCode.SUCCESS.code, message, data)
        }

        /**
         * return failed result
         *
         * @param errorCode error code
         */
        fun failed(errorCode: ResultCode): CommonResult<Nothing?> {
            return CommonResult(errorCode.code, errorCode.message, null)
        }

        /**
         * return failed result
         *
         * @param errorCode 错误码
         * @param message   错误信息
         */
        fun failed(errorCode: ResultCode, message: String): CommonResult<Nothing?> {
            return CommonResult(errorCode.code, message, null)
        }

        /**
         * return failed result
         *
         * @param message 提示信息
         */
        fun failed(message: String): CommonResult<Nothing?> {
            return CommonResult(ResultCode.FAILED.code, message, null)
        }

        fun badRequest(message: String): CommonResult<Nothing?> {
            return CommonResult(ResultCode.BAD_REQUEST.code, message, null)
        }

        /**
         * return failed result
         */
        fun failed(): CommonResult<Nothing?> {
            return failed(ResultCode.FAILED)
        }

        /**
         * return failed result when parameter verify failed
         */
        fun validateFailed(): CommonResult<Nothing?> {
            return failed(ResultCode.VALIDATE_FAILED)
        }

        /**
         * return failed result when parameter verify failed
         *
         * @param message 提示信息
         */
        fun validateFailed(message: String): CommonResult<Nothing?> {
            return CommonResult(ResultCode.VALIDATE_FAILED.code, message, null)
        }

        /**
         * return result when not login
         */
        fun <T : Any>  unauthorized(data: T): CommonResult<T> {
            return CommonResult(ResultCode.UNAUTHORIZED.code, ResultCode.UNAUTHORIZED.message, data)
        }

        /**
         * return result when not authorized
         */
        fun <T : Any>  forbidden(data: T): CommonResult<T> {
            return CommonResult(ResultCode.FORBIDDEN.code, ResultCode.FORBIDDEN.message, data)
        }
    }

    fun toJson(): JSON {
        val tokenMap: MutableMap<String, String> = HashMap()
        tokenMap["Error Code"] = this.code.toString()
        tokenMap["message"] = this.message
        tokenMap["data"] = this.data.toString()
        return JSONUtil.parse(tokenMap.toString())
    }

    override fun toString(): String {
//        val tokenMap: MutableMap<String, String> = HashMap()
//        tokenMap["Error Code"] = this.code.toString()
//        tokenMap["message"] = this.message
//        tokenMap["data"] = this.data.toString()
        return JSONUtil.toJsonStr(this)
    }
}