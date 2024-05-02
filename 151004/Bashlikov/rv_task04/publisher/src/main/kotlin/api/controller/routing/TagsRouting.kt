package api.controller.routing

import by.bashlikovvv.api.dto.request.CreateTagDto
import by.bashlikovvv.api.dto.request.UpdateTagDto
import domain.model.Response
import by.bashlikovvv.services.TagService
import util.respond
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import util.getWithCheck

fun Route.tagsRouting() {
    val tagsService: TagService by inject()
    
    getTags(tagsService)
    createTag(tagsService)
    deleteTagById(tagsService)
    getTagById(tagsService)
    updateTag(tagsService)
}

private fun Route.getTags(tagsService: TagService) {
    get("/tags") {
        val tags = tagsService.getAll()

        respond(
            isCorrect = { tags.isNotEmpty() },
            onCorrect = { call.respond(status = HttpStatusCode.OK, tags) },
            onIncorrect = {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = Response(HttpStatusCode.OK.value, "")
                )
            }
        )
    }
}

private fun Route.createTag(tagsService: TagService) {
    post("/tags") {
        val createTagDto: CreateTagDto = getWithCheck { call.receive() } ?: return@post call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )
        val addedTag = getWithCheck { tagsService.create(createTagDto) } ?: return@post call.respond(
            status = HttpStatusCode.Forbidden,
            message = Response(HttpStatusCode.Forbidden.value, "")
        )

        call.respond(
            status = HttpStatusCode.Created,
            message = addedTag
        )
    }
}

private fun Route.deleteTagById(tagsService: TagService) {
    delete("/tags/{id?}") {
        val id = call.parameters["id"] ?: return@delete call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )
        val removedItem = tagsService.delete(id.toLong())

        respond(
            isCorrect = { removedItem },
            onCorrect = {
                call.respond(
                    status = HttpStatusCode.NoContent,
                    message = Response(HttpStatusCode.OK.value, "")
                )
            },
            onIncorrect = {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = Response(HttpStatusCode.BadRequest.value, "")
                )
            }
        )
    }
}

private fun Route.getTagById(tagsService: TagService) {
    get("/tags/{id?}") {
        val id = call.parameters["id"] ?: return@get call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )
        val requestedItem = tagsService.getById(id.toLong())

        respond(
            isCorrect = { requestedItem != null },
            onCorrect = {
                call.respond(status = HttpStatusCode.OK, requestedItem!!)
            },
            onIncorrect = {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = Response(HttpStatusCode.BadRequest.value, "")
                )
            }
        )
    }
}

private fun Route.updateTag(tagsService: TagService) {
    put("/tags") {
        val updateTagDto: UpdateTagDto = getWithCheck { call.receive() } ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )
        val updatedTag = tagsService.update(
            tagId = updateTagDto.id,
            updateTagDto = updateTagDto
        )

        respond(
            isCorrect = { updatedTag != null },
            onCorrect = {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = updatedTag!!
                )
            },
            onIncorrect = {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = Response(HttpStatusCode.BadRequest.value, "")
                )
            }
        )
    }
}