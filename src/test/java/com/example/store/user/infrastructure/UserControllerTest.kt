package com.example.store.user.infrastructure

import com.example.store.auth.infrastructure.RegisterRequest
import com.example.store.user.infrastructure.repository.QueryUserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserControllerTest(
    private val mockMvc: MockMvc,
    private val userRepository: QueryUserRepository
) {
    private lateinit var cookieAdmin: String

    private val objectMapper = ObjectMapper()

    @BeforeAll
    fun setUpAll() {
        userRepository.deleteAll()

        val authRequest = RegisterRequest("admin@admin", "admin")
        val json = objectMapper.writeValueAsString(authRequest)

        val mvcResult = mockMvc.perform(
            MockMvcRequestBuilders.post("/auth/register/admin")
                .contentType("application/json")
                .content(json)
        )
            .andExpect(status().isOk)
            .andReturn()

        cookieAdmin = mvcResult.response.getCookie("access_token")?.value ?: ""
    }

    @AfterAll
    fun tearDownAll() {
        userRepository.deleteAll()
    }

    @Test
    fun findAllUsersShouldReturnAllUsers() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/users")
                .cookie(Cookie("access_token", cookieAdmin))
        )
            .andExpect(status().isOk)
            .andReturn()
    }

}