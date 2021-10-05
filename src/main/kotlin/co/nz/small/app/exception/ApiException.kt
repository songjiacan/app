package co.nz.small.app.exception

import co.nz.small.app.common.ResultCode

//class ApiException(errorCode: ResultCode)  : RuntimeException(errorCode.message) {
class ApiException : RuntimeException {
    var errorCode: ResultCode? = null

    constructor(errorCode: ResultCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }

    constructor(message: String) : super(message) {}
    constructor(cause: Throwable) : super(cause) {}
    constructor(message: String, cause: Throwable) : super(message, cause) {}

//    fun getErrorCode(): ResultCode? {
//        return errorCode
//    }
}