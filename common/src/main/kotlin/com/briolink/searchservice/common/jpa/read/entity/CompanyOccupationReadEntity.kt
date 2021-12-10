package com.briolink.searchservice.common.jpa.read.entity

import org.hibernate.annotations.Type
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "company_occupation", schema = "read")
@Entity
class CompanyOccupationReadEntity(
    @Id
    @Type(type = "pg-uuid")
    @Column(name = "id", nullable = false)
    val id: UUID,

    @Column(name = "name", nullable = false, length = 255)
    var name: String
) : BaseReadEntity()
