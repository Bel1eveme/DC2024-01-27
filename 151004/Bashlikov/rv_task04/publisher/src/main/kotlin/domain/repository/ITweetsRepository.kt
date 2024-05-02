package domain.repository

import by.bashlikovvv.data.local.model.TweetEntity

interface ITweetsRepository {

    suspend fun create(tweet: TweetEntity): Long

    suspend fun read(id: Long): TweetEntity?

    suspend fun readAll(): List<TweetEntity?>

    suspend fun readBYEditorId(editorId: Long): List<TweetEntity?>

    suspend fun update(id: Long, tweet: TweetEntity): Boolean

    suspend fun delete(id: Long): Boolean

}