package co.nz.small.app.service.impl

import co.nz.small.app.models.Title
import co.nz.small.app.persistence.TitleRepo
import co.nz.small.app.service.TitleService
import org.springframework.stereotype.Service

@Service
class TitleServiceImpl(val titleRepo: TitleRepo): TitleService {

    override fun getTitleListsByID(id: Long): List<Title> {
       return titleRepo.findTitleListsByID(id)
    }

    override fun save(title: Title) {
        titleRepo.save(title)
    }

    override fun getLatestTitleByID(id: Long): Title? {
        val titleList = titleRepo.findTitleListsByID(id)
        return if (titleList.isEmpty())
         null
        else {
            titleList.sortedBy { it.lastModified }.last()
        }

    }
}