package uy.kohesive.injekt.example

// mock classes used for the example

data class HttpServerConfig(val host: String, val port: Int, val workerThreads: Int)
data class HttpContext(val params: Map<String, String> = emptyMap())
data class HttpServer(val host: String, val port: Int) {
    fun withThreads(threadCount: Int): HttpServer  { return this }
    fun handleRequest(handle: (context: HttpContext)->Unit) { }
}


class DontCreateUntilWeNeedYa()
class LazyDazy()
class NamedPet(val name: String)
class KnownObject()

data class DatabaseConnectionConfig(val host: String, val username: String, val password: String)
data class JdbcDatabaseConnection(val cfg: DatabaseConnectionConfig)
