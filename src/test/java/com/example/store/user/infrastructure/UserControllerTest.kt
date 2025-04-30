package com.example.store.user.infrastructure

import com.example.store.auth.infrastructure.RegisterRequest
import com.example.store.auth.infrastructure.TokenResponse
import com.example.store.user.infrastructure.repository.QueryUserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val userRepository: QueryUserRepository
) {
    private lateinit var jwtAdmin: String

    private val objectMapper = ObjectMapper()

    @BeforeAll
    fun setUpAll() {
        userRepository.deleteAll()

        val authRequest = RegisterRequest("admin@admin", "admin")
        val json = objectMapper.writeValueAsString(authRequest)

        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/auth/register/admin")
            .contentType("application/json")
            .content(json))
            .andExpect(status().isOk)
            .andReturn()

        val jsonResponse = mvcResult.response.contentAsString
        val response = objectMapper.readValue(jsonResponse, TokenResponse::class.java)
        jwtAdmin = response.accessToken
    }

    @AfterAll
    fun tearDownAll() {
        userRepository.deleteAll()
    }

    @Test
    fun findAllUsersShouldReturnAllUsers() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
            .header("Authorization", "Bearer $jwtAdmin"))
            .andExpect(status().isOk)
            .andReturn()
    }

}