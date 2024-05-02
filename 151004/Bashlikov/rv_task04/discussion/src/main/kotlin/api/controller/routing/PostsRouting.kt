package api.controller.routing

import api.dto.request.CreatePostDto
import api.dto.request.UpdatePostDto
import api.dto.response.PostDto
import domain.model.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import services.PostService
import util.getWithCheck
import util.respond

fun Route.postsRouting() {
    val postsService: PostService by inject()

    getPosts(postsService)
    createPost(postsService)
    deletePostById(postsService)
    getPostById(postsService)
    updatePost(postsService)
}

private fun Route.getPosts(postsService: PostService) {
    get("/posts") {
        val posts = postsService.getAll()

        respond(
            isCorrect = { posts.isNotEmpty() },
            onCorrect = { call.respond(status = HttpStatusCode.OK, posts) },
            onIncorrect = {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = Response(HttpStatusCode.OK.value, "")
                )
            }
        )
    }
}

private fun Route.createPost(postsService: PostService) {
    post("/posts") {
        val post: CreatePostDto = getWithCheck { call.receive() } ?: return@post call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.Forbidden.value, PostDto(0, 0, "", ""))
        )
        val addedPost = getWithCheck { postsService.create(post) } ?: return@post call.respond(
            status = HttpStatusCode.Forbidden,
            message = Response(HttpStatusCode.Forbidden.value, PostDto(0, 0, "", ""))
        )

        call.respond(
            status = HttpStatusCode.Created,
            message = addedPost
        )
    }
}

private fun Route.deletePostById(postsService: PostService) {
    delete("/posts/{id?}") {
        val id = call.parameters["id"] ?: return@delete call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )
        val removedItem = postsService.delete(id.toLong())

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

private fun Route.getPostById(postsService: PostService) {
    get("/posts/{id?}") {
        val id = call.parameters["id"] ?: return@get call.respond(
            status = HttpStatusCode.BadRequest,
            message =  Response(HttpStatusCode.BadRequest.value, PostDto(0, 0, "", ""))
        )
        val requestedItem = postsService.getById(id.toLong())

        respond(
            isCorrect = { requestedItem != null },
            onCorrect = {
                call.respond(status = HttpStatusCode.OK, requestedItem!!)
            },
            onIncorrect = {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message =  Response(HttpStatusCode.BadRequest.value, PostDto(0, 0, "", ""))
                )
            }
        )
    }
}

private fun Route.updatePost(postsService: PostService) {
    put("/posts") {
        val updatePostDto: UpdatePostDto = getWithCheck { call.receive() } ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )
        val updatedPost = postsService.update(
            postId = updatePostDto.id,
            updatePostDto = updatePostDto
        )

        respond(
            isCorrect = { updatedPost != null },
            onCorrect = {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = updatePostDto
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