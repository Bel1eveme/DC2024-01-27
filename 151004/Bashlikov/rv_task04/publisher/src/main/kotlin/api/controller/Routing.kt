package api.controller

import api.controller.routing.editorsRouting
import api.controller.routing.postsRouting
import api.controller.routing.tagsRouting
import api.controller.routing.tweetsRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        route("/api/v1.0") {
            tweetsRouting()
            editorsRouting()
            tagsRouting()
            postsRouting()
        }
    }
}