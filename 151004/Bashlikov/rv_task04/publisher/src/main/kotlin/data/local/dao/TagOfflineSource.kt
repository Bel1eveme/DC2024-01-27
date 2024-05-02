package data.local.dao

import domain.contract.DatabaseContract.TagsTable
import data.local.model.TagEntity
import domain.exception.DataSourceExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Statement

class TagOfflineSource(private val connection: Connection) {

    companion object {
        /* Create tags table */
        private const val CREATE_TABLE_TAGS =
            "CREATE TABLE ${TagsTable.TABLE_NAME} " +
            "(" +
                    "${TagsTable.COLUMN_ID} SERIAL PRIMARY KEY, " +
                    "${TagsTable.COLUMN_NAME} VARCHAR(32)" +
            ");"
        /* Add tag */
        private const val INSERT_TAG =
            "INSERT INTO ${TagsTable.TABLE_NAME} " +
            "(" +
                    "${TagsTable.COLUMN_NAME} " +
            ") VALUES (?);"
        /* Get tag by id */
        private const val SELECT_TAG_BY_ID =
            "SELECT " +
                    "${TagsTable.COLUMN_NAME} " +
            "FROM ${TagsTable.TABLE_NAME} " +
            "WHERE ${TagsTable.COLUMN_ID} = ?;"
        /* Get all tags */
        private const val SELECT_TAGS =
            "SELECT " +
                    "${TagsTable.COLUMN_ID}, " +
                    "${TagsTable.COLUMN_NAME} " +
            "FROM ${TagsTable.TABLE_NAME} "
        /* Update existing tag */
        private const val UPDATE_TAG =
            "UPDATE ${TagsTable.TABLE_NAME} " +
            "SET ${TagsTable.COLUMN_NAME} = ? " +
            "WHERE ${TagsTable.COLUMN_ID} = ?;"
        /* Delete tag */
        private const val DELETE_TAG =
            "DELETE FROM ${TagsTable.TABLE_NAME} " +
            "WHERE ${TagsTable.COLUMN_ID} = ?;"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_TAGS)
    }

    suspend fun create(tagEntity: TagEntity): Long = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_TAG, Statement.RETURN_GENERATED_KEYS)
        statement.apply {
            setString(1, tagEntity.name)
            executeUpdate()
        }

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getLong(1)
        } else {
            throw DataSourceExceptions.RecordCreationException("Unable to retrieve the id of the newly inserted tag")
        }
    }

    suspend fun read(id: Long): TagEntity = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_TAG_BY_ID)
        statement.setLong(1, id)

        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            val name = resultSet.getString(TagsTable.COLUMN_NAME)
            return@withContext TagEntity(
                id = id, name = name
            )
        } else {
            throw DataSourceExceptions.RecordNotFoundException("Tag record not found")
        }
    }

    suspend fun readAll(): List<TagEntity> = withContext(Dispatchers.IO) {
        val result = mutableListOf<TagEntity>()
        val statement = connection.prepareStatement(SELECT_TAGS)

        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            val id = resultSet.getLong(TagsTable.COLUMN_ID)
            val name = resultSet.getString(TagsTable.COLUMN_NAME)
            result.add(
                TagEntity(
                    id = id,
                    name = name
                )
            )
        }

        result
    }

    suspend fun update(id: Long, tagEntity: TagEntity) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_TAG)
        statement.apply {
            setString(1, tagEntity.name)
            setLong(2, id)
        }

        return@withContext try {
            statement.executeUpdate()
        } catch (e: Exception) {
            throw DataSourceExceptions.RecordModificationException("Can not modify tag record")
        }
    }

    suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_TAG)
        statement.setLong(1, id)

        return@withContext try {
            statement.executeUpdate()
        } catch (e: Exception) {
            throw DataSourceExceptions.RecordDeletionException("Can not delete tag record")
        }
    }

}