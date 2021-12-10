package com.briolink.searchservice.common.jpa.func

import com.vladmihalcea.hibernate.type.array.DateArrayType
import com.vladmihalcea.hibernate.type.array.DoubleArrayType
import com.vladmihalcea.hibernate.type.array.IntArrayType
import com.vladmihalcea.hibernate.type.array.LongArrayType
import com.vladmihalcea.hibernate.type.array.StringArrayType
import com.vladmihalcea.hibernate.type.array.TimestampArrayType
import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import org.hibernate.QueryException
import org.hibernate.dialect.function.SQLFunction
import org.hibernate.engine.spi.Mapping
import org.hibernate.engine.spi.SessionFactoryImplementor
import org.hibernate.type.Type
import java.sql.Timestamp
import java.util.Date
import java.util.UUID

class ArrayAggFunc : SQLFunction {
    @Throws(QueryException::class)
    override fun render(type: Type?, args: List<*>, sessionFactoryImplementor: SessionFactoryImplementor?): String {
        val field = args.first()
        return "array_agg($field}"
    }

    @Throws(QueryException::class)
    override fun getReturnType(columnType: Type?, mapping: Mapping?): Type? {
        if (columnType == null) {
            return null
        }
        val componentType = columnType.returnedClass
        if (componentType == UUID::class.java) {
            return UUIDArrayType.INSTANCE
        } else if (componentType == String::class.java) {
            return StringArrayType.INSTANCE
        } else if (componentType == Int::class.javaPrimitiveType || componentType == Int::class.java) {
            return IntArrayType.INSTANCE
        } else if (componentType == Long::class.javaPrimitiveType || componentType == Long::class.java) {
            return LongArrayType.INSTANCE
        } else if (componentType == Double::class.javaPrimitiveType || componentType == Double::class.java) {
            return DoubleArrayType.INSTANCE
        } else if (Timestamp::class.java.isAssignableFrom(componentType)) {
            return TimestampArrayType.INSTANCE
        } else if (Date::class.java.isAssignableFrom(componentType)) {
            return DateArrayType.INSTANCE
        }
        throw IllegalStateException("Array_agg is not supported for $componentType mapped by $columnType")
    }

    override fun hasArguments(): Boolean {
        return true
    }

    override fun hasParenthesesIfNoArguments(): Boolean {
        return false
    }
}
