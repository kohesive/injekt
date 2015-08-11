package uy.kohesive.injekt.registry.default

import java.util.concurrent.ConcurrentMap

// TODO: this is not perfect, the factory could be called more than once if two threads race to initialize a value
//       JDK 8 has a real version of ConcurrentMap.computeIfAbsent
public inline fun <K,V> ConcurrentMap<K, V>.concurrentGetOrPutProxy(key: K, defaultValue: ()-> V) : V {
    var answer = this.get(key)
    if (answer == null) {
        answer = defaultValue()
        var temp = this.putIfAbsent(key, answer!!)
        if (temp != null) {
            answer = temp
        }
    }
    return answer!!
}

