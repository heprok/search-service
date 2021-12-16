package com.briolink.searchservice.api.service.user

import com.blazebit.persistence.CriteriaBuilderFactory
import com.blazebit.persistence.PagedList
import com.briolink.searchservice.api.dto.SortDirectionEnum
import com.briolink.searchservice.api.service.user.dto.UserSortDto
import com.briolink.searchservice.common.jpa.read.entity.UserReadEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Service
@Transactional
class UserService(
    private val entityManager: EntityManager,
    private val criteriaBuilderFactory: CriteriaBuilderFactory,
) {
    fun getList(
        sort: UserSortDto? = null,
        offset: Int = 0,
        limit: Int = 10,
    ): PagedList<UserReadEntity> {
        val cbf = criteriaBuilderFactory.create(entityManager, UserReadEntity::class.java)
        val cb = cbf.from(UserReadEntity::class.java)

        if (sort != null)
            cb.orderBy(sort.key.field, sort.direction == SortDirectionEnum.ASC)

        return cb.orderByDesc("id").page(offset, limit).resultList
    }
}
