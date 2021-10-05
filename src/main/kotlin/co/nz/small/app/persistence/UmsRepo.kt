package co.nz.small.app.persistence

import co.nz.small.app.models.UmsAdmin
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface UmsRepo : CrudRepository<UmsAdmin, Long> {

    @Query("SELECT u FROM UmsAdmin u WHERE u.username = :username")
    fun findByUsername(@Param("username") username: String): UmsAdmin?
}

//interface UserRepository : CrudRepository<User, Long> {
//    fun findByLogin(login: String): User?
//}







//EntityManagerFactory emf = Persistence.createEntityManagerFactory("UmsAdminPU");
//        em = emf.createEntityManager();
//        em.getTransaction().begin();
//        UmsAdmin emp = new UmsAdmin(id, firstName, lastName, dept);
//        em.persist(emp);
//        em.getTransaction().commit();
//    }
