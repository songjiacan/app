package co.nz.small.app.component

import co.nz.small.app.utils.JwtTokenUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationTokenFilter(val jwtTokenUtil: JwtTokenUtil): OncePerRequestFilter() {
    val LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter::class.java)

    //val userDetailsService: UserDetailsService,

    @Value("\${jwt.tokenHeader}")
    val tokenHeader: String = ""
    @Value("\${jwt.tokenHead}")
    val tokenHead: String = ""

//    @Autowired
//    private lateinit var jwtTokenUtil: JwtTokenUtil

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authHeader = request.getHeader(this.tokenHeader)
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
        val authToken: String = authHeader.substring(this.tokenHead.length) // The part after "Bearer "
        val username: String? = jwtTokenUtil.getUserNameFromToken(authToken)
        LOGGER.info("checking username:{}", username)
        if (username==null)
            SecurityContextHolder.getContext().authentication = null
        else {
            val authentication: Authentication = UsernamePasswordAuthenticationToken(username, null, emptyList())
            SecurityContextHolder.getContext().authentication = authentication
            LOGGER.info("authenticated user:{}", username)
//                val userDetails: UserDetails = this.userDetailsService.loadUserByUsername(username)
//                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
//                if (jwtTokenUtil.validateToken(authToken)) {
//                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
//                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
//                    LOGGER.info("authenticated user:{}", username)
//                    SecurityContextHolder.getContext().authentication = authentication
//                }
            }
        }
        chain.doFilter(request, response)
    }

}