package uy.kohesive.injekt.api


/**
 *  A startup module that registers and uses singletons/object factories from a specific scope
 */
abstract class InjektScopedMain(val scope: InjektScope) : InjektModule {
    init {
        scope.registerInjectables()
    }
}

/**
 * A package of injectable items that can be included into a scope of someone else
 */
interface InjektModule {
    fun registerWith(intoScope: InjektScope) {
        intoScope.registerInjectables()
    }

    fun InjektScope.registerInjectables()
}


