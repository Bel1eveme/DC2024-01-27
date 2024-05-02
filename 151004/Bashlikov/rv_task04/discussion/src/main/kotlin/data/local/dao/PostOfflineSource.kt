package data.local.dao

import data.local.model.PostEntity
import domain.contract.DatabaseContract.PostsTable
import domain.exception.DataSourceExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostOfflineSource(private val session: com.datastax.driver.core.Session) {

    companion object {
        private const val SELECT_POSTS =
            "SELECT " +
                    "${PostsTable.COLUMN_COUNTRY}, " +
                    "${PostsTable.COLUMN_TWEET_ID}, " +
                    "${PostsTable.COLUMN_ID}, " +
                    "${PostsTable.COLUMN_CONTENT} " +
            "FROM ${PostsTable.TABLE_NAME};"
        /* Update exists post by id */
        private const val UPDATE_POST =
            "UPDATE ${PostsTable.TABLE_NAME} " +
            "SET " +
                    "${PostsTable.COLUMN_COUNTRY}, " +
                    "${PostsTable.COLUMN_TWEET_ID} = ?, " +
                    "${PostsTable.COLUMN_ID}, " +
                    "${PostsTable.COLUMN_CONTENT} = ? " +
            "WHERE ${PostsTable.COLUMN_ID} = ?;"
        /* Delete post from table */
        private const val DELETE_POST =
            "DELETE FROM ${PostsTable.TABLE_NAME} " +
            "WHERE ${PostsTable.COLUMN_ID} = ?;"
    }

    init {
        session.execute("DROP TABLE distcomp.tbl_post_by_country")
        session.execute("CREATE TABLE distcomp.tbl_post_by_country (country text, tweet_id bigint, id bigint, content text, PRIMARY KEY ((country), tweet_id, id))")
        session.execute("TRUNCATE distcomp.tbl_post_by_country")
    }

    suspend fun create(postEntity: PostEntity): Long = withContext(Dispatchers.IO) {
        val id = System.currentTimeMillis()
        session.execute(
            "INSERT INTO distcomp.tbl_post_by_country (country, tweet_id, id, content) " +
                    "VALUES ('${postEntity.country}', ${postEntity.tweetId}, ${id}, '${postEntity.content}');"
        )

        val rs = session.execute(SELECT_POSTS)
        val generatedKeys = rs.all()
        println("MYTAG create" + readAll())
        if (generatedKeys.isNotEmpty()) {
            return@withContext id
        } else {
            throw DataSourceExceptions.RecordCreationException("Unable to retrieve the id of the newly inserted post")
        }
    }

    suspend fun read(id: Long): PostEntity = withContext(Dispatchers.IO) {
        val all = readAll()
        if (all.isNotEmpty()) {

            return@withContext all.last { it.id == id }
        } else {
            throw DataSourceExceptions.RecordNotFoundException("Post record not found")
        }
    }

    suspend fun readAll(): List<PostEntity> = withContext(Dispatchers.IO) {
        val result = mutableListOf<PostEntity>()

        val resultSet = session.execute(SELECT_POSTS)
        for (row in resultSet.all()) {
            val id = row.getLong(PostsTable.COLUMN_ID)
            val tweetId = row.getLong(PostsTable.COLUMN_TWEET_ID)
            val content = row.getString(PostsTable.COLUMN_CONTENT)
            val country = row.getString(PostsTable.COLUMN_COUNTRY)
            result.add(
                PostEntity(
                    id = id,
                    tweetId = tweetId,
                    content = content,
                    country = country
                )
            )
        }

        result
    }

    suspend fun update(id: Long, postEntity: PostEntity) = withContext(Dispatchers.IO) {
        return@withContext try {
            delete(id)
            session.execute(
                "INSERT INTO distcomp.tbl_post_by_country (country, tweet_id, id, content) " +
                        "VALUES ('${postEntity.country}', ${postEntity.tweetId}, ${id}, '${postEntity.content}');"
            )

            println("MYTAG update" + readAll())
            1
        } catch (e: Exception) {
            throw DataSourceExceptions.RecordModificationException("Can not modify post record")
        }
    }

    suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        return@withContext try {
            val entity = read(id)
            session.execute(
                "DELETE FROM ${PostsTable.TABLE_NAME} " +
                "WHERE ${PostsTable.COLUMN_ID} = $id AND ${PostsTable.COLUMN_COUNTRY} = '${entity.country}' AND ${PostsTable.COLUMN_TWEET_ID} = ${entity.tweetId};"
            )

            try {
                println("MYTAG delete" + readAll())
                read(id)

                1
            } catch (_: DataSourceExceptions.RecordNotFoundException) {
                1
            }
        } catch (e: Exception) {
            throw DataSourceExceptions.RecordDeletionException("Can not delete post record")
        }
    }

}