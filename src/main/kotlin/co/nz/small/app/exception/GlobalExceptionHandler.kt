package co.nz.small.app.exception

import cn.hutool.json.JSON
import co.nz.small.app.common.CommonResult
import co.nz.small.app.common.ResultCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ApiException::class)
    fun handle(e: ApiException): ResponseEntity<String> {
        return if (e.errorCode != null) {
            ResponseEntity.status(400).body(CommonResult.failed(e.errorCode!!).toString())   //CommonResult.failed(e.errorCode!!)
        } else ResponseEntity.status(400).body(CommonResult.badRequest(e.message?:"").toString())
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidException(e: MethodArgumentNotValidException): CommonResult<*>? {
        val bindingResult = e.bindingResult
        var message: String? = null
        if (bindingResult.hasErrors()) {
            val fieldError = bindingResult.fieldError
            if (fieldError != null) {
                message = fieldError.field + fieldError.defaultMessage
            }
        }
        return CommonResult.validateFailed(message?:"")
    }

    @ResponseBody
    @ExceptionHandler(BindException::class)
    fun handleValidException(e: BindException): CommonResult<*>? {
        val bindingResult: BindingResult = e.bindingResult
        var message: String? = null
        if (bindingResult.hasErrors()) {
            val fieldError = bindingResult.fieldError
            if (fieldError != null) {
                message = fieldError.field + fieldError.defaultMessage
            }
        }
        return CommonResult.validateFailed(message?:"")
    }
}