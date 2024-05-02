package data.repository

import by.bashlikovvv.data.local.model.TweetEntity
import data.local.dao.TweetOfflineSource
import domain.repository.ITweetsRepository

class TweetsRepository(
    private val tweetOfflineSource: TweetOfflineSource
) : ITweetsRepository {
    override suspend fun create(tweet: TweetEntity): Long {
        return tweetOfflineSource.create(tweet)
    }

    override suspend fun read(id: Long): TweetEntity? {
        return try {
            tweetOfflineSource.read(id)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun readAll(): List<TweetEntity?> {
        return tweetOfflineSource.readAll()
    }

    override suspend fun readBYEditorId(editorId: Long): List<TweetEntity?> {
        return tweetOfflineSource.readByEditorId(editorId)
    }

    override suspend fun update(id: Long, tweet: TweetEntity): Boolean {
        return tweetOfflineSource.update(id, tweet) > 0
    }

    override suspend fun delete(id: Long): Boolean {
        return tweetOfflineSource.delete(id) > 0
    }
}