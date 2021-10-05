package co.nz.small.app.config

import co.nz.small.app.component.JwtAuthenticationTokenFilter
import co.nz.small.app.component.RestAuthenticationEntryPoint
import co.nz.small.app.component.RestfulAccessDeniedHandler
import co.nz.small.app.service.UmsAdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * SpringSecurity
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private val adminService: UmsAdminService? = null

    @Autowired
    private val restfulAccessDeniedHandler: RestfulAccessDeniedHandler? = null

    @Autowired
    private val restAuthenticationEntryPoint: RestAuthenticationEntryPoint? = null

    @Autowired
    private val jwtAuthenticationTokenFilter: JwtAuthenticationTokenFilter? = null

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity.csrf() // 由于使用的是JWT，我们这里不需要csrf
            .disable()
            .sessionManagement() // 基于token，所以不需要session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(
                HttpMethod.GET,  // 允许对于网站静态资源的无授权访问
                "/",
                "/*.html",
                "/favicon.ico",
                "/**/*.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/admin"
            )
            .permitAll()
            .antMatchers(
                "/admin/login",
                "/admin/register",
                "/api/todo/**",
                "/h2-console/**"
            ) // 对登录注册要允许匿名访问,allow any request from to do list
            .permitAll()
            .antMatchers(HttpMethod.OPTIONS) //跨域请求会先进行一次options请求
            .permitAll()
            //.antMatchers("/**") // let all request go through when test 测试时全部运行访问
            //.permitAll()
            .anyRequest() // other requests other than above setting all need auth除上面外的所有请求全部需要鉴权认证
            .authenticated()

        httpSecurity
            .headers()
            .cacheControl()    // Disable cache
            .and()
            .frameOptions().sameOrigin();  //allow h2-console to access from same origin
        // Add JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
        //        httpSecurity.addFilterBefore( authenticationJwtTokenFilter(),  UsernamePasswordAuthenticationFilter.class);
        // Add handler for un-auth and un-logon, then return custom results
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
        httpSecurity.exceptionHandling()
            .authenticationEntryPoint { _: HttpServletRequest?, response: HttpServletResponse, ex: AuthenticationException ->
                response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    ex.message
                )
            }
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService())
            .passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    //    @Bean
    //    public UserDetailsService userDetailsService() {
    //        //获取登录用户信息
    //        return username -> {
    //            UmsAdmin admin = adminService.getAdminByUsername(username);
    //            if (admin != null) {
    ////                List<UmsPermission> permissionList = adminService.getPermissionList(admin.getId());
    //                List<UmsPermission> permissionList = new ArrayList<>();
    //                permissionList.add(new UmsPermission("",""));
    //                return new AdminUserDetails(admin,permissionList);
    //            }
    //            throw new UsernameNotFoundException("user name or password not found");
    //        };
    //    }
    //    @Bean
    //    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
    //        return new JwtAuthenticationTokenFilter();
    //    }
    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}