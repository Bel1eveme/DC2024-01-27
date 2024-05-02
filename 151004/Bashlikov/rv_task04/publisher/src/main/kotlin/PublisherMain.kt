import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        factory = Netty,
        port = 24110,
        host = "0.0.0.0",
        module = Application::publisher
    ).start(wait = true)
}

fun Application.publisher() = configurePublisherModule()