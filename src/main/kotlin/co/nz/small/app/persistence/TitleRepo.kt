package co.nz.small.app.persistence

import co.nz.small.app.models.Title
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.UUID

interface TitleRepo : CrudRepository<Title, UUID> {

    @Query("SELECT t FROM Title t WHERE t.id = :id")
    fun findTitleListsByID(@Param("id") id: Long): List<Title>
}