package uy.kohesive.injekt


/**
 *  A module that registers and uses singletons/object factories
 */
public abstract class InjektMain() : InjektRegistrar {
    abstract fun InjektRegistrar.registerInjektables()

    init {
        registerInjektables()
    }
}

/**
 * A package of injectable items that can be included intoa  module
 */
public interface InjektModule {
    internal fun registerWith(intoModule: InjektRegistrar) {
        intoModule.exportInjektables()
    }

    abstract fun InjektRegistrar.exportInjektables()
}
