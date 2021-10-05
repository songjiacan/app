package co.nz.small.app.controllers

import co.nz.small.app.dto.UmsAdminLoginParam
import co.nz.small.app.service.UmsAdminService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class UmsController(val adminService: UmsAdminService) {
    @Value("\${jwt.tokenHeader}")
    private val tokenHeader: String = ""

    @Value("\${jwt.tokenHead}")
    private val tokenHead: String = ""

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    @ResponseBody
    fun login(@Validated @RequestBody umsAdminLoginParam: UmsAdminLoginParam):ResponseEntity<String> {
        val umsAdmin = adminService.getAdminByUsername(umsAdminLoginParam.username)

        return if (umsAdmin == null) {
            ResponseEntity.status(401).body("user name not found")
        } else {
            val token: String = adminService.login(umsAdminLoginParam.username, umsAdminLoginParam.password)
            if (token.isEmpty())
            {
                return ResponseEntity.status(401).body("user's name not match password")
            } else {
                val tokenMap: MutableMap<String, String> = HashMap()
                tokenMap["token"] = token
                tokenMap["tokenHead"] = tokenHead
                ResponseEntity.ok().body(tokenMap.toString())
            }
        }
    }
}