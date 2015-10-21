package uy.kohesive.injekt.api

import java.lang.reflect.*

@Suppress("UNCHECKED_CAST")
public fun Type.erasedType(): Class<Any> {
    return when (this) {
        is Class<*> -> this as Class<Any>
        is ParameterizedType -> this.getRawType().erasedType()
        is GenericArrayType -> {
            // getting the array type is a bit trickier
            val elementType = this.getGenericComponentType().erasedType()
            val testArray = java.lang.reflect.Array.newInstance(elementType, 0)
            testArray.javaClass
        }
        is TypeVariable<*> -> {
            // not sure yet
            throw IllegalStateException("Not sure what to do here yet")
        }
        is WildcardType -> {
            this.getUpperBounds()[0].erasedType()
        }
        else -> throw IllegalStateException("Should not get here.")
    }
}

public inline fun <reified T: Any> typeRef(): FullTypeReference<T> = object:FullTypeReference<T>(){}
public inline fun <reified T: Any> fullType(): FullTypeReference<T> = object:FullTypeReference<T>(){}

public interface TypeReference<T> {
    public val type: Type
}

public abstract class FullTypeReference<T> protected constructor() : TypeReference<T> {
    override public val type: Type = javaClass.getGenericSuperclass().let { superClass ->
        if (superClass is Class<*>) {
            throw IllegalArgumentException("Internal error: TypeReference constructed without actual type information")
        }
        (superClass as ParameterizedType).getActualTypeArguments()[0]
    }
}



