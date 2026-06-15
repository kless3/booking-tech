package com.ems.userservice.repository

import com.ems.userservice.domain.User
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, UUID> {
    fun existsByEmail(email: String): Boolean
}
