package co.nz.small.app.models

import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
class Title (
    var description: String?,
    @NotBlank(message= "Owner name is mandatory, cannot be empty")
    var ownerName: String,
    var previousOwnerName: String?,
    val id: Long,
    var lastModified: LocalDateTime?,
    var modifiedBy: String?
)
{
    @Id
    @GeneratedValue
    val uuid: UUID = UUID.randomUUID()


    override fun toString(): String {
        return "Title(description='$description', ownerName='$ownerName', id=$id)"
    }
}


//@Entity
//class User(
//    var login: String,
//    var firstname: String,
//    var lastname: String,
//    var description: String? = null,
//    @Id @GeneratedValue var id: Long? = null)