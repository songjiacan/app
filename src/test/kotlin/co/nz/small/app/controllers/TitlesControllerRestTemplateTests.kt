package co.nz.small.app.controllers

import co.nz.small.app.AppApplication
import co.nz.small.app.models.Title
import org.junit.Assert
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.LinkedMultiValueMap

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [AppApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TitlesControllerRestTemplateTests {

    @LocalServerPort
    private val port = 0

    var restTemplate = TestRestTemplate()
    var getHeaders = HttpHeaders()
    var postHeaders = HttpHeaders(LinkedMultiValueMap(mapOf(
        "Content-Type" to listOf("application/json")
    )))

    @BeforeAll
    fun getToken() {
        val response = post("/admin/login","{\"username\":\"tester\",\"password\":\"123456\"}")
        val tokenStr = response.body?.toString()
        val token = tokenStr?.substringAfter("token=")?.dropLast(1)
        getHeaders.add(AUTHORIZATION, "Bearer $token")
        postHeaders.add(AUTHORIZATION, "Bearer $token")
    }

    @Test
    fun testGetTitle() {
        val response = get("/api/titles/1/latest", Title::class.java)
        Assert.assertEquals("Jane Doe", response.body?.ownerName)
        Assert.assertEquals("Lot 1 on Block 1", response.body?.description)
        //Assert.assertThat(response.body.ownerName, is("Jane Doe"))   //need org.hamcrest.CoreMatchers
    }

    @Test
    fun testUpdateTitleOwner() { // Check that the update request returns the expected response
        var response = post("/api/titles/1", "{\"id\":1,\"ownerName\":\"Brian Davies\"}", Title::class.java)
        Assert.assertEquals("Brian Davies", response.body?.ownerName)
        Assert.assertEquals("Lot 1 on Block 1", response.body?.description)

        // Check that a fetch returns the updated owner name, and the original description
        response = get("/api/titles/1/latest", Title::class.java)
        Assert.assertEquals("Brian Davies", response.body?.ownerName)
        Assert.assertEquals("Lot 1 on Block 1", response.body?.description)
        Assert.assertEquals("tester", response.body?.modifiedBy)
    }

    private fun get(url: String): ResponseEntity<String> {
        val response = restTemplate.exchange(
            createURLWithPort(url),
            HttpMethod.GET,
            HttpEntity(null, getHeaders),
            String::class.java
        )
        println(response.body)
        return response
    }

    private fun <T> get(url: String, responseType: Class<T>): ResponseEntity<T> {
        return restTemplate.exchange(
            createURLWithPort(url),
            HttpMethod.GET,
            HttpEntity(null, getHeaders),
            responseType
        )
    }

    private fun post(url: String, body: String): ResponseEntity<String> {
        val response = restTemplate.exchange(createURLWithPort(url),
                HttpMethod.POST, HttpEntity(body, postHeaders), String::class.java)
        println(response.body)
        return response
    }

    private fun <T> post(url: String, body: String, responseType: Class<T>): ResponseEntity<T> {
        return restTemplate.exchange(createURLWithPort(url),
            HttpMethod.POST, HttpEntity(body, postHeaders), responseType)
    }

    private fun createURLWithPort(uri: String): String {
        return "http://localhost:$port$uri"
    }
}