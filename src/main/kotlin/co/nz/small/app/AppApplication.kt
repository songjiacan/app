package co.nz.small.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@ComponentScan(includeFilters = [ComponentScan.Filter(Service::class), ComponentScan.Filter(Component::class)])
@EntityScan(basePackages= ["co.nz.small.app.models"])
@EnableJpaRepositories(basePackages= ["co.nz.small.app.persistence","co.nz.small.app.models"])
@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class AppApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
	runApplication<AppApplication>(*args)
}
