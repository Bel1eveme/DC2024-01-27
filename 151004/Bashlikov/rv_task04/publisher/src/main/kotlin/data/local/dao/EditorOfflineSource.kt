package data.local.dao

import data.local.model.EditorEntity
import domain.contract.DatabaseContract.EditorsTable
import domain.exception.DataSourceExceptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Statement

class EditorOfflineSource(private val connection: Connection) {

    companion object {
        /* Create editors table */
        private const val CREATE_TABLE_EDITORS =
            "CREATE TABLE ${EditorsTable.TABLE_NAME} (" +
                    "${EditorsTable.COLUMN_ID} SERIAL PRIMARY KEY, " +
                    "${EditorsTable.COLUMN_LOGIN} VARCHAR(64) UNIQUE, " +
                    "${EditorsTable.COLUMN_PASSWORD} VARCHAR(128), " +
                    "${EditorsTable.COLUMN_FIRSTNAME} VARCHAR(64), " +
                    "${EditorsTable.COLUMN_LASTNAME} VARCHAR(64)" +
            ");"
        /* Add new editor at table */
        private const val INSERT_EDITOR =
            "INSERT INTO ${EditorsTable.TABLE_NAME} (" +
                    "${EditorsTable.COLUMN_LOGIN}, " +
                    "${EditorsTable.COLUMN_PASSWORD}, " +
                    "${EditorsTable.COLUMN_FIRSTNAME}, " +
                    EditorsTable.COLUMN_LASTNAME +
            ") VALUES (?, ?, ?, ?);"
        /* Get editor by id */
        private const val SELECT_EDITOR_BY_ID =
            "SELECT " +
                    "${EditorsTable.COLUMN_LOGIN}, " +
                    "${EditorsTable.COLUMN_PASSWORD}, " +
                    "${EditorsTable.COLUMN_FIRSTNAME}, " +
                    "${EditorsTable.COLUMN_LASTNAME} " +
            "FROM ${EditorsTable.TABLE_NAME} " +
            "WHERE ${EditorsTable.COLUMN_ID} = ?;"
        /* Get all editors */
        private const val SELECT_EDITORS =
            "SELECT " +
                    "${EditorsTable.COLUMN_ID}, " +
                    "${EditorsTable.COLUMN_LOGIN}, " +
                    "${EditorsTable.COLUMN_PASSWORD}, " +
                    "${EditorsTable.COLUMN_FIRSTNAME}, " +
                    "${EditorsTable.COLUMN_LASTNAME} " +
            "FROM ${EditorsTable.TABLE_NAME};"
        /* Update exists editor */
        private const val UPDATE_EDITOR =
            "UPDATE ${EditorsTable.TABLE_NAME} " +
            "SET " +
                    "${EditorsTable.COLUMN_LOGIN} = ?, " +
                    "${EditorsTable.COLUMN_PASSWORD} = ?, " +
                    "${EditorsTable.COLUMN_FIRSTNAME} = ?, " +
                    "${EditorsTable.COLUMN_LASTNAME} = ? " +
            "WHERE ${EditorsTable.COLUMN_ID} = ?;"
        /* Delete exists editor */
        private const val DELETE_EDITOR =
            "DELETE FROM ${EditorsTable.TABLE_NAME} " +
            "WHERE ${EditorsTable.COLUMN_ID} = ?;"
    }

    init {
        val statement = connection.createStatement()
        statement.executeUpdate(CREATE_TABLE_EDITORS)
    }

    suspend fun create(editorEntity: EditorEntity): Long = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(INSERT_EDITOR, Statement.RETURN_GENERATED_KEYS)
        statement.apply {
            setString(1, editorEntity.login)
            setString(2, editorEntity.password)
            setString(3, editorEntity.firstname)
            setString(4, editorEntity.lastname)
            executeUpdate()
        }

        val generatedKeys = statement.generatedKeys
        if (generatedKeys.next()) {
            return@withContext generatedKeys.getLong(1)
        } else {
            throw DataSourceExceptions.RecordCreationException("Unable to retrieve the id of the newly inserted editor")
        }
    }

    suspend fun read(id: Long): EditorEntity = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(SELECT_EDITOR_BY_ID)
        statement.setLong(1, id)

        val resultSet = statement.executeQuery()
        if (resultSet.next()) {
            val login = resultSet.getString(EditorsTable.COLUMN_LOGIN)
            val password = resultSet.getString(EditorsTable.COLUMN_PASSWORD)
            val firstname = resultSet.getString(EditorsTable.COLUMN_FIRSTNAME)
            val lastname = resultSet.getString(EditorsTable.COLUMN_LASTNAME)
            return@withContext EditorEntity(
                id = id,
                login = login,
                password = password,
                firstname = firstname,
                lastname = lastname
            )
        } else {
            throw DataSourceExceptions.RecordNotFoundException("Editor record not found")
        }
    }

    suspend fun readAll(): List<EditorEntity?> = withContext(Dispatchers.IO) {
        val result = mutableListOf<EditorEntity>()
        val statement = connection.prepareStatement(SELECT_EDITORS)

        val resultSet = statement.executeQuery()
        while (resultSet.next()) {
            val id = resultSet.getLong(EditorsTable.COLUMN_ID)
            val login = resultSet.getString(EditorsTable.COLUMN_LOGIN)
            val password = resultSet.getString(EditorsTable.COLUMN_PASSWORD)
            val firstname = resultSet.getString(EditorsTable.COLUMN_FIRSTNAME)
            val lastname = resultSet.getString(EditorsTable.COLUMN_LASTNAME)
            result.add(
                EditorEntity(
                    id = id,
                    login = login,
                    password = password,
                    firstname = firstname,
                    lastname = lastname
                )
            )
        }

        result
    }

    suspend fun update(id: Long, editorEntity: EditorEntity) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(UPDATE_EDITOR)
        statement.apply {
            setString(1, editorEntity.login)
            setString(2, editorEntity.password)
            setString(3, editorEntity.firstname)
            setString(4, editorEntity.lastname)
            setLong(5, id)
        }

        return@withContext try {
            statement.executeUpdate()
        } catch (e: Exception) {
            throw DataSourceExceptions.RecordModificationException("Can not modify editor record")
        }
    }

    suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(DELETE_EDITOR)
        statement.setLong(1, id)

        return@withContext try {
            statement.executeUpdate()
        } catch (e: Exception) {
            throw DataSourceExceptions.RecordDeletionException("Can not delete editor record")
        }
    }

}