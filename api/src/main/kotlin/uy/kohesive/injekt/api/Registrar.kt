package uy.kohesive.injekt.api

public interface InjektRegistrar: InjektRegistry, InjektFactory  {
    public fun importModule(submodule: InjektModule) {
        submodule.registerWith(this)
    }
}
