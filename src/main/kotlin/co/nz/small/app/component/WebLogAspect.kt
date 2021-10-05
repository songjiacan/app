package co.nz.small.app.component

import cn.hutool.core.util.StrUtil
import cn.hutool.core.util.URLUtil
import cn.hutool.json.JSONUtil
import co.nz.small.app.dto.WebLog
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.Signature
import org.aspectj.lang.annotation.*
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils


import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.util.*

@Aspect
@Component
@Order(1)
class WebLogAspect {
    private val logger: Logger = LoggerFactory.getLogger(WebLogAspect::class.java)

    @Pointcut("execution(public * co.nz.small.app.controllers.TitlesController.*(..))")
    fun webLog() {
    }

    @Before("webLog()")
    @Throws(Throwable::class)
    fun doBefore(joinPoint: JoinPoint?) {
    }

    @AfterReturning(value = "webLog()", returning = "ret")
    @Throws(Throwable::class)
    fun doAfterReturning(ret: Any?) {
    }

    @Around("webLog()")
    @Throws(Throwable::class)
    fun doAround(joinPoint: ProceedingJoinPoint): Any? {
        val startTime = System.currentTimeMillis()
        //Get current request
        val attributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        val request = attributes.request
        //record request
        val webLog = WebLog()
        val result: Any = joinPoint.proceed()
        val signature: Signature = joinPoint.signature
        val methodSignature: MethodSignature = signature as MethodSignature
        val method: Method = methodSignature.method
//        if (method.isAnnotationPresent(ApiOperation::class.java)) {
//            val apiOperation: ApiOperation = method.getAnnotation(ApiOperation::class.java)
//            webLog.setDescription(apiOperation.value())
//        }
        val endTime = System.currentTimeMillis()
        val urlStr = request.requestURL.toString()
        webLog.authToken = request.getHeader("Authorization")
        webLog.basePath= StrUtil.removeSuffix(urlStr, URLUtil.url(urlStr).path)
        webLog.ip= request.remoteUser
        webLog.method= request.method
        webLog.parameter= getParameter(method, joinPoint.args)
        webLog.result = result
        webLog.spendTime= (endTime - startTime).toInt()
        webLog.startTime = startTime
        webLog.uri = request.requestURI
        webLog.url = request.requestURL.toString()
        webLog.requestedSessionId = request.getHeader("Session-Id")    //request.requestedSessionId  -- no value as session management disabled in SecurityConfig
        logger.info("{}", JSONUtil.parse(webLog))
        return result
    }


    private fun getParameter(method: Method, args: Array<Any>): Any? {
        val argList: MutableList<Any> = ArrayList()
        val parameters: Array<Parameter> = method.parameters
        for (i in parameters.indices) {
            val requestBody: RequestBody? = parameters[i].getAnnotation(RequestBody::class.java)
            if (requestBody != null) {
                argList.add(args[i])
            }
            val requestParam: RequestParam? = parameters[i].getAnnotation(RequestParam::class.java)
            if (requestParam != null) {
                val map: MutableMap<String, Any> = HashMap()
                var key: String = parameters[i].name
                if (!requestParam.value.isEmpty()) key = requestParam.value
                map[key] = args[i]
                argList.add(map)
            }
        }
        return when (argList.size) {
            0 -> {
                null
            }
            1 -> {
                argList[0]
            }
            else -> {
                argList
            }
        }
    }
}