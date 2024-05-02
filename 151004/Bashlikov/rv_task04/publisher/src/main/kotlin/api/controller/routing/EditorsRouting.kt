package api.controller.routing

import api.dto.request.CreateEditorDto
import api.dto.request.UpdateEditorDto
import util.respond
import domain.model.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import services.EditorService
import util.getWithCheck

internal fun Route.editorsRouting() {
    val editorsService: EditorService by inject()

    getEditors(editorsService)
    createEditor(editorsService)
    deleteEditorById(editorsService)
    getEditorById(editorsService)
    updateEditor(editorsService)
}

private fun Route.getEditors(editorsService: EditorService) {
    get("/editors") {
        val editors = editorsService.getAll()

        respond(
            isCorrect = { editors.isNotEmpty() },
            onCorrect = { call.respond(status = HttpStatusCode.OK, editors) },
            onIncorrect = {
                call.respond(
                    status = HttpStatusCode.OK,
                    Response(HttpStatusCode.OK.value, "")
                )
            }
        )
    }
}

private fun Route.createEditor(editorsService: EditorService) {
    post("/editors") {
        val createEditorDto: CreateEditorDto? = getWithCheck { call.receive() }
        createEditorDto ?: return@post call.respond(
            status = HttpStatusCode.BadRequest, Response(HttpStatusCode.BadRequest.value, "")
        )
        val addedEditor = getWithCheck { editorsService.create(createEditorDto) } ?: return@post call.respond(
            status = HttpStatusCode.Forbidden,
            Response(HttpStatusCode.Forbidden.value, "")
        )

        call.respond(
            status = HttpStatusCode.Created,
            message = addedEditor
        )
    }
}

private fun Route.deleteEditorById(editorsService: EditorService) {
    delete("/editors/{id?}") {
        val id = call.parameters["id"] ?: return@delete call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )
        val removedItem = editorsService.delete(id.toLong())

        respond(
            isCorrect = { removedItem },
            onCorrect = {
                call.respond(
                    status = HttpStatusCode.NoContent,
                    Response(HttpStatusCode.OK.value, "")
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

private fun Route.getEditorById(editorsService: EditorService) {
    get("/editors/{id?}") {
        val id = call.parameters["id"] ?: return@get call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )
        val requestedItem = editorsService.getById(id.toLong())

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

private fun Route.updateEditor(editorsService: EditorService) {
    put("/editors") {
        val updateEditorDto: UpdateEditorDto = getWithCheck { call.receive() } ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )

        val updatedEditor = editorsService.update(
            editorId = updateEditorDto.id,
            updateEditorDto = updateEditorDto
        )

        respond(
            isCorrect = { updatedEditor != null },
            onCorrect = {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = updatedEditor!!
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