package api.controller

import api.controller.routing.postsRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        route("/api/v1.0") {
            postsRouting()
        }
    }
}