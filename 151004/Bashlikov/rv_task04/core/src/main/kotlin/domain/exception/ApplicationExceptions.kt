package domain.exception

sealed class ApplicationExceptions(message: String? = null) : RuntimeException(message) {

    class UpdateException(message: String? = null) : ApplicationExceptions(message)

    class CreateException(message: String? = null) : ApplicationExceptions(message)

    class DeleteException(message: String? = null) : ApplicationExceptions(message)

    class ReadException(message: String? = null) : ApplicationExceptions(message)

}

sealed class DataSourceExceptions(message: String? = null) : RuntimeException(message) {

    class RecordNotFoundException(message: String? = null) : DataSourceExceptions(message)

    class RecordCreationException(message: String? = null) : DataSourceExceptions(message)

    class RecordModificationException(message: String? = null) : DataSourceExceptions(message)

    class RecordDeletionException(message: String? = null) : DataSourceExceptions(message)

}