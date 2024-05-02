package util

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.pipeline.*

suspend fun ApplicationCall.redirectInternally(path: String) {
    val cp = object: RequestConnectionPoint by this.request.local {
        override val remoteAddress: String = path
    }
    val req = object: ApplicationRequest by this.request {
        override val local: RequestConnectionPoint = cp
    }
    val call = object: ApplicationCall by this {
        override val request: ApplicationRequest = req
    }

    this.application.execute(call)
}