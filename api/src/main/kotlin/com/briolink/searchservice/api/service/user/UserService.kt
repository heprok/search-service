package com.briolink.searchservice.api.service.user

import com.blazebit.persistence.CriteriaBuilderFactory
import com.blazebit.persistence.PagedList
import com.blazebit.persistence.ParameterHolder
import com.blazebit.persistence.WhereBuilder
import com.briolink.searchservice.api.dto.SortDirectionEnum
import com.briolink.searchservice.api.service.user.dto.UserFiltersDto
import com.briolink.searchservice.api.service.user.dto.UserSortDto
import com.briolink.searchservice.common.jpa.enumeration.LocationTypeEnum
import com.briolink.searchservice.common.jpa.read.entity.UserReadEntity
import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import org.hibernate.jpa.TypedParameterValue
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
        filters: UserFiltersDto,
        sort: UserSortDto? = null,
        offset: Int = 0,
        limit: Int = 10,
    ): PagedList<UserReadEntity> {
        val cbf = criteriaBuilderFactory.create(entityManager, UserReadEntity::class.java)
        val cb = cbf.from(UserReadEntity::class.java)

        setFilters(cb, filters)

        if (sort != null)
            cb.orderBy(sort.key.field, sort.direction == SortDirectionEnum.ASC)

        return cb.orderByDesc("id").page(offset, limit).resultList
    }

    private fun <T> setFilters(
        cb: T,
        filters: UserFiltersDto
    ): T where T : WhereBuilder<T>, T : ParameterHolder<T> {
        with(filters) {
            if (!currentPlaceWorkCompanyIds.isNullOrEmpty()) cb.where("currentPlaceOfWorkCompanyId")
                .`in`(currentPlaceWorkCompanyIds)
            if (!searchText.isNullOrBlank()) cb.whereExpression("function('fts_partial', _keywordsSearch, :searchText) = true")
                .setParameter("searchText", searchText)
            if (!previousPlaceWorkCompanyIds.isNullOrEmpty()) {
                cb
                    .whereExpression("array_contains(previousPlaceOfWorkCompanyIds, :companyRolesIds) = true")
                    .setParameter(
                        "companyRolesIds",
                        TypedParameterValue(UUIDArrayType.INSTANCE, previousPlaceWorkCompanyIds.toTypedArray())
                    )
            }
            if (!jobPositionTitleIds.isNullOrEmpty()) cb.where("positionTitleId").`in`(jobPositionTitleIds)
            if (!companyIndustryIds.isNullOrEmpty()) cb.where("industryId").`in`(companyIndustryIds)
            if (!locationIds.isNullOrEmpty()) {
                val countryIds = locationIds.filter { it.type == LocationTypeEnum.Country }.map { it.id }
                val stateIds = locationIds.filter { it.type == LocationTypeEnum.State }.map { it.id }
                val cityIds = locationIds.filter { it.type == LocationTypeEnum.City }.map { it.id }

                cb.whereOr()
                    .where("countryId").`in`(countryIds)
                    .where("stateId").`in`(stateIds)
                    .where("cityId").`in`(cityIds)
                    .endOr()
            }
        }

        return cb
    }
}
