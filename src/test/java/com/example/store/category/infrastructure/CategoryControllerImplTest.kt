package com.example.store.category.infrastructure

import com.example.store.auth.infrastructure.RegisterRequest
import com.example.store.auth.infrastructure.TokenResponse
import com.example.store.category.domain.Category
import com.example.store.category.infrastructure.dto.CategoryDTO
import com.example.store.category.infrastructure.repository.QueryCategoryRepository
import com.example.store.product.infrastructure.repository.QueryProductRepository
import com.example.store.user.infrastructure.repository.QueryUserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryControllerImplTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val productRepository: QueryProductRepository,
    @Autowired private val categoryRepository: QueryCategoryRepository,
    @Autowired private val userRepository: QueryUserRepository
) {
    private var JWTadmin: String = ""

    private val objectMapper = ObjectMapper()

    @BeforeAll
    fun setUpAll(){

        productRepository.deleteAll()
        categoryRepository.deleteAll()
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

        JWTadmin = response.accessToken
    }

    @AfterEach
    fun tearDown() {
        productRepository.deleteAll()
        categoryRepository.deleteAll()
    }

    @AfterAll
    fun tearDownAll() {
        userRepository.deleteAll()
    }

    @Test
    fun save() {
        val categoryDTO = CategoryDTO("category 1")
        val categoryJson = objectMapper.writeValueAsString(categoryDTO)

        val mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
                    .contentType("application/json")
                    .content(categoryJson)
                    .header("Authorization", "Bearer $JWTadmin"))
            .andExpect(status().isCreated)
            .andReturn()

        val response = mockMvcResult.response.contentAsString
        val category = objectMapper.readValue(response, Category::class.java)

        assertNotNull(category.id)

        assertEquals("category 1", category.name)

        assertEquals(1, categoryRepository.count())
    }

    @Test
    fun findAll(){
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories"))
            .andExpect(status().isOk)
            .andReturn()
    }

    @Test
    fun findById(){
        val categoryDTO = CategoryDTO("category 1")
        val categoryJson = objectMapper.writeValueAsString(categoryDTO)

        val mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
                    .contentType("application/json")
                    .content(categoryJson)
                    .header("Authorization", "Bearer $JWTadmin"))
            .andExpect(status().isCreated)
            .andReturn()

        val response = mockMvcResult.response.contentAsString
        val category = objectMapper.readValue(response, Category::class.java)

        val mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories/${category.id}"))
            .andExpect(status().isOk)
            .andReturn()

        val jsonResponse = mvcResult.response.contentAsString
        val categoryFind = objectMapper.readValue(jsonResponse, Category::class.java)

        assertEquals(category.id, categoryFind.id)
        assertEquals("category 1", categoryFind.name)

        assertEquals(1, categoryRepository.count())
    }

    @Test
    fun update(){
        val categoryDTO = CategoryDTO("category 1")
        val categoryJson = objectMapper.writeValueAsString(categoryDTO)

        val mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
                    .contentType("application/json")
                    .content(categoryJson)
                    .header("Authorization", "Bearer $JWTadmin"))
            .andExpect(status().isCreated)
            .andReturn()

        val response = mockMvcResult.response.contentAsString
        val category = objectMapper.readValue(response, Category::class.java)

        val categoryDTOUpdate = CategoryDTO("category 2")
        val categoryJsonUpdate = objectMapper.writeValueAsString(categoryDTOUpdate)

        val mockMvcResultUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/categories/${category.id}")
                .contentType("application/json")
                .content(categoryJsonUpdate)
                .header("Authorization", "Bearer $JWTadmin"))
            .andExpect(status().isOk)
            .andReturn()

        val responseUpdate = mockMvcResultUpdate.response.contentAsString
        val categoryUpdate = objectMapper.readValue(responseUpdate, Category::class.java)

        assertEquals(category.id, categoryUpdate.id)
        assertEquals("category 2", categoryUpdate.name)

        assertEquals(1, categoryRepository.count())
    }

    @Test
    fun delete(){
        val categoryDTO = CategoryDTO("category 1")
        val categoryJson = objectMapper.writeValueAsString(categoryDTO)

        val mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/categories")
                    .contentType("application/json")
                    .content(categoryJson)
                    .header("Authorization", "Bearer $JWTadmin"))
            .andExpect(status().isCreated)
            .andReturn()

        val response = mockMvcResult.response.contentAsString
        val category = objectMapper.readValue(response, Category::class.java)

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/categories/${category.id}")
                    .header("Authorization", "Bearer $JWTadmin"))
            .andExpect(status().isNoContent)
            .andReturn()

        assertEquals(0, categoryRepository.count())
    }
}