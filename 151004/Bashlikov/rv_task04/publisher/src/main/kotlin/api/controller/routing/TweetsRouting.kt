package api.controller.routing

import by.bashlikovvv.api.dto.request.CreateTweetDto
import by.bashlikovvv.api.dto.request.UpdateTweetDto
import domain.model.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import services.TweetService
import util.getWithCheck
import util.respond

fun Route.tweetsRouting() {
    val tweetService: TweetService by inject()

    getTweets(tweetService)
    createTweet(tweetService)
    deleteTweetById(tweetService)
    getTweetById(tweetService)
    updateTweet(tweetService)
}

private fun Route.getTweets(tweetService: TweetService) {
    get("/tweets") {
        val tweets = tweetService.getAll()

        respond(
            isCorrect = { tweets.isNotEmpty() },
            onCorrect = { call.respond(status = HttpStatusCode.OK, tweets) },
            onIncorrect = {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = Response(HttpStatusCode.OK.value, "")
                )
            }
        )
    }
}

private fun Route.createTweet(tweetsService: TweetService) {
    post("/tweets") {
        val tweet: CreateTweetDto = getWithCheck { call.receive() } ?: return@post call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )
        val addedTweet = getWithCheck { tweetsService.create(tweet) } ?: return@post call.respond(
            status = HttpStatusCode.Forbidden,
            message = Response(HttpStatusCode.Forbidden.value, "")
        )

        call.respond(
            status = HttpStatusCode.Created,
            message = addedTweet
        )
    }
}

private fun Route.deleteTweetById(tweetsService: TweetService) {
    delete("/tweets/{id?}") {
        val id = call.parameters["id"] ?: return@delete call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )
        val removedItem = tweetsService.delete(id.toLong())

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

private fun Route.getTweetById(tweetsService: TweetService) {
    get("/tweets/{id?}") {
        val id = call.parameters["id"] ?: return@get call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )
        val requestedItem = tweetsService.getById(id.toLong())

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

private fun Route.updateTweet(tweetsService: TweetService) {
    put("/tweets") {
        val updateTweetDto: UpdateTweetDto = getWithCheck { call.receive() } ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )
        val tweetId = tweetsService.getByEditorId(updateTweetDto.editorId)?.id ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = Response(HttpStatusCode.BadRequest.value, "")
        )
        val updatedTweet = tweetsService.update(
            tweetId = tweetId,
            updateTweetDto = UpdateTweetDto(
                editorId = updateTweetDto.editorId,
                title = updateTweetDto.title,
                content = updateTweetDto.content,
                name = updateTweetDto.name
            )
        )

        respond(
            isCorrect = { updatedTweet != null },
            onCorrect = {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = updatedTweet!!
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