package co.nz.small.app.controllers

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import co.nz.small.app.AppApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.OK
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.*
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [AppApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TitlesControllersTest {
    var token: String = ""

    @LocalServerPort
    private val port = 8080

    @BeforeAll
    fun getToken() {
        RestAssured.baseURI = createURLWithPort()
        RestAssured.port= port
        token = getTokenByLogon().substringAfter("token=").dropLast(1)
    }

    @Test
    @Order(1)
    fun thatGetLatestTitleReturns200AndTitleEntitySuccess() {
        var requestSpec = RequestSpecBuilder()
            .setBasePath("/api/titles/{userId}/latest")
            .addPathParam("userId",2)
            .addHeader(HttpHeaders.AUTHORIZATION, "Bearer $token")
            .build()
        val response =
            given()
                .spec(requestSpec)
            .`when`()
                .get()
            .then()
                .statusCode(OK.value())
                .assertThat().body("ownerName", `is`("Bob Smith"))
                .assertThat().body("description", `is`("Lot 2 on Block 1"))
                .extract().body().asString()
        print(response)
    }

    @Test
    @Order(2)
    fun testUpdateTitleOwner() { // Check that the update request returns the expected response
        var requestSpec = RequestSpecBuilder()
            .setBasePath("/api/titles/1")
            .addHeader("Content-Type","application/json")
            .addHeader(HttpHeaders.AUTHORIZATION, "Bearer $token")
            .setBody("{\"id\":1,\"ownerName\":\"Brian Davies\"}")
            .build()

        given()
            .spec(requestSpec)
        .`when`()
            .post()
        .then()
            .statusCode(OK.value())
            .assertThat().body("ownerName", `is`("Brian Davies"))
            .assertThat().body("description", `is`("Lot 1 on Block 1"))

        requestSpec = RequestSpecBuilder()
            .setBasePath("/api/titles/{userId}/latest")
            .addPathParam("userId",1)
            .addHeader(HttpHeaders.AUTHORIZATION, "Bearer $token")
            .build()

        // Check that a fetch returns the updated owner name, and the original description
        given()
            .spec(requestSpec)
        .`when`()
            .get()
        .then()
            .statusCode(OK.value())
            .assertThat().body("ownerName", `is`("Brian Davies"))
            .assertThat().body("description", `is`("Lot 1 on Block 1"))
    }

    fun getTokenByLogon(): String{
        var requestSpec = RequestSpecBuilder()
            .setBasePath("/admin/login")
            .addHeader("Content-Type","application/json")
            .setBody("{\"username\":\"tester\",\"password\":\"123456\"}")
            .build()

        val response =
            given()
                .spec(requestSpec)
            .`when`()
                .post()
            .then()
                .statusCode(OK.value())
                .extract().body().asString()
        println(response)
        return response
    }

    private fun createURLWithPort(): String {
        return "http://localhost:$port"
    }
}