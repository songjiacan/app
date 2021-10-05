package co.nz.small.app.dto

data class WebLog(
        val description: String?="",
        val username: String?="",
        var startTime: Long?=0,
        var spendTime: Int?=0,
        var basePath: String?="",
        var uri: String?="",
        var url: String?="",
        var method: String?="",
        var ip: String?="",
        var authToken: String?="",
        var requestedSessionId: String?="",
        var parameter : Any?= "",
        var result: Any?=""
)