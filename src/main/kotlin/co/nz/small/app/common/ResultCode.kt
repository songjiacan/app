package co.nz.small.app.common

enum class ResultCode( var code: Long,  var message: String) {
    SUCCESS(200, "success"),
    FAILED(500, "Internal server error"),
    VALIDATE_FAILED(404, "Parameters failed verification"),
    BAD_REQUEST(400, "Bad request"),
    UNAUTHORIZED(401, "Not able to logon or token expired"),
    FORBIDDEN(403, "No access to related resources")
}