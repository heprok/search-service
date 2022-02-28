package com.briolink.searchservice.common.jpa.read.repository

import com.briolink.searchservice.common.jpa.read.entity.UserReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface UserReadRepository : JpaRepository<UserReadEntity, UUID> {

    @Modifying
    @Query(
        """
        UPDATE read.user
        SET
           data = jsonb_set(data, '{previousPlaceOfWorkCompanies}', 
           (
              SELECT
                 jsonb_agg( 
                     CASE
                        WHEN e ->> 'companyId' = :companyId
                        THEN 
                            jsonb_set(jsonb_set(jsonb_set( e,
                                '{name}', to_jsonb(cast(:name as varchar))), 
                                '{slug}', to_jsonb(cast(:slug as varchar))),
                                '{logo}', to_jsonb(cast(:logo as varchar))) 
                        ELSE e 
                     END
                 )
             FROM jsonb_array_elements(data -> 'previousPlaceOfWorkCompanies') e
            )
        ) 
        WHERE previous_place_of_work_company_ids @> array[CAST(:companyId as uuid)]""",
        nativeQuery = true
    )
    fun updatePreviousPlaceWork(
        @Param("companyId") companyId: String,
        @Param("name") name: String,
        @Param("slug") slug: String,
        @Param("logo") logo: String?,
    )

    @Modifying
    @Query(
        """UPDATE UserReadEntity c
           SET 
               c.industryId = :industryId,
               c.data = function('jsonb_sets', c.data,
                    '{currentPlaceOfWorkCompany,name}', :name, text,
                    '{currentPlaceOfWorkCompany,slug}', :slug, text,
                    '{currentPlaceOfWorkCompany,logo}', :logo, text,
                    '{industryName}', :industryName, text
           ),
                c._keywordsSearch = function('concat_ws', '~;~',
                    c.fullName,
                    function('jsonb_get', c.data, 'currentPlaceOfWorkCompany', 'companyName'),
                    :industryName,
                    function('jsonb_get', c.data, 'user', 'location'),
                    function('jsonb_get', c.data, 'currentPlaceOfWorkCompany', 'jobPositionTitle'),
                    function('jsonb_get', c.data, 'user', 'description')
                )
            WHERE c.currentPlaceOfWorkCompanyId = :companyId""",
    )
    fun updateCurrentPlaceWork(
        @Param("companyId") companyId: UUID,
        @Param("name") name: String,
        @Param("slug") slug: String,
        @Param("logo") logo: String?,
        @Param("industryId") industryId: UUID?,
        @Param("industryName") industryName: String?
    )

    @Modifying
    @Query("UPDATE UserReadEntity c SET c.numberOfVerification = ?2 WHERE c.id = ?1")
    fun updateNumberOfVerification(id: UUID, numberOfVerification: Int)
}
