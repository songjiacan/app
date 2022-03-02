package co.nz.small.app.controllers

import co.nz.small.app.models.Title
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hello")
class HelloController {

    @GetMapping("/api")
    fun getTitle(): ResponseEntity<String> {
        return ResponseEntity.ok().body("I am alive")
    }
}