package co.nz.small.app.component

import cn.hutool.json.JSONUtil
import co.nz.small.app.common.CommonResult
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RestfulAccessDeniedHandler : AccessDeniedHandler {
    @Throws(IOException::class, ServletException::class)
    override fun handle(request: HttpServletRequest,
                        response: HttpServletResponse,
                        e: AccessDeniedException) {
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"
        response.writer.println(JSONUtil.parse(CommonResult.forbidden(e.message?:"")))
        response.writer.flush()
    }
}