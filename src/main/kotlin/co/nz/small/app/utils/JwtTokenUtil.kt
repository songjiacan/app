package co.nz.small.app.utils

import cn.hutool.core.date.DateUtil
import cn.hutool.core.util.StrUtil
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.HashMap


@Component
class JwtTokenUtil {
    private val logger = LoggerFactory.getLogger(JwtTokenUtil::class.java)
    private val clainKeyUsername = "sub"
    private val claimKeyCreated = "created"

    @Value("\${jwt.secret}")
    private val secret: String? = null

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    @Value("\${jwt.tokenHead}")
    private val tokenHead: String? = null

    @Value("\${jwt.tokenHeader}")
    val tokenHeader: String = ""


    /**
     * 根据负责生成JWT的token
     */
    private fun generateToken(claims: Map<String, Any>): String {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    /**
     * 从token中获取JWT中的负载
     */
    private fun getClaimsFromToken(token: String): Claims? {
        var claims: Claims?
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .body
        } catch (e: Exception) {
            logger.info("JWT verification failed:{}", token)
            throw UsernameNotFoundException("JWT verification failed")
        }
        return claims
    }

    /**
     * 生成token的过期时间
     */
    private fun generateExpirationDate(): Date? {
        return Date(System.currentTimeMillis() + expiration!! * 1000)
    }

    /**
     * 从token中获取登录用户名
     */
    fun getUserNameFromToken(token: String): String? {
        var username: String = try {
            val claims = getClaimsFromToken(token)
            claims!!.subject
        } catch (e: Exception) {
            return null
        }
        return if (!isTokenExpired(token))
            username
        else
            null
    }

    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUserNameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun validateToken(token: String): Boolean {
        return  !isTokenExpired(token)
    }

    /**
     * 判断token是否已经失效
     */
    private fun isTokenExpired(token: String): Boolean {
        val expiredDate = getExpiredDateFromToken(token)
        return expiredDate.before(Date())
    }

    /**
     * 从token中获取过期时间
     */
    private fun getExpiredDateFromToken(token: String): Date {
        val claims = getClaimsFromToken(token)
        return claims!!.expiration
    }

    /**
     * 根据用户信息生成token
     */
    fun generateToken(userDetails: String): String {
        val claims: MutableMap<String, Any> = HashMap()
//        claims[clainKeyUsername] = userDetails.username
        claims[clainKeyUsername] = userDetails
        claims[claimKeyCreated] = Date()
        return generateToken(claims)
    }

    /**
     * 当原来的token没过期时是可以刷新的
     *
     * @param oldToken 带tokenHead的token
     */
    fun refreshHeadToken(oldToken: String): String? {
        if (StrUtil.isEmpty(oldToken)) {
            return null
        }
        val token = oldToken.substring(tokenHead!!.length)
        if (StrUtil.isEmpty(token)) {
            return null
        }
        //token校验不通过
        val claims = getClaimsFromToken(token) ?: return null
        //如果token已经过期，不支持刷新
        if (isTokenExpired(token)) {
            return null
        }
        //如果token在30分钟之内刚刷新过，返回原token
        return if (tokenRefreshJustBefore(token, 30 * 60)) {
            token
        } else {
            claims[claimKeyCreated] = Date()
            generateToken(claims)
        }
    }

    /**
     * 判断token在指定时间内是否刚刚刷新过
     * @param token 原token
     * @param time 指定时间（秒）
     */
    private fun tokenRefreshJustBefore(token: String, time: Int): Boolean {
        val claims = getClaimsFromToken(token)
        val created = claims!!.get(claimKeyCreated, Date::class.java)
        val refreshDate = Date()
        //刷新时间在创建时间的指定时间内
        return if (refreshDate.after(created) && refreshDate.before(DateUtil.offsetSecond(created, time))) {
            true
        } else false
    }
}