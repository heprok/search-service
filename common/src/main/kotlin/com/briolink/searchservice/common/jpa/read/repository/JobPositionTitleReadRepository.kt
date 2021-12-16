package com.briolink.searchservice.common.jpa.read.repository

import com.briolink.searchservice.common.jpa.read.entity.JobPositionTitleReadEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional
import java.util.UUID

interface JobPositionTitleReadRepository : JpaRepository<JobPositionTitleReadEntity, UUID> {
    @Query("SELECT c FROM JobPositionTitleReadEntity c WHERE lower(c.name) = lower(?1)")
    fun findByName(name: String): Optional<JobPositionTitleReadEntity>

    @Query("SELECT c FROM JobPositionTitleReadEntity c WHERE (:query is null or function('fts_partial', c.name, :query) = true)")
    fun getAutocompleteByName(
        @Param("query") query: String?,
        pageable: Pageable = Pageable.ofSize(10)
    ): List<JobPositionTitleReadEntity>
}
