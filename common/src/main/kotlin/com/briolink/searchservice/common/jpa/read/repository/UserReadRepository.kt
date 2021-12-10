package com.briolink.searchservice.common.jpa.read.repository

import com.briolink.searchservice.common.jpa.read.entity.UserReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserReadRepository : JpaRepository<UserReadEntity, UUID>
