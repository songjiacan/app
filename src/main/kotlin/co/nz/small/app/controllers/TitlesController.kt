package co.nz.small.app.controllers

import co.nz.small.app.exception.ApiException
import co.nz.small.app.models.Title
import co.nz.small.app.service.TitleService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder.*
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.validation.Valid
import kotlin.reflect.jvm.internal.impl.serialization.deserialization.FlexibleTypeDeserializer

@RestController
@RequestMapping("/api/titles")
class TitlesController(val titleService: TitleService) {

    @GetMapping("/{id}/latest")
    fun getLatestTitle(@PathVariable id: Long): ResponseEntity<Title> {
        val title = titleService.getLatestTitleByID(id)

        return if (title == null)
            ResponseEntity.notFound().build()
        else
            ResponseEntity.ok().body(title)
    }

    @GetMapping("/{id}")
    fun getTitle(@PathVariable id: Long): ResponseEntity<List<Title>> {
        val titles = titleService.getTitleListsByID(id)
        return if (titles.isEmpty())
            ResponseEntity.notFound().build()
        else
            ResponseEntity.ok().body(titles)
    }

    @PostMapping("/{id}")
    fun updateTitle(@PathVariable id: Long, @Valid @RequestBody body: Title): ResponseEntity<Title> {
        val title = titleService.getLatestTitleByID(id)
        return if (title == null)
            ResponseEntity.notFound().build()
        else {
            if (body.ownerName.isNullOrBlank())
            {
                throw ApiException("owner name cannot be empty or blank")
            }
            if (body.ownerName == title.ownerName)
            {
                throw ApiException("owner name cannot be same as previous owner's name")
            }

            val titleNew = Title(
                title.description,
                body.ownerName,
                title.ownerName,
                id,
                LocalDateTime.now(),
                getContext().authentication.name
            )
            titleService.save(titleNew)
            ResponseEntity.ok().body(titleNew)
        }
    }
}