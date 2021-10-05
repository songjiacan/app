package co.nz.small.app.persistence

import org.assertj.core.api.Assertions.assertThat
import co.nz.small.app.models.Title
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.LocalDateTime

@DataJpaTest
class RepositoriesTests @Autowired constructor(
    val entityManager: TestEntityManager,
    val titleRepo: TitleRepo){

    @Test
    fun `When findTitleListsByID then return Title lists`() {
        val title = Title("Lot 3 on Block 1", "Phil Lee", "Phil Lee",3, LocalDateTime.now(),"System")
        entityManager.merge(title)
        entityManager.flush()
        val found = titleRepo.findTitleListsByID(3)
        assertThat(found).hasSize(2).first().isEqualToIgnoringGivenFields(title,"uuid","lastModified")
    }

}