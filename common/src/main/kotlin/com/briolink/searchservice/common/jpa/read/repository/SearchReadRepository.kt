package com.briolink.searchservice.common.jpa.read.repository

import com.briolink.searchservice.common.jpa.read.entity.SearchReadEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional
import java.util.UUID

interface SearchReadRepository : JpaRepository<SearchReadEntity, UUID> {
    @Query("SELECT s FROM SearchReadEntity s WHERE (:query is null or function('fts_partial', s.name, :query) = true) AND s._type = :type")
    fun getAutocompleteByNameAndType(
        @Param("query") query: String?,
        @Param("type") type: Int,
        pageable: Pageable = Pageable.ofSize(10)
    ): List<SearchReadEntity>

    @Query("SELECT s FROM SearchReadEntity s WHERE s.name = ?1 AND s._type = ?2")
    fun findByNameAndType(name: String, type: Int): Optional<SearchReadEntity>

    @Query("SELECT s FROM SearchReadEntity s WHERE function('array_contains_element', s.objectIds, ?1) = true AND s._type = ?2")
    fun findByObjectIdAndType(companyId: UUID, type: Int): Optional<SearchReadEntity>

    @Query("SELECT (count(s) > 0) FROM SearchReadEntity s WHERE function('array_contains_element', s.objectIds, :objectId) = true AND s._type = :type AND s.name = :name") // ktlint-disable max-line-length
    fun existsByNameAndObjectIds(
        @Param("name") name: String,
        @Param("objectId") objectId: UUID,
        @Param("type") type: Int
    ): Boolean

    @Query("SELECT s FROM SearchReadEntity s WHERE (?1 is null or function('fts_partial', s.name, ?1) = true) AND s._type IN ?2")
    fun getAutocompleteByNameAndTypes(
        query: String?,
        searchTypes: List<Int>?,
        pageable: Pageable = Pageable.ofSize(10)
    ): List<SearchReadEntity>

//
//    @Query("SELECT s FROM SearchReadEntity s WHERE function('array_contains_element', s.objectIds, :objectId) = true AND s._type = :type AND s.name = :name") // ktlint-disable max-line-length
//    fun findByNameAndObjectIdAndType(@Param("name") name: String, @Param("objectId") objectId: UUID, @Param("type") type: Int): Optional<SearchReadEntity>
}
