package services

import by.bashlikovvv.api.dto.request.CreateTweetDto
import by.bashlikovvv.api.dto.request.UpdateTweetDto
import by.bashlikovvv.api.dto.response.TweetDto

interface TweetService {

    suspend fun create(createTweetDto: CreateTweetDto): TweetDto?

    suspend fun getAll(): List<TweetDto?>

    suspend fun getById(tweetId: Long): TweetDto?

    suspend fun getByEditorId(editorId: Long): TweetDto?

    suspend fun update(tweetId: Long, updateTweetDto: UpdateTweetDto): TweetDto?

    suspend fun delete(tweetId: Long): Boolean

}