package uy.kohesive.injekt.example

import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.*
import java.text.SimpleDateFormat
import java.util.*


public class SmallApp {
    companion object : InjektMain() { // my app starts here with a static main()
        @JvmStatic public fun main(args: Array<String>) {
            SmallApp().run()
        }

        override fun InjektRegistrar.registerInjectables() {
            addPerThreadFactory { SimpleDateFormat("yyyy-MM-dd") }
        }
    }

    public fun run(dateFormatter: SimpleDateFormat = Injekt.get()) {
        System.out.println("I survived the date formatting!!! ${dateFormatter.format(Date())}")
    }
}