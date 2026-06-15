package com.ems.userservice.mapper

import com.ems.userservice.domain.User
import com.ems.userservice.dto.response.UserResponse

fun User.toResponse() = UserResponse(
    id = id,
    email = email,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
