import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        factory = Netty,
        port = 24130,
        host = "0.0.0.0",
        module = Application::discussion
    ).start(wait = true)
}

fun Application.discussion() = configureDiscussionModule()