package uy.kohesive.injekt.api

import java.lang.reflect.*

// TODO: commented out code is because dealing with people wanting to key off of erased generic classes, is not so clear and clean.
//       Kotlin MyClass::class.java does not create the same result as a class with generics does when reified.   Causes different
//       results in the methods below.

@Suppress("UNCHECKED_CAST")
fun Type.erasedType(): Class<Any> {
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
// public inline fun <reified T: Any> rawType(): TypeReference<T> = object:ErasedTypeReference<T>(){}
// public inline fun <reified T: Any> eraseType(): TypeReference<T> = object:ErasedTypeReference<T>(){}

public interface TypeReference<T> {
    public val type: Type
//    public final val cyg: ClassWithGenerics
//       get() = ClassWithGenerics.fromUnknown(type)
}

public abstract class FullTypeReference<T> protected constructor() : TypeReference<T> {
    // public val erasedType: Class<Any> get() = type.eraseGenerics()

    override public val type: Type = javaClass.getGenericSuperclass() let { superClass ->
        if (superClass is Class<*>) {
            throw IllegalArgumentException("Internal error: TypeReference constructed without actual type information")
        }
        (superClass as ParameterizedType).getActualTypeArguments()[0]
    }
}

/*
public abstract class ErasedTypeReference<T> protected constructor() : TypeReference<T> {
    override public val type: Class<Any> = javaClass.getGenericSuperclass() let { superClass ->
        if (superClass is Class<*>) {
            throw IllegalArgumentException("Internal error: TypeReference constructed without actual type information")
        }
        (superClass as ParameterizedType).getActualTypeArguments()[0].eraseGenerics()
    }
}
*/



