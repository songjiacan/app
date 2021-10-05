package co.nz.small.app.service

import co.nz.small.app.models.UmsAdmin

interface UmsAdminService {
    fun getAdminByUsername(username: String): UmsAdmin?
    fun login(username: String, password: String): String
}