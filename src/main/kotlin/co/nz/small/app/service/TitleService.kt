package co.nz.small.app.service

import co.nz.small.app.models.Title

interface TitleService {
    fun getTitleListsByID(id: Long): List<Title>
    fun save(title: Title)
    fun getLatestTitleByID(id: Long): Title?
}