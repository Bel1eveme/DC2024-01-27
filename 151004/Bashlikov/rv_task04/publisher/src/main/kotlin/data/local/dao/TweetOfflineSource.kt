package data.local.dao

import by.bashlikovvv.data.local.model.TweetEntity
import domain.contract.DatabaseContract.TweetsTable
import domain.exception.DataSourceExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Statement

class TweetOfflineSource(private val connection: Connection) {

    companion object {
        /* Create tweets table */
        private const val CREATE_TABLE_TWEETS =
            "CREATE TABLE ${TweetsTable.TABLE_NAME} " +
            "(" +
                    "${TweetsTable.COLUMN_ID} SERIAL PRIMARY KEY, " +
                    "${TweetsTable.COLUMN_EDITOR_ID} BIGINT, " +
                    "${TweetsTable.COLUMN_TITLE} VARCHAR(64) UNIQUE, " +
                    "${TweetsTable.COLUMN_CONTENT} VARCHAR(2048), " +
                    "${TweetsTable.COLUMN_CREATED} TIMESTAMP, " +
                    "${TweetsTable.COLUMN_MODIFIED} TIMESTAMP" +
            ");"
        /* Add a new tweet at the table */
        private const val INSERT_TWEET =
            "INSERT INTO ${TweetsTable.TABLE_NAME} " +
            "(" +
                    "${TweetsTable.COLUMN_EDITOR_ID}, " +
                    "${TweetsTable.COLUMN_TITLE}, " +
                    "${TweetsTable.COLUMN_CONTENT}, " +
                    "${TweetsTable.COLUMN_CREATED}, " +
                    TweetsTable.COLUMN_MODIFIED +
            ") VALUES (?, ?, ?, ?, ?);"
        /* Get tweet by id */
        private const val SELECT_TWEET_BY_ID =
            "SELECT " +
                    "${TweetsTable.COLUMN_EDITOR_ID}, " +
                    "${TweetsTable.COLUMN_TITLE}, " +
                    "${TweetsTable.COLUMN_CONTENT}, " +
                    "${TweetsTable.COLUMN_CREATED}, " +
                    "${TweetsTable.COLUMN_MODIFIED} " +
            "FROM ${TweetsTable.TABLE_NAME} " +
            "WHERE ${TweetsTable.COLUMN_ID} = ?;"
        /* Get all tweets */
        private const val SELECT_TWEETS =
            "SELECT " +
                    "${TweetsTable.COLUMN_ID}, " +
                    "${TweetsTable.COLUMN_EDITOR_ID}, " +
                    "${TweetsTable.COLUMN_TITLE}, " +
                    "${TweetsTable.COLUMN_CONTENT}, " +
                    "${TweetsTable.COLUMN_CREATED}, " +
                    "${TweetsTable.COLUMN_MODIFIED} " +
            "FROM ${TweetsTable.TABLE_NAME};"
        /* Get all tweets by editor id */
        private const val SELECT_TWEETS_BY_EDITOR_ID =
            "SELECT " +
                    "${TweetsTable.COLUMN_ID}, " +
                    "${TweetsTable.COLUMN_TITLE}, " +
                    "${TweetsTable.COLUMN_CONTENT}, " +
                    "${TweetsTable.COLUMN_CREATED}, " +
                    "${TweetsTable.COLUMN_MODIFIED} " +
            "FROM ${TweetsTable.TABLE_NAME} " +
            "WHERE ${TweetsTable.COLUMN_EDITOR_ID} = ?;"
        /* Update exists tweet */
        private const val UPDATE_TWEET =
            "UPDATE ${TweetsTable.TABLE_NAME} " +
            "SET " +
                    "${TweetsTable.COLUMN_EDITOR_ID} = ?, " +
                    "${TweetsTable.COLUMN_TITLE} = ?, " +
                    "${TweetsTable.COLUMN_CONTENT} = ?, " +
                    "${TweetsTable.COLUMN_CREATED} = ?, " +
                    "${TweetsTable.COLUMN_MODIFIED} = ? " +
            "WHERE ${TweetsTable.COLUMN_ID} = ?;"
        /* Remove tweet from table */
        private const val DELETE_TWEET =
            "DELETE FROM ${TweetsTable.TABLE_NAME} " +
            "WHERE ${TweetsTable.COLUMN_ID} = ?;"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_TWEETS)
    }

    suspend fun create(tweet: TweetEntity): Long = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_TWEET, Statement.RETURN_GENERATED_KEYS)
        statement.apply {
            setLong(1, tweet.editorId)
            setString(2, tweet.title)
            setString(3, tweet.content)
            setTimestamp(4, tweet.created)
            setTimestamp(5, tweet.modified)
            executeUpdate()
        }

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getLong(1)
        } else {
            throw DataSourceExceptions.RecordCreationException("Unable to retrieve the id of the newly inserted tweet")
        }
    }

    suspend fun read(id: Long): TweetEntity = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_TWEET_BY_ID)
        statement.setLong(1, id)

        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            val editorId = resultSet.getLong(TweetsTable.COLUMN_EDITOR_ID)
            val title = resultSet.getString(TweetsTable.COLUMN_TITLE)
            val content = resultSet.getString(TweetsTable.COLUMN_CONTENT)
            val created = resultSet.getTimestamp(TweetsTable.COLUMN_CREATED)
            val modified = resultSet.getTimestamp(TweetsTable.COLUMN_MODIFIED)
            return@withContext TweetEntity(
                id = id,
                editorId = editorId,
                title = title,
                content = content,
                created = created,
                modified = modified
            )
        } else {
            throw DataSourceExceptions.RecordNotFoundException("Editor record not found")
        }
    }

    suspend fun readAll(): List<TweetEntity?> = withContext(Dispatchers.IO) {
        val result = mutableListOf<TweetEntity>()
        val statement = connection.prepareStatement(SELECT_TWEETS)

        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            val id = resultSet.getLong(TweetsTable.COLUMN_ID)
            val editorId = resultSet.getLong(TweetsTable.COLUMN_EDITOR_ID)
            val title = resultSet.getString(TweetsTable.COLUMN_TITLE)
            val content = resultSet.getString(TweetsTable.COLUMN_CONTENT)
            val created = resultSet.getTimestamp(TweetsTable.COLUMN_CREATED)
            val modified = resultSet.getTimestamp(TweetsTable.COLUMN_MODIFIED)
            result.add(
                TweetEntity(
                    id = id,
                    editorId = editorId,
                    title = title,
                    content = content,
                    created = created,
                    modified = modified
                )
            )
        }

        result
    }

    suspend fun readByEditorId(editorId: Long): List<TweetEntity?> = withContext(Dispatchers.IO) {
        val result = mutableListOf<TweetEntity>()
        val statement = connection.prepareStatement(SELECT_TWEETS_BY_EDITOR_ID)
        statement.setLong(1, editorId)

        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            val id = resultSet.getLong(TweetsTable.COLUMN_ID)
            val title = resultSet.getString(TweetsTable.COLUMN_TITLE)
            val content = resultSet.getString(TweetsTable.COLUMN_CONTENT)
            val created = resultSet.getTimestamp(TweetsTable.COLUMN_CREATED)
            val modified = resultSet.getTimestamp(TweetsTable.COLUMN_MODIFIED)
            result.add(
                TweetEntity(
                    id = id,
                    editorId = editorId,
                    title = title,
                    content = content,
                    created = created,
                    modified = modified
                )
            )
        }

        result
    }

    suspend fun update(id: Long, tweet: TweetEntity) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_TWEET)
        statement.apply {
            setLong(1, tweet.editorId)
            setString(2, tweet.title)
            setString(3, tweet.content)
            setTimestamp(4, tweet.created)
            setTimestamp(5, tweet.modified)
            setLong(6, id)
        }

        return@withContext try {
            statement.executeUpdate()
        } catch (e: Exception) {
            throw DataSourceExceptions.RecordModificationException("Can not modify tweet record")
        }
    }

    suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_TWEET)
        statement.setLong(1, id)

        return@withContext try {
            statement.executeUpdate()
        } catch (e: Exception) {
            throw DataSourceExceptions.RecordDeletionException("Can not delete tweet record")
        }
    }

}