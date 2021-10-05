package co.nz.small.app

import co.nz.small.app.models.Title
import co.nz.small.app.persistence.TitleRepo
import co.nz.small.app.persistence.UmsRepo
import co.nz.small.app.service.TitleService
import co.nz.small.app.service.impl.TitleServiceImpl
import com.google.common.util.concurrent.Runnables.doNothing
//import co.nz.small.app.service.UmsAdminService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.justRun
import io.mockk.mockk
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@DataJpaTest
class UmsControllerMockkTest(@Autowired val titleService:  TitleServiceImpl) {
//    @MockkBean
//    lateinit var adminService: UmsAdminService

//    @InjectMockKs
//    lateinit var titleRepo: TitleRepo

//    @MockkBean
//    lateinit var UmsRepo: UmsRepo


//    @Test
//    fun `No exist user`() {
//        every {adminService.getAdminByUsername("No exist user")} returns null
//        mockMvc.perform(MockMvcRequestBuilders
//            .post("/admin/login")
//            .content("{\"username\":\"No exist user\",\"password\":\"123456\"}")
//            .accept(MediaType.APPLICATION_JSON)
//            )
//            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
////            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.TEXT_PLAIN))
//            .andExpect(content().string("user name not found"))
//    }

    @Test
    fun `Get Title by ID`() {
        var title: Title = Title(
            "Lot 2 on Block 1",
            "Bob Smith",
            "Jack Song",
            2,
            LocalDateTime.now(),
            "System"
        )
        val titleRepo = mockk<TitleRepo>()
        var titleList: List<Title> = mutableListOf(title)
//        doNothing().when (titleRepo).save(Any())
        every { titleRepo.save(title) } returns title
        every { titleRepo.findTitleListsByID(2) } returns titleList
//        io.mockk.every {titleRepo.findTitleListsByID(1)}
        //titleService.getTitleListsByID(1)
        Assert.assertEquals(titleList.first().toString(),titleService.getTitleListsByID(2).first().toString())
    }
}



//having issues with initializer, Field titleRepo in co.nz.small.app.service.impl.TitleServiceImpl required a bean named 'entityManagerFactory' that could not be found.
//https://stackoverflow.com/questions/42182426/spring-boot-repository-field-required-a-bean-named-entitymanagerfactory-that/52027772