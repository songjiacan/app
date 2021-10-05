package co.nz.small.app.exception

import co.nz.small.app.common.ResultCode

class Asserts {
    companion object {
        fun fail(message: String?) {
            throw ApiException(message ?: "")
        }

        fun fail(errorCode: ResultCode) {
            throw ApiException(errorCode)
        }
    }
}