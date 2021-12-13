package com.briolink.searchservice.common.jpa.func

import org.hibernate.QueryException
import org.hibernate.dialect.function.SQLFunction
import org.hibernate.engine.spi.Mapping
import org.hibernate.engine.spi.SessionFactoryImplementor
import org.hibernate.type.BooleanType
import org.hibernate.type.Type

class ConcatWsFunc : SQLFunction {
    @Throws(QueryException::class)
    override fun render(type: Type?, args: List<*>, sessionFactoryImplementor: SessionFactoryImplementor?): String {
        if (args.count() < 3) throw RuntimeException("concat_ws has invalid arguments")

        val sep = args.first()
        val arguments = args.toMutableList().apply { removeFirst() }.map { it ?: "" }
        return "concat_ws('$sep', ${arguments.joinToString { "," }})"
    }

    @Throws(QueryException::class)
    override fun getReturnType(columnType: Type?, mapping: Mapping?): Type {
        return BooleanType()
    }

    override fun hasArguments(): Boolean {
        return true
    }

    override fun hasParenthesesIfNoArguments(): Boolean {
        return false
    }
}
