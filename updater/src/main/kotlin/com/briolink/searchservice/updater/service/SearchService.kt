package com.briolink.searchservice.updater.service

import com.briolink.searchservice.common.jpa.enumeration.SearchTypeEnum
import com.briolink.searchservice.common.jpa.read.entity.SearchReadEntity
import com.briolink.searchservice.common.jpa.read.repository.SearchReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Transactional
@Service
class SearchService(private val searchReadRepository: SearchReadRepository) {
    fun createSearchItem(objectId: UUID, name: String, type: SearchTypeEnum): SearchReadEntity {
        if (!searchReadRepository.existsByNameAndObjectIds(
                name = name,
                objectId = objectId,
                type = type.value
            )
        ) {
            val searchReadEntity = searchReadRepository.findByNameAndType(name, type.value)
            if (searchReadEntity.isEmpty) {
                searchReadRepository.findByObjectIdAndType(objectId, type.value).also {
                    if (it.isPresent) removeObjectId(objectId, type)
                }
                return searchReadRepository.save(
                    SearchReadEntity(name = name).apply {
                        objectIds = mutableListOf(objectId)
                        this.type = type
                    }
                )
            } else {
                searchReadEntity.get().apply {
                    objectIds.add(objectId)
                    return searchReadRepository.save(this)
                }
            }
        } else return SearchReadEntity(name = name).apply {
            objectIds.add(objectId)
            this.type = type
        }
    }

    fun deleteSearchItem(objectId: UUID, name: String, type: SearchTypeEnum) {
        val searchReadEntity = searchReadRepository.findByNameAndType(name, type.value)
        if (searchReadEntity.isPresent)
            searchReadEntity.get().apply {
                objectIds.remove(objectId)
                if (objectIds.isEmpty()) searchReadRepository.delete(this)
            }
    }

    fun removeObjectId(objectId: UUID, type: SearchTypeEnum) {
        val searchReadEntity = searchReadRepository.findByObjectIdAndType(objectId, type.value)
        if (searchReadEntity.isPresent) {
            searchReadEntity.get().apply {
                objectIds.remove(objectId)
                if (objectIds.isEmpty()) searchReadRepository.delete(this)
                else searchReadRepository.save(this)
            }
        }
    }
}
