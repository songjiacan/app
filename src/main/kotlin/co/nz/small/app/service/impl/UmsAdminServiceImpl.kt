package co.nz.small.app.service.impl

import co.nz.small.app.dto.AdminUserDetails
import co.nz.small.app.models.UmsAdmin
import co.nz.small.app.models.UmsPermission
import co.nz.small.app.persistence.UmsRepo
import co.nz.small.app.service.UmsAdminService
import co.nz.small.app.utils.JwtTokenUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UmsAdminServiceImpl (val umsRepo: UmsRepo,
                           val passwordEncoder: PasswordEncoder,
                           val jwtTokenUtil: JwtTokenUtil) : UmsAdminService {
    private val logger: Logger = LoggerFactory.getLogger(UmsAdminServiceImpl::class.java)

    override fun getAdminByUsername(username: String): UmsAdmin? {
        return umsRepo.findByUsername(username)
    }

    override fun login(username: String, password: String): String {
        var token = ""
        try {
            val permissionList: MutableList<UmsPermission> = ArrayList<UmsPermission>()
            permissionList.add(UmsPermission("",""))
            val userDetails: UserDetails = AdminUserDetails(getAdminByUsername(username)!!, permissionList)
            if (!passwordEncoder.matches(password, userDetails.password)) {
                throw BadCredentialsException("Incorrect password")
            }
//            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
//            SecurityContextHolder.getContext().authentication = authentication
            token = jwtTokenUtil.generateToken(username)
        } catch (e: AuthenticationException) {
            logger.warn("Logon Failed:{}", e.message)
        }
        return token
    }
}
