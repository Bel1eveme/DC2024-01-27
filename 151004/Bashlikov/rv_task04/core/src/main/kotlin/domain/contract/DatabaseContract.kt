package domain.contract

object DatabaseContract {

    object EditorsTable {
        const val TABLE_NAME = "editors"
        const val COLUMN_ID = "editors_id"
        const val COLUMN_LOGIN = "editors_login"
        const val COLUMN_PASSWORD = "editors_password"
        const val COLUMN_FIRSTNAME = "editors_firstname"
        const val COLUMN_LASTNAME = "editors_lastname"
    }

    object TweetsTable {
        const val TABLE_NAME = "tweets"
        const val COLUMN_ID = "tweets_id"
        const val COLUMN_EDITOR_ID = "tweets_editor_id"
        const val COLUMN_TITLE = "tweets_title"
        const val COLUMN_CONTENT = "tweets_content"
        const val COLUMN_CREATED = "tweets_created"
        const val COLUMN_MODIFIED = "tweets_modified"
    }

    object PostsTable {
        const val TABLE_NAME = "distcomp.tbl_post_by_country"
        const val COLUMN_COUNTRY = "country"
        const val COLUMN_TWEET_ID = "tweet_id"
        const val COLUMN_ID = "id"
        const val COLUMN_CONTENT = "content"
    }

    object TagsTable {
        const val TABLE_NAME = "tags"
        const val COLUMN_ID = "tags_ID"
        const val COLUMN_NAME = "tags_NAME"
    }

    object TweetTagsTable {
        const val TABLE_NAME = "tweet_tags"
        const val COLUMN_ID = "tweet_tags_id"
        const val COLUMN_TWEET_ID = "tweet_tags_tweet_id"
        const val COLUMN_TAG_ID = "tweet_tags_tag_id"
    }

}