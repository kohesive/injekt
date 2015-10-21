package uy.kohesive.injekt.api


/**
 *  A startup module that registers and uses singletons/object factories from a specific scope
 */
public abstract class InjektScopedMain(public val scope: InjektScope) : InjektModule {
    init {
        scope.registrar.registerInjectables()
    }
}

/**
 * A package of injectable items that can be included into a scope of someone else
 */
public interface InjektModule {
    fun registerWith(intoModule: InjektRegistrar) {
        intoModule.registerInjectables()
    }

    fun InjektRegistrar.registerInjectables()
}


