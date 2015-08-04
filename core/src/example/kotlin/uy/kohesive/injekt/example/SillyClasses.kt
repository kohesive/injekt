package uy.kohesive.injekt.example

// mock classes used for the example

data class HttpServerConfig(val host: String, val port: Int, val workerThreads: Int)
data class HttpContext(val params: Map<String, String> = emptyMap())
data class HttpServer(val host: String, val port: Int) {
    public fun withThreads(threadCount: Int): HttpServer  { return this }
    public fun handleRequest(handle: (context: HttpContext)->Unit) { }
}


data class DontCreateUntilWeNeedYa()
data class LazyDazy()
data class NamedPet(val name: String)
data class KnownObject()

data class DatabaseConnectionConfig(val host: String, username: String, password: String)
data class JdbcDatabaseConnection(val cfg: DatabaseConnectionConfig)
