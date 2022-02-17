package com.briolink.searchservice.common.jpa.read.repository

import com.briolink.searchservice.common.jpa.read.entity.SyncLogPK
import com.briolink.searchservice.common.jpa.read.entity.SyncLogReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SyncLogReadRepository : JpaRepository<SyncLogReadEntity, SyncLogPK> {

    @Query("""SELECT count(1) > 0 FROM SyncLogReadEntity s WHERE s.syncId = ?1 AND s._objectSync = ?2 AND s._service = ?3 AND s.completed IS NULL""") // ktlint-disable max-line-length
    fun existsNotCompletedBySyncIdAndObjectSyncAndServiceId(syncId: Int, objectSync: Int, serviceId: Int): Boolean

    @Query("""SELECT s FROM SyncLogReadEntity s WHERE s.syncId = ?1 AND s._service = ?2""")
    fun findBySyncIdAndServiceId(syncId: Int, serviceId: Int): List<SyncLogReadEntity>

    @Query("SELECT count(1) > 0 FROM SyncLogReadEntity s WHERE s.syncId = ?1 AND s._service = ?2 AND s.completed IS NULL OR s.withError = true") // ktlint-disable max-line-length
    fun existsNotCompletedAndOrWithErrorBySyncIdAndServiceId(syncId: Int, serviceId: Int): Boolean

    @Query("SELECT count(1) > 0 FROM SyncLogReadEntity s WHERE s.syncId = ?1 AND s._service = ?2")
    fun existsBySyncIdAndServiceId(syncId: Int, serviceId: Int): Boolean
}
