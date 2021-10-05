package co.nz.small.app.dto

import co.nz.small.app.models.UmsAdmin
import co.nz.small.app.models.UmsPermission
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

class AdminUserDetails(val umsAdmin: UmsAdmin, val permissionList: List<UmsPermission>): UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        //return current users' permission
        return permissionList.stream()
//                .filter { permission: UmsPermission -> permission.value != null }
                .map { permission: UmsPermission -> SimpleGrantedAuthority(permission.value) }
                .collect(Collectors.toList())
    }

    override fun getPassword(): String? {
        return umsAdmin.password
    }

    override fun getUsername(): String? {
        return umsAdmin.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return umsAdmin.status.equals(1)
    }
}