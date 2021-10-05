package co.nz.small.app.config

import co.nz.small.app.models.Title
import co.nz.small.app.models.UmsAdmin
import co.nz.small.app.persistence.TitleRepo
import co.nz.small.app.persistence.UmsRepo
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

@Configuration
class DataConfiguration {

    @Bean
    fun databaseInitializer(umsRepo: UmsRepo,titleRepo: TitleRepo) = ApplicationRunner {
        titleRepo.save(Title("Lot 1 on Block 1", "Jane Doe", "Jane Doe",1, LocalDateTime.now(),"System"))
        titleRepo.save(Title("Lot 2 on Block 1", "Bob Smith", "Bob Smith",2, LocalDateTime.now(),"System"))
        titleRepo.save(Title("Lot 3 on Block 1", "Phil Lee", "Phil Lee",3, LocalDateTime.now(),"System"))

        //use connection string from spring.datasource.url  by default use name jdbc:h2:mem:testdb
        umsRepo.save(UmsAdmin("tester", "\$2a\$10\$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG", "songjiacan@gmail.com","tester","tester", LocalDateTime.now().toString(),LocalDateTime.now().toString(),"1",LocalDateTime.now()))
        umsRepo.save(UmsAdmin("admin", "\$2a\$10\$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG", "songjiacan@gmail.com","admin","admin", LocalDateTime.now().toString(),LocalDateTime.now().toString(),"1",LocalDateTime.now()))
        umsRepo.save(UmsAdmin("jack", "\$2a\$10\$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG", "songjiacan@gmail.com","Solicitor","user", LocalDateTime.now().toString(),LocalDateTime.now().toString(),"1",LocalDateTime.now()))

        for (user in umsRepo.findAll()) {
            println("User info: $user")
        }

        for (title in titleRepo.findAll()) {
            println("Title info: $title")
        }
    }
}


//CommandLineRunner need to parse parameters, while ApplicationRunner doesn't need to.