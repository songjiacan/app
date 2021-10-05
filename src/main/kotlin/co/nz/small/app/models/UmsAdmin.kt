package co.nz.small.app.models

//import io.ebean.Model
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class UmsAdmin (
        var username: String,
        var password: String,
        var email: String,
        var nick_name: String,
        var role: String,
        var create_time: String,
        var login_time: String,
        var status: String,
        var lastModified: LocalDateTime
)
{
    @Id
    @GeneratedValue
    val id: Long = 0

    override fun toString(): String {
        return "UmsAdmin(id=$id, username='$username', email='$email', status=$status, lastModified=$lastModified, create_time=$create_time)"
    }

}
