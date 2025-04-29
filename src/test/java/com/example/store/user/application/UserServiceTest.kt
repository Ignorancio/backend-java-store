package com.example.store.user.application

import com.example.store.user.domain.Role
import com.example.store.user.domain.User
import com.example.store.user.domain.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserServiceImpl

    @Test
    fun findByIdWhenUserIdNoExistShouldReturnThrowIllegalArgumentException() {
        val userId = UUID.randomUUID()
        Mockito.`when`(userRepository.findById(Mockito.any(UUID::class.java))).thenReturn(Optional.empty())

        val exception = assertThrows<IllegalArgumentException> {
            userService.findById(userId)
        }

        assertEquals("Usuario no encontrado", exception.message)
    }

    @Test
    fun findByIdWhenUserIdExistShouldReturnUser() {
        val userId = UUID.randomUUID()
        Mockito.`when`(userRepository.findById(Mockito.any(UUID::class.java))).thenReturn(Optional.of(
            User.builder()
                .id(userId)
                .email("JohnDoe@gmail.com")
                .roles(setOf(Role.USER))
                .password("password123")
                .build()
        ))

        val user = userService.findById(userId)

        assertEquals(userId, user.id)
    }

    @Test
    fun findAllShouldReturnListOfUsers() {
        Mockito.`when`(userRepository.findAll()).thenReturn(listOf())

        val users = userService.findAll()

        assertEquals(0, users.size)
    }
}